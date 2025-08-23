package de.hybris.platform.deeplink.services;

import de.hybris.platform.deeplink.model.rules.DeeplinkUrlModel;
import org.apache.velocity.VelocityContext;

public interface DeeplinkUrlService
{
    LongUrlInfo generateUrl(String paramString);


    String generateShortUrl(DeeplinkUrlModel paramDeeplinkUrlModel, Object paramObject);


    String parseTemplate(String paramString, VelocityContext paramVelocityContext);
}
