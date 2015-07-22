package com.example.arthur.captura;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Arthur on 15.10.2014.
 */
public class Alquerque extends Game {
    int size;
    List<String> moveLog = new ArrayList<String>();

    private void createOrthogonalEdges(int vertexId) {
        int i = vertexId;

        List<Edge> newEdges = new ArrayList<Edge>();

        int up = i + size;
        if (up < size * size) {
            newEdges.add(new Edge(up, EdgeType.VERTICAL, board[up]));
        }
        int down = i - size;
        if (down >= 0) {
            newEdges.add(new Edge(down, EdgeType.VERTICAL, board[down]));
        }
        int left = i - 1;
        if (left >= 0 && i % size != 0) {
            newEdges.add(new Edge(left, EdgeType.HORIZONTAL, board[left]));
        }
        int right = i + 1;
        if (right < size * size && (i + 1) % size != 0) {
            newEdges.add(new Edge(right, EdgeType.HORIZONTAL, board[right]));
        }
        Edge[] edgeArray = newEdges.toArray(new Edge[newEdges.size()]);
        board[i].addEdges(edgeArray);
    }

    private void createDiagonalEdges(int vertexId) {
        int j = vertexId;

        List<Edge> newEdges = new ArrayList<Edge>();

        int up_right = j + size + 1;
        if (up_right < size * size && (j + 1) % size != 0) {
            newEdges.add(new Edge(up_right, EdgeType.DIAGONAL_R, board[up_right]));
        }

        int down_left = j - size - 1;
        if (down_left > 0 && j % size != 0) {
            newEdges.add(new Edge(down_left, EdgeType.DIAGONAL_R, board[down_left]));
        }

        int up_left = j + size - 1;
        if (up_left < size * size && j % size != 0) {
            newEdges.add(new Edge(up_left, EdgeType.DIAGONAL_L, board[up_left]));
        }

        int down_right = j - size + 1;
        if (down_right > 0 && (j + 1) % size != 0) {
            newEdges.add(new Edge(down_right, EdgeType.DIAGONAL_L, board[down_right]));
        }
        Edge[] edgeArray = newEdges.toArray(new Edge[newEdges.size()]);
        board[j].addEdges(edgeArray);

    }

    public Alquerque(int size) {
        if (size % 2 == 0) {
            this.size = size - 1;
        } else {
            this.size = size;
        }

        //Starts the turn counter
        turn = 1;

        //Creates the board
        board = new Vertex[size * size];
        for (int i = 0; i < size * size; i++) {
            board[i] = new Vertex(null, i);
        }

        //creates the ORTHOGONAL edges
        for (int i = 0; i < size * size; i++) {
            this.createOrthogonalEdges(i);
        }

        //Creates the DIAGONAL edges
        for (int i = 0; i < size * size; i = i + 2) {
            this.createDiagonalEdges(i);
        }

        //Creates the Stones
        int nStones = (size * size - 1) / 2; //Number of Stones per Player
        stonesP1 = new Stone[nStones];
        stonesP2 = new Stone[nStones];

        //Center has no stoned
        int boardCenter = ((size * size) - 1) / 2;

        for (int i = 0; i < nStones; i++) {
            stonesP1[i] = new Stone(i, Owner.PLAYER1, board[i]);
        }
        for (int i = 0; i < nStones; i++) {
            stonesP2[i] = new Stone(i + boardCenter + 1, Owner.PLAYER2, board[i + boardCenter + 1]);
        }


    }

       public boolean validMove(Stone[] player, int stone, int dst) {
        Vertex source = player[stone].getV();
        Vertex destiny = board[dst];
        if (isAdjacent(source, destiny) && isFree(destiny)) {
            return true;
        } else return false;
    }

    public boolean isFree(Vertex v) {
        for (int i = 0; i < stonesP1.length; i++) {
            if (stonesP1[i].getV() == v || stonesP2[i].getV() == v) {
                return false;
            }
        }
        return true;
    }

    public boolean isAdjacent(Vertex v1, Vertex v2) {
        for (int i = 0; i < v1.getEdges().length; i++) {
            if (v1.getEdges()[i].getV() == v2) {
                return true;
            }
        }
        return false;
    }

    //Functions that I might really need.
    public List<List> compulsoryCapture(List<List> possibleMoves) {
        for (List move : possibleMoves)
        {
            if (move.size()>2) {
                possibleMoves.remove(move);
            }
        }
        return possibleMoves;
    }

    public void move(Stone[] player, List<Integer> move) {
        String log = new String();
        if (player == stonesP2) log = String.valueOf(player[move.get(0)-11].getV().getId());
        else log = String.valueOf(player[move.get(0)].getV().getId());
        log = log + "-";
        log = log + String.valueOf(board[move.get(1)].getId());
        if (move.size() > 2) {
            log = log + "-" + String.valueOf(board[move.get(2)].getId());
        }
        this.moveLog.add(log);

        if (move.size() == 2) {
            //simple move
            int stone = move.get(0);
            int dst = move.get(1);
            if (player == stonesP2)
            {
                stone=stone-11;
            }
            player[stone].setV(board[dst]);
        }
        else {
            //capture
            int stone = move.get(0);
            int capture = move.get(1);
            int dst = move.get(2);
            player[stone].setV(board[dst]);

            for (int i = 0; i < stonesP1.length; i++) {
                if (stonesP1[i].getV() == board[capture]) {
                    stonesP1[i].setV(null);
                }
                if (stonesP2[i].getV() == board[capture]) {
                    stonesP2[i].setV(null);
                }
            }

        }

    }

    public int nextTurn() {
        this.turn = this.turn +1;
        return  this.turn;
    }

    public Owner turnOf(int turn) {
        if (turn % 2 == 0) {
            return Owner.PLAYER2;
        } else return Owner.PLAYER1;
    }

    public List<Integer> randomMove (List<List> possibleMoves) {
        Random RNGesus = new Random();
        int r = RNGesus.nextInt(possibleMoves.size());
        return possibleMoves.get(r);
    }

    public boolean moveCheck(List<List> possibleMoves, List<Integer> move) {
        if (possibleMoves.contains(move))
        {
            return true;
        }
        else return false;
    }

    public Vertex[] vertexesAdjacentOf(Vertex v) {
        if (v == null) return null;
        Edge[] edges = v.getEdges();
        Vertex[] adjacentVs = new Vertex[edges.length];
        for (int i = 0; i < edges.length; i++) {
            adjacentVs[i] = edges[i].getV();
        }
        return adjacentVs;
    }

    public Owner whoHolds(Vertex v) {
        for (int i = 0; i < stonesP1.length; i++) {
            if (stonesP1[i].getV() == v) {
                return Owner.PLAYER1;
            }
            if (stonesP2[i].getV() == v) {
                return Owner.PLAYER2;
            }
        }
        return Owner.NONE;
    }



    public List<List> actions(Stone[] stonesP1, Stone[] stonesP2, int turn) {
        List<List> showMeYourMoves = new ArrayList<List>();
        Stone[] activePlayer, dummy;
        Owner friend;

        if (turnOf(turn) == Owner.PLAYER1) {
            activePlayer = stonesP1;
            dummy = stonesP2;
            friend = Owner.PLAYER1;

        } else {
            activePlayer = stonesP2;
            dummy = stonesP1;
            friend = Owner.PLAYER2;

        }

        for (int i = 0; i < activePlayer.length; i++) {
            Vertex[] adjacentVs = vertexesAdjacentOf(activePlayer[i].getV());
            for (int j = 0; j < adjacentVs.length; j++) {
                List<Integer> move = new ArrayList<Integer>();

                if (whoHolds(adjacentVs[j]) == friend) {
                    //means this adjacent vertex is occupied by a FRIEND
                    //thus no move here is possible.
                    move = null;

                }
                else if (whoHolds(adjacentVs[j]) == Owner.NONE) {
                    //means this adjacent vertex is free
                    //you can move there.
                    move.add(activePlayer[i].getId());
                    move.add(adjacentVs[j].getId());
                }
                else {
                    //means this adjacent vertex, in this case, adjacentVs[j]
                    //is occupied by a FOE. A capture might be possible.
                    Vertex activeV = activePlayer[j].getV();
                    move = null;
                    for (int k = 0; k < activeV.getEdges().length; k++) {
                        if (activeV.getEdges()[k].getV() == adjacentVs[j]) {
                            EdgeType link1 = activeV.getEdges()[k].getType();
                            for (int k2 = 0; k2 < adjacentVs[j].getEdges().length; k2++) {
                                if (adjacentVs[j].getEdges()[k2].getType() == link1) {
                                    if (whoHolds(adjacentVs[j].getEdges()[k2].getV()) == Owner.NONE) {
                                        move.add(activePlayer[i].getId());
                                        move.add(adjacentVs[j].getId());
                                        move.add(adjacentVs[j].getEdges()[k2].getV().getId());
                                    }
                                }
                            }
                        }
                    }

                }
            if (move != null) showMeYourMoves.add(move);
            }
        }
        return showMeYourMoves;
    }



}