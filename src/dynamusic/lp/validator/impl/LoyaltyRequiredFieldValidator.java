package dynamusic.lp.validator.impl;


import dynamusic.lp.LoyaltyConfiguration;
import dynamusic.lp.validator.LoyaltyTransactionValidator;
import dynamusic.lp.validator.ValidationResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

public class LoyaltyRequiredFieldValidator implements LoyaltyTransactionValidator {

    private static final Collection VALID = Collections.EMPTY_LIST;
    private static final String MSG_NOT_SET_SUFFIX = " property isn't set";

    private LoyaltyConfiguration loyaltyConfiguration;

    public ValidationResult validateTransaction(Dictionary value) {

        List<String> msg = new ArrayList<String>();
        Object amount = value.get(getLoyaltyConfiguration().getAmountPropertyName().toUpperCase());
        if (amount == null){
            msg.add(getLoyaltyConfiguration().getAmountPropertyName() + MSG_NOT_SET_SUFFIX);
        }
        String profile = (String) value.get(getLoyaltyConfiguration().getProfileIdPropertyName().toUpperCase());
        if (profile == null || profile.length() == 0){
            msg.add(getLoyaltyConfiguration().getProfileIdPropertyName() + MSG_NOT_SET_SUFFIX);
        }
        return msg.isEmpty() ? new ValidationResult() : new ValidationResult(msg);

    }

    public LoyaltyConfiguration getLoyaltyConfiguration() {
        return loyaltyConfiguration;
    }

    public void setLoyaltyConfiguration(LoyaltyConfiguration loyaltyConfiguration) {
        this.loyaltyConfiguration = loyaltyConfiguration;
    }
}
