package mindtickle.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Round {
    private int roundNumber;
    private Map<User, Stats> userStatsMap;
    private int cycle;

    public Round(Queue<User> users) {
        this.roundNumber=1;
        this.userStatsMap = new HashMap<>();
        this.cycle=0;
        for(User user: users){
            userStatsMap.put(user, new Stats());
        }
    }

    public int getPlayerCurrentPos(User user){
        return userStatsMap.get(user).getPos();
    }

    public void updateRound(User user, int diceValue, int rank, int newPosition){
        Stats stats= userStatsMap.get(user);
        stats.setPos(newPosition);
        stats.setRank(rank + stats.getRank());
        stats.setDiceValue(diceValue);
        this.cycle++;

        if(cycle == userStatsMap.size()){
            printStats();
            cycle =0;
        }
    }

    private void printStats(){
        System.out.println("Round : "+ this.roundNumber);
        for(Map.Entry<User, Stats> userStats : userStatsMap.entrySet()){
            User user= userStats.getKey();
            Stats stats= userStats.getValue();

            System.out.println(user.getName()+" "+ stats.getPos()+" "+ stats.getRank()+ " "+ stats.getDiceValue());
        }
        this.roundNumber++;
    }
}
