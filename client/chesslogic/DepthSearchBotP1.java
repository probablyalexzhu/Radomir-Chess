package chesslogic;

import java.util.ArrayList;

public class DepthSearchBotP1 extends Bot {
    
    private int depth;
    private int side;
    
    public DepthSearchBotP1(int depth, int side)  {
        this.depth = depth;
        this.side = side;
    }
    
    private int score(Board b)  {
        if(b.ended()) {
            if(b.getKings()[Constants.WHITE].isChecked(b, b.getKingTiles()[Constants.WHITE])) {
                return -60;
            }
            else if(b.getKings()[Constants.BLACK].isChecked(b, b.getKingTiles()[Constants.BLACK])) {
                return 60;
            }
            else {
                return -1;
            }
        }
        else {
            int out = 0;
            for(int i = 0; i < 64; i++) {
                if (i == 27 || i == 28 || i == 35 || i == 36) out++;
                if(b.getTiles()[i/8][i%8].getPiece() != null) {
                    out += b.getTiles()[i/8][i%8].getPiece().getPoints()*(1 - 2*this.side);
                }
            }
            return out;
        }
    }
    
    @Override
    public String nextMove(ChessGame g)  {
        ArrayList<String> possibleMoves = legalMoves(g.getCurrentPos());
        int[] bestScore;
        int[] temp;
        String bestMove;
        bestMove = possibleMoves.get(0);
        g.move(bestMove.substring(0, 2), bestMove.substring(2, 4), bestMove.substring(4, 5));
        bestScore = average(g);
        g.undo();
        for(int i = 1; i < possibleMoves.size(); i++) {
            g.move(possibleMoves.get(i).substring(0, 2), possibleMoves.get(i).substring(2, 4), possibleMoves.get(i).substring(4, 5));
            temp = average(g);
            if(temp[0]*bestScore[1] >= temp[1]*bestScore[0]) {
                bestScore = temp;
                bestMove = possibleMoves.get(i);
            }
            g.undo();
        }
        return bestMove;
    }
    
    private int[] average(ChessGame g, int tail)  {
        if(tail == 0) {
            int[] out = new int[2];
            out[0] = score(g.getCurrentPos());
            out[1] = 1;
            return out;
        }
        else if(g.getCurrentPos().ended()) {
            int[] out = new int[2];
            out[0] = score(g.getCurrentPos());
            out[1] = 1;
            return out;
        }
        else {
            ArrayList<String> possibleMoves = legalMoves(g.getCurrentPos());
            int curScore = 0;
            int leafCount = 0;
            int[] temp;
            int[] out;
            for(int i = 0; i < possibleMoves.size(); i++) {
                g.move(possibleMoves.get(i).substring(0, 2), possibleMoves.get(i).substring(2, 4), possibleMoves.get(i).substring(4, 5));
                temp = average(g, tail - 1);
                curScore += temp[0];
                leafCount += temp[1];
                g.undo();
            }
            out = new int[2];
            out[0] = curScore;
            out[1] = leafCount;
            return out;
        }
    }
    
    private int[] average(ChessGame g)  {
        return average(g, depth);
    }
}