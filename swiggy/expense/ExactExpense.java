package swiggy.expense;

import swiggy.model.User;
import swiggy.split.Split;

import java.util.List;

public class ExactExpense extends Expense{
    public ExactExpense(User paidBy, List<Split> splits, double totalAmount) {
        super(paidBy, splits, totalAmount);
    }
}
