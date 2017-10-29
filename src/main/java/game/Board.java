package game;

public class Board {
    public final Figure[][] fields;
    public final int width;
    public final int height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        fields = new Figure[width][];
        for (int i = 0; i < width; i++)
            fields[i] = new Figure[height];
        init();
    }

    public void init() {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                fields[i][j] = new EmptyField();
    }

    public Board copy() {
        Board result = new Board(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                result.fields[i][j] = fields[i][j].copy();
        return result;
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                result += fields[i][j].getPlayer();
            result += "\n";
        }
        return result;
    }

    public Figure get(int x, int y) {
        return fields[x][y];
    }

    public boolean isEmpty(int x, int y) {
        return fields[x][y].getType() == 0;
    }

    public void set(int x, int y, Figure value) {
        fields[x][y] = value;
    }
}
