package game;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public abstract class Game {

    public abstract void makeMove(SimpleMove move);

    public abstract boolean checkWin(int player);

    public abstract boolean valid(int x, int y, int player);

    public Game(String id) {
        this.id = id;
        players = new HashMap<Integer, Player>();
    }

    @Getter @Setter protected int currentPlayerNumber;
    @Getter @Setter protected int playersNumber;
    @Getter protected Board board;
    public final String id;
    @Getter @Setter protected boolean finished = false;
    public final Map<Integer, Player> players;
    protected AiEngine aiEngine;

    public static void print(String str) {
        System.out.println(str);
    }

    // for sea battle it will be calculated value
    protected boolean changePlayer() {
        return true;
    }

    protected void nextPlayer() {
        currentPlayerNumber = nextPlayer(currentPlayerNumber);
    }
    
    public int nextPlayer(int player) {
        if (changePlayer()) {
            player++;
            if (player > playersNumber)
                player = 1;
        }
        return player;
    }
}
