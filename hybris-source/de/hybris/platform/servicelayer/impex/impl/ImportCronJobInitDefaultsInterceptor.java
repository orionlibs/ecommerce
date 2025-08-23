package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.type.TypeService;
import org.springframework.beans.factory.annotation.Required;

public class ImportCronJobInitDefaultsInterceptor implements InitDefaultsInterceptor
{
    private CronJobService cronJobService;
    private TypeService typeService;


    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof ImpExImportCronJobModel)
        {
            ImpExImportCronJobModel cronJobModel = (ImpExImportCronJobModel)model;
            ComposedTypeModel comptypemodel = this.typeService.getComposedTypeForClass(model.getClass());
            String jobcode = this.typeService.getAttributeDescriptor(comptypemodel, "job").getAttributeType().getCode();
            if(model.getClass().equals(ImpExImportCronJobModel.class) || jobcode.equals("ImpExImportJob"))
            {
                ImpExImportJobModel job;
                try
                {
                    job = (ImpExImportJobModel)this.cronJobService.getJob("ImpEx-Import");
                }
                catch(UnknownIdentifierException e)
                {
                    job = (ImpExImportJobModel)ctx.getModelService().create("ImpExImportJob");
                    job.setCode("ImpEx-Import");
                    ctx.getModelService().save(job);
                }
                cronJobModel.setJob((JobModel)job);
            }
        }
    }


    @Required
    public void setCronJobService(CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
    }


    @Required
    public void setTypeService(TypeService typeservice)
    {
        this.typeService = typeservice;
    }
}
