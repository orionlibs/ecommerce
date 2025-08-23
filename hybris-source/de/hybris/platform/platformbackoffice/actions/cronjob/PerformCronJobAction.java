package de.hybris.platform.platformbackoffice.actions.cronjob;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformCronJobAction extends PermissionAwareCronJobAction
{
    private static final Logger LOG = LoggerFactory.getLogger(PerformCronJobAction.class);
    @Resource
    private CronJobService cronJobService;


    public ActionResult<Object> perform(ActionContext<CronJobModel> ctx)
    {
        CronJobModel cronJob = (CronJobModel)ctx.getData();
        if(LOG.isInfoEnabled())
        {
            LOG.info(String.format("Performing CronJob %s from Backoffice!", new Object[] {cronJob.toString()}));
        }
        this.cronJobService.performCronJob(cronJob);
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<CronJobModel> ctx)
    {
        return isCurrentUserAllowedToRun(ctx);
    }


    public boolean needsConfirmation(ActionContext<CronJobModel> ctx)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<CronJobModel> ctx)
    {
        return ctx.getLabel("perform.confirm");
    }
}
