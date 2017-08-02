package dynamusic.system.command;


import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.nucleus.GenericService;
import atg.repository.RepositoryException;

import javax.transaction.TransactionManager;

public class RepositoryCommandInvoker extends GenericService{

    private static final Object FAIL = "FAIL_COMMAND_INVOKE";
    private TransactionManager transactionManager;

    /**
     * execute with TransactionDemarcation.REQUIRED and rollback if failed, log all errors
     * @param command - command with execution code.
     * @return object returned by command or null is operation failed
     */
    public Object executeDefault(RepositoryCommand command){
        return executeAndCheckObjectFail(command, TransactionDemarcation.REQUIRED, true, false);
    }

    /**
     * execute with TransactionDemarcation.REQUIRED and rollback if failed;
     * @param command - command with execution code.
     * @return true, if operation had success, false if fail
     */
    public boolean executeAndIsSuccessDefault(RepositoryCommand command){
        return executeAndIsSuccess(command, TransactionDemarcation.REQUIRED, true);
    }

    /**
     * @return object returned by command or RepositoryCommandInvoker.FAIL is operation failed
     **/
    public Object executeAndLog(RepositoryCommand command, int transactionLevel,
                                boolean rollback){
        return executeAndCheckObjectFail(command, transactionLevel, rollback, true);
    }

    /**
     * For use if command.execute() return nothing (null)
     * @return true, if operation had success, false if fail
     */
    public boolean executeAndIsSuccess(RepositoryCommand command, int transactionLevel,
                                       boolean rollback){

        Object o = executeAndCheckObjectFail(command, transactionLevel, rollback, true);
        return !isFail(o);
    }

    /**
     * For use if command.execute() return it's result. Return special object FAIL indicating
     * that execution failed;
     */
    public Object executeAndCheckObjectFail(RepositoryCommand command, int transactionLevel,
                                            boolean rollback, boolean checkFailure){
        if (isLoggingDebug()){
            logDebug(" proceed to execute " + command);
        }
        TransactionDemarcation td = new TransactionDemarcation();
        boolean isErrorOccured = true;
        try {
            td.begin(getTransactionManager(), transactionLevel);
            Object result = command.execute();
            isErrorOccured = false;
            return result;
        } catch (TransactionDemarcationException e) {
            logError("Demarcation exception :" + command.getErrorString(), e);
        } catch (RepositoryException e) {
            logError("Repository exception :" + command.getErrorString(), e);
        } finally {
            if (rollback && isErrorOccured){
                closeTransactionAndRollback(td);
            } else {
                closeTransactionQuietly(td);
            }
        }
        return checkFailure ? FAIL : null;
    }

    public boolean isFail(Object returnedObject){
        return FAIL.equals(returnedObject);
    }

    private void closeTransactionQuietly(TransactionDemarcation td){
        try {
            td.end();
        } catch (TransactionDemarcationException e) {
            logError("creating transaction demarcation failed", e);
        }
    }

    private void closeTransactionAndRollback(TransactionDemarcation td){

    }


    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
