package dynamusic.lp.exception;


public class AddItemException extends ManagerException {

    public AddItemException() {
        super();
    }

    public AddItemException(String message) {
        super(message);
    }

    public AddItemException(Throwable cause) {
        super(cause);
    }
}
