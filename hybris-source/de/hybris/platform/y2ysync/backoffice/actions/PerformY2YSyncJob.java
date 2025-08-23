package de.hybris.platform.y2ysync.backoffice.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.y2ysync.backoffice.data.Y2YSyncJobForm;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import org.apache.log4j.Logger;

public class PerformY2YSyncJob extends AbstractComponentWidgetAdapterAware implements CockpitAction<Y2YSyncJobModel, Object>
{
    private static final Logger LOG = Logger.getLogger(PerformY2YSyncJob.class);
    public static final String SOCKET_OUT_PERFORM_Y2Y_SYNC_JOB = "performY2YSyncJob";


    public ActionResult<Object> perform(ActionContext<Y2YSyncJobModel> ctx)
    {
        ActionResult<Object> result;
        Y2YSyncJobModel syncJob = (Y2YSyncJobModel)ctx.getData();
        if(syncJob != null)
        {
            Y2YSyncJobForm y2ySyncForm = new Y2YSyncJobForm();
            y2ySyncForm.setSyncJob(syncJob);
            result = new ActionResult("success");
            LOG.info("starting perform Y2YSyncJob action for : " + syncJob.getCode());
            sendOutput("performY2YSyncJob", y2ySyncForm);
        }
        else
        {
            result = new ActionResult("error");
            LOG.error("Cloning container not possible: Source container could not be found");
        }
        return result;
    }


    public boolean canPerform(ActionContext<Y2YSyncJobModel> actionContext)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<Y2YSyncJobModel> actionContext)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<Y2YSyncJobModel> actionContext)
    {
        return null;
    }
}
