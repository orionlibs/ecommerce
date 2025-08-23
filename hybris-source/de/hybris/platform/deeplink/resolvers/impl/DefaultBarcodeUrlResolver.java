package de.hybris.platform.deeplink.resolvers.impl;

import de.hybris.platform.deeplink.dao.DeeplinkUrlDao;
import de.hybris.platform.deeplink.pojo.DeeplinkUrlInfo;
import de.hybris.platform.deeplink.resolvers.BarcodeUrlResolver;

public class DefaultBarcodeUrlResolver implements BarcodeUrlResolver
{
    public static final String TOKEN_VALUE_SEPARATOR = "-";
    private DeeplinkUrlDao deeplinkUrlDao;


    public DeeplinkUrlInfo resolve(String token)
    {
        String[] splittedTokenValue = token.split("-");
        Object contextObject = null;
        if(splittedTokenValue.length == 2)
        {
            contextObject = getDeeplinkUrlDao().findObject(splittedTokenValue[1]);
        }
        DeeplinkUrlInfo deeplinkUrlInfo = new DeeplinkUrlInfo();
        deeplinkUrlInfo.setContextObject(contextObject);
        deeplinkUrlInfo.setDeeplinkUrl(getDeeplinkUrlDao().findDeeplinkUrlModel(splittedTokenValue[0]));
        return deeplinkUrlInfo;
    }


    public DeeplinkUrlDao getDeeplinkUrlDao()
    {
        return this.deeplinkUrlDao;
    }


    public void setDeeplinkUrlDao(DeeplinkUrlDao deeplinkUrlDao)
    {
        this.deeplinkUrlDao = deeplinkUrlDao;
    }
}
