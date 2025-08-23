package de.hybris.platform.deeplink.resolvers;

import de.hybris.platform.deeplink.pojo.DeeplinkUrlInfo;

public interface BarcodeUrlResolver
{
    DeeplinkUrlInfo resolve(String paramString);
}
