package dynamusic.lp;


import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.nucleus.GenericService;
import atg.repository.*;
import atg.repository.rql.RqlStatement;
import dynamusic.lp.exception.AddItemException;
import dynamusic.lp.exception.ManagerException;
import dynamusic.lp.exception.RemoveItemException;
import dynamusic.lp.validator.LoyaltyTransactionValidator;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LoyaltyManager extends GenericService {

    private static final String USER_TYPE = "user";
    private static final String USER_TRANSACTION_PROP = "loyaltyTransactions";

    private Repository loyaltyRepository;
    private Repository profileRepository;
    private TransactionManager transactionManager;

    private LoyaltyTransactionValidator[] validators;

    private LoyaltyConfiguration loyaltyConfiguration;


    public List<RepositoryItem>  getUserTransactions(String profileId)  {
        TransactionDemarcation td = new TransactionDemarcation();
        try {
            td.begin(getTransactionManager(), TransactionDemarcation.REQUIRED);

            Repository repo = getLoyaltyRepository();
            RepositoryView loyaltyView = repo.getView(loyaltyConfiguration.getLoyaltyItemDescriptorName());
            Object rqlparams[] = new Object[1];
            rqlparams[0] = profileId;

            RqlStatement findTransactions = RqlStatement
                    .parseRqlStatement(loyaltyConfiguration.getProfileIdPropertyName()  + " = ?0");

            RepositoryItem[] items = findTransactions.executeQuery(loyaltyView, rqlparams);
            return Arrays.asList(items);

        } catch (TransactionDemarcationException e) {
            error("Demarcation exception during getting user transactions" + profileId, e);
        } catch (RepositoryException e) {
            error("Repository exception during getting user transactions" + profileId, e);
        } finally {
            try {
                td.end();
            } catch (TransactionDemarcationException e) {
                error("creating transaction demarcation failed", e);
            }
        }
        return Collections.emptyList();
    }


    public void createTransaction(String profileId, Integer amount, String description,
                                  boolean withUserLink){
        TransactionDemarcation td = new TransactionDemarcation();
        try {
            td.begin(getTransactionManager(), TransactionDemarcation.REQUIRED);

            MutableRepository repo = (MutableRepository) getLoyaltyRepository();
            MutableRepositoryItem item = repo.createItem(loyaltyConfiguration.getLoyaltyItemDescriptorName());
            item.setPropertyValue(loyaltyConfiguration.getAmountPropertyName(),amount );
            if (description!=null){
                item.setPropertyValue("description", description);
            }
            item.setPropertyValue(loyaltyConfiguration.getProfileIdPropertyName(), profileId);
            long now = System.currentTimeMillis();
            item.setPropertyValue(loyaltyConfiguration.getCreationDatePropertyName(), now);

            repo.addItem(item);
            if (withUserLink){
                addTransactionToUser(item.getRepositoryId(), profileId);
            }
        } catch (TransactionDemarcationException e) {
            error("Demarcation exception during getting user transactions" + profileId, e);
        } catch (RepositoryException e) {
            error("Repository exception during getting user transactions" + profileId, e);
        } catch (AddItemException e) {
//            nop
        } finally {
            try {
                td.end();
            } catch (TransactionDemarcationException e) {
                error("creating transaction demarcation failed", e);
            }
        }

    }



    public void addTransactionToUser(String transactionId, String profileId) throws AddItemException {

        TransactionDemarcation td = new TransactionDemarcation();
        MutableRepository userRepo = (MutableRepository) getProfileRepository();

        try {
            td.begin(getTransactionManager(), TransactionDemarcation.MANDATORY);

            MutableRepositoryItem user = userRepo.getItemForUpdate(profileId, USER_TYPE);
            RepositoryItem transaction = userRepo.getItem(transactionId, loyaltyConfiguration
                    .getLoyaltyItemDescriptorName());

            Collection userTransactions = (Collection) user.getPropertyValue(USER_TRANSACTION_PROP);

            userTransactions.add(transaction);
            user.setPropertyValue(USER_TRANSACTION_PROP, userTransactions);
            userRepo.updateItem(user);

        } catch (TransactionDemarcationException e) {
            _handleAddExceptions(transactionId, profileId, e);
            throw new AddItemException();
        } catch (RepositoryException e) {
            _handleAddExceptions(transactionId, profileId, e);
            throw new AddItemException();
        } finally {
            _closeTransactionQuietly(td);
        }
    }

    public void validateCreateTransaction(){

    }

    public void removeTransactionFromUser(String transactionId, String profileId) throws RemoveItemException {
        TransactionDemarcation td = new TransactionDemarcation();
        MutableRepository userRepo = (MutableRepository) getProfileRepository();
        try {
            td.begin(getTransactionManager(), TransactionDemarcation.MANDATORY);

            MutableRepositoryItem user = userRepo.getItemForUpdate(profileId, USER_TYPE);
            RepositoryItem transaction = userRepo.getItem(transactionId, loyaltyConfiguration
                    .getLoyaltyItemDescriptorName());

            Collection userTransactions = (Collection) user.getPropertyValue(USER_TRANSACTION_PROP);

            userTransactions.remove(transaction);
            user.setPropertyValue(USER_TRANSACTION_PROP, userTransactions);
            userRepo.updateItem(user);

        } catch (TransactionDemarcationException e) {
            _handleRempveExceptions(transactionId, profileId, e);
            throw new RemoveItemException(" demarcation failed");
        } catch (RepositoryException e) {
            _handleRempveExceptions(transactionId, profileId, e);
            throw new RemoveItemException(" internal problem with exception");
        } finally {
            _closeTransactionQuietly(td);
        }
    }

    private void _handleAddExceptions(String transactionId, String userId, Throwable e){
        final String format = " adding transaction id=%s to user id=%s. ";
        if (isLoggingError()){
            logError("Exception during " + String.format(format, transactionId, userId), e);
        }
        try {
            getTransactionManager().setRollbackOnly();
        } catch (SystemException e1) {
            if (isLoggingError()){
                logError("Fail to rollback after " + String.format(format, transactionId, userId), e);
            }
        }
    }

    private void _handleRempveExceptions(String transactionId, String userId, Throwable e){
        final String format = " removing transaction id=%s from user id=%s. ";
        if (isLoggingError()){
            logError("Exception during " + String.format(format, transactionId, userId), e);
        }
        try {
            getTransactionManager().setRollbackOnly();
        } catch (SystemException e1) {
            if (isLoggingError()){
                logError("Fail to rollback after " + String.format(format, transactionId, userId), e);
            }
        }
    }

    private void _closeTransactionQuietly(TransactionDemarcation td){
        try {
            td.end();
        } catch (TransactionDemarcationException e) {
            error("creating transaction demarcation failed", e);
        }
    }

    private void error(String msg, Exception e){
        if (isLoggingError())
            logError(msg, e);
    }

    public Repository getLoyaltyRepository() {
        return loyaltyRepository;
    }

    public void setLoyaltyRepository(Repository loyaltyRepository) {
        this.loyaltyRepository = loyaltyRepository;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public LoyaltyConfiguration getLoyaltyConfiguration() {
        return loyaltyConfiguration;
    }

    public void setLoyaltyConfiguration(LoyaltyConfiguration loyaltyConfiguration) {
        this.loyaltyConfiguration = loyaltyConfiguration;
    }

    public Repository getProfileRepository() {
        return profileRepository;
    }

    public void setProfileRepository(Repository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public LoyaltyTransactionValidator[] getValidators() {
        return validators;
    }

    public void setValidators(LoyaltyTransactionValidator[] validators) {
        this.validators = validators;
    }
}
