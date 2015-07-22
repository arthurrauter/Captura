package com.example.arthur.captura;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Arthur on 27.11.2014.
 */
public class StoneGraphics {

    private Bitmap bitmap;
    private int x;
    private int y;
    private boolean touched;

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
    }

    public void handleActionDown(int eventX, int eventY) {
        if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth()/2))) {
            if (eventY >= (y - bitmap.getHeight() / 2) && (y <= (y + bitmap.getHeight() / 2))) {
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }

    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getBitmap() {

        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public StoneGraphics(Bitmap bitmap, int x, int y) {

        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }

    public StoneGraphics(int x, int y) {
        this.x = x;
        this.y = y;
    }


}
