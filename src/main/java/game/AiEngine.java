package game;

import lombok.Getter;
import lombok.Setter;

public abstract class AiEngine {

    protected Game game;
    @Getter @Setter protected int difficulty = 0;

    public AiEngine(Game game, int difficulty) {
        this.game = game;
        this.difficulty = difficulty;
    }

    public abstract SimpleMove calcMove();
}
