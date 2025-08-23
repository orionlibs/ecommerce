package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.model.CompositeEntryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;

public class CompositeEntryJobValidateInterceptor implements ValidateInterceptor
{
    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CompositeEntryModel)
        {
            CompositeEntryModel compositeModel = (CompositeEntryModel)model;
            if(compositeModel.getTriggerableJob() != null)
            {
                if(!(compositeModel.getTriggerableJob() instanceof de.hybris.platform.cronjob.jalo.TriggerableJob) &&
                                !(compositeModel.getTriggerableJob() instanceof ServicelayerJobModel))
                {
                    throw new InterceptorException("Assigned Job either does not implements de.hybris.platform.cronjob.jalo.TriggerableJob or is not an instance of " + ServicelayerJobModel.class
                                    .getName() + "!");
                }
            }
        }
    }
}
