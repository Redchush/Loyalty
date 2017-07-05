package dynamusic.lp.exception;


public class ValidationException  extends  Exception{

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
