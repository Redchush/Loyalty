package dynamusic.lp;


import java.util.*;

import static dynamusic.lp.LoyaltyConstants.AMOUNT_PRN;
import static dynamusic.lp.LoyaltyConstants.DESCRIPTION_PRN;
import static dynamusic.lp.LoyaltyConstants.PROFILE_ID_PRN;

public class CommandHelper {

    public static Map<String, Object> createPropertyMap(String profileId, Integer amount, String description,
                                                        boolean ignoreNulls){
       return createMap(new HashMap<String, Object>(), profileId, amount, description, ignoreNulls);
    }

    public static Dictionary<String, Object> createPropertyDictionary(String profileId, Integer amount, String
              description, boolean ignoreNulls){
        return (Dictionary<String, Object>) createMap(new Hashtable<String, Object>(),profileId, amount, description, ignoreNulls);
       }

    public static  Map<String, Object> createMap(Map<String, Object> result, String profileId, Integer amount,
                                                 String description, boolean ignoreNulls){
       if (!ignoreNulls){
           result.put(AMOUNT_PRN, amount);
           result.put(PROFILE_ID_PRN, profileId);
           result.put(DESCRIPTION_PRN, description);
       } else {
           if (amount != null){
               result.put(AMOUNT_PRN, amount);
           }
           if (profileId != null){
               result.put(PROFILE_ID_PRN, profileId);
           }
           if (description != null){
               result.put(DESCRIPTION_PRN, description);
           }
       }
       return result;
    }

}
