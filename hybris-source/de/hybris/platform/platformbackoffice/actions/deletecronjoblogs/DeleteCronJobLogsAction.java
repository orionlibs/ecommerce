package de.hybris.platform.platformbackoffice.actions.deletecronjoblogs;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.platformbackoffice.actions.cronjob.PermissionAwareCronJobAction;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteCronJobLogsAction extends PermissionAwareCronJobAction
{
    private static final Logger LOG = LoggerFactory.getLogger(DeleteCronJobLogsAction.class);
    @Resource
    private ModelService modelService;
    @Resource
    private FlexibleSearchService flexibleSearchService;
    @Resource
    private NotificationService notificationService;


    public ActionResult<Object> perform(ActionContext<CronJobModel> actionContext)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Deleting %s JobLogs from CronJob: %s", new Object[] {Integer.valueOf(((CronJobModel)actionContext.getData()).getLogs().size()), ((CronJobModel)actionContext.getData()).getCode()}));
        }
        List<Object> success = new ArrayList();
        List<Object> errors = new ArrayList();
        try
        {
            SearchResult<Object> searchResult;
            FlexibleSearchQuery fsq = createFlexibleSearchQuery((CronJobModel)actionContext.getData());
            do
            {
                searchResult = getFlexibleSearchService().search(fsq);
                errors.addAll(searchResult.getResult());
                getModelService().removeAll(searchResult.getResult());
                success.addAll(searchResult.getResult());
                errors.clear();
            }
            while(searchResult.getCount() >= searchResult.getRequestedCount());
        }
        catch(ModelRemovalException mre)
        {
            getNotificationService().notifyUser(getNotificationSource(actionContext), "RemoveObject", NotificationEvent.Level.FAILURE, new Object[] {errors
                            .stream().collect(Collectors.toMap(e -> e, e -> mre))});
            return new ActionResult("error");
        }
        finally
        {
            if(!success.isEmpty())
            {
                getNotificationService().notifyUser(getNotificationSource(actionContext), "RemoveObject", NotificationEvent.Level.SUCCESS, new Object[] {success});
            }
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Deleted all JobLogs from CronJob: %s", new Object[] {((CronJobModel)actionContext.getData()).getCode()}));
        }
        return new ActionResult("success");
    }


    @Deprecated(since = "6.7", forRemoval = true)
    protected String getNotificationSource(ActionContext<CronJobModel> ctx)
    {
        return getNotificationService().getWidgetNotificationSource(ctx);
    }


    protected FlexibleSearchQuery createFlexibleSearchQuery(CronJobModel cronJobModel)
    {
        int start = 0;
        int count = 100;
        FlexibleSearchQuery fsq = new FlexibleSearchQuery("SELECT {pk} FROM {JobLog} WHERE {CronJob} IN ({{ SELECT {pk} FROM {CronJob} WHERE {code}=?code }})");
        fsq.addQueryParameter("code", cronJobModel.getCode());
        fsq.setStart(0);
        fsq.setCount(100);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Created following query to search for CronJob logs: %s", new Object[] {fsq.toString()}));
        }
        return fsq;
    }


    public boolean canPerform(ActionContext<CronJobModel> actionContext)
    {
        return isCurrentUserAllowedToRun(actionContext);
    }


    public boolean needsConfirmation(ActionContext<CronJobModel> actionContext)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<CronJobModel> actionContext)
    {
        return actionContext.getLabel("perform.confirm");
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
