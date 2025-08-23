package de.hybris.platform.mediaconversion.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.mediaconversion.conversion.ImageMagickSecurityService;

public class ConversionMediaFormat extends GeneratedConversionMediaFormat
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ImageMagickSecurityService securityService = getImageMagickSecurityService();
        String conversion = (String)allAttributes.get("conversion");
        if(conversion == null)
        {
            return super.createItem(ctx, type, allAttributes);
        }
        ImageMagickSecurityService.ConvertCommandValidationResult validationResult = securityService.isCommandSecure(conversion);
        if(!validationResult.isSecure())
        {
            throw new JaloInvalidParameterException(validationResult.getMessage(), 0);
        }
        return super.createItem(ctx, type, allAttributes);
    }


    private ImageMagickSecurityService getImageMagickSecurityService()
    {
        return (ImageMagickSecurityService)Registry.getApplicationContext().getBean("imageMagickSecurityService", ImageMagickSecurityService.class);
    }
}
