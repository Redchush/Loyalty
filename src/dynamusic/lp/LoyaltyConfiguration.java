package dynamusic.lp;


public class LoyaltyConfiguration {

    private String idTransactionPropertyName;
    private String amountPropertyName;
    private String profileIdPropertyName;
    private String creationDatePropertyName;

    private String loyaltyItemDescriptorName;
    private String loyaltyManagerRole;

    public String getAmountPropertyName() {
        return amountPropertyName;
    }

    public void setAmountPropertyName(String amountPropertyName) {
        this.amountPropertyName = amountPropertyName;
    }

    public String getProfileIdPropertyName() {
        return profileIdPropertyName;
    }

    public void setProfileIdPropertyName(String profileIdPropertyName) {
        this.profileIdPropertyName = profileIdPropertyName;
    }

    public String getLoyaltyItemDescriptorName() {
        return loyaltyItemDescriptorName;
    }

    public void setLoyaltyItemDescriptorName(String loyaltyItemDescriptorName) {
        this.loyaltyItemDescriptorName = loyaltyItemDescriptorName;
    }

    public String getLoyaltyManagerRole() {
        return loyaltyManagerRole;
    }

    public void setLoyaltyManagerRole(String loyaltyManagerRole) {
        this.loyaltyManagerRole = loyaltyManagerRole;
    }

    public String getCreationDatePropertyName() {
        return creationDatePropertyName;
    }

    public void setCreationDatePropertyName(String creationDatePropertyName) {
        this.creationDatePropertyName = creationDatePropertyName;
    }

    public String getIdTransactionPropertyName() {
        return idTransactionPropertyName;
    }

    public void setIdTransactionPropertyName(String idTransactionPropertyName) {
        this.idTransactionPropertyName = idTransactionPropertyName;
    }
}
