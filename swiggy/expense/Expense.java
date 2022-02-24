package swiggy.expense;

import swiggy.model.User;
import swiggy.split.Split;

import java.util.List;

public abstract class Expense {
    private User paidBy;
    private List<Split> splits;
    private double totalAmount;

    public Expense(User paidBy, List<Split> splits, double totalAmount) {
        this.paidBy = paidBy;
        this.splits = splits;
        this.totalAmount = totalAmount;
    }
}
