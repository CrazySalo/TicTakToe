package game.tictactoe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.util.Combinations;

import game.AiEngine;
import game.Board;
import game.Game;
import game.SimpleMove;

public class TicTakToeAiEngine extends AiEngine {

    public SimpleMove moves[];
    //all lines - 3 horizontal, 3 vertical, 2 diagonal 
    public int lines[][] = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, { 0, 4, 8 },
            { 6, 4, 2 } };
    private boolean firstMove = true;

    public TicTakToeAiEngine(Game game, int difficulty) {
        super(game, difficulty);

        moves = new SimpleMove[game.getBoard().width * game.getBoard().height];
        int cntr = 0;
        for (int x = 0; x < game.getBoard().width; x++)
            for (int y = 0; y < game.getBoard().height; y++) {
                moves[cntr] = new SimpleMove(x, y, 0);
                cntr++;
            }

    }

    @Override
    public SimpleMove calcMove() {
        //easy level
        if (difficulty == 0) {
            List<SimpleMove> moves = validMoves();
            SimpleMove randomMove = moves.get((int)(Math.random() * moves.size()));
            return randomMove;
        }
        //first move of first player
        if (firstMove && game.getCurrentPlayerNumber() == 1) {
            firstMove = false;
            if (Math.random() < 0.5) {
                return new SimpleMove(1, 1, game.getCurrentPlayerNumber());
            } else {
                int x = Math.random() < 0.5 ? 0:2;
                int y = Math.random() < 0.5 ? 0:2;
                return new SimpleMove(x, y, game.getCurrentPlayerNumber());
            }
        }
        int players[] = { game.getCurrentPlayerNumber(), game.nextPlayer(game.getCurrentPlayerNumber())};
        //can AI or enemy win immediately?
        for (int j = 0; j < players.length; j++)
            for (int i = 0; i < lines.length; i++) {
                int an = canBeFilled(lines[i], players[j]);
                if (an != -1) {
                    SimpleMove move = moves[lines[i][an]];
                    move.setPlayer(game.getCurrentPlayerNumber());
                    return move;
                }
            }
        
        //normal level
        if (difficulty < 2) {
            List<SimpleMove> moves = validMoves();
            SimpleMove randomMove = moves.get((int)(Math.random() * moves.size()));
            return randomMove;
        }
        //impossible level
        return analyseMoves();
    }

    //find line with 2 of 3 fields filled by one player 
    protected int canBeFilled(int line[], int player) {
        int fig[] = new int[line.length];
        for (int i = 0; i < line.length; i++)
            fig[i] = game.getBoard().get(moves[line[i]].getX(), moves[line[i]].getY()).getPlayer();
        for (int i = 0; i < line.length; i++)
            if (fig[i] == 0 && fig[(i + 1) % line.length] == player && fig[(i + 2) % line.length] == player)
                return i;
        return -1;
    }

    //do we have a line fully filled by one player?
    protected boolean lineFilled(Board board, int line[], int player) {
        int fig[] = new int[line.length];
        for (int i = 0; i < line.length; i++)
            fig[i] = board.get(moves[line[i]].getX(), moves[line[i]].getY()).getPlayer();
        for (int i = 0; i < line.length; i++)
            if (fig[i] == player && fig[(i + 1) % line.length] == player && fig[(i + 2) % line.length] == player)
                return true;
        return false;
    }
    
    //return valid moves
    List<SimpleMove> validMoves() {
        List<SimpleMove> moves = new ArrayList<SimpleMove>();
        for (int x = 0; x < game.getBoard().width; x++)
            for (int y = 0; y < game.getBoard().height; y++) {
                if (game.valid(x, y, game.getCurrentPlayerNumber())) {
                    moves.add(new SimpleMove(x, y, game.getCurrentPlayerNumber()));
                }
            }
        return moves;
    }

    //return optimal move
    protected SimpleMove analyseMoves() {
        List<SimpleMove> moves = validMoves();
        int maxWeight = 0;
        int minWeight = 100;
        int enemyPlayer = game.nextPlayer(game.getCurrentPlayerNumber());
                
        List<SimpleMove> freeMoves = new ArrayList<SimpleMove>(moves.size());
        List<SimpleMove> bestMoves = new ArrayList<SimpleMove>();
        bestMoves.addAll(moves);
        int deep2 = moves.size() >= 6 ? 2 : 1;
        //we analyze next move of current player and after next 2 moves for both player: 1+deep/2+deep/2
        //we don't care about order of deep2+deep2 moves
        if (moves.size() >= deep2*2+1) {
            SimpleMove anMoves[] = new SimpleMove[deep2*2+1];
            for (int i = 0; i < moves.size(); i++) {
                anMoves[0] = moves.get(i);
                anMoves[0].setPlayer(game.getCurrentPlayerNumber());
                Set<Integer> set = new HashSet<Integer>();
                Set<Integer> enemyset = new HashSet<Integer>();
                //deep/2 moves of AI current player
                for (int[] combination: new Combinations(moves.size()-1, deep2)) {
                    freeMoves.addAll(moves);
                    freeMoves.remove(anMoves[0]);
                    for (int j = deep2 - 1; j >= 0 ; j--) {
                        anMoves[j+1] = freeMoves.get(combination[j]);
                        anMoves[j+1].setPlayer(game.getCurrentPlayerNumber());
                        freeMoves.remove(anMoves[j+1]);
                    }
                    //deep/2 moves of enemy player
                    for (int[] combinationEnemy: new Combinations(freeMoves.size(), deep2)) {
                        for (int j = deep2 - 1; j >= 0 ; j--) {
                            anMoves[j+1+deep2] = freeMoves.get(combinationEnemy[j]);
                            anMoves[j+1+deep2].setPlayer(enemyPlayer);
                        }
                        set.addAll(analyseVariant(anMoves, game.getCurrentPlayerNumber()));
                        enemyset.addAll(analyseVariant(anMoves, enemyPlayer));
                    }
                    freeMoves.clear();
                }
                System.out.println(anMoves[0].getX()+" "+anMoves[0].getY()+" "+set.size()+" "+enemyset.size()+" "+deep2);
                //it's new "best" move
                if (maxWeight < set.size() || (maxWeight == set.size() && minWeight > enemyset.size())) {
                    maxWeight = set.size();
                    minWeight = enemyset.size();
                    bestMoves.clear();
                    bestMoves.add(anMoves[0]);
                } else 
                //it's move of the same quality as "best"
                if (maxWeight == set.size() && minWeight == enemyset.size()) {
                    bestMoves.add(moves.get(i));
                }
            }
        }
        SimpleMove optimalMove = bestMoves.get((int)(Math.random() * bestMoves.size()));
        optimalMove.setPlayer(game.getCurrentPlayerNumber());
        return optimalMove;
    }

    //return numbers of filled lines
    protected Set<Integer> analyseVariant(SimpleMove move[], int player) {
        Board board = game.getBoard().copy();
        Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < move.length; i++) {
            board.get(move[i].getX(), move[i].getY()).setPlayer(move[i].getPlayer());
        }
        for (int i = 0; i < lines.length; i++) {
            if (lineFilled(board, lines[i], player)) {
                set.add(i);
            }
        }
        return set;
    }

}
