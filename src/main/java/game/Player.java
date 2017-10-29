package game;

import lombok.Getter;
import lombok.Setter;

public class Player {
    public final int id;
    @Getter @Setter private PlayerType type;

    public Player(int id, PlayerType type) {
        this.id = id;
        this.type = type;
    }

    public String toString() {
        return "Player " + id + " (" + type + ")";
    }

    public boolean isAI() {
        return type.equals(PlayerType.AI);
    }
}
