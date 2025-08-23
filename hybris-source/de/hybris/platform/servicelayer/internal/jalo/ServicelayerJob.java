package de.hybris.platform.servicelayer.internal.jalo;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.RedeployUtilities;
import org.apache.log4j.Logger;

public class ServicelayerJob extends GeneratedServicelayerJob
{
    private static final Logger log = Logger.getLogger(ServicelayerJob.class.getName());


    protected CronJob.CronJobResult performCronJob(CronJob cronJob) throws AbortCronJobException
    {
        CronJobModel model = (CronJobModel)getModelService().get(cronJob);
        PerformResult result = getPerformable().perform(model);
        if(RedeployUtilities.isShutdownInProgress())
        {
            log.info("CronJob: " + cronJob.getCode() + " interrupted because of System in shutdown, the result: " + result
                            .getResult() + ", status: " + result.getStatus());
        }
        EnumerationValue cronJobResult = (EnumerationValue)getModelService().getSource(
                        getTypeService().getEnumerationValue((HybrisEnumValue)result.getResult()));
        EnumerationValue cronJobStatus = (EnumerationValue)getModelService().getSource(
                        getTypeService().getEnumerationValue((HybrisEnumValue)result.getStatus()));
        return new CronJob.CronJobResult(cronJobStatus, cronJobResult);
    }


    protected boolean canPerform(CronJob cronJob)
    {
        return (Registry.getApplicationContext().containsBean(getSpringId()) && getPerformable().isPerformable());
    }


    protected boolean canUndo(CronJob cronJob)
    {
        return false;
    }


    @ForceJALO(reason = "something else")
    public boolean isAbortable(CronJob conJob)
    {
        return getPerformable().isAbortable();
    }


    protected JobPerformable getPerformable()
    {
        return (JobPerformable)Registry.getApplicationContext().getBean(getSpringId());
    }


    private ModelService getModelService()
    {
        return (ModelService)Registry.getApplicationContext().getBean("modelService");
    }


    private TypeService getTypeService()
    {
        return (TypeService)Registry.getApplicationContext().getBean("typeService");
    }
}
