package com.example.arthur.captura;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Arthur on 26.11.2014.
 */
public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = DrawingSurface.class.getSimpleName();
    public List<String> gameSequence = new ArrayList<String>();
    public boolean waitingForInput;
    int h;
    int w;
    int xPadding;
    int rows;
    int spacing;
    int yPadding;
    int groundZeroX;
    int groundZeroY;
    List<Integer> move = new ArrayList<Integer>();
    private EngineThread coreThread;
    private boolean stoneSelected = false;
    private Bitmap background;
    private List<DrawableStone> drawableStones;
    private List<DrawableVertex> drawableVertexes;
    private DrawableStone selectedDrawableStone = null;
    private boolean series = false;

    public DrawingSurface(Context context, String gameName) {
        super(context);
        getHolder().addCallback(this);

        if (gameName == "series") {
            setSeries(true);
        }

        gameSequence.add("felli");
        gameSequence.add("pretwa");
        gameSequence.add("serpent");
        gameSequence.add("alquerque");
        gameSequence.add("felliP");
        gameSequence.add("alquerqueP");
        gameSequence.add("checkers");

        if (isSeries()) {
            gameName = gameSequence.get(0);
            gameSequence.remove(0);
        }

        coreThread = new EngineThread(getHolder(), this, gameName);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        w = size.x;
        h = size.y;

        makeBoards();


        setFocusable(true);
    }

    public boolean isSeries() {
        return series;
    }

    public void setSeries(boolean series) {
        this.series = series;
    }

    public DrawableVertex findDrawableVertex(List<DrawableVertex> drawableVertexes, Vertex coreVertex) {
        for (DrawableVertex d : drawableVertexes) {
            if (d.getV() == coreVertex) {
                return d;
            }
        }
        return null;
    }

    public DrawableStone findDrawableStone(List<DrawableStone> drawableStones, Stone coreStone) {
        for (DrawableStone d : drawableStones) {
            if (d.getS() == coreStone) {
                return d;
            }
        }
        return null;
    }

    public Stone findCoreStone(List<Stone> coreStones, DrawableStone drawableVersion) {
        for (Stone s : coreStones) {
            if (s == drawableVersion.getS()) {
                return s;
            }
        }
        return null;
    }

    public Vertex findCoreVertex(List<Vertex> coreVertexes, DrawableVertex drawableVersion) {
        for (Vertex v : coreVertexes) {
            if (v == drawableVersion.getV()) {
                return v;
            }
        }
        return null;
    }

    public void makeBoards() {
        List<Stone> coreStones = coreThread.getCapture().realState.stones;
        List<Vertex> coreVertexes = Arrays.asList(coreThread.getCapture().getBoard());
        drawableStones = new ArrayList<DrawableStone>();
        drawableVertexes = new ArrayList<DrawableVertex>();

        for (Vertex coreVertex : coreVertexes) {
            DrawableVertex dv = new DrawableVertex(coreVertex);
            drawableVertexes.add(dv);
        }

        for (Stone coreStone : coreStones) {
            DrawableStone ds = new DrawableStone(coreStone);
            if (coreStone.getOwner() == Owner.PLAYER1)
                ds.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.green_stone));
            if (coreStone.getOwner() == Owner.PLAYER2)
                ds.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.yellow_stone));
            Bitmap bmp = ds.getBitmap();
            int scaling = 1;
            bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() * scaling, bmp.getHeight() * scaling, false);
            ds.setBitmap(bmp);

            drawableStones.add(ds);
        }

        if (coreThread.getCapture().getGameName() == "felli"
                || coreThread.getCapture().getGameName() == "felliP") {

            if (coreThread.getCapture().getGameName() == "felliP") {
                coreThread.getCapture().setPromotionOn(true);
            }

            BitmapFactory.Options mNoScale = new BitmapFactory.Options();
            mNoScale.inScaled = false;
            background = BitmapFactory.decodeResource(getResources(), R.drawable.felli, mNoScale);

            xPadding = (w - 700) / 2;
            yPadding = (h - 700) / 2;


            for (DrawableVertex dv : drawableVertexes) {
                int x = 0;
                int y = 0;

                switch (dv.getV().getId()) {
                    case 0:
                        x = 30;
                        y = 679;
                        break;
                    case 1:
                        x = 350;
                        y = 677;
                        break;
                    case 2:
                        x = 669;
                        y = 677;
                        break;
                    case 3:
                        x = 674;
                        y = 19;
                        break;
                    case 4:
                        x = 350;
                        y = 19;
                        break;
                    case 5:
                        x = 22;
                        y = 19;
                        break;
                    case 6:
                        x = 172;
                        y = 530;
                        break;
                    case 7:
                        x = 350;
                        y = 530;
                        break;
                    case 8:
                        x = 530;
                        y = 530;
                        break;
                    case 9:
                        x = 530;
                        y = 164;
                        break;
                    case 10:
                        x = 350;
                        y = 161;
                        break;
                    case 11:
                        x = 165;
                        y = 165;
                        break;
                    case 12:
                        x = 350;
                        y = 350;
                        break;
                }

                dv.setX(xPadding + x);
                dv.setY(yPadding + y);

            }


        }
        if (coreThread.getCapture().getGameName() == "checkers") {

            BitmapFactory.Options mNoScale = new BitmapFactory.Options();
            mNoScale.inScaled = false;
            background = BitmapFactory.decodeResource(getResources(), R.drawable.damas, mNoScale);

            xPadding = (w - 700) / 2;
            yPadding = (h - 700) / 2;

            int s = 4;

            for (DrawableVertex dv : drawableVertexes) {
                for (int i = 0; i < s; i++) {
                    for (int j = 0; j < s; j++) {
                        int indexA = i * 2 * s + j + s;
                        int indexB = i * 2 * s + j;

                        if (dv.getV().getId() == indexB) {
                            dv.setX(xPadding + 15 + j * 190);
                            dv.setY(h - yPadding - 15 - i * 190);
                        }
                        if (dv.getV().getId() == indexA) {
                            dv.setX(xPadding + 110 + j * 190);
                            dv.setY(h - yPadding - 110 - i * 190);
                        }

                    }
                }

            }

        }

        if (coreThread.getCapture().getGameName() == "pretwa") {
            BitmapFactory.Options mNoScale = new BitmapFactory.Options();
            mNoScale.inScaled = false;
            background = BitmapFactory.decodeResource(getResources(), R.drawable.small_pretwa, mNoScale);

            xPadding = (w - 700) / 2;
            yPadding = (h - 700) / 2;


            for (DrawableVertex dv : drawableVertexes) {
                int x = 0;
                int y = 0;

                switch (dv.getV().getId()) {
                    case 0:
                        x = 17;
                        y = 341;
                        break;
                    case 1:
                        x = 181;
                        y = 646;
                        break;
                    case 2:
                        x = 521;
                        y = 646;
                        break;
                    case 3:
                        x = 679;
                        y = 342;
                        break;
                    case 4:
                        x = 501;
                        y = 42;
                        break;
                    case 5:
                        x = 188;
                        y = 35;
                        break;
                    case 6:
                        x = 150;
                        y = 343;
                        break;
                    case 7:
                        x = 248;
                        y = 526;
                        break;
                    case 8:
                        x = 444;
                        y = 525;
                        break;
                    case 9:
                        x = 542;
                        y = 345;
                        break;
                    case 10:
                        x = 452;
                        y = 158;
                        break;
                    case 11:
                        x = 246;
                        y = 155;
                        break;
                    case 12:
                        x = 350;
                        y = 350;
                        break;
                }

                dv.setX(xPadding + x);
                dv.setY(yPadding + y);

            }


        }

        if (coreThread.getCapture().getGameName() == "serpent") {
            BitmapFactory.Options mNoScale = new BitmapFactory.Options();
            mNoScale.inScaled = false;
            background = BitmapFactory.decodeResource(getResources(), R.drawable.serpente, mNoScale);

            xPadding = (w - 700) / 2;
            yPadding = (h - 338) / 2;


            for (DrawableVertex dv : drawableVertexes) {
                int x = 0;
                int y = 0;

                switch (dv.getV().getId()) {
                    case 0:
                        x = 12;
                        y = 175;
                        break;
                    case 1:
                        x = 66;
                        y = 23;
                        break;
                    case 2:
                        x = 71;
                        y = 323;
                        break;
                    case 3:
                        x = 129;
                        y = 175;
                        break;
                    case 5:
                        x = 180;
                        y = 321;
                        break;
                    case 4:
                        x = 180;
                        y = 17;
                        break;
                    case 6:
                        x = 237;
                        y = 175;
                        break;
                    case 7:
                        x = 296;
                        y = 20;
                        break;
                    case 8:
                        x = 296;
                        y = 320;
                        break;
                    case 9:
                        x = 343;
                        y = 175;
                        break;
                    case 10:
                        x = 406;
                        y = 15;
                        break;
                    case 11:
                        x = 407;
                        y = 320;
                        break;
                    case 12:
                        x = 465;
                        y = 175;
                        break;
                    case 13:
                        x = 522;
                        y = 15;
                        break;
                    case 14:
                        x = 520;
                        y = 320;
                        break;
                    case 15:
                        x = 573;
                        y = 175;
                        break;
                    case 16:
                        x = 636;
                        y = 15;
                        break;
                    case 17:
                        x = 630;
                        y = 320;
                        break;
                    case 18:
                        x = 686;
                        y = 175;
                        break;


                }

                dv.setX(xPadding + x);
                dv.setY(yPadding + y);

            }


        }


        if (coreThread.getCapture().getGameName() == "alquerque"
                || coreThread.getCapture().getGameName() == "alquerqueP") {

            if (coreThread.getCapture().getGameName() == "alquerqueP") {
                coreThread.getCapture().setPromotionOn(true);
            }

            int alquerqueSize = coreThread.getCapture().getSize();
            BitmapFactory.Options mNoScale = new BitmapFactory.Options();
            mNoScale.inScaled = false;
            background = BitmapFactory.decodeResource(getResources(), R.drawable.alquerque, mNoScale);

            xPadding = (w - 700) / 2;
            spacing = 165;
            int yCorrection = -5;
            yPadding = (h - 700) / 2;
            groundZeroX = xPadding + 20;
            groundZeroY = h - yPadding - 30;

            for (int i = 0; i < alquerqueSize; i++) {
                for (int j = 0; j < alquerqueSize; j++) {

                    int x = groundZeroX + (spacing * j);
                    int y = groundZeroY - ((spacing + yCorrection) * i);

                    for (DrawableVertex dv : drawableVertexes) {
                        if (dv.getV().getId() == j + alquerqueSize * i) {
                            dv.setX(x);
                            dv.setY(y);
                        }

                    }
                }
            }

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        coreThread.setRunning(true);
        coreThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        coreThread.setRunning(false);

        boolean retry = true;
        while (retry) {
            try {
                coreThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "Taunty");
            if (stoneSelected) {
                for (DrawableVertex v : drawableVertexes) {
                    v.handleActionDown((int) event.getX(), (int) event.getY());
                    if (v.isTouched()) {

                        move.add(selectedDrawableStone.getS().getId());
                        move.add(v.getV().getId());
                        waitingForInput = false;
                        stoneSelected = false;
                        selectedDrawableStone.setTouched(false);
                        selectedDrawableStone = null;
                        break;
                    }
                }
                if (selectedDrawableStone != null) selectedDrawableStone.setTouched(false);
                stoneSelected = false;
                selectedDrawableStone = null;

            } else {
                for (DrawableStone s : drawableStones) {
                    s.handleActionDown((int) event.getX(), (int) event.getY());
                    if (s.isTouched()) {
                        if (s.getS().getOwner() == Owner.PLAYER1) {
                            selectedDrawableStone = s;
                            stoneSelected = true;
                        }
                        break;
                    }
                }
            }
        }

        return true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(background, xPadding, yPadding, null);

        for (DrawableStone ds : drawableStones) {
            ds.draw(canvas);
        }
    }

    public void updateDrawablesPosition() {
        //Log.d(TAG, "updating Drawables");

        List<Stone> coreStones = coreThread.getCapture().realState.stones;

        Iterator<DrawableStone> dsIterator = drawableStones.iterator();
        while (dsIterator.hasNext()) {
            DrawableStone ds = dsIterator.next();
            if (coreStones.contains(ds.getS())) {
                Vertex v = ds.getS().getV();
                DrawableVertex dv = findDrawableVertex(drawableVertexes, v);
                ds.setX(dv.getX());
                ds.setY(dv.getY());
            } else {
                dsIterator.remove();
            }
        }


        /*for (DrawableStone ds : drawableStones) {

            if(coreStones.contains(ds.getS())) {
                Vertex v = ds.getS().getV();
                DrawableVertex dv = findDrawableVertex(drawableVertexes, v);
                ds.setX(dv.getX());
                ds.setY(dv.getY());
            }
            else drawableStones.remove(ds);
        }*/
    }

    public void gameOverDraw(Canvas canvas, Owner winner) {
        int color;
        Bitmap b;
        BitmapFactory.Options mNoScale = new BitmapFactory.Options();
        mNoScale.inScaled = false;
        if (winner == Owner.PLAYER1) {
            color = Color.GREEN;
            b = BitmapFactory.decodeResource(getResources(), R.drawable.p1wins, mNoScale);
        } else {
            color = Color.YELLOW;
            b = BitmapFactory.decodeResource(getResources(), R.drawable.p2wins, mNoScale);
        }

        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(background, xPadding, yPadding, null);

        canvas.drawBitmap(b, (w - b.getWidth()) / 2, 100, null);
    }
}
