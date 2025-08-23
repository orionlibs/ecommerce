package de.hybris.platform.mediaconversion.model.interceptors;

import de.hybris.platform.mediaconversion.conversion.ImageMagickSecurityService;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import org.apache.commons.lang.StringUtils;

public class ConvertCommandMediaFormatValidateInterceptor implements ValidateInterceptor<ConversionMediaFormatModel>
{
    private final ImageMagickSecurityService securityService;


    public ConvertCommandMediaFormatValidateInterceptor(ImageMagickSecurityService securityService)
    {
        this.securityService = securityService;
    }


    public void onValidate(ConversionMediaFormatModel model, InterceptorContext ctx) throws InterceptorException
    {
        String conversion = model.getConversion();
        if(StringUtils.isBlank(conversion))
        {
            return;
        }
        ImageMagickSecurityService.ConvertCommandValidationResult validationResult = this.securityService.isCommandSecure(conversion);
        if(!validationResult.isSecure())
        {
            throw new MediaConversionModelValidationException(validationResult.getMessage(), this);
        }
    }
}
