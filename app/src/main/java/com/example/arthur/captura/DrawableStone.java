package com.example.arthur.captura;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

/**
 * Created by Arthur on 02.12.2014.
 */
public class DrawableStone {
    private Stone s;
    private int x, y;
    private Bitmap bitmap;
    private boolean touched;

    public DrawableStone(Stone s, int x, int y, Bitmap bitmap) {
        this.s = s;
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
        touched = false;

    }

    public DrawableStone(Stone s) {
        this.s = s;
    }

    public Stone getS() {
        return s;
    }

    public void setS(Stone s) {
        this.s = s;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void draw(Canvas canvas) {

        int selectLift = 0;
        if (isTouched()) {
            selectLift = 20;
        }

        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2) - selectLift, null);

    }

    public void handleActionDown(int eventX, int eventY) {
        if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth() / 2))) {
            if (eventY >= (y - bitmap.getHeight() / 2) && (eventY <= (y + bitmap.getHeight() / 2))) {
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
}
