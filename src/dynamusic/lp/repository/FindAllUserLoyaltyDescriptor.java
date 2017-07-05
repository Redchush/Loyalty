package dynamusic.lp.repository;


import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryItemImpl;
import atg.repository.RepositoryPropertyDescriptor;
import dynamusic.lp.LoyaltyManager;

import java.util.ArrayList;

public class FindAllUserLoyaltyDescriptor extends RepositoryPropertyDescriptor{

    private LoyaltyManager loyaltyManager;

    public FindAllUserLoyaltyDescriptor() {
        super();

    }

    @Override
    public Object getPropertyValue(RepositoryItemImpl pItem, Object pValue)  {
        String repositoryId = pItem.getRepositoryId();
        try {
            if (getLoyaltyManager() == null){
                System.out.print("havn't managerL");
            } else {
                System.out.println("have managerL");
            }
            return loyaltyManager.getUserTransactions(repositoryId);
        }  catch (Exception e){
            throw new NullPointerException("manager : " + loyaltyManager);
        }
    }

    public LoyaltyManager getLoyaltyManager() {
        return loyaltyManager;
    }

    public void setLoyaltyManager(LoyaltyManager loyaltyManager) {
        this.loyaltyManager = loyaltyManager;
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
