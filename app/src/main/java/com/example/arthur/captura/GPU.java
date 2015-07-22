package com.example.arthur.captura;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Arthur on 25.11.2014.
 */
public class GPU extends SurfaceView implements Runnable {

    private SurfaceHolder holder;
    private Bitmap image;


    volatile boolean running = false;
    Thread renderThread = null;


    public GPU(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        image = BitmapFactory.decodeResource(getResources(), R.drawable.ball);

    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }



    @Override
    public void run() {
        while(running) {
            if(!holder.getSurface().isValid())
                continue; //wait till it becomes valid
            Canvas canvas = holder.lockCanvas();
            canvas.drawARGB(255, 150, 150, 10);
            //canvas.drawCircle(cx, cy, radius, paint);

            holder.unlockCanvasAndPost(canvas);
            update();
        }

    }

    private void update() {

    }

    public void pause() {
        running = false;
        boolean retry = true;
        while(retry) {
            try {
                renderThread.join();
                retry = false;
            } catch( InterruptedException e) {
                //retry
            }
        }
    }
}