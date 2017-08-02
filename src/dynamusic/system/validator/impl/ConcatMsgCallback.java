package dynamusic.system.validator.impl;


import dynamusic.system.validator.ValidatorCallback;

public class ConcatMsgCallback implements ValidatorCallback<Void> {

    private StringBuilder msg = new StringBuilder("");
    private String separator = ";";

    public ConcatMsgCallback() {}

    public void executeCallback(String errorMsg, Void result) {
        msg.append(errorMsg).append(separator);
    }

    public String getMsgString(){
        return msg.toString();
    }
    public StringBuilder getMsg() {
        return msg;
    }

    public void setMsg(StringBuilder msg) {
        this.msg = msg;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
