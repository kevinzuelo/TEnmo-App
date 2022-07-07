package exceptions;

public class InvalidAccountException extends Exception{

    public InvalidAccountException() {
        super("Please enter a valid account ID.");
    }
}
