package swiggy;

import swiggy.expense.ExpenseType;
import swiggy.model.User;
import swiggy.split.EqualSplit;
import swiggy.split.ExactSplit;
import swiggy.split.Split;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        User u1= new User("user1","User1","abc@gmail.com","123451" );
        User u2= new User("user2","User2","abc1@gmail.com","123452" );
        User u3= new User("user3","User3","abc2@gmail.com","123453" );
        User u4= new User("user4","User4","abc3@gmail.com","123454" );

        ExpenseManager expenseManager= new ExpenseManager();
        expenseManager.addUser(u1);
        expenseManager.addUser(u2);
        expenseManager.addUser(u3);
        expenseManager.addUser(u4);

        Scanner scanner= new Scanner(System.in);

        while(true){
            String command= scanner.nextLine();
            String[] commands= command.split(" ");
            String commandType= commands[0];

            switch (commandType){
                case "SHOW":
                    if(commands.length == 1){
                        expenseManager.showExpense();
                    }else{
                        expenseManager.showExpense(commands[1]);
                    }
                    break;

                case "EXPENSE":
                    String paidBy= commands[1];
                    double amount= Double.parseDouble(commands[2]);
                    int totalUser= Integer.parseInt(commands[3]);
                    String expenseType= commands[4+totalUser];
                    List<Split> splits= new ArrayList<>();

                    switch (expenseType){
                        case "EXACT":
                            for(int i=0;i<totalUser;i++){
                                splits.add(new ExactSplit(expenseManager.getUser(commands[4+i]), Double.parseDouble(commands[5+totalUser+i])));
                            }
                            expenseManager.createExpense(ExpenseType.EXACT,amount,splits, paidBy);
                            break;

                        case "EQUAL":
                            for(int i=0;i<totalUser;i++){
                                splits.add(new EqualSplit(expenseManager.getUser(commands[4+i])));
                            }
                            expenseManager.createExpense(ExpenseType.EQUAL,amount,splits, paidBy);
                            break;
                    }

                    break;
            }
        }
    }
}
