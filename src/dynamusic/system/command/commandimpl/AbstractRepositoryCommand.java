package dynamusic.system.command.commandimpl;


import dynamusic.system.command.RepositoryCommand;

public abstract class AbstractRepositoryCommand implements RepositoryCommand{

    private String errorString;


    public Object getErrorString() {
        return null;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }
}
