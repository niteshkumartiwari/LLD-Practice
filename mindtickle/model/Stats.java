package mindtickle.model;

public class Stats {
    private int rank;
    private int pos;
    private int diceValue;

    public Stats(int rank, int pos, int diceValue) {
        this.rank = rank;
        this.pos = pos;
        this.diceValue = diceValue;
    }

    public Stats() {
        this.rank= 0;
        this.pos= 1;
        this.diceValue=0;
    }

    public int getRank() {
        return rank;
    }

    public int getPos() {
        return pos;
    }

    public int getDiceValue() {
        return diceValue;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setDiceValue(int diceValue) {
        this.diceValue = diceValue;
    }
}
