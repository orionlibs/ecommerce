package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class TriggerJobValidateInterceptor implements ValidateInterceptor
{
    private ModelService modelService;


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof TriggerModel)
        {
            TriggerModel triggerModel = (TriggerModel)model;
            if(triggerModel.getJob() != null)
            {
                if(!(this.modelService.getSource(triggerModel.getJob()) instanceof de.hybris.platform.cronjob.jalo.TriggerableJob) &&
                                !(triggerModel.getJob() instanceof ServicelayerJobModel))
                {
                    throw new InterceptorException("Assigned Job either does not implements de.hybris.platform.cronjob.jalo.TriggerableJob or is not an instance of " + ServicelayerJobModel.class
                                    .getName() + "!");
                }
            }
        }
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
