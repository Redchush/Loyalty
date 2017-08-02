package dynamusic.lp.validator.impl;


import atg.repository.RepositoryItem;
import dynamusic.lp.LoyaltyConstants;
import dynamusic.system.validator.DictionaryValidator;
import dynamusic.system.validator.ValidationResult;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Set;

import static dynamusic.lp.LoyaltyConstants.LOYALTY_MANAGER_ROLE;

public class RoleValidator implements DictionaryValidator {

    private RepositoryItem profile;

    public static final String PROFILE_KEY = "CURRENT_PROFILE";

    private static final String PROFILE_ROLES_PRN = "roles";
    private static final ValidationResult INVALID_ROLE;
    private static final ValidationResult TRANSIENT;
    static {
        TRANSIENT = new ValidationResult(Collections.singletonList("User isn't log in"), ValidationResult.STATUS_FAIL);
        INVALID_ROLE = new ValidationResult(Collections.singletonList("User has invalid role"), ValidationResult.STATUS_FAIL);
    }

    public RoleValidator() {}

    public RoleValidator(RepositoryItem profile) {
        this.profile = profile;
    }

    public ValidationResult validate(Dictionary value) {
        RepositoryItem profile = this.profile == null ? (RepositoryItem) value.get(PROFILE_KEY) : getProfile();
        return profile.isTransient() ? TRANSIENT :
               !isProfileRoleValid() ? INVALID_ROLE : ValidationResult.OK_RESULT;
    }

    private boolean isProfileRoleValid(){
        Set<RepositoryItem> roles = (Set<RepositoryItem>) profile.getPropertyValue(PROFILE_ROLES_PRN);
        if (roles != null){
            for (RepositoryItem role : roles){
                Object name = role.getPropertyValue(LoyaltyConstants.ROLE_NAME_PRN);
                if (LOYALTY_MANAGER_ROLE.equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

    public RepositoryItem getProfile() {
        return profile;
    }

    public void setProfile(RepositoryItem profile) {
        this.profile = profile;
    }
}
