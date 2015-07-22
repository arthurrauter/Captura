package com.example.arthur.captura;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur on 26.11.2014.
 */
public class EngineThread extends Thread {
    private boolean running;
    private SurfaceHolder surfaceHolder;
    private DrawingSurface drawingSurface;
    private static final String TAG = EngineThread.class.getSimpleName();
    private static final long FRAME_PERIOD = 20000000L;
    private static final long FRAME_PERIOD_DIVISOR = 10000000L;

    private Capture capture;


    public EngineThread(SurfaceHolder surfaceHolder, DrawingSurface drawingSurface, String gameName) {
        super();
        this.surfaceHolder = surfaceHolder;

        int size = 0; //it is hardcoded withing Capture
        int maxTurns = 999;
        boolean compulsoryCapture = true;
        boolean multipleCaptures = true;
        this.capture = new Capture(gameName,
                size , 5, maxTurns, compulsoryCapture, multipleCaptures);
        this.drawingSurface = drawingSurface;

    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        Log.d(TAG, "Starting game loop");
        long startingTime = System.nanoTime();
        Canvas canvas;
        if (running) drawingSurface.updateDrawablesPosition();
        drawingSurface.waitingForInput = true;

        while (running) {
            canvas = null;

            List<List> possibleMoves = new ArrayList<List>();
            List<Integer> someMove = new ArrayList<Integer>();
            String moveTrace = new String();

            possibleMoves = capture.actions(capture.realState);
            if (!possibleMoves.isEmpty()) {
                if (capture.turnOf(capture.realState) == Owner.PLAYER2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    someMove = capture.randomMove(possibleMoves);
                    someMove = capture.minimaxDecisionAB(capture.realState);
                    moveTrace = capture.makeTheMove(capture.realState, someMove);
                    capture.updateMovesHistory(moveTrace);
                    capture.realState.nextTurn();
                }
                else {
                    if (drawingSurface.waitingForInput == false) {
                        someMove = capture.translateHumanMove(drawingSurface.move);
                        if (someMove != null) {
                            moveTrace = capture.makeTheMove(capture.realState, someMove);
                            capture.updateMovesHistory(moveTrace);
                            capture.realState.nextTurn();
                        }
                    }
                    drawingSurface.move.clear();
                    drawingSurface.waitingForInput = true;

                }

            } else {


                if(capture.isTheGameOver(capture.realState)){
                    Owner winner = capture.whoIsTheWinner(capture.realState);
                    try {
                        canvas = this.surfaceHolder.lockCanvas();
                        synchronized (surfaceHolder) {
                            this.drawingSurface.gameOverDraw(canvas, winner);
                        }
                    } finally {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                    setRunning(false);
                    Log.d(TAG, "entering sleep");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "out of sleep");

                    if (drawingSurface.isSeries()) {
                        if(!drawingSurface.gameSequence.isEmpty()) {
                            String gameName = drawingSurface.gameSequence.get(0);
                            drawingSurface.gameSequence.remove(0);
                            capture = new Capture(gameName, 0, 99, 999, true, true);
                            setRunning(true);
                            drawingSurface.makeBoards();
                            if (running) drawingSurface.updateDrawablesPosition();
                        }
                        else{
                            break;
                        }

                    }else{
                        break;
                    }
                }
            }

            drawingSurface.updateDrawablesPosition();

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.drawingSurface.onDraw(canvas);
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }
    }

    public Capture getCapture() {
        return capture;
    }}
