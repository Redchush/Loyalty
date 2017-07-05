package dynamusic.lp.handler;


import atg.droplet.DropletException;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryItem;
import atg.repository.servlet.RepositoryFormHandler;
import atg.servlet.DynamoHttpServletRequest;
import atg.servlet.DynamoHttpServletResponse;
import dynamusic.lp.LoyaltyConfiguration;
import dynamusic.lp.LoyaltyManager;
import dynamusic.lp.exception.AddItemException;
import dynamusic.lp.exception.RemoveItemException;
import dynamusic.lp.validator.LoyaltyTransactionValidator;
import dynamusic.lp.validator.ValidationResult;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Set;

public class LoyaltyFormHandler extends RepositoryFormHandler{

    private LoyaltyManager loyaltyManager;
    private LoyaltyTransactionValidator[] validators;
    private RepositoryItem profile;

    private LoyaltyConfiguration loyaltyConfiguration;
    private String invalidRoleErrorURL;
    private Boolean checkRequiredWithValidator;


    @Override
    protected void preCreateItem(DynamoHttpServletRequest request, DynamoHttpServletResponse response)
            throws ServletException, IOException {
        debug("Pre Create transaction. Checking for valid user and necessary fields.");
        if (!_checkProfileValid()){
            return;
        }
        Dictionary value = getValue();
        executeValidators(value);

    }

    @Override
    protected void postCreateItem(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse)
            throws ServletException, IOException {
        try {
            if (!this.getFormError() && getRepositoryId() != null){
                debug("Post create transaction. Adding transaction to user.");
                String profileId = (String) getValue().get(getLoyaltyConfiguration()
                                                                .getProfileIdPropertyName()
                                                                .toUpperCase());
                loyaltyManager.addTransactionToUser(getRepositoryId(),profileId);
            }
        } catch (AddItemException e) {
            addFormException(new DropletException("Transaction was't created by internal reasons."));
        }
    }

    @Override
    protected void postUpdateItemProperties(MutableRepositoryItem pItem) throws ServletException, IOException {
        super.postUpdateItemProperties(pItem);
        long now = System.currentTimeMillis();
        pItem.setPropertyValue(loyaltyConfiguration.getCreationDatePropertyName(), now);
    }

    /**
     * This functionality is eliminated. Do nothing
     */
    @Override
    public boolean handleUpdate(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse)
            throws ServletException, IOException {
        return true;
    }

    @Override
    protected void preDeleteItem(DynamoHttpServletRequest request, DynamoHttpServletResponse response)
            throws ServletException, IOException {
        debug("preDeleteItem called");
        if (!_checkProfileValid()){
            return;
        }
    }

    @Override
    protected void postDeleteItem(DynamoHttpServletRequest pRequest, DynamoHttpServletResponse pResponse)
            throws ServletException, IOException {
        try {
            if (!this.getFormError()){
                debug("Post create transaction. Adding transaction to user.");
                Object profileId = getValue().get(getLoyaltyConfiguration().getProfileIdPropertyName().toUpperCase());
                String removedTransaction =
                        (String) getValue().get(getLoyaltyConfiguration().getProfileIdPropertyName().toUpperCase());

                loyaltyManager.removeTransactionFromUser(removedTransaction, (String) profileId);
            }
        } catch (RemoveItemException e) {
            addFormException(new DropletException("Transaction was't deleted by internal reasons."));
        }
    }

    private boolean _checkProfileValid(){
        if (profile.isTransient()){
            addFormException(new DropletException("User isn't log in"));
            return false;
        }

        if (!_isProfileRoleValid()){
            addFormException(new DropletException("User has invalid role"));
            return false;
        }
        return true;
    }

    private boolean _isProfileRoleValid(){
        Set roles = (Set) profile.getPropertyValue("roles");
        if (roles != null){
            for (Object item : roles){
                RepositoryItem role = (RepositoryItem) item;
                Object name = role.getPropertyValue("name");
                if (loyaltyConfiguration.getLoyaltyManagerRole().equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean executeValidators(Dictionary value){
        debug("Values from request : " + value);
        if (getValidators() != null && checkRequiredWithValidator != null && checkRequiredWithValidator){
            if (isLoggingDebug()){
                logDebug(Arrays.toString(validators));
            }
            for (LoyaltyTransactionValidator validator : getValidators()){
                if (validator != null){
                    ValidationResult validationResult = validator.validateTransaction(value);
                    if (validationResult.getStatus() != ValidationResult.STATUS_OK){
                        for (String result: validationResult.getMsg()){
                            addFormException(new DropletException(result));
                        }
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void debug(String message){
        if (isLoggingDebug()){
            logDebug(message);
        }
    }

    private void setURLToAll(String url, boolean error){
        if (error){
            this.setCreateErrorURL(url);
            this.setDeleteErrorURL(url);
            this.setUpdateErrorURL(url);
        } else {
            this.setCreateSuccessURL(url);
            this.setDeleteSuccessURL(url);
            this.setUpdateSuccessURL(url);
        }
    }

    public LoyaltyManager getLoyaltyManager() {
        return loyaltyManager;
    }

    public void setLoyaltyManager(LoyaltyManager loyaltyManager) {
        this.loyaltyManager = loyaltyManager;
    }

    public RepositoryItem getProfile() {
        return profile;
    }

    public void setProfile(RepositoryItem profile) {
        this.profile = profile;
    }

    public String getInvalidRoleErrorURL() {
        return invalidRoleErrorURL;
    }

    public void setInvalidRoleErrorURL(String invalidRoleErrorURL) {
        this.invalidRoleErrorURL = invalidRoleErrorURL;
        setURLToAll(invalidRoleErrorURL, true);

    }

    public LoyaltyTransactionValidator[] getValidators() {
        return validators;
    }

    public void setValidators(LoyaltyTransactionValidator[] validators) {
        this.validators = validators;
    }

    public LoyaltyConfiguration getLoyaltyConfiguration() {
        return loyaltyConfiguration;
    }

    public void setLoyaltyConfiguration(LoyaltyConfiguration loyaltyConfiguration) {
        this.loyaltyConfiguration = loyaltyConfiguration;
    }

    public Boolean getCheckRequiredWithValidator() {
        return checkRequiredWithValidator;
    }

    public void setCheckRequiredWithValidator(Boolean checkRequiredWithValidator) {
        this.checkRequiredWithValidator = checkRequiredWithValidator;
    }
}
