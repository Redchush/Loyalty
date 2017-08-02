package dynamusic.lp;


import atg.dtm.TransactionDemarcationException;
import atg.repository.MutableRepository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import dynamusic.system.command.commandimpl.CreateOrUpdateItem;

import java.util.Dictionary;
import java.util.Map;

import static dynamusic.lp.LoyaltyConstants.LOYALTY_ITEM_DRN;
import static dynamusic.lp.LoyaltyConstants.PROFILE_ID_PRN;

public class CreateOrUpdateTransaction extends CreateOrUpdateItem {

    private LoyaltyManager loyaltyManager;
    private boolean createLink;

    public CreateOrUpdateTransaction() {}

    public CreateOrUpdateTransaction(LoyaltyManager loyaltyManager, Dictionary<String, Object> properties, boolean
            createLink) {
        super((MutableRepository) loyaltyManager.getLoyaltyRepository(), LOYALTY_ITEM_DRN);
        this.loyaltyManager = loyaltyManager;
        this.setDictionaryProperties(properties);
        this.createLink = createLink;
    }

    public CreateOrUpdateTransaction(LoyaltyManager loyaltyManager, Map<String, Object> properties,
                                     boolean createLink) {
        super((MutableRepository) loyaltyManager.getLoyaltyRepository(), LOYALTY_ITEM_DRN);
        this.loyaltyManager = loyaltyManager;
        this.setMapProperties(properties);
        this.createLink = createLink;
    }

    @Override
    public RepositoryItem execute() throws TransactionDemarcationException, RepositoryException {
        RepositoryItem transaction = super.execute();
        logDebug("item created: " + transaction);
        if (isCreateLink() && getUpdateId() == null ||
            isCreateLink() && getUpdateId() != null && getMapProperties().get(PROFILE_ID_PRN) != null)
        {
            getLoyaltyManager().addTransactionToUser((String) getMapProperties().get(PROFILE_ID_PRN),
                    transaction.getRepositoryId());
        }
        return transaction;
    }

    public LoyaltyManager getLoyaltyManager() {
        return loyaltyManager;
    }

    public void setLoyaltyManager(LoyaltyManager loyaltyManager) {
        this.loyaltyManager = loyaltyManager;
    }

    public boolean isCreateLink() {
        return createLink;
    }

    public void setCreateLink(boolean createLink) {
        this.createLink = createLink;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CreateOrUpdateTransaction{");
        sb.append(super.toString()).append(" ");

        sb.append("loyaltyManager=").append(loyaltyManager);
        sb.append(", createLink=").append(createLink);
        sb.append('}');
        return sb.toString();
    }
}
