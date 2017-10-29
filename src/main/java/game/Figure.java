package game;

import lombok.Getter;
import lombok.Setter;

public class Figure {
    public final int id;
    @Getter @Setter private int type;
    @Getter @Setter private int player;

    public Figure(int id, int type, int player) {
        this.id = id;
        this.type = type;
        this.player = player;
    }

    public Figure copy() {
        return new Figure(this.id, this.type, this.player);
    }
}
