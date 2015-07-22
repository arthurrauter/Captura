package com.example.arthur.captura;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur on 14.11.2014.
 */
public class MinimaxNode {
    public State state;
    public List<Integer> move;
    public MinimaxNode father;
    public List<MinimaxNode> children;
    public int evaluationValue = 0;
    public int alpha;
    public int beta;

    public MinimaxNode(State state,
                       List<Integer> move, MinimaxNode father, List<MinimaxNode> children,
                       int evaluationValue, int alpha, int beta) {
        this.state = new State(state);
        this.move = move;
        this.father = father;

        if (children != null) {
            this.children = new ArrayList<MinimaxNode>();
            for (MinimaxNode n : children) {
                this.children.add(n);
            }
        } else
            this.children = null;

        this.evaluationValue = evaluationValue;
        this.alpha = alpha;
        this.beta = beta;
    }
}
