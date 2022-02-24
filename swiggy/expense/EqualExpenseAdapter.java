package swiggy.expense;

import swiggy.model.User;
import swiggy.split.Split;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class EqualExpenseAdapter implements ExpenseAdapter{

    public EqualExpenseAdapter(){
    }

    @Override
    public Expense getExpense(User user, double totalAmount, List<Split> splits) {
        double individualSplitAmount= (totalAmount*100/splits.size())/100.0;
        individualSplitAmount = BigDecimal.valueOf(individualSplitAmount)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        for(Split split: splits){
            split.setAmount(individualSplitAmount);
        }

        double leftOverValue= totalAmount - individualSplitAmount* splits.size();
        leftOverValue = BigDecimal.valueOf(leftOverValue)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        double amountToAdd= individualSplitAmount + leftOverValue;
        amountToAdd = BigDecimal.valueOf(amountToAdd)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        splits.get(0).setAmount(amountToAdd);

        return new EqualExpense(user, splits, totalAmount);
    }
}
