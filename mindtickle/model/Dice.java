package mindtickle.model;

public class Dice {
    private static int minValue=1;
    private static int maxValue=6;

    public static int rollDice(){
        int range = maxValue - minValue + 1;
        return  (int)(Math.random() * range) + minValue;
    }
}
