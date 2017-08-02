package dynamusic.lp;


import atg.dtm.TransactionDemarcation;
import atg.nucleus.GenericService;
import atg.nucleus.ServiceException;
import atg.repository.MutableRepository;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.rql.RqlStatement;
import dynamusic.system.CollectionUtils;
import dynamusic.system.command.RepositoryCommandInvoker;
import dynamusic.system.command.commandimpl.GetItemListByQuery;
import dynamusic.system.command.commandimpl.change_list.AddItemToListProperty;
import dynamusic.system.command.commandimpl.change_list.ChangeListProperty;
import dynamusic.system.command.commandimpl.change_list.RemoveItemFromListProperty;
import dynamusic.system.validator.DictionaryValidator;
import dynamusic.system.validator.ValidationStrategy;
import dynamusic.system.validator.ValidatorCallback;

import java.util.*;

import static dynamusic.lp.LoyaltyConstants.*;

public class LoyaltyManager extends GenericService {

    private Repository loyaltyRepository;
    private Repository profileRepository;
    private RepositoryCommandInvoker invoker;
    private ValidationStrategy validationStrategy;
    private DictionaryValidator[] validators;

    private RqlStatement findUserTransactionsRQL;

    @Override
    public void doStartService() throws ServiceException {
        super.doStartService();
        try {
            findUserTransactionsRQL = RqlStatement.parseRqlStatement(PROFILE_ID_PRN  + " = ?0");
        } catch (RepositoryException e) {
            throw new ServiceException("Can't parse rql statements", e);
        }
        if (isLoggingDebug()){
            logDebug("Manager created: " + debugString());
        }
    }

    public List<RepositoryItem>  getUserTransactions(String profileId)  {
        Object o = getInvoker().executeAndLog(new GetItemListByQuery(getProfileRepository(),
                        LOYALTY_ITEM_DRN, findUserTransactionsRQL, new Object[]{profileId}),
                TransactionDemarcation.REQUIRED, false);

        return o==null ? Collections.<RepositoryItem>emptyList() : (List<RepositoryItem>) o;
    }

    public RepositoryItem createTransaction(String profileId, Integer amount, String description,
                                            boolean withUserLink){
        Dictionary<String, Object> propertyDictionary =
                CommandHelper.createPropertyDictionary(profileId, amount, description, false);
        boolean success = validateCreateTransaction(propertyDictionary, null);
        RepositoryItem result = null;
        if (success){
            result = (RepositoryItem) getInvoker().executeDefault(
                    new CreateOrUpdateTransaction(this, propertyDictionary,withUserLink));
        }
        return result;
    }

    public boolean validateCreateTransaction(Dictionary<String, Object> value, ValidatorCallback callback){
        return getValidationStrategy().executeValidation(getValidators(), value, callback);
    }

    public boolean validateCreateTransaction(Map<String, Object> value, ValidatorCallback callback){
        return getValidationStrategy().executeValidation(getValidators(), CollectionUtils.convertMapToDictionary(value), callback);
    }

    /**
     * @return true if operation has success, false if operation failed
     */
    public boolean addTransactionToUser(String profileId, String transactionId){
        ChangeListProperty command = fillChangeCommand(new AddItemToListProperty(), profileId, transactionId);
        return getInvoker().executeAndIsSuccess(command, TransactionDemarcation.REQUIRED, false);
    }

    /**
     * @return if operation has success
     */
    public boolean removeTransactionFromUser(String profileId, String transactionId){
        ChangeListProperty command = fillChangeCommand(new RemoveItemFromListProperty(), profileId, transactionId);
        return getInvoker().executeAndIsSuccess(command, TransactionDemarcation.REQUIRED, false);
    }

    private ChangeListProperty fillChangeCommand(ChangeListProperty command, String mainItemIdValue, String
            slaveItemIdValue){
        command.setMainRepository((MutableRepository) getProfileRepository());
        command.setSlaveRepository(getLoyaltyRepository());
        command.setMainItemType(USER_ITEM_DRN);
        command.setSlaveItemType(LOYALTY_ITEM_DRN);
        command.setMainItemIdValue(mainItemIdValue);
        command.setSlaveItemIdValue(slaveItemIdValue);
        command.setMainItemSlaveProperty(USER_TRANSACTION_PRN);
        return command;
    }


    private String debugString() {
        final StringBuilder sb = new StringBuilder("LoyaltyManager{");
        sb.append(super.toString()).append(" ");
        sb.append("loyaltyRepository=").append(loyaltyRepository);
        sb.append(", profileRepository=").append(profileRepository);
        sb.append(", invoker=").append(invoker);
        sb.append(", validationStrategy=").append(validationStrategy);
        sb.append(", validators=").append(Arrays.toString(validators));
        sb.append(", findUserTransactionsRQL=").append(findUserTransactionsRQL);
        sb.append('}');
        return sb.toString();
    }

    public Repository getLoyaltyRepository() {
        return loyaltyRepository;
    }

    public void setLoyaltyRepository(Repository loyaltyRepository) {
        this.loyaltyRepository = loyaltyRepository;
    }


    public Repository getProfileRepository() {
        return profileRepository;
    }

    public void setProfileRepository(Repository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public DictionaryValidator[] getValidators() {
        return validators;
    }

    public void setValidators(DictionaryValidator[] validators) {
        this.validators = validators;
    }

    public RepositoryCommandInvoker getInvoker() {
        return invoker;
    }

    public void setInvoker(RepositoryCommandInvoker invoker) {
        this.invoker = invoker;
    }

    public ValidationStrategy getValidationStrategy() {
        return validationStrategy;
    }

    public void setValidationStrategy(ValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy;
    }
}
