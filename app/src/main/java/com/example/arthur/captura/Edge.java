package com.example.arthur.captura;

/**
 * Created by Arthur on 13.10.2014.
 */
public class Edge {
    private int linksTo;
    private EdgeType type;
    private Vertex v;

    public Edge(int linksTo, EdgeType type, Vertex v) {
        this.linksTo = linksTo;
        this.type = type;
        this.v = v;
    }

    public EdgeType getType() {
        return type;
    }

    public Vertex getV() {
        return v;
    }
}
