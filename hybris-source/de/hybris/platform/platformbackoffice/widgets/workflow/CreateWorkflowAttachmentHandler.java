package de.hybris.platform.platformbackoffice.widgets.workflow;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class CreateWorkflowAttachmentHandler implements FlowActionHandler
{
    protected static final String ATTACHMENT_KEY = "workflowItemAttachment";
    protected static final String NOTIFICATION_SOURCE = "dragAndDropItemToWorkflowAppendWarningSingle";
    private NotificationService notificationService;


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        WorkflowItemAttachmentModel workflowAttachment = getWorkflowAttachment(adapter);
        WorkflowModel workflow = workflowAttachment.getWorkflow();
        if(hasAttachment(workflow.getAttachments(), workflowAttachment))
        {
            getNotificationService().notifyUser("dragAndDropItemToWorkflowAppendWarningSingle", "UpdateObject", NotificationEvent.Level.WARNING, new Object[] {workflowAttachment
                            .getItem()});
            return;
        }
        List<WorkflowActionModel> actions = workflow.getActions();
        actions.forEach(action -> {
            List<WorkflowItemAttachmentModel> actionAttachments = action.getAttachments();
            if(!hasAttachment(actionAttachments, workflowAttachment))
            {
                List<WorkflowItemAttachmentModel> modifiableCollection = new ArrayList<>();
                modifiableCollection.addAll(actionAttachments);
                modifiableCollection.add(workflowAttachment);
                action.setAttachments(modifiableCollection);
            }
        });
        workflow.setActions(actions);
        workflowAttachment.setActions(actions);
        workflowAttachment.setWorkflow(workflow);
        setWorkflowAttachment(adapter, workflowAttachment);
        adapter.done();
    }


    protected boolean hasAttachment(List<WorkflowItemAttachmentModel> attachments, WorkflowItemAttachmentModel attachment)
    {
        return attachments.stream().map(WorkflowItemAttachmentModel::getItem).anyMatch(e -> e.equals(attachment.getItem()));
    }


    protected WorkflowItemAttachmentModel getWorkflowAttachment(FlowActionHandlerAdapter adapter)
    {
        return (WorkflowItemAttachmentModel)adapter.getWidgetInstanceManager().getModel().getValue("workflowItemAttachment", WorkflowItemAttachmentModel.class);
    }


    protected void setWorkflowAttachment(FlowActionHandlerAdapter adapter, WorkflowItemAttachmentModel workflowAttachment)
    {
        adapter.getWidgetInstanceManager().getModel().setValue("workflowItemAttachment", workflowAttachment);
    }


    public NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    @Required
    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
