package dynamusic.lp.exception;


public class RemoveItemException extends ManagerException {

    public RemoveItemException() {
    }

    public RemoveItemException(String message) {
        super(message);
    }

    public RemoveItemException(Throwable cause) {
        super(cause);
    }
}
