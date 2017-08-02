package dynamusic.lp.repository;


import atg.repository.RepositoryItem;
import atg.repository.RepositoryItemImpl;
import atg.repository.RepositoryPropertyDescriptor;

import java.util.List;

import static dynamusic.lp.LoyaltyConstants.USER_TRANSACTION_PRN;

public class FindLoyaltyAmountDescriptor extends RepositoryPropertyDescriptor {

    public FindLoyaltyAmountDescriptor() {
        super();
    }

    @Override
    public Object getPropertyValue(RepositoryItemImpl pItem, Object pValue) {
        List<RepositoryItem> transactions = (List<RepositoryItem>) pItem.getPropertyValue(USER_TRANSACTION_PRN);
        return transactions != null ? findLoyaltyAmount(transactions) : 0;
    }

    private Integer findLoyaltyAmount(List<RepositoryItem> transactions){
        Integer result = 0;
        for (RepositoryItem transaction: transactions){
            Integer amount = (Integer) transaction.getPropertyValue("amount");
            if (amount != null)
            {
                result+=amount;
            }
        }
        return result;
    }

    @Override
    public boolean isQueryable() {
        return false;
    }

    @Override
    public boolean isWritable() {
        return false;
    }
}
