package dynamusic.system.validator;


public interface ValidatorCallback<D> {

     void executeCallback(String errorMsg, D result);
}
