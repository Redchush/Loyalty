package dynamusic.lp.handler;


import atg.droplet.DropletException;
import atg.repository.servlet.RepositoryFormHandler;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import dynamusic.lp.LoyaltyConstants;
import dynamusic.lp.LoyaltyManager;
import dynamusic.system.validator.DictionaryValidator;
import dynamusic.system.validator.ValidationStrategy;
import dynamusic.system.validator.impl.AddFormExceptionCallback;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Dictionary;

public class LoyaltyFormHandler extends RepositoryFormHandler{

    private static final String PROFILE_ID_KEY = LoyaltyConstants.PROFILE_ID_PRN.toUpperCase();

    private LoyaltyManager loyaltyManager;
    private DictionaryValidator[] validators;
    private ValidationStrategy validationStrategy;

    @Override
    protected void preCreateItem(DynamoHttpServletRequest request, DynamoHttpServletResponse response)
            throws ServletException, IOException {
        if (isLoggingDebug()){
            logDebug("Pre Create transaction. Checking for valid user and necessary fields.\nValues from request : " +
                    getValue());
        }
        validationStrategy.executeValidation(validators, getValue(), new AddFormExceptionCallback(this));
    }

    @Override
    protected void postCreateItem(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse)
            throws ServletException, IOException {
        if (!this.getFormError() && getRepositoryId() != null){
            logDebug("Post create transaction. Adding transaction to user.");
            String profileId = (String) getValue().get(PROFILE_ID_KEY);
            boolean success = loyaltyManager.addTransactionToUser(profileId, getRepositoryId());
            if (!success) {
                addFormException(new DropletException("Transaction was't created by internal reasons."));
            }
        }
    }


    public LoyaltyManager getLoyaltyManager() {
        return loyaltyManager;
    }

    public void setLoyaltyManager(LoyaltyManager loyaltyManager) {
        this.loyaltyManager = loyaltyManager;
    }

    public DictionaryValidator[] getValidators() {
        return validators;
    }

    public void setValidators(DictionaryValidator[] validators) {
        this.validators = validators;
    }

    public ValidationStrategy getValidationStrategy() {
        return validationStrategy;
    }

    public void setValidationStrategy(ValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy;
    }

}
