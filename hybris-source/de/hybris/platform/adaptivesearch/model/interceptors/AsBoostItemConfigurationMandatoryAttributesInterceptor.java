package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.model.AbstractAsBoostItemConfigurationModel;
import de.hybris.platform.adaptivesearch.strategies.AsItemModelHelper;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.MandatoryAttributesValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AsBoostItemConfigurationMandatoryAttributesInterceptor extends MandatoryAttributesValidator
{
    private AsItemModelHelper asItemModelHelper;


    public void onValidate(Object model, InterceptorContext context) throws InterceptorException
    {
        if(!context.isNew(model) || !isCorrupted((AbstractAsBoostItemConfigurationModel)model))
        {
            super.onValidate(model, context);
        }
    }


    protected boolean isCorrupted(AbstractAsBoostItemConfigurationModel boostItemConfiguration)
    {
        String expectedUniqueIdx = this.asItemModelHelper.generateBoostItemConfigurationUniqueIdx(boostItemConfiguration);
        String uniqueIdx = boostItemConfiguration.getUniqueIdx();
        return !StringUtils.equals(expectedUniqueIdx, uniqueIdx);
    }


    public AsItemModelHelper getAsItemModelHelper()
    {
        return this.asItemModelHelper;
    }


    @Required
    public void setAsItemModelHelper(AsItemModelHelper asItemModelHelper)
    {
        this.asItemModelHelper = asItemModelHelper;
    }
}
