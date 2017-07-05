package dynamusic.lp.validator.impl;


import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;
import atg.nucleus.GenericService;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import dynamusic.lp.LoyaltyConfiguration;
import dynamusic.lp.validator.LoyaltyTransactionValidator;
import dynamusic.lp.validator.ValidationResult;

import javax.transaction.TransactionManager;
import java.util.Collections;
import java.util.Dictionary;

public class LoyaltyUserExistValidator extends GenericService implements LoyaltyTransactionValidator {

    private static final String USER_TYPE = "user";
    private LoyaltyConfiguration loyaltyConfiguration;
    private Repository profileRepository;
    private TransactionManager transactionManager;

    public ValidationResult validateTransaction(Dictionary value) {
        String profileIdValue = (String) value.get(loyaltyConfiguration.getProfileIdPropertyName().toUpperCase());
        _debug("Starting validate profile " + profileIdValue + " Dictionary state: " + profileIdValue);
        TransactionDemarcation td = new TransactionDemarcation();
        try {
            td.begin(getTransactionManager(), TransactionDemarcation.REQUIRED);
            try {
                RepositoryItem item = profileRepository.getItem(profileIdValue, USER_TYPE);
                if (item == null){
                    return _getUserNotExistResult(profileIdValue);
                }
            } catch (RepositoryException e) {
               return _logError(e, profileIdValue);
            }
        } catch (TransactionDemarcationException e) {
            return _logError(e, profileIdValue);
        } finally {
            try {
                td.end();
            } catch (TransactionDemarcationException e) {
               _logError(e, profileIdValue);
            }
        }
        return new ValidationResult();
    }



    private ValidationResult _getUserNotExistResult(String profileID){
        return new ValidationResult(Collections.singletonList("User for which transaction created not exist.id=" +
                profileID),
                ValidationResult.STATUS_FAIL);
    }

    private ValidationResult _logError(Exception e, String profileId){
        if (isLoggingError()){
            logError("Internal problem during validate profileId on existence " + profileId, e);
        }
        return new ValidationResult(Collections.singletonList("Internal system problem during checking profile for " +
                "transaction: id=" + profileId),
                ValidationResult.STATUS_FAIL);
    }

    private void _debug(String message){
        if (isLoggingDebug()){
            logDebug(message);
        }
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

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
