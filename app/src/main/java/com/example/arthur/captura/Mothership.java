package com.example.arthur.captura;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Arthur on 02.12.2014.
 */
public class Mothership extends View implements Runnable
{
    private static final String TAG = Mothership.class.getSimpleName();
    private static final long FRAME_PERIOD = 20000000L;
    private static final long FRAME_PERIOD_DIVISOR = 10000000L;
    private boolean keepRunning = true;

    private GameEngine engine;

    public Mothership(Context context) {
        super(context);
        engine = new GameEngine(context);
        Log.d(TAG, TAG + " will call start.");
        this.start();
    }

    public void start() {
        Log.d(TAG, TAG + " has started.");
        new Thread(this).start();
    }

    @Override
    public void run() {

        long elapsedTime = 0L;

        while(keepRunning) {
            long startingTime = System.nanoTime();

            engine.update();
            engine.render();

            elapsedTime = System.nanoTime() - startingTime;
            if (elapsedTime < FRAME_PERIOD) {
                try {
                    Thread.sleep((FRAME_PERIOD-elapsedTime)/FRAME_PERIOD_DIVISOR);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public boolean onTouchEvent(MotionEvent event) {
        return engine.onTouchEvent(event);
    }



}

