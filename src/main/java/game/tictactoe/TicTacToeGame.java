package game.tictactoe;

import java.util.Random;

import game.Board;
import game.Figure;
import game.Game;
import game.Player;
import game.PlayerType;
import game.SimpleMove;

public class TicTacToeGame extends Game {

    private int width = 3;
    private long seed = System.nanoTime();

    public TicTacToeGame(String id, boolean vsAI, Integer difficulty) {
        super(id);
        setCurrentPlayerNumber(1);
        board = new Board(width, width);

        players.put(1, new Player(1, PlayerType.HUMAN));
        players.put(2, new Player(2, PlayerType.HUMAN));
        playersNumber = 2;

        if (vsAI) {
            aiEngine = new TicTakToeAiEngine(this, difficulty);
            if (new Random(seed).nextBoolean()) {
                players.get(1).setType(PlayerType.AI);
            } else {
                players.get(2).setType(PlayerType.AI);
            }
        }

        if (players.get(currentPlayerNumber).isAI())
            makeMove(aiEngine.calcMove());
    }

    public void makeMove(SimpleMove move) {
        Game.print("-----------------");
        Game.print(board.toString());
        Game.print(players.get(currentPlayerNumber).toString() + " " + move.toString() + " "
                + (valid(move.getX(), move.getY(), currentPlayerNumber) ? "valid" : "fail"));

        if (finished)
            return;
        if (move.getPlayer() != currentPlayerNumber)
            return;
        if (valid(move.getX(), move.getY(), currentPlayerNumber)) {
            board.set(move.getX(), move.getY(), new Figure(-1, 1, currentPlayerNumber));
            nextPlayer();
        }

        finished = checkWin(move.getPlayer());

        Game.print(board.toString());
        Game.print(finished ? "win" : "-----------------");

        if (!finished && players.get(currentPlayerNumber).isAI()) {
            move = aiEngine.calcMove();
            makeMove(move);
        }
    }

    public boolean valid(int x, int y, int player) {
        return board.isEmpty(x, y);
    }

    public boolean checkWin(int player) {
        int emptyFields = width * width;
        for (int x = 0; x < width; x++) {
            int cntrX = 0, cntrY = 0;
            for (int y = 0; y < width; y++) {
                if (board.get(x, y).getPlayer() == player)
                    cntrX++;
                if (board.get(y, x).getPlayer() == player)
                    cntrY++;

                if (board.get(x, y).getPlayer() > 0)
                    emptyFields--;
            }
            if (cntrX == width || cntrY == width || emptyFields == 0) {
                return true;
            }
        }

        int cntr1 = 0, cntr2 = 0;
        for (int x = 0; x < width; x++) {
            if (board.get(x, x).getPlayer() == player)
                cntr1++;
            if (board.get(x, width - x - 1).getPlayer() == player)
                cntr2++;
        }
        if (cntr1 == width || cntr2 == width) {
            return true;
        }

        return false;
    }
}
