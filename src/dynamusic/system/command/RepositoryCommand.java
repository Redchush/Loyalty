package dynamusic.system.command;


import atg.dtm.TransactionDemarcationException;
import atg.repository.RepositoryException;

public interface RepositoryCommand {

    Object execute() throws TransactionDemarcationException, RepositoryException;

    Object getErrorString();

}
