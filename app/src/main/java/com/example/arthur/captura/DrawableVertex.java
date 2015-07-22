package com.example.arthur.captura;

/**
 * Created by Arthur on 02.12.2014.
 */
public class DrawableVertex {
    private Vertex v;
    private int x, y;
    private int touchRadius = 50;
    private boolean touched;

    public DrawableVertex(Vertex v, int x, int y) {
        this.v = v;
        this.x = x;
        this.y = y;
    }

    public DrawableVertex(Vertex v) {
        this.v = v;
    }

    public Vertex getV() {
        return v;
    }

    public void setV(Vertex v) {
        this.v = v;
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

    public void handleActionDown(int eventX, int eventY) {
        if (eventX >= (x - touchRadius) && (eventX <= (x + touchRadius))) {
            if (eventY >= (y - touchRadius) && (eventY <= (y + touchRadius))) {
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }

    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public boolean isTouched() {
        return touched;
    }
}