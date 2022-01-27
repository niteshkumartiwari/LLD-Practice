package mindtickle.model;

import java.util.Queue;

public class Game {
    private Board board;
    private Queue<User> players;
    private Boolean isActive;
    private Round round;

    public Game(Board board, Queue<User> players){
        this.board= board;
        this.players= players;
        this.isActive= true;
        this.round= new Round(players);
    }

    public void updateBoard(int diceValue){
        if(!isActive) return;

        User currentPlayer= players.poll();
        players.add(currentPlayer);
        int currentPos= round.getPlayerCurrentPos(currentPlayer);
        int score= diceValue;

        int finalPos= currentPos+diceValue;
        if(board.getSnakes().containsKey(finalPos)){
            finalPos = board.getSnakes().get(finalPos);
            score = finalPos - currentPos;
        }
        else if(board.getLadders().containsKey(finalPos)){
            finalPos = board.getLadders().get(finalPos);
            score += finalPos - currentPos;
        }

        if(finalPos > 100){
            round.updateRound(currentPlayer, diceValue, 0, currentPos);
        }else{
            round.updateRound(currentPlayer, diceValue, score, finalPos);
        }

        if(finalPos == 100){
            declareWinner(currentPlayer);
            this.isActive=false;
        }

        if(isActive && players.peek().getUserType()==UserType.SYSTEM){
            updateBoard(Dice.rollDice());
        }
    }

    private void declareWinner(User user){
        System.out.println("Winner: "+ user.getName());
    }

    public Boolean getActive() {
        return isActive;
    }
}
