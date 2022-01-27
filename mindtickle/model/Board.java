package mindtickle.model;

import java.util.Map;


public class Board {
    private final int row;
    private final int col;
    private final Map<Integer, Integer> snakes;
    private final Map<Integer, Integer> ladders;

    public Board(int row, int col, Map<Integer, Integer> snakes, Map<Integer, Integer> ladders) {
        this.row = row;
        this.col = col;
        this.snakes = snakes;
        this.ladders = ladders;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Map<Integer, Integer> getSnakes() {
        return snakes;
    }

    public Map<Integer, Integer> getLadders() {
        return ladders;
    }
}
