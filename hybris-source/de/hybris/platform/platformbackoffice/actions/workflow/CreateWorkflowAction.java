package de.hybris.platform.platformbackoffice.actions.workflow;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.platformbackoffice.widgets.workflow.CreateWorkflowForm;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateWorkflowAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<WorkflowTemplateModel, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(CreateWorkflowAction.class);
    private static final String NOTIFICATION_EVENT_UNSAVED_TEMPLATE = "WorkflowUnsavedTemplate";
    @Resource
    private ObjectFacade objectFacade;
    @Resource
    private UserService userService;
    @Resource
    private NotificationService notificationService;


    public ActionResult<Object> perform(ActionContext<WorkflowTemplateModel> ctx)
    {
        WorkflowTemplateModel workflowTemplate = (WorkflowTemplateModel)ctx.getData();
        ActionResult<Object> result = new ActionResult("error");
        if(getObjectFacade().isModified(workflowTemplate))
        {
            LOG.debug("Unable to create workflow from unsaved template: {}", workflowTemplate.getName());
            getNotificationService().notifyUser(getNotificationSource(ctx), "WorkflowUnsavedTemplate", NotificationEvent.Level.FAILURE, new Object[] {workflowTemplate});
        }
        else if(workflowTemplate != null)
        {
            LOG.debug("Creating workflow template: {}", workflowTemplate);
            CreateWorkflowForm createWorkflowForm = new CreateWorkflowForm();
            createWorkflowForm.setWorkflowTemplate(workflowTemplate);
            createWorkflowForm.setWorkflowName(workflowTemplate.getName());
            createWorkflowForm.getWorkflowActionTemplates().addAll(workflowTemplate.getActions());
            createWorkflowForm.setOwner(getUserService().getCurrentUser());
            createWorkflowForm.setDescription(workflowTemplate.getDescription());
            result = new ActionResult("success");
            sendOutput("createWorkflowForm", createWorkflowForm);
        }
        return result;
    }


    @Deprecated(since = "6.7", forRemoval = true)
    protected String getNotificationSource(ActionContext<WorkflowTemplateModel> ctx)
    {
        return getNotificationService().getWidgetNotificationSource(ctx);
    }


    public boolean canPerform(ActionContext<WorkflowTemplateModel> ctx)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<WorkflowTemplateModel> ctx)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<WorkflowTemplateModel> ctx)
    {
        return null;
    }


    protected ObjectFacade getObjectFacade()
    {
        return this.objectFacade;
    }


    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
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
