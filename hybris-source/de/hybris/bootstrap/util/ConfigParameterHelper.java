package de.hybris.bootstrap.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigParameterHelper
{
    public static Map<String, String> getParametersMatching(Map<String, String> origParams, String keyRegExp, boolean stripMatchingKey)
    {
        Map<String, String> ret = null;
        Pattern pattern = Pattern.compile(keyRegExp, 2);
        for(Map.Entry<String, String> e : origParams.entrySet())
        {
            Matcher matcher = pattern.matcher(e.getKey());
            if(matcher.matches())
            {
                if(ret == null)
                {
                    ret = new HashMap<>(origParams.size() + 1, 1.0F);
                }
                if(stripMatchingKey)
                {
                    ret.put(matcher.group(1), e.getValue());
                    continue;
                }
                ret.put(e.getKey(), e.getValue());
            }
        }
        return (ret != null) ? ret : Collections.<String, String>emptyMap();
    }
}
