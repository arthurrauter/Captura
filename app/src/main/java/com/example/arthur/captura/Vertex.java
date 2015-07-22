package com.example.arthur.captura;


/**
 * Created by Arthur on 13.10.2014.
 */
public class Vertex {
    private int id;
    private Edge[] edges;

    public Vertex(Edge[] edges, int id) {
        this.edges = edges;
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public void addEdges(Edge[] e) {
        if (edges == null) {
            this.createEdges(e.length);
            edges = e;
        } else {
            Edge[] newEdges = new Edge[e.length + edges.length];
            int i;
            for (i = 0; i < edges.length; i++) {
                newEdges[i] = edges[i];
            }
            for (int j = 0; j < e.length; j++) {
                newEdges[i + j] = e[j];
            }
            this.edges = newEdges;
        }
    }

    private void createEdges(int howMany) {
        this.edges = new Edge[howMany];
    }

}
