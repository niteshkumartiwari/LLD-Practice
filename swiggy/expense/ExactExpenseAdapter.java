package swiggy.expense;

import swiggy.exception.InvalidAmountException;
import swiggy.model.User;
import swiggy.split.Split;

import java.util.List;

public class ExactExpenseAdapter implements ExpenseAdapter{
    public ExactExpenseAdapter(){
    }

    @Override
    public Expense getExpense(User paidBy, double totalAmount, List<Split> splits) {
        double totalAmountInSplit=0;

        for(Split split: splits){
            totalAmountInSplit += split.getAmount();
        }

        if(totalAmount != totalAmountInSplit){
            throw new InvalidAmountException();
        }

        return new ExactExpense(paidBy,splits, totalAmount);
    }
}
