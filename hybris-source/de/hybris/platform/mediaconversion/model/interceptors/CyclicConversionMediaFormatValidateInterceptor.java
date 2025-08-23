package de.hybris.platform.mediaconversion.model.interceptors;

import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class CyclicConversionMediaFormatValidateInterceptor implements ValidateInterceptor<ConversionMediaFormatModel>
{
    public void onValidate(ConversionMediaFormatModel model, InterceptorContext ctx) throws MediaConversionModelValidationException
    {
        if(ctx.isNew(model) || ctx.isModified(model, "inputFormat"))
        {
            Set<ConversionMediaFormatModel> ancestors = new LinkedHashSet<>();
            ConversionMediaFormatModel format = model;
            while(format != null)
            {
                if(!ancestors.add(format))
                {
                    throw new MediaConversionModelValidationException("Cycle detected in input formats of '" + model + "': " +
                                    Arrays.toString(ancestors.toArray()), null, this);
                }
                format = format.getInputFormat();
            }
        }
    }
}
