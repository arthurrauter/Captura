package com.example.arthur.captura;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur on 13.10.2014.
 */
public class Match {
    Capture capture;
    String winner = new String();

    public Match(int boardSize, String gameName) {

        capture = new Capture(gameName, 5, 999, 999, true, true);
        List<List> possibleMoves = new ArrayList<List>();
        List<Integer> someMove = new ArrayList<Integer>();
        String moveTrace = new String();

        while (!capture.isTheGameOver(capture.realState)) {
            if(capture.turnOf(capture.realState) == Owner.PLAYER1) {
                someMove = capture.minimaxDecisionAB(capture.realState);
            }
            else {
                possibleMoves = capture.actions(capture.realState);
                someMove = capture.randomMove(possibleMoves);
            }
            moveTrace = capture.makeTheMove(capture.realState, someMove);
            capture.updateMovesHistory(moveTrace);
            capture.realState.nextTurn();
        }
        Owner winnerEnum = capture.whoIsTheWinner(capture.realState);
        if (winnerEnum == Owner.PLAYER1) winner = "Player 1";
        if (winnerEnum == Owner.PLAYER2) winner = "Player 2";
    }

}

