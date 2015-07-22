package com.example.arthur.captura;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Arthur on 02.12.2014.
 */
public class Renderer extends SurfaceView {

    private static final String TAG = DrawingSurface.class.getSimpleName();
    private StoneGraphics stoneGraphics;
    private SurfaceHolder surfaceHolder;

    public Renderer(Context context, StoneGraphics s) {
        super(context);
        surfaceHolder = getHolder();
        stoneGraphics = s;
        stoneGraphics.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.green_stone));
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        stoneGraphics.draw(canvas);
    }

    public void render() {
        Canvas c = null;
        while (c == null) {
            c = surfaceHolder.lockCanvas();
        }
        if (c != null) {
        this.onDraw(c);
        surfaceHolder.unlockCanvasAndPost(c);}
    }
}

