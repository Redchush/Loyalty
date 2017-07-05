package dynamusic.lp.validator;


import java.util.Collections;
import java.util.List;

public class ValidationResult {

    private List<String> msg;
    private int status;

    public static final int STATUS_OK = 0;
    public static final int STATUS_FAIL = 1;

    public ValidationResult(List<String> msg, int status) {
        this.msg = msg;
        this.status = status;
    }

    public ValidationResult(List<String> msg) {
        this.msg = msg;
        setStatus(STATUS_FAIL);
    }

    public ValidationResult() {
        setStatus(STATUS_OK);
        msg= Collections.emptyList();
    }

    public List<String> getMsg() {
        return msg;
    }

    public void setMsg(List<String> msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
