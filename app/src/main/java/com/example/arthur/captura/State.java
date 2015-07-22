package com.example.arthur.captura;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur on 14.11.2014.
 */
public class State {
    public List<Stone> stones;
    public int turn;

    public State(List<Stone> stones, int turn) {
        this.stones = stones;
        this.turn = turn;
    }

    public State(State original) {
        ArrayList<Stone> stonesCopy = new ArrayList<Stone>();
        for (Stone s : original.stones) {
            stonesCopy.add(new Stone(s));
        }
        this.stones = stonesCopy;
        this.turn = original.turn;
    }

    public void nextTurn() {
        turn++;
    }
}
