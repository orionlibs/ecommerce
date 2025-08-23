package de.hybris.platform.oauth2.util;

import de.hybris.platform.util.Config;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;

public class OIDCUtils
{
    private static final String ERROR_MESSAGE = "Server error. Can't generate id_token.";
    private static final Logger LOG = Logger.getLogger(OIDCUtils.class);


    public static String getPropertyName(String key, String clientID)
    {
        return StringUtils.isEmpty(clientID) ? ("oauth2." +
                        key) : ("oauth2." +
                        clientID + "." + key);
    }


    public static String getPropertyValue(String key, String clientID, boolean mandatory)
    {
        String value = Config.getString(getPropertyName(key, clientID), null);
        if(StringUtils.isEmpty(value) && mandatory)
        {
            LOG.warn("OAuth2 error, missing config value " + getPropertyName(key, clientID));
            throw new InvalidRequestException("Server error. Can't generate id_token.");
        }
        if(StringUtils.isEmpty(value))
        {
            value = Config.getString(getPropertyName(key, null), null);
        }
        return value;
    }
}
