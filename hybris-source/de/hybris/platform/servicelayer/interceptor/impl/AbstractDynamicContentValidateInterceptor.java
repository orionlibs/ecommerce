package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.core.model.AbstractDynamicContentModel;
import de.hybris.platform.dynamiccontent.DynamicContentChecksumCalculator;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Required;

public class AbstractDynamicContentValidateInterceptor implements ValidateInterceptor<AbstractDynamicContentModel>
{
    private DynamicContentChecksumCalculator dynamicContentChecksumCalculator;
    private FlexibleSearchService flexibleSearchService;


    @Required
    public void setDynamicContentChecksumCalculator(DynamicContentChecksumCalculator dynamicContentChecksumCalculator)
    {
        this.dynamicContentChecksumCalculator = dynamicContentChecksumCalculator;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public void onValidate(AbstractDynamicContentModel model, InterceptorContext ctx) throws InterceptorException
    {
        ValidationResult validationResult = getValidationResult(model, ctx, this.dynamicContentChecksumCalculator, this.flexibleSearchService);
        if(!validationResult.succeeded())
        {
            throw new InterceptorException("Validation of " + model + " has failed with message '" + validationResult
                            .getErrorMessage() + "'.", this);
        }
    }


    public static boolean isModelValid(AbstractDynamicContentModel model, InterceptorContext ctx, DynamicContentChecksumCalculator checksumCalculator, FlexibleSearchService flexibleSearchService)
    {
        ValidationResult validationResult = getValidationResult(model, ctx, checksumCalculator, flexibleSearchService);
        return validationResult.succeeded();
    }


    private static ValidationResult getValidationResult(AbstractDynamicContentModel model, InterceptorContext ctx, DynamicContentChecksumCalculator checksumCalculator, FlexibleSearchService flexibleSearchService)
    {
        Validator validator = new Validator(model, ctx, checksumCalculator, flexibleSearchService);
        return validator.validate();
    }
}
