package chesslogic;

import java.util.ArrayList;
import java.util.Collections;
import java.lang.InterruptedException;

/**
 * [RadomirBot.java]
 * Multithreaded depth search bot
 * @author 
 * @version 1.0 Jan 24, 2022
 * Each thread added increases the speed marginally. Tested with intel i3-8130U, 12 GB RAM (not sure what type, too lazy to find out)
 */

public class RadomirBot extends Bot {
    
    private int depth;
    private int side;
    private int countUndos;
    private int countMoves;
    
    private int[] directionXOne, directionYOne, directionXTwo, directionYTwo;
    private RunSearch[] runnables;
    private Thread[] threads;
    private String[] myMoves;
    private int[] myScores;
    private int numThreads;
    
    private OpeningTreeV2 opening;
    private boolean isOpening;
    
    
    public RadomirBot(int depth, int side, int threads) {
        opening = new OpeningTreeV2();
        isOpening = true;
        this.depth = depth;
        this.side = side;
        directionXOne = new int[] {-1, -1, 1, 1, 0, 0, -1, 1};
        directionYOne = new int[] {1, -1, -1, 1, -1, 1, 0, 0};
        directionXTwo = new int[] {-2, 2, -2, 2, 0, 0, -2, 2, -1, 1, -2, 2, -1, 1, -2, 2};
        directionYTwo = new int[] {0, 0, -2, 2, -2, 2, 2, -2, 2, 2, -1, -1, -2, -2, 1, 1};
        runnables = new RunSearch[threads];
        this.threads = new Thread[threads];
        myMoves = new String[threads];
        myScores = new int[threads];
        this.numThreads = threads;
    }
    
    public int getDepth(){
        return this.depth;
    }
    
    public String nextMove(ChessGame g) {
        System.out.println("Opening depth: " + opening.depth());
        System.out.println("Game depth: " + g.getCurrentPos().getTurn());
        System.out.println("isOpening: " + isOpening);
        if(g.getStringMoves().isEmpty()) {
            isOpening = true;
        }
        else if(opening.depth() >= g.getCurrentPos().getTurn()) {
            System.out.println("in loop");
            for(int i = opening.depth(); i >= g.getCurrentPos().getTurn(); i--) {
                opening.prevMove();
            }
            if(opening.getData() != null) {
                System.out.println("Current move: " + opening.getData());
            }
            System.out.println("Next openings: " + opening.getNextMoves());
            System.out.println("Move to check: " + g.getStringMoves().peek().substring(0, 4));
            if(opening.hasNext(g.getStringMoves().peek().substring(0, 4) + " ")) {
                isOpening = true;
                System.out.println("true");
            }
            else {
                isOpening = false;
                System.out.println("false");
            }
            System.out.println("out of loop");
        }
        
        if(isOpening) {
            System.out.println("checking openings");
            if(g.getStringMoves().isEmpty()) { //occurs only if bot is white
                System.out.println("it is turn 1");
                return opening.getRandomMove();
            }
            else {
                System.out.println("it is not turn 1");
                String prevMove = g.getStringMoves().peek().substring(0, 4) + " ";
                System.out.println(prevMove);
                if(!opening.hasNext()) {
                    isOpening = false;
                    System.out.println("this node is a leaf");
                }
                else if(!opening.hasNext(prevMove)) {
                    isOpening = false;
                    System.out.println("this node does not contain " + prevMove);
                    System.out.println("Available moves: " + opening.getNextMoves());
                }
                else {
                    opening.nextMove(prevMove);
                    System.out.println("this move works");
                    if(opening.hasNext()) {
                        System.out.println("Found in opening database, ending search");
                        return opening.getRandomMove();
                    }
                    else {
                        System.out.println("This move is a leaf");
                        isOpening = false;
                    }
                }
            }
        }
        
        ArrayList<String> moves = legalMoves(g.getCurrentPos());
        ArrayList<ArrayList<String>> partition = new ArrayList<ArrayList<String>>();
        for(int i = 0; i < this.numThreads && i < moves.size(); i++) {
            partition.add(new ArrayList<String>());
        }
        for(int i = 0; i < moves.size(); i++) {
            partition.get(i%numThreads).add(moves.get(i));
        }
        for(int i = 0; i < numThreads && i < moves.size(); i++) {
            runnables[i] = new RunSearch(g.copy(), this.depth, this.side, partition.get(i), this.myMoves, this.myScores, i, this.directionXOne, this.directionYOne, this.directionXTwo, this.directionYTwo);
            threads[i] = new Thread(runnables[i]);
            threads[i].start();
        }
        for(int i = 0; i < numThreads && i < moves.size(); i++) {
            try {
                threads[i].join();
            } catch(InterruptedException e) {e.printStackTrace(); }
        }
        int index = 0;
        int temp = myScores[0];
        for(int i = 1; i < numThreads && i < moves.size(); i++) {
            if(myScores[i] > temp) {
                temp = myScores[i];
                index = i;
            }
        }
        return myMoves[index];
    }
    
