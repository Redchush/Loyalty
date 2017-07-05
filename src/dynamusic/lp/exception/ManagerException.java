package dynamusic.lp.exception;


public class ManagerException extends Exception {

    public ManagerException() {
    }

    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(Throwable cause) {
        super(cause);
    }
}
