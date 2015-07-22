package com.example.arthur.captura;

import android.app.Activity;
import android.content.Context;import android.util.Log;
import android.view.MotionEvent;import android.view.View;

/**
 * Created by Arthur on 02.12.2014.
 */
public class GameEngine extends View{

    private static final String TAG = GameEngine.class.getSimpleName();
    private StoneGraphics stoneGraphics;
    private Renderer renderer;

    public GameEngine(Context context) {
        super(context);
        stoneGraphics = new StoneGraphics(50,50);
        Log.d(TAG, TAG + " just before new Renderer");
        renderer = new Renderer(context, stoneGraphics);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            stoneGraphics.handleActionDown((int) event.getX(), (int) event.getY());
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (stoneGraphics.isTouched()) {
                stoneGraphics.setX((int) event.getX());
                stoneGraphics.setY((int) event.getY());
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (stoneGraphics.isTouched()) {
                stoneGraphics.setTouched(false);
            }
        }


        return true;
    }

    public void update(float deltaTime) {

    }

    public void render() {
        renderer.render();

    }

    public void update() {

    }
}