    //all calculations are done in separate threads
    private class RunSearch implements Runnable {
        
        private int side;
        private ChessGame game;
        private int depth;
        private ArrayList<String> check;
        
        private String[] myMoves;
        private int[] myScores;
        private int mySection;
        
        private int[][] attackPoints;
        private int[][] placementPoints;
        private int[] directionXOne, directionYOne, directionXTwo, directionYTwo;
        
        RunSearch(ChessGame g, int depth, int side, ArrayList<String> toCheck, String[] myMoves, int[] myScores, int mySection, int[] directionXOne, int[] directionYOne, int[] directionXTwo, int[] directionYTwo) {
            this.depth = depth;
            this.side = side;
            this.game = g;
            this.check = toCheck;
            this.myMoves = myMoves;
            this.myScores = myScores;
            this.mySection = mySection;
            placementPoints = new int[8][8];
            attackPoints = new int[8][8];
            this.directionXOne = directionXOne;
            this.directionYOne = directionYOne;
            this.directionXTwo = directionXTwo;
            this.directionYTwo = directionYTwo;
            resetPlacementPoints();
        }
        
        public boolean check(int x, int y){
            if (x >= 0 && x < 8 && y >= 0 && y < 8) return true;
            return false;
        }
        
        public void resetAttackPoints(int x, int y){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    attackPoints[i][j] = 3;
                }
            }
            for (int i = 0; i < 8; i++) {
                attackPoints[i][0] = 1;
                attackPoints[i][7] = 1;
                attackPoints[0][i] = 1;
                attackPoints[7][i] = 1;
            }
            for (int i = 1; i < 7; i++) {
                attackPoints[i][1] = 2;
                attackPoints[i][6] = 2;
                attackPoints[1][i] = 2;
                attackPoints[6][i] = 2;
            }
            attackPoints[x][y] += 4;
            for (int i = 0; i < 16; i++){
                if (check(x + directionXTwo[i], y + directionYTwo[i])){
                    attackPoints[x + directionXTwo[i]][y + directionYTwo[i]] += 2;
                }
            }
            for (int i = 0; i < 8; i++){
                if (check(x + directionXOne[i], y + directionYOne[i])){
                    attackPoints[x + directionXOne[i]][y + directionYOne[i]] += 1;
                }
            }
        }
        
        public void resetPlacementPoints(){
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    placementPoints[i][j] = 3;
                }
            }
            for (int i = 0; i < 8; i++) {
                placementPoints[i][0] = 1;
                placementPoints[i][7] = 1;
                placementPoints[0][i] = 1;
                placementPoints[7][i] = 1;
            }
            for (int i = 1; i < 7; i++) {
                placementPoints[i][1] = 2;
                placementPoints[i][6] = 2;
                placementPoints[1][i] = 2;
                placementPoints[6][i] = 2;
            }
        }
        
        private int score(Board b)  {
            int out = 0;
            if(b.ended()) {
                if(b.getKings()[Constants.WHITE].isChecked(b, b.getKingTiles()[Constants.WHITE])) {
                    out = -1000;
                }
                else if(b.getKings()[Constants.BLACK].isChecked(b, b.getKingTiles()[Constants.BLACK])) {
                    out = 1000;
                }
                else out = 0;
            }
            for(int i = 0; i < b.getPieces().get(0).size(); i++) {
                out = out + b.getPieces().get(0).get(i).getPiece().getPoints();
            }
            for(int i = 0; i < b.getPieces().get(1).size(); i++) {
                out = out - b.getPieces().get(1).get(i).getPiece().getPoints();
            }
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    if (b.getTiles()[i][j].getPiece() != null){
                        if (b.getTiles()[i][j].getPiece().getColour() == 0) out += placementPoints[i][j];
                        else out -= placementPoints[i][j];
                    }
                }
            }
            resetAttackPoints(b.getKingTiles()[1].getX(), b.getKingTiles()[1].getY());
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    if (b.getTiles()[i][j].getPiece() != null && b.getTiles()[i][j].getPiece().getColour() == 0){
                        for (Tile t : b.getTiles()[i][j].getPiece().range(b, b.getTiles()[i][j])){
                            out += attackPoints[t.getX()][t.getY()];
                        }
                    }
                }
            }
            resetAttackPoints(b.getKingTiles()[0].getX(), b.getKingTiles()[0].getY());
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    if (b.getTiles()[i][j].getPiece() != null && b.getTiles()[i][j].getPiece().getColour() == 1){
                        for (Tile t : b.getTiles()[i][j].getPiece().range(b, b.getTiles()[i][j])){
                            out -= attackPoints[t.getX()][t.getY()];
                        }
                    }
                }
            }
            return out * (b.getToMove() == 1 ? -1 : 1);
        }
        
        public int search(ChessGame g, int depth, int alpha, int beta, int cnt) {
            if(g.getCurrentPos().ended()) {
                return score(g.getCurrentPos());
            }
            else if(depth == 0) {
                return score(g.getCurrentPos());
            }
            else {
                int temp;
                ArrayList<Move> possibleMoves;
                if(cnt == 0) {
                    possibleMoves = sortMoves(game.getCurrentPos(), check);
                }
                else {
                    possibleMoves = sortMoves(g.getCurrentPos(), legalMoves(g.getCurrentPos()));
                }
                for(int i = 0; i < possibleMoves.size(); i++) {
                    String curMove = possibleMoves.get(i).move;
                    g.move(curMove.substring(0, 2), curMove.substring(2, 4), curMove.substring(4, 5));
                    temp = -search(g, depth - 1, -beta, -alpha, cnt+1);
                    g.undo();
                    if (temp >= beta){
                        return beta;
                    }
                    if (temp > alpha){
                        alpha = temp;
                        if (cnt == 0){
                            myMoves[mySection] = curMove;
                            myScores[mySection] = alpha;
                        }
                    }
                }
            }
            return alpha;
        }
        
        public ArrayList<Move> sortMoves(Board b, ArrayList<String> temp){
            ArrayList<Move> sortedMoves = new ArrayList<>();
            for (String move : temp){
                int[] curPos = Constants.chessToCoord(move.substring(0, 2));
                int[] newPos = Constants.chessToCoord(move.substring(2, 4));
                String promotion = move.substring(4, 5);
                int score = 0;
                if (b.getTiles()[newPos[0]][newPos[1]].getPiece() != null)
                    score += (b.getTiles()[newPos[0]][newPos[1]].getPiece().getPoints());
                score += placementPoints[newPos[0]][newPos[1]];
                if (promotion != null){
                    if (promotion.equals("Q")) score += Constants.QUEEN_POINTS;
                    if (promotion.equals("R")) score += Constants.ROOK_POINTS;
                    if (promotion.equals("B")) score += Constants.BISHOP_POINTS;
                    if (promotion.equals("N")) score += Constants.KNIGHT_POINTS;
                }
                sortedMoves.add(new Move(move, score));
            }
            Collections.sort(sortedMoves);
            return sortedMoves;
        }
        
        @Override
        public void run() {
            search(game, this.depth, -99999, 99999, 0);
        }
        
    }
    
}