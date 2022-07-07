package exceptions;

public class IdNotFoundException extends Exception{
    public IdNotFoundException() {
        super("ID not found.");
    }

}
