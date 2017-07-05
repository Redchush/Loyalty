package dynamusic.lp.repository;


import atg.repository.RepositoryItem;
import atg.repository.RepositoryItemImpl;
import atg.repository.RepositoryPropertyDescriptor;

import java.util.List;

public class FindLoyaltyAmountDescriptor extends RepositoryPropertyDescriptor {

    public FindLoyaltyAmountDescriptor() {
        super();
    }

    @Override
    public Object getPropertyValue(RepositoryItemImpl pItem, Object pValue) {

        List transactions = (List) pItem.getPropertyValue("loyaltyTransactions");

        if (transactions != null){
            return findLoyaltyAmount(transactions);
        } else {
            return new Integer(0);
        }
    }

    private Integer findLoyaltyAmount(List transactions){
        Integer result = 0;
        for (Object transaction: transactions){
            RepositoryItem item = (RepositoryItem) transaction;
            Integer amount = (Integer) item.getPropertyValue("amount");
            if (amount != null){
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
