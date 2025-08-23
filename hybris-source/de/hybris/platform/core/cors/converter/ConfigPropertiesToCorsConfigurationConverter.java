package de.hybris.platform.core.cors.converter;

import de.hybris.platform.core.cors.constants.CorsConstants;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.cors.CorsConfiguration;

public class ConfigPropertiesToCorsConfigurationConverter
{
    public CorsConfiguration createCorsConfiguration(String contextName, Map<String, String> corsConfiguration)
    {
        CorsConfiguration configuration = new CorsConfiguration();
        for(Map.Entry<String, String> corsConfigurationProperty : corsConfiguration.entrySet())
        {
            CorsConstants.Key key = extractKey(corsConfigurationProperty.getKey(), contextName);
            if(key != null)
            {
                key.updateConfiguration(configuration, corsConfigurationProperty.getValue());
            }
        }
        return configuration;
    }


    private CorsConstants.Key extractKey(String hybrisPropertyKey, String contextName)
    {
        Pattern pattern = Pattern.compile("corsfilter\\." + contextName + "\\.(.*)");
        Matcher matcher = pattern.matcher(hybrisPropertyKey);
        try
        {
            if(matcher.find())
            {
                return CorsConstants.Key.valueOf(matcher.group(1));
            }
        }
        catch(IllegalArgumentException exception)
        {
            return null;
        }
        return null;
    }
}
