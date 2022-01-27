package mindtickle;

import mindtickle.model.Board;
import mindtickle.model.Game;
import mindtickle.model.User;
import mindtickle.model.UserType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Driver {
    public static void main(String[] args) throws IOException {
        Scanner sc= new Scanner(System.in);
        System.out.println("Give Board size: row ");
        int row= sc.nextInt();
        System.out.println("Give Board size: col ");
        int col= sc.nextInt();

        System.out.println("How many Snakes to add ? ");
        int noSnakes= sc.nextInt();
        Map<Integer,Integer> snakeMap= new HashMap<>();
        for(int i=0;i< noSnakes;i++){
            System.out.println("Give Start: ");
            int start= sc.nextInt();
            System.out.println("Give End: ");
            int end= sc.nextInt();

            snakeMap.put(start, end);
        }

        System.out.println("How many Ladders to add ? ");
        int ladders= sc.nextInt();
        Map<Integer,Integer> ladderMap= new HashMap<>();
        for(int i=0;i< ladders;i++){
            System.out.println("Give Start: ");
            int start= sc.nextInt();
            System.out.println("Give End: ");
            int end= sc.nextInt();

            ladderMap.put(start, end);
        }

        Board board= new Board(row, col, snakeMap, ladderMap);

        System.out.println("How many players to add ? ");
        int noPlayer= sc.nextInt();
        List<User> users = new ArrayList<>();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        for(int i=0;i<noPlayer;i++){
            System.out.println("Give name ? ");
            String name= reader.readLine();
            users.add(new User(name));
        }

        System.out.println("Add System Player? : (y or n) ");
        String option= reader.readLine();

        if(option.equals("y")){
            users.add(new User("SYSTEM::USER", UserType.SYSTEM));
        }

        Queue<User> userQueue= decideOrder(users);

        Game game= new Game(board, userQueue);

        while (game.getActive()){
            System.out.println("Give Dice Value: ");
            int diceValue= sc.nextInt();
            game.updateBoard(diceValue);
        }
    }

    private static Queue<User> decideOrder(List<User> users){
        Collections.shuffle(users);

        Queue<User> players= new LinkedList<>();
        for(User user: users){
            players.add(user);
        }

        if(players.peek().getUserType() == UserType.SYSTEM){
            players.add(players.poll());
        }

        return players;
    }
}
