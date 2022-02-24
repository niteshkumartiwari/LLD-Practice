package swiggy.expense;


import swiggy.model.User;
import swiggy.split.Split;

import java.util.List;

public interface ExpenseAdapter {
    public Expense getExpense(User user, double totalAmount, List<Split> splits);
}
