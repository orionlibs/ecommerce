package de.hybris.platform.platformbackoffice.actions.cronjob;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbortCronJobAction extends PermissionAwareCronJobAction
{
    private static final Logger LOG = LoggerFactory.getLogger(AbortCronJobAction.class);
    @Resource
    private CronJobService cronJobService;
    @Resource
    private ModelService modelService;


    public ActionResult<Object> perform(ActionContext<CronJobModel> ctx)
    {
        CronJobModel cronJob = (CronJobModel)ctx.getData();
        if(LOG.isInfoEnabled())
        {
            LOG.info(String.format("Aborting CronJob %s from Backoffice!", new Object[] {cronJob.toString()}));
        }
        this.cronJobService.requestAbortCronJob(cronJob);
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<CronJobModel> ctx)
    {
        if(!isCurrentUserAllowedToRun(ctx))
        {
            return false;
        }
        CronJobModel cronJob = (CronJobModel)ctx.getData();
        return (!this.modelService.isNew(cronJob) && this.cronJobService.isAbortable(cronJob) && this.cronJobService.isRunning(cronJob));
    }


    public boolean needsConfirmation(ActionContext<CronJobModel> ctx)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<CronJobModel> ctx)
    {
        return ctx.getLabel("perform.abort");
    }
}
