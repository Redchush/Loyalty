package dynamusic.lp.validator;


import atg.droplet.DropletException;
import atg.nucleus.logging.ApplicationLogging;

import java.util.Collection;
import java.util.Dictionary;

public interface LoyaltyTransactionValidator {

    ValidationResult validateTransaction(Dictionary value);
}
