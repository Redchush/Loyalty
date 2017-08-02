package dynamusic.system;


import java.util.*;

public class CollectionUtils {

    public static Map<String, Object> convertDictionaryToMap(Dictionary<String, Object> dictionary){
        Map<String, Object> result = new HashMap<String, Object>();
        Enumeration<String> keys = dictionary.keys();
        while(keys.hasMoreElements())
        {
            String descriptor = keys.nextElement();
            result.put(descriptor, dictionary.get(descriptor));
        }
        return result;
    }

    public static Dictionary<String, Object> convertMapToDictionary(Map<String, Object> map){
        Dictionary<String, Object> result = new Hashtable<String, Object>();
        for(Map.Entry<String, Object> entry : map.entrySet())
        {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
