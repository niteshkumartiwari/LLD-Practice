package swiggy.expense;

import swiggy.exception.InvalidExpenseType;
import swiggy.model.User;
import swiggy.split.Split;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseCommand {
    private final Map<ExpenseType, ExpenseAdapter> EXPENSE_MAP;

    public ExpenseCommand(){
        EXPENSE_MAP= new HashMap<>();
        EXPENSE_MAP.put(ExpenseType.EXACT, new ExactExpenseAdapter());
        EXPENSE_MAP.put(ExpenseType.EQUAL, new EqualExpenseAdapter());
    }

    public Expense getExpense(ExpenseType expenseType, User paidBy, double totalAmount, List<Split> splits){
        if(!EXPENSE_MAP.containsKey(expenseType)){
            throw new InvalidExpenseType(expenseType.toString());
        }

        return EXPENSE_MAP.get(expenseType).getExpense(paidBy, totalAmount, splits);
    }
}
