package dynamusic.system.validator;


import java.util.Collections;
import java.util.List;

public class ValidationResult<D> {

    private List<String> errorMsg;
    private int status;
    private D result;

    public static final int STATUS_OK = 0;
    public static final int STATUS_FAIL = 1;
    public static final int STATUS_FAIL_AND_RESULT = 2;

    public static final ValidationResult OK_RESULT = new ValidationResult(STATUS_OK);

    public ValidationResult() {
        setStatus(STATUS_OK);
        errorMsg = Collections.emptyList();
    }

    public ValidationResult(int status) {
        this.status = status;
        errorMsg = Collections.emptyList();
    }

    public ValidationResult(List<String> errorMsg, int status) {
        this.errorMsg = errorMsg;
        this.status = status;
    }

    public ValidationResult(List<String> errorMsg) {
        this.errorMsg = errorMsg;
        setStatus(STATUS_FAIL);
    }

    public List<String> getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(List<String> errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public D getResult() {
        return result;
    }

    public void setResult(D result) {
        this.result = result;
    }
}
