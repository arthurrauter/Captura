package com.example.arthur.captura;

/**
 * Created by Arthur on 13.10.2014.
 */
public class Stone {
    private int id;
    private Owner owner;
    private boolean promoted;
    private Vertex v;

    public Stone(int id, Owner owner, Vertex v) {
        this.id = id;
        this.owner = owner;
        this.promoted = false;
        this.v = v;
    }

    public Stone(Stone s) {
        this.id = s.getId();
        this.owner = s.getOwner();
        this.promoted = s.isPromoted();
        this.v = s.getV();
    }

    public int getId() {

        return id;
    }

    public Owner getOwner() {
        return owner;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public Vertex getV() {
        return v;
    }

    public void setV(Vertex v) {
        this.v = v;
    }
}
