package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.GenericTypeResolver;

public class JobPerformableGenericTypeValidator implements ValidateInterceptor
{
    private CronJobService cronJobService;


    @Required
    public void setCronJobService(CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
    }


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CronJobModel)
        {
            CronJobModel cronJobModel = (CronJobModel)model;
            if(cronJobModel.getJob() instanceof ServicelayerJobModel)
            {
                ServicelayerJobModel jobModel = (ServicelayerJobModel)cronJobModel.getJob();
                Class<?> performableType = getType(jobModel);
                if(!JobPerformable.class.isAssignableFrom(performableType))
                {
                    throw new InterceptorException("Assigned bean as performable for a job " + jobModel + " is not JobPerformable interface (or its subimplementation) : " + performableType
                                    .getName());
                }
                Class[] typeArgs = GenericTypeResolver.resolveTypeArguments(performableType, JobPerformable.class);
                if(typeArgs != null && !typeArgs[0].isAssignableFrom(cronJobModel.getClass()))
                {
                    throw new InterceptorException("Can not assign the CronJob " + model + " to ServiceLayerJob " + jobModel + ", the requested  performable generic type is " + typeArgs[0] + " which is less specific than " + cronJobModel
                                    .getClass());
                }
            }
        }
    }


    private Class getType(ServicelayerJobModel jobModel)
    {
        Object performableObejct = this.cronJobService.getPerformable(jobModel);
        return performableObejct.getClass();
    }
}
