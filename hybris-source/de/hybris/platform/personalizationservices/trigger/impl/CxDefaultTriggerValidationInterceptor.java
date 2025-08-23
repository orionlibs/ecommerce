package de.hybris.platform.personalizationservices.trigger.impl;

import de.hybris.platform.personalizationservices.model.CxAbstractTriggerModel;
import de.hybris.platform.personalizationservices.model.CxDefaultTriggerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import org.apache.commons.lang3.StringUtils;

public class CxDefaultTriggerValidationInterceptor extends BaseTriggerInterceptor implements ValidateInterceptor<CxDefaultTriggerModel>
{
    public void onValidate(CxDefaultTriggerModel model, InterceptorContext context) throws InterceptorException
    {
        if(model == null || model.getVariation() == null || StringUtils.isEmpty(model.getCode()))
        {
            return;
        }
        isTriggerUnique((CxAbstractTriggerModel)model, model.getVariation(), context);
    }
}
