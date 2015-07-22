package com.example.arthur.captura;

/**
 * Created by Arthur on 13.10.2014.
 */
public class Game {
    Vertex[] board;
    Stone[] stonesP1, stonesP2;
    int turn;

    //actions(state)
    public void actions2(){}

    //player(state)
    public void turnOf2(){}

    //results(state,action)
    public boolean move2(Stone[] player, int stone, int src, int dst){
        if(player[stone].getV() != board[src]) {
            return false;
        }
        else{
            player[stone].setV(board[dst]);
            return true;
        }
    }

    //terminal_test(state)
    public void gameOver(){}

    //evaluation(state,player) ~= whoIsTheWinner(state,player)
    public void evaluation(){}
    }
