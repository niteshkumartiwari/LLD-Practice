package swiggy.exception;

public class InvalidAmountException extends RuntimeException{
    public InvalidAmountException() {
        super("Invalid amount provided in split");
    }
}
