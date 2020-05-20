package exceptions;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException(String m){
        super(m);
    }
}
