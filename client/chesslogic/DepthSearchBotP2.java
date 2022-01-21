package chesslogic;

import java.util.ArrayList;

public class DepthSearchBotP2 extends Bot {
    
    private int depth;
    private int side;
    private int countUndos;
    private int countMoves;
    
    public DepthSearchBotP2(int depth, int side) {
        this.depth = depth;
        this.side = side;
    }
    
    private int score(Board b)  {
        if(b.ended()) {
            if(b.getKings()[Constants.WHITE].isChecked(b, b.getKingTiles()[Constants.WHITE])) {
                return -200;
            }
            else if(b.getKings()[Constants.BLACK].isChecked(b, b.getKingTiles()[Constants.BLACK])) {
                return 200;
            }
            else {
                return -1;
            }
        }
        else {
            int out = 0;
            for(int i = 0; i < b.getPieces().get(0).size(); i++) {
                out = out + b.getPieces().get(0).get(i).getPiece().getPoints();
            }
            for(int i = 0; i < b.getPieces().get(1).size(); i++) {
                out = out - b.getPieces().get(1).get(i).getPiece().getPoints();
            }
            return out;
        }
    }
    
    private int search(ChessGame g, int depth) {
        if(g.getCurrentPos().ended()) {
            return score(g.getCurrentPos());
        }
        else if(depth == 0) {
            return score(g.getCurrentPos());
        }
        else {
            int out;
            int temp;
            ArrayList<String> possibleMoves = legalMoves(g.getCurrentPos());
            g.move(possibleMoves.get(0).substring(0, 2), possibleMoves.get(0).substring(2, 4), possibleMoves.get(0).substring(4, 5));
            out = search(g, depth - 1);
            g.undo();
            for(int i = 1; i < possibleMoves.size(); i++) {
                g.move(possibleMoves.get(i).substring(0, 2), possibleMoves.get(i).substring(2, 4), possibleMoves.get(i).substring(4, 5));
                temp = search(g, depth - 1);
                if(temp*(1 - 2*this.side) > out*(1 - 2*this.side) && g.getCurrentPos().getToMove() == this.side) {
                    out = temp;
                }
                else if(temp*(1 - 2*this.side) < out*(1 - 2*this.side) && g.getCurrentPos().getToMove() != this.side) {
                    out = temp;
                }
                g.undo();
            }
            return out;
        }
    }
    
    public String nextMove(ChessGame g) {
        countMoves = 0;
        countUndos = 0;
        ArrayList<String> possibleMoves = legalMoves(g.getCurrentPos());
        int curScore;
        String out;
        int temp;
        if(possibleMoves.size() > 0) {
            out = possibleMoves.get(0);
            g.move(out.substring(0, 2), out.substring(2, 4), out.substring(4, 5));
            curScore = search(g, this.depth);
            g.undo();
            for(int i = 1; i < possibleMoves.size(); i++) {
                g.move(possibleMoves.get(i).substring(0, 2), possibleMoves.get(i).substring(2, 4), possibleMoves.get(i).substring(4, 5));
                temp = search(g, this.depth);
                if(temp*(1 - 2*this.side) > curScore*(1 - 2*this.side)) {
                    System.out.println(possibleMoves.get(i) + " is a better move than " + out + " with a score of " + temp + ", compared to " + curScore);
                    out = possibleMoves.get(i);
                    curScore = temp;
                }
                g.undo();
            }
            return out;
        }
        return null;
    }
    
}