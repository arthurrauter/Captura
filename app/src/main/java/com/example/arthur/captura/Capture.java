package com.example.arthur.captura;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Arthur on 05.11.2014.
 */
public class Capture {

    //game mechanics:
    public State realState;
    private Vertex[] board;
    private List<Integer> promotionVertexesP1, promotionVertexesP2;
    private int treeDepth = 6;
    private Owner currentMAX;
    public Owner winner = Owner.NONE;
    //other
    public List<String> movesHistory;
    //game definitions:
    private String gameName;
    private int size;
    private int capturesPerPlayerLimit;
    private int turnLimit;
    private boolean compulsoryCapture;
    private boolean multipleCaptures;
    private boolean movementRestriction;
    private boolean promotionOn = false;

    public Capture(String gameName, int size,
                   int capturesPerPlayerLimit, int turnLimit,
                   boolean compulsoryCapture, boolean multipleCaptures) {

        this.gameName = gameName;
        this.size = size;
        this.capturesPerPlayerLimit = capturesPerPlayerLimit;
        this.turnLimit = turnLimit;
        this.compulsoryCapture = compulsoryCapture;
        this.multipleCaptures = multipleCaptures;

        if (this.gameName == "alquerque" || this.gameName == "alquerqueP") {
            this.size = 5;
            Alquerque(this.size);
        }

        if (this.gameName == "pretwa") {
            this.size = 2;
            Pretwa(this.size);
        }

        if (this.gameName == "felli" || this.gameName == "felliP") {
            this.size = 2;
            Felli(this.size);
        }

        if (this.gameName == "serpent") {
            this.size = 6;
            Serpent(this.size);
        }

        if (this.gameName == "checkers") {
            this.size = 6;
            checkers(this.size);
        }


        movesHistory = new ArrayList<String>();
    }

    public String getGameName() {
        return gameName;
    }

    public int getSize() {
        return size;
    }

    public Vertex[] getBoard() {
        return board;
    }

    public boolean isPromotionOn() {
        return promotionOn;
    }

    public void setPromotionOn(boolean promotionOn) {
        this.promotionOn = promotionOn;
    }

    private void checkers(int size) {
        movementRestriction = true;
        size = 4;
        board = new Vertex[size * size * 2];
        int finalIndex = size * size * 2 - 1;
        for (int i = 0; i <= finalIndex; i++) {
            board[i] = new Vertex(null, i);
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                int indexA = i * 2 * size + j + size;
                int upRightA = indexA + size + 1;
                int downRightA = indexA - size;
                int upLeftA = indexA + size;
                int downLeftA = indexA - size + 1;

                int indexB = i * 2 * size + j;
                int upRightB = indexB + size;
                int downRightB = indexB - size - 1;
                int upLeftB = indexB + size - 1;
                int downLeftB = indexB - size;

                List<Edge> edgesA = new ArrayList<Edge>();
                if (i < size - 1 && j < size - 1)
                    edgesA.add(new Edge(upRightA, EdgeType.DIAGONAL_R, board[upRightA]));
                if (i < size - 1)
                    edgesA.add(new Edge(upLeftA, EdgeType.DIAGONAL_L, board[upLeftA]));
                if (j < size - 1)
                    edgesA.add(new Edge(downLeftA, EdgeType.DIAGONAL_L, board[downLeftA]));
                edgesA.add(new Edge(downRightA, EdgeType.DIAGONAL_R, board[downRightA]));
                Edge[] edgesAArray = edgesA.toArray(new Edge[edgesA.size()]);
                board[indexA].addEdges(edgesAArray);


                List<Edge> edgesB = new ArrayList<Edge>();
                edgesB.add(new Edge(upRightB, EdgeType.DIAGONAL_R, board[upRightB]));
                if (j > 0)
                    edgesB.add(new Edge(upLeftB, EdgeType.DIAGONAL_L, board[upLeftB]));
                if (i > 0)
                    edgesB.add(new Edge(downLeftB, EdgeType.DIAGONAL_L, board[downLeftB]));
                if (i > 0 && j > 0)
                    edgesB.add(new Edge(downRightB, EdgeType.DIAGONAL_R, board[downRightB]));
                Edge[] edgesBArray = edgesB.toArray(new Edge[edgesB.size()]);
                board[indexB].addEdges(edgesBArray);
            }
        }

        List<Stone> stones = new ArrayList<Stone>();
        int rowsToFill = 3;
        for (int i = 0; i < size * rowsToFill; i++) {
            stones.add(new Stone(i, Owner.PLAYER1, board[i]));
        }

        for (int i = 0; i < size * rowsToFill; i++) {
            stones.add(new Stone(i + 20, Owner.PLAYER2, board[i + 20]));
        }

        /*
        for (int i = 0; i < 9; i++) {
            stones.add(new Stone(i + 12, Owner.PLAYER2, board[i + 12]));
        }*/

        this.realState = new State(stones, 1);


    }

    private void Serpent(int size) {

        //creates the board
        int finalIndex = size * 3;
        board = new Vertex[finalIndex + 1];
        for (int i = 0; i <= finalIndex; i++) {
            board[i] = new Vertex(null, i);
        }

        for (int i = 0; i <= finalIndex; i++) {
            int upRight = i + 1;
            int downRight = i - 1;
            int upLeft = i - 2;
            int downLeft = i + 2;
            List<Edge> edges = new ArrayList<Edge>();

            if (i % 3 == 2 || i % 3 == 0) {
                if (upRight < finalIndex) {
                    edges.add(new Edge(upRight, EdgeType.DIAGONAL_R, board[upRight]));
                }
                if (upLeft >= 0) {
                    edges.add(new Edge(upLeft, EdgeType.DIAGONAL_L, board[upLeft]));
                }
            }
            if (i % 3 == 1 || i % 3 == 0) {
                if (downRight >= 0) {
                    edges.add(new Edge(downRight, EdgeType.DIAGONAL_R, board[downRight]));
                }
                if (downLeft < finalIndex) {
                    edges.add(new Edge(downLeft, EdgeType.DIAGONAL_L, board[downLeft]));
                }
            }

            if (i % 3 == 0) {
                int right = i + 3;
                int left = i - 3;
                if (right <= finalIndex)
                    edges.add(new Edge(downRight, EdgeType.HORIZONTAL, board[right]));
                if (left >= 0) edges.add(new Edge(downRight, EdgeType.HORIZONTAL, board[left]));
            }


            Edge[] edgesArray = edges.toArray(new Edge[edges.size()]);
            board[i].addEdges(edgesArray);
        }


        List<Stone> stones = new ArrayList<Stone>();
        Owner p = Owner.PLAYER1;
        for (int i = 0; i <= finalIndex; i++) {

            if (i % 3 == 1) {
                stones.add(new Stone(i, Owner.PLAYER1, board[i]));
            }
            if (i % 3 == 2) {
                stones.add(new Stone(i, Owner.PLAYER2, board[i]));
            }
            if (i % 3 == 0) {
                if (i == finalIndex / 2) {
                    p = Owner.PLAYER2;
                } else {
                    stones.add(new Stone(i, p, board[i]));
                }
            }
        }

        this.realState = new State(stones, 1);


    }

    private void Felli(int ringCount) {

        int ringSize = 6;
        int totalVs = ringCount * ringSize + 1;
        int centerIndex = totalVs - 1;

        //creates the board
        board = new Vertex[totalVs];
        for (int i = 0; i < totalVs; i++) {
            board[i] = new Vertex(null, i);
        }

        int index = 0;
        int cutTheLoops = 0;
        EdgeType t = EdgeType.DIAGONAL_R;
        for (int i = 0; i < ringCount; i++) {
            for (int j = 0; j < ringSize; j++) {
                List<Edge> edges = new ArrayList<Edge>();

                int clockwise;
                if (index == ringSize * (i + 1) - 1) clockwise = ringSize * i;
                else clockwise = index + 1;
                if (cutTheLoops != 2 && cutTheLoops != 5)
                    edges.add(new Edge(clockwise, EdgeType.HORIZONTAL, board[clockwise]));


                int counterClock;
                if (index - ringSize * i == 0) counterClock = ringSize * (i + 1) - 1;
                else counterClock = index - 1;
                if (cutTheLoops != 0 && cutTheLoops != 3)
                    edges.add(new Edge(counterClock, EdgeType.HORIZONTAL, board[counterClock]));

                int inward = index + ringSize;
                if (inward > centerIndex) inward = centerIndex;
                edges.add(new Edge(inward, t, board[inward]));

                int outward = index - ringSize;
                if (outward >= 0) edges.add(new Edge(outward, t, board[outward]));

                Edge[] edgesArray = edges.toArray(new Edge[edges.size()]);

                board[index].addEdges(edgesArray);
                index++;
                cutTheLoops++;
                if (cutTheLoops > 5) cutTheLoops = 0;
                if (index == centerIndex) break;
                if (t == EdgeType.DIAGONAL_R) t = EdgeType.VERTICAL;
                else if (t == EdgeType.VERTICAL) t = EdgeType.DIAGONAL_L;
                else if (t == EdgeType.DIAGONAL_L) t = EdgeType.DIAGONAL_R;
            }
        }
        //the center does obey the usual rules.
        List<Edge> edges = new ArrayList<Edge>();
        t = EdgeType.DIAGONAL_R;
        for (int i = ringSize; i > 0; i--) {
            int vx = centerIndex - i;
            edges.add(new Edge(vx, t, board[vx]));
            if (t == EdgeType.DIAGONAL_R) t = EdgeType.VERTICAL;
            else if (t == EdgeType.VERTICAL) t = EdgeType.DIAGONAL_L;
            else if (t == EdgeType.DIAGONAL_L) t = EdgeType.DIAGONAL_R;
        }
        Edge[] edgesArray = edges.toArray(new Edge[edges.size()]);
        board[centerIndex].addEdges(edgesArray);

        List<Stone> stones = new ArrayList<Stone>();
        for (int i = 0; i < ringSize; i++) {
            Owner p;
            if (i >= ringSize / 2) p = Owner.PLAYER2;
            else p = Owner.PLAYER1;

            stones.add(new Stone(i, p, board[i]));
            stones.add(new Stone(i + ringSize, p, board[i + ringSize]));

        }

        this.realState = new State(stones, 1);

        promotionVertexesP1 = new ArrayList<Integer>();
        promotionVertexesP1.add(5);
        promotionVertexesP1.add(3);

        promotionVertexesP2 = new ArrayList<Integer>();
        promotionVertexesP2.add(0);
        promotionVertexesP2.add(2);


    }

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

    public void Alquerque(int size) {
        if (size % 2 == 0) {
            this.size = size - 1;
        } else {
            this.size = size;
        }

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

        //Creates the components of the initial state
        //Creates the Stones
        int nStones = (size * size - 1) / 2; //Number of Stones per Player
        List<Stone> stones = new ArrayList<Stone>();

        //Center has no stone

        int boardCenter = ((size * size) - 1) / 2;

        for (int i = 0; i < nStones; i++) {
            stones.add(new Stone(i, Owner.PLAYER1, board[i]));
        }
        for (int i = 0; i < nStones; i++) {
            stones.add(new Stone(i + boardCenter + 1, Owner.PLAYER2, board[i + boardCenter + 1]));
        }

/*
        stones.add(new Stone(0, Owner.PLAYER1, board[0]));
        stones.add(new Stone(23, Owner.PLAYER2, board[23]));
        stones.get(0).setPromoted(true);
        stones.get(1).setPromoted(true);
*/
/* Test case for multiple captures
        stones.add(new Stone(8, Owner.PLAYER2, board[8]));
        stones.add(new Stone(6, Owner.PLAYER2, board[6]));
        stones.add(new Stone(10, Owner.PLAYER2, board[10]));
        stones.add(new Stone(16, Owner.PLAYER2, board[16]));
        stones.add(new Stone(18, Owner.PLAYER2, board[18]));
        stones.add(new Stone(14, Owner.PLAYER2, board[14]));
        stones.add(new Stone(7, Owner.PLAYER1, board[7]));
*/
        //Creates the Initial state:
        int turn = 1;
        State s0 = new State(stones, turn);
        this.realState = s0;

        promotionVertexesP2 = new ArrayList<Integer>();
        promotionVertexesP1 = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            promotionVertexesP2.add(i);
            promotionVertexesP1.add(i + (size * (size - 1)));
        }
    }

    public void Pretwa(int ringCount) {

        int ringSize = 6;
        int totalVs = ringCount * ringSize + 1;
        int centerIndex = totalVs - 1;

        //creates the board
        board = new Vertex[totalVs];
        for (int i = 0; i < totalVs; i++) {
            board[i] = new Vertex(null, i);
        }

        int index = 0;
        EdgeType t = EdgeType.DIAGONAL_R;
        for (int i = 0; i < ringCount; i++) {
            for (int j = 0; j < ringSize; j++) {
                List<Edge> edges = new ArrayList<Edge>();

                int clockwise;
                if (index == ringSize * (i + 1) - 1) clockwise = ringSize * i;
                else clockwise = index + 1;
                edges.add(new Edge(clockwise, EdgeType.CIRCULAR, board[clockwise]));


                int counterClock;
                if (index - ringSize * i == 0) counterClock = ringSize * (i + 1) - 1;
                else counterClock = index - 1;
                edges.add(new Edge(counterClock, EdgeType.CIRCULAR, board[counterClock]));

                int inward = index + ringSize;
                if (inward > centerIndex) inward = centerIndex;
                edges.add(new Edge(inward, t, board[inward]));

                int outward = index - ringSize;
                if (outward >= 0) edges.add(new Edge(outward, t, board[outward]));

                Edge[] edgesArray = edges.toArray(new Edge[edges.size()]);

                board[index].addEdges(edgesArray);
                index++;
                if (index == centerIndex) break;
                if (t == EdgeType.DIAGONAL_R) t = EdgeType.VERTICAL;
                else if (t == EdgeType.VERTICAL) t = EdgeType.DIAGONAL_L;
                else if (t == EdgeType.DIAGONAL_L) t = EdgeType.DIAGONAL_R;
            }
        }
        //the center does obey the usual rules.
        List<Edge> edges = new ArrayList<Edge>();
        t = EdgeType.DIAGONAL_R;
        for (int i = ringSize; i > 0; i--) {
            int vx = centerIndex - i;
            edges.add(new Edge(vx, t, board[vx]));
            if (t == EdgeType.DIAGONAL_R) t = EdgeType.VERTICAL;
            else if (t == EdgeType.VERTICAL) t = EdgeType.DIAGONAL_L;
            else if (t == EdgeType.DIAGONAL_L) t = EdgeType.DIAGONAL_R;
        }
        Edge[] edgesArray = edges.toArray(new Edge[edges.size()]);
        board[centerIndex].addEdges(edgesArray);

        //creates the stones
        List<Stone> stones = new ArrayList<Stone>();
        for (int i = 0; i < ringSize; i++) {
            Owner p;
            if (i >= ringSize / 2) p = Owner.PLAYER2;
            else p = Owner.PLAYER1;

            stones.add(new Stone(i, p, board[i]));
            stones.add(new Stone(i + ringSize, p, board[i + ringSize]));

        }

        //create the Initial State
        this.realState = new State(stones, 1);


    }

    public Owner turnOf(State state) {
        if (state.turn % 2 == 0) {
            return Owner.PLAYER2;
        } else return Owner.PLAYER1;
    }

    public Vertex[] neighboursOf(Vertex v) {
        if (v == null) return null;
        Edge[] edges = v.getEdges();
        Vertex[] adjacentVs = new Vertex[edges.length];
        for (int i = 0; i < edges.length; i++) {
            adjacentVs[i] = edges[i].getV();
        }
        return adjacentVs;
    }

    public Owner ownerOf(State state, Vertex v) {
        Owner owner = Owner.NONE;
        for (Stone s : state.stones) {
            if (s.getV() == v) {
                owner = s.getOwner();
                break; //I WANT TO BREAK FREE
            }
        }
        return owner;
    }

    public Stone stoneOf(State state, Vertex v) {
        Stone stone = null;
        for (Stone s : state.stones) {
            if (s.getV() == v) {
                stone = s;
                break;
            }
        }
        return stone;
    }

    public Stone findStoneByID(State state, int id) {
        Stone stone = null;
        for (Stone s : state.stones) {
            if (s.getId() == id) {
                stone = s;
                break;
            }
        }
        return stone;
    }

    public List<List> actions(State state) {

        List<Stone> stones = state.stones;
        int turn = state.turn;

        List<List> showMeYourMoves = new ArrayList<List>();
        Owner currentPlayer = turnOf(state);

        for (int i = 0; i < stones.size(); i++) {
            if (stones.get(i).getOwner() == currentPlayer) {
                if (capturesCheck(state, stones.get(i), null) != null) {
                    showMeYourMoves.addAll(capturesCheck(state, stones.get(i), null));
                }
            }
        }

        if (!showMeYourMoves.isEmpty()
                && compulsoryCapture
                && multipleCaptures) {
            showMeYourMoves = enforceMultipleCompulsoryCaptures(showMeYourMoves);
        }


        if (compulsoryCapture != true || showMeYourMoves.isEmpty()) {
            for (int i = 0; i < stones.size(); i++) {
                if (stones.get(i).getOwner() == currentPlayer) {
                    if (simpleMovesCheck(state, stones.get(i)) != null) {
                        showMeYourMoves.addAll(simpleMovesCheck(state, stones.get(i)));
                    }
                }
            }

        }


        return showMeYourMoves;
    }

    public boolean isTheGameOver(State state) {

        int nStones = (size * size - 1) / 2; //Number of Stones per Player
        int stonesP1 = countStones(state, Owner.PLAYER1);
        int stonesP2 = countStones(state, Owner.PLAYER2);

        List<List> possibleMoves = actions(state);
        if (!possibleMoves.isEmpty()) {
            if (state.turn >= turnLimit
                    || nStones - stonesP1 >= capturesPerPlayerLimit
                    || nStones - stonesP2 >= capturesPerPlayerLimit) {
                if (stonesP1 > stonesP2) {
                    winner = Owner.PLAYER1;
                } else winner = Owner.PLAYER2;
                return true;
            }
        } else {
            if (turnOf(state) == Owner.PLAYER1) winner = Owner.PLAYER2;
            if (turnOf(state) == Owner.PLAYER2) winner = Owner.PLAYER1;
            return true;
        }
        return false;
    }

    public Owner whoIsTheWinner(State state) {
        return winner;
    }

    private int countStones(State state, Owner player) {
        int counter = 0;
        for (Stone s : state.stones) if (s.getOwner() == player) counter++;
        return counter;
    }

    private List<List> enforceMultipleCompulsoryCaptures(List<List> possibleCaptures) {
        List<List> validCaptures = new ArrayList<List>();

        int biggestMove = possibleCaptures.get(0).size();
        for (List<Integer> m : possibleCaptures) {
            if (m.size() > biggestMove) biggestMove = m.size();
        }
        for (List<Integer> m : possibleCaptures) {
            if (m.size() >= biggestMove) validCaptures.add(m);
        }

        return validCaptures;
    }

    private List<List> capturesCheck(State state, Stone stone, List<Integer> pastCaptures) {

        List<List> captures = new ArrayList<List>();
        Owner currentPlayer = stone.getOwner();
        List<Integer> captureMove;
        if (pastCaptures == null) pastCaptures = new ArrayList<Integer>();

        //check each edge:
        for (int i = 0; i < stone.getV().getEdges().length; i++) {
            Edge edgeOne = stone.getV().getEdges()[i];

            //edge has to be linked to a vertex occupied by FOE:
            if (ownerOf(state, edgeOne.getV()) != Owner.NONE
                    && ownerOf(state, edgeOne.getV()) != currentPlayer
                    && false == pastCaptures.contains(edgeOne.getV().getId())) {

                EdgeType linkOne = edgeOne.getType();

                for (int j = 0; j < edgeOne.getV().getEdges().length; j++) {
                    Edge edgeTwo = edgeOne.getV().getEdges()[j];
                    EdgeType linkTwo = edgeTwo.getType();
                    if (linkOne == linkTwo
                            && ownerOf(state, edgeTwo.getV()) == Owner.NONE) {

                        //capture is possible
                        int rollingStoneID = stone.getId();
                        int whereTheCaptureHappens = edgeOne.getV().getId();
                        int whereTheRollingStoneLands = edgeTwo.getV().getId();

                        Vertex startingVertex = stone.getV();
                        //Stone suspendedStone = stoneOf(edgeOne.getV());

                        stone.setV(board[whereTheRollingStoneLands]);
                        //stones.remove(suspendedStone);

                        pastCaptures.add(whereTheCaptureHappens);
                        List<List> furtherCaptures = new ArrayList<List>();
                        if (multipleCaptures) {
                            furtherCaptures = capturesCheck(state, stone, pastCaptures);
                        }
                        if (!furtherCaptures.isEmpty()) {
                            for (int k = 0; k < furtherCaptures.size(); k++) {
                                captureMove = new ArrayList<Integer>();
                                captureMove.add(rollingStoneID);
                                captureMove.add(whereTheCaptureHappens);
                                captureMove.add(whereTheRollingStoneLands);
                                captureMove.addAll(furtherCaptures.get(k));
                                captures.add(captureMove);
                            }
                        } else {
                            captureMove = new ArrayList<Integer>();
                            captureMove.add(rollingStoneID);
                            captureMove.add(whereTheCaptureHappens);
                            captureMove.add(whereTheRollingStoneLands);
                            captures.add(captureMove);
                        }
                        stone.setV(startingVertex);
                        //stones.add(suspendedStone);

                    }
                }
            }
        }
        return captures;

    }

    private List<List> simpleMovesCheck(State state, Stone stone) {

        List<List> simpleMoves = new ArrayList<List>();
        Vertex[] neighbours = neighboursOf(stone.getV());
        Owner currentPlayer = stone.getOwner();

        List<Integer> move;
        List<List> promotedMoves = null;

        for (int j = 0; j < neighbours.length; j++) {
            move = new ArrayList<Integer>();
            if (ownerOf(state, neighbours[j]) == currentPlayer) {
                //move can't happen, because neighbour[j] is occupied by a friend.
                move = null;
            } else {
                if (ownerOf(state, neighbours[j]) == Owner.NONE) {
                    //simple move, because neighbour[j] is free.
                    if (stone.isPromoted()) {
                        promotedMoves = promotedSimpleMove(state, stone, neighbours[j]);
                    } else {
                        int rollingStoneID = stone.getId();
                        int rollToVertexID = neighbours[j].getId();
                        move.add(rollingStoneID);
                        move.add(rollToVertexID);

                        if (movementRestriction) {
                            int swap = 1;
                            if (currentPlayer == Owner.PLAYER2) swap = -1;
                            // #math
                            if (stone.getV().getId() * swap > rollingStoneID * swap) {
                                if (stone.getV().getId() < stone.getId())
                                    move = null;
                            }
                        }

                    }
                } else {
                    move = null;
                }
            }
            if (stone.isPromoted()) {
                assert promotedMoves != null;
                if (!promotedMoves.isEmpty()) simpleMoves.addAll(promotedMoves);
            } else {
                if (move != null) simpleMoves.add(move);
            }
        }
        return simpleMoves;
    }

    private List<List> promotedSimpleMove(State state, Stone stone, Vertex freeNeighbour) {
        EdgeType orientation = null;
        for (int i = 0; i < stone.getV().getEdges().length; i++) {
            if (stone.getV().getEdges()[i].getV() == freeNeighbour)
                orientation = stone.getV().getEdges()[i].getType();
        }
        List<List> promotedMoves = new ArrayList<List>();
        List<Integer> move = new ArrayList<Integer>();
        move.add(stone.getId());
        int notCapture = 1;
        move.add(notCapture);
        List<Integer> visitedVertexes = new ArrayList<Integer>();

        Vertex queenPosition = stone.getV();
        boolean moveMOAR = true;
        while (moveMOAR) {
            moveMOAR = false;
            for (Edge e : queenPosition.getEdges()) {
                if (e.getType() == orientation) {
                    if (ownerOf(state, e.getV()) == Owner.NONE
                            && !visitedVertexes.contains(e.getV().getId())) {

                        visitedVertexes.add(e.getV().getId());
                        move.add(e.getV().getId());
                        promotedMoves.add(new ArrayList(move));
                        queenPosition = e.getV();
                        moveMOAR = true;
                        break;

                    } else {
                        moveMOAR = false;
                    }
                }
            }
        }
        return promotedMoves;
    }

    public List<Integer> randomMove(List<List> possibleMoves) {
        Random RNGesus = new Random();
        int r = RNGesus.nextInt(possibleMoves.size());
        return possibleMoves.get(r);
    }

    public String makeTheMove(State state, List<Integer> move) {
        String moveTrace = new String();

        int rollingStoneID = move.get(0);
        Stone rollingStone = findStoneByID(state, rollingStoneID);

        if (rollingStone == null) {
            return "FAIL";
        }

        moveTrace = String.valueOf(rollingStone.getV().getId());
        int notCapture = 1;

        if (rollingStone.isPromoted() && move.get(1) == notCapture) {
            int rollTo = move.get(move.size() - 1);
            rollingStone.setV(board[rollTo]);
            moveTrace += "-rolling-";
            moveTrace += String.valueOf(rollTo);

        } else {
            if (move.size() == 2) {
                int rollTo = move.get(1);
                rollingStone.setV(board[rollTo]);
                moveTrace += "-";
                moveTrace += String.valueOf(rollTo);

            } else {
                for (int i = 0; i < move.size(); i += 3) {
                    int captorStoneID = move.get(i);
                    int whereTheCaptureHappens = move.get(i + 1);
                    int whereTheCaptureLands = move.get(i + 2);

                    Stone captorStone = findStoneByID(state, captorStoneID);
                    Stone capturedStone = stoneOf(state, board[whereTheCaptureHappens]);
                    state.stones.remove(capturedStone);
                    captorStone.setV(board[whereTheCaptureLands]);

                    moveTrace += "-";
                    moveTrace += String.valueOf(whereTheCaptureHappens);
                    moveTrace += "-";
                    moveTrace += String.valueOf(whereTheCaptureLands);

                }
            }
        }
        moveTrace += ".";
        return moveTrace;
    }

    public void updateMovesHistory(String moveTrace) {
        movesHistory.add(moveTrace);
    }

    public List<Integer> minimaxDecisionAB(State state) {
        List<Integer> move;
        MinimaxNode root = new MinimaxNode(state,
                null, null, new ArrayList<MinimaxNode>(), -1, -1, -1);

        int cutOff = 1;
        this.currentMAX = turnOf(state);
        int v = maxValue(root, cutOff, -9999, 9999);
        move = findTheMove(root, v);
        return move;
    }

    private List<Integer> findTheMove(MinimaxNode root, int v) {
        for (MinimaxNode n : root.children) {
            if (n.evaluationValue == v) return n.move;
        }
        return null;
    }

    private int maxValue(MinimaxNode node, int cutOff, int alpha, int beta) {
        if (isTheGameOver(node.state) || cutOff >= treeDepth) {
            node.evaluationValue = evaluationFunction(node.state, currentMAX, "quadraticDifference");
            return node.evaluationValue;
        }
        node.evaluationValue = -9999;

        List<List> possibleMoves = actions(node.state);

        for (List<Integer> m : possibleMoves) {

            MinimaxNode child = new MinimaxNode(node.state,
                    m, node, null, -1, -1, -1);
            makeTheMove(child.state, m);
            child.state.nextTurn();
            if (node.children == null) node.children = new ArrayList<MinimaxNode>();
            node.children.add(child);
            int resultMinValue = minValue(child, cutOff + 1, alpha, beta);
            if (node.evaluationValue < resultMinValue) node.evaluationValue = resultMinValue;
            if (node.evaluationValue >= beta) return node.evaluationValue;
            else if (alpha < node.evaluationValue) alpha = node.evaluationValue;
        }
        return node.evaluationValue;
    }

    private int minValue(MinimaxNode node, int cutOff, int alpha, int beta) {

        if (isTheGameOver(node.state) || cutOff >= treeDepth) {
            node.evaluationValue = evaluationFunction(node.state, currentMAX, "quadraticDifference");
            return node.evaluationValue;
        }

        node.evaluationValue = 9999;

        List<List> possibleMoves = actions(node.state);

        for (List<Integer> m : possibleMoves) {
            MinimaxNode child = new MinimaxNode(node.state,
                    m, node, null, -1, -1, -1);
            makeTheMove(child.state, m);
            if (node.children == null) node.children = new ArrayList<MinimaxNode>();
            node.children.add(child);
            int resultMaxValue = maxValue(child, cutOff + 1, alpha, beta);
            if (node.evaluationValue > resultMaxValue) node.evaluationValue = resultMaxValue;
            if (node.evaluationValue <= alpha) return node.evaluationValue;
            else if (beta < node.evaluationValue) beta = node.evaluationValue;
        }
        return node.evaluationValue;
    }

    public int evaluationFunction(State state, Owner player, String type) {
        if (type == "quadraticDifference") return quadraticDifference(state, player);

        return simpleCountEV(state, player);
    }

    private int quadraticDifference(State state, Owner player) {
        int dif = 0;
        int stonesP1 = countStones(state, Owner.PLAYER1);
        int stonesP2 = countStones(state, Owner.PLAYER2);
        if (player == Owner.PLAYER1) {
            dif = stonesP1 - stonesP2;
        } else {
            dif = stonesP2 - stonesP1;
        }

        if (dif > 0) dif = dif * dif;
        else dif = -(dif * dif);

        return dif;
    }

    private int simpleCountEV(State state, Owner player) {
        return countStones(state, player);
    }

    public List<Integer> translateHumanMove(List<Integer> humanMove) {
        List<List> possibleMoves = actions(realState);
        for (List<Integer> m : possibleMoves) {
            if (m.get(0) == humanMove.get(0)) {
                if (m.get(m.size() - 1) == humanMove.get(humanMove.size() - 1)) {
                    return m;
                }
            }
        }
        return null;
    }

    public void nextTurn(State state) {
        if (isPromotionOn()) {
            List<Integer> promotionVertexes;
            for (Stone s : state.stones) {
                if (s.getOwner() == Owner.PLAYER1) promotionVertexes = promotionVertexesP1;
                else promotionVertexes = promotionVertexesP2;

                if (promotionVertexes.contains(s.getV())) {
                    s.setPromoted(true);
                }
            }

            state.nextTurn();
        }
    }

}
