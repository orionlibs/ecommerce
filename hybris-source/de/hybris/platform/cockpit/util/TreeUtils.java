package de.hybris.platform.cockpit.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreeUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(TreeUtils.class);
    private static final Map<String, String[]> camelCaseMap = (Map)new ConcurrentHashMap<>();


    public static boolean matchCamelCase(String value, String searchString)
    {
        boolean ret = false;
        String[] valueTokens = value.split("(?=\\p{Lu})");
        String[] searchTokens = camelCaseMap.get(searchString);
        if(searchTokens == null)
        {
            searchTokens = searchString.split("(?=\\p{Lu})");
            camelCaseMap.put(searchString, searchTokens);
        }
        if(searchTokens.length <= valueTokens.length && searchTokens.length > 0)
        {
            boolean matchCC = true;
            for(int i = 0; i < searchTokens.length; i++)
            {
                matchCC &= matchFilterStringSimple(valueTokens[i], searchTokens[i]);
            }
            ret = matchCC;
        }
        return ret;
    }


    public static boolean matchFilterStringSimple(String value, String filterString)
    {
        return StringUtils.startsWithIgnoreCase(value, filterString);
    }


    public static boolean matchFilterString(Object data, String filterString, boolean camelCase)
    {
        boolean match = false;
        if(data instanceof String)
        {
            match = matchFilterStringSimple((String)data, filterString);
            if(!match && camelCase)
            {
                match = matchCamelCase((String)data, filterString);
            }
        }
        return match;
    }
}
