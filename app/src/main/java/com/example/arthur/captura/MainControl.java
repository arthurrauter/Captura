package com.example.arthur.captura;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Arthur on 01.12.2014.
 */
public class MainControl extends View implements Runnable {

    private static final String TAG = DrawingSurface.class.getSimpleName();
    private static final long FRAME_PERIOD = 20000000L;
    private static final long FRAME_PERIOD_DIVISOR = 10000000L;
    private boolean keepRunning = true;

    public MainControl(Context context) {
        super(context);
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
    return false;
    }



}
