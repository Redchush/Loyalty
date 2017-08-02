package dynamusic.system.validator.impl;


import atg.droplet.DropletException;
import atg.droplet.GenericFormHandler;
import dynamusic.system.validator.ValidatorCallback;

public class AddFormExceptionCallback implements ValidatorCallback<Void>{

    private GenericFormHandler handler;

    public AddFormExceptionCallback(GenericFormHandler handler) {
        this.handler = handler;
    }

    public GenericFormHandler getHandler() {
        return handler;
    }

    public void setHandler(GenericFormHandler handler) {
        this.handler = handler;
    }

    public void executeCallback(String errorMsg, Void result) {
        handler.addFormException(new DropletException(errorMsg));
    }
}
