package swiggy;

import swiggy.expense.Expense;
import swiggy.expense.ExpenseCommand;
import swiggy.expense.ExpenseType;
import swiggy.model.User;
import swiggy.split.Split;

import java.util.*;

public class ExpenseManager {
    private Map<String, User> userMap;
    private Map<String, Map<String, Double>> balanceSheet;
    private List<Expense> expenses;
    private ExpenseCommand expenseCommand;

    public ExpenseManager() {
        this.userMap= new HashMap<>();
        this.balanceSheet= new HashMap<>();
        this.expenses= new ArrayList<>();
        this.expenseCommand= new ExpenseCommand();
    }

    public void addUser(User user){
        userMap.put(user.getId(), user);
        balanceSheet.put(user.getId(), new HashMap<>());
    }

    public void createExpense(ExpenseType expenseType, double totalAmount, List<Split> splits, String userId){
        User paidBy= userMap.get(userId);
        Expense expense= expenseCommand.getExpense(expenseType, paidBy, totalAmount, splits);

        //Update the balance-sheet
        for(Split split: splits){
            //update for the user-who-gave
            String paidTo = split.getUser().getId();
            Map<String, Double> balance= balanceSheet.get(userId);
            if(!balance.containsKey(paidTo)){
                balance.put(paidTo, 0.0);
            }
            balance.put(paidTo, balance.get(paidTo) + split.getAmount());

            //update for the user-who-took
            balance= balanceSheet.get(paidTo);
            if(!balance.containsKey(userId)){
                balance.put(userId, 0.0);
            }
            balance.put(userId, balance.get(userId) - split.getAmount());
        }

        //Add in the expenses
        expenses.add(expense);
    }

    public void showExpense(String userId){
        Map<String, Double> balance= balanceSheet.get(userId);
        boolean noBalanceToShow= true;

        for(Map.Entry<String,Double> entry: balance.entrySet()){
            if(!entry.getValue().equals(0.0)){
                noBalanceToShow= false;
                printBalance(userId, entry.getKey(), entry.getValue());
            }
        }

        if(noBalanceToShow){
            System.out.println("No balances");
        }
    }

    public void showExpense(){
        boolean noBalanceToShow= true;
        Set<String> alreadyShown= new HashSet<>();

        for(Map.Entry<String, Map<String, Double>> entry: balanceSheet.entrySet()){
            String user1= entry.getKey();
            for(Map.Entry<String, Double> entry1: entry.getValue().entrySet()){
                if(!entry1.getValue().equals(0.0)){
                    noBalanceToShow= false;
                    if(!alreadyShown.contains(user1+"#"+entry1.getKey())){
                        printBalance(user1, entry1.getKey(), entry1.getValue());
                    }
                    alreadyShown.add(user1+"#"+entry1.getKey());
                    alreadyShown.add(entry1.getKey()+"#"+ user1);
                }
            }
        }

        if(noBalanceToShow){
            System.out.println("No balances");
        }
    }

    private void printBalance(String user1, String user2, double amount){
        if(amount < 0){//u1 owes
            System.out.println(userMap.get(user1).getName()+ " owes "+ userMap.get(user2).getName()+": "+ Math.abs(amount));
        }else{
            System.out.println(userMap.get(user2).getName()+ " owes "+ userMap.get(user1).getName()+": "+ Math.abs(amount));
        }
    }

    public User getUser(String userId){
        return userMap.get(userId);
    }
}
