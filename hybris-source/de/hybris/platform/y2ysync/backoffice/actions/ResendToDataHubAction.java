package de.hybris.platform.y2ysync.backoffice.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.task.internal.SyncTaskFactory;
import javax.annotation.Resource;
import org.apache.log4j.Logger;

public class ResendToDataHubAction implements CockpitAction<Y2YSyncCronJobModel, Object>
{
    @Resource
    SyncTaskFactory syncTaskFactory;
    private static final Logger LOG = Logger.getLogger(ResendToDataHubAction.class);


    public ActionResult<Object> perform(ActionContext<Y2YSyncCronJobModel> ctx)
    {
        LOG.info("Resending to DataHub action!");
        Y2YSyncCronJobModel y2YSyncCronJob = (Y2YSyncCronJobModel)ctx.getData();
        this.syncTaskFactory.resendToDataHub(y2YSyncCronJob.getCode(), y2YSyncCronJob.getJob().getNodeGroup());
        return new ActionResult("success");
    }


    public boolean canPerform(ActionContext<Y2YSyncCronJobModel> ctx)
    {
        Y2YSyncCronJobModel y2YSyncCronJob = (Y2YSyncCronJobModel)ctx.getData();
        if(y2YSyncCronJob == null || y2YSyncCronJob.getJob() == null)
        {
            return false;
        }
        return y2YSyncCronJob.getJob().getSyncType().equals(Y2YSyncType.DATAHUB);
    }


    public boolean needsConfirmation(ActionContext<Y2YSyncCronJobModel> ctx)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<Y2YSyncCronJobModel> ctx)
    {
        return ctx.getLabel("perform.confirm");
    }
}
