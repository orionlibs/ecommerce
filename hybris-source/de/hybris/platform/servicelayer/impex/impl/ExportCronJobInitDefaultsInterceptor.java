package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.cronjob.ImpExExportCronJobModel;
import de.hybris.platform.impex.model.cronjob.ImpExExportJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.CSVConstants;
import org.springframework.beans.factory.annotation.Required;

public class ExportCronJobInitDefaultsInterceptor implements InitDefaultsInterceptor
{
    private CronJobService cronJobService;
    private TypeService typeService;


    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof ImpExExportCronJobModel)
        {
            ImpExExportCronJobModel cronJobModel = (ImpExExportCronJobModel)model;
            cronJobModel.setMode(ImpExValidationModeEnum.EXPORT_REIMPORT_STRICT);
            cronJobModel.setCommentCharacter(Character.valueOf('#'));
            cronJobModel.setFieldSeparator(Character.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR));
            cronJobModel.setQuoteCharacter(Character.valueOf(CSVConstants.DEFAULT_QUOTE_CHARACTER));
            cronJobModel.setSingleFile(Boolean.FALSE);
            ComposedTypeModel comptypemodel = this.typeService.getComposedTypeForClass(model.getClass());
            String jobcode = this.typeService.getAttributeDescriptor(comptypemodel, "job").getAttributeType().getCode();
            if(model.getClass().equals(ImpExExportCronJobModel.class) || jobcode.equals("ImpExExportJob"))
            {
                ImpExExportJobModel job;
                try
                {
                    job = (ImpExExportJobModel)this.cronJobService.getJob("ImpEx-Export");
                }
                catch(UnknownIdentifierException e)
                {
                    job = (ImpExExportJobModel)ctx.getModelService().create("ImpExExportJob");
                    job.setCode("ImpEx-Export");
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
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
