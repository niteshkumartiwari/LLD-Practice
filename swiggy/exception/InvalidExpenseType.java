package swiggy.exception;

public class InvalidExpenseType extends RuntimeException{
    public InvalidExpenseType(String expenseType) {
        super("Invalid Expense-type : "+ expenseType);
    }
}
