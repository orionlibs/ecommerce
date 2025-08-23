package de.hybris.platform.platformbackoffice.actions.validation;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import de.hybris.platform.validation.services.ValidationService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReloadValidationEngineAction implements CockpitAction<Object, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(ReloadValidationEngineAction.class);
    private static final String NOTIFICATION_EVENT_RELOAD = "ValidationReload";
    @Resource
    private ValidationService validationService;
    @Resource
    private NotificationService notificationService;


    public ActionResult<Object> perform(ActionContext<Object> ctx)
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info("Reloading validation engine from Backoffice!");
        }
        getValidationService().reloadValidationEngine();
        getNotificationService().notifyUser(getNotificationSource(ctx), "ValidationReload", NotificationEvent.Level.SUCCESS, new Object[0]);
        return new ActionResult("success");
    }


    @Deprecated(since = "6.7", forRemoval = true)
    protected String getNotificationSource(ActionContext<?> actionContext)
    {
        return getNotificationService().getWidgetNotificationSource(actionContext);
    }


    public boolean canPerform(ActionContext<Object> ctx)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<Object> ctx)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<Object> ctx)
    {
        return ctx.getLabel("perform.reload");
    }


    protected ValidationService getValidationService()
    {
        return this.validationService;
    }


    public void setValidationService(ValidationService validationService)
    {
        this.validationService = validationService;
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
