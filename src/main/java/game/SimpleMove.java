package game;

import lombok.Getter;
import lombok.Setter;

public class SimpleMove {
    @Getter @Setter private int x;
    @Getter @Setter private int y;
    @Getter @Setter private int player;

    public SimpleMove() {
    };

    public SimpleMove(int x, int y, int player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public String toString() {
        return "" + x + " " + y + " p:" + player;
    }
}
