package dynamusic.lp.validator.impl;


import atg.core.util.StringUtils;
import atg.nucleus.GenericService;
import atg.nucleus.ServiceException;
import dynamusic.lp.LoyaltyConstants;
import dynamusic.system.validator.DictionaryValidator;
import dynamusic.system.validator.ValidationResult;

import java.util.*;

/**
 * NOTE: fh BEFORE calling first pre.. method define the type of values! If it is numeric - trim and
 * convert
 */
public class RequiredFieldDictionaryValidator extends GenericService implements DictionaryValidator {

    private static final String MSG_NOT_SET_SUFFIX = " property isn't set";
    private static final String MSG_NOT_0 = "%s must be set and can't equals 0";
    private static final String AMOUNT_KEY;
    private static final String PROFILE_ID_KEY;

    private Map<String, String> requiredPropertiesUpperCased;
    private Map<String, String> requiredProperties;

    static {
        AMOUNT_KEY = LoyaltyConstants.AMOUNT_PRN.toUpperCase();
        PROFILE_ID_KEY = LoyaltyConstants.PROFILE_ID_PRN.toUpperCase();
    }

    @Override
    public void doStartService() throws ServiceException {
        super.doStartService();
        reconfigure();
    }

    public void reconfigure(){
        if (requiredProperties == null)
        {
            this.requiredProperties = new HashMap<String, String>();
        }
        if (requiredPropertiesUpperCased == null)
        {
            requiredPropertiesUpperCased = new HashMap<String, String>();
            for(Map.Entry<String, String> entry : requiredProperties.entrySet()){
                requiredPropertiesUpperCased.put(entry.getKey().toUpperCase(), entry.getValue());
            }
        }
        requiredProperties.putAll(requiredPropertiesUpperCased);
    }

    public ValidationResult validate(Dictionary value) {
        List<String> errorMsg = new ArrayList<String>();
        if (value == null)
        {
            return new ValidationResult(Arrays.asList("No one property"), ValidationResult.STATUS_FAIL);
        }

        Enumeration<String> keys = value.keys();
        while(keys.hasMoreElements())
        {
            String element = keys.nextElement();
            if (requiredProperties.containsKey(element) && StringUtils.isBlank(element))
            {
                errorMsg.add(element + MSG_NOT_SET_SUFFIX);
            }

        }
        if (new Integer(0).equals(value.get(AMOUNT_KEY)))
        {
            errorMsg.add(String.format(MSG_NOT_0, requiredProperties.get(AMOUNT_KEY)));
        }
        return errorMsg.isEmpty() ? new ValidationResult(ValidationResult.STATUS_OK)
                                  : new ValidationResult(errorMsg, ValidationResult.STATUS_FAIL);
    }

    public Map<String, String> getRequiredPropertiesUpperCased() {
        return requiredPropertiesUpperCased;
    }

    public void setRequiredPropertiesUpperCased(Map<String, String> requiredPropertiesUpperCased) {
        this.requiredPropertiesUpperCased = requiredPropertiesUpperCased;
    }

    public Map<String, String> getRequiredProperties() {
        return requiredProperties;
    }

    public void setRequiredProperties(Map<String, String> requiredProperties) {
        this.requiredProperties = requiredProperties;
    }
}
