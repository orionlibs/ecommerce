package de.hybris.platform.deeplink;

import de.hybris.platform.util.Config;

public class DeeplinkUtils
{
    private static final String DEEPLINK_URL_PARAMETER_DEFAULT_NAME = "id";


    public static String getDeeplinkParameterName()
    {
        return Config.getString("deeplink.url.parameter.name", "id");
    }
}
