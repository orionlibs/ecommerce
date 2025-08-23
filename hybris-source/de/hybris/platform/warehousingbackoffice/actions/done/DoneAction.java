package de.hybris.platform.warehousingbackoffice.actions.done;

import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.impl.DefaultWorkflowService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zul.Messagebox;

public class DoneAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Collection<WorkflowActionModel>, Collection<WorkflowActionModel>>
{
    protected static final String SOCKET_OUT_CONTEXT = "nodeSelected";
    protected static final String SOCKET_OUT_DESELECT = "deselectItems";
    protected static final String NODE_SELECTED = "warehousing.treenode.taskassignment.inbox";
    protected static final String CANCELLED_TASKS_MESSAGE = "warehousingbackoffice.taskassignment.done.cancelledtasks.message";
    protected static final String CANCELLED_TASKS_TITLE = "warehousingbackoffice.taskassignment.done.cancelledtasks.title";
    @Resource
    private DefaultWorkflowService newestWorkflowService;
    @Resource
    private WorkflowProcessingService workflowProcessingService;


    public boolean canPerform(ActionContext<Collection<WorkflowActionModel>> actionContext)
    {
        boolean isFulfillmentInternal = false;
        try
        {
            if(actionContext != null && CollectionUtils.isNotEmpty((Collection)actionContext.getData()))
            {
                Collection<WorkflowActionModel> workflowActions = (Collection<WorkflowActionModel>)actionContext.getData();
                isFulfillmentInternal = workflowActions.stream().allMatch(workflowActionModel -> (((ConsignmentModel)workflowActionModel.getAttachmentItems().iterator().next()).getFulfillmentSystemConfig() == null));
            }
        }
        catch(ClassCastException classCastException)
        {
        }
        return isFulfillmentInternal;
    }


    public String getConfirmationMessage(ActionContext<Collection<WorkflowActionModel>> actionContext)
    {
        return null;
    }


    public boolean needsConfirmation(ActionContext<Collection<WorkflowActionModel>> actionContext)
    {
        return false;
    }


    public ActionResult<Collection<WorkflowActionModel>> perform(ActionContext<Collection<WorkflowActionModel>> actionContext)
    {
        Collection<WorkflowActionModel> tasks = new ArrayList<>();
        tasks.addAll((Collection<? extends WorkflowActionModel>)actionContext.getData());
        Collection<WorkflowActionModel> cancelledTasks = (Collection<WorkflowActionModel>)tasks.stream().filter(task -> getNewestWorkflowService().getWorkflowForCode(task.getWorkflow().getCode()).getStatus().equals(CronJobStatus.ABORTED)).collect(Collectors.toList());
        if(!cancelledTasks.isEmpty())
        {
            displayCancellationMessageBox(cancelledTasks, actionContext);
        }
        sendOutput("deselectItems", tasks);
        ((Collection)actionContext.getData()).clear();
        tasks.removeAll(cancelledTasks);
        tasks.forEach(task -> getWorkflowProcessingService().decideAction(task, task.getDecisions().iterator().next()));
        SimpleNode inboxSimpleNode = new SimpleNode("warehousing.treenode.taskassignment.inbox");
        sendOutput("nodeSelected", inboxSimpleNode);
        ActionResult<Collection<WorkflowActionModel>> actionResult = new ActionResult("success");
        actionResult.getStatusFlags().add(ActionResult.StatusFlag.OBJECT_PERSISTED);
        return actionResult;
    }


    protected void displayCancellationMessageBox(Collection<WorkflowActionModel> cancelledTasks, ActionContext<Collection<WorkflowActionModel>> actionContext)
    {
        StringBuilder cancellationMessage = new StringBuilder(actionContext.getLabel("warehousingbackoffice.taskassignment.done.cancelledtasks.message"));
        cancelledTasks.forEach(task -> cancellationMessage.append(" ").append(((ConsignmentModel)task.getAttachmentItems().get(0)).getCode()).append(","));
        cancellationMessage.setCharAt(cancellationMessage.length() - 1, '.');
        Messagebox.show(cancellationMessage.toString(), actionContext.getLabel("warehousingbackoffice.taskassignment.done.cancelledtasks.title"), 1, "z-messagebox-icon z-messagebox-exclamation", null);
    }


    protected WorkflowProcessingService getWorkflowProcessingService()
    {
        return this.workflowProcessingService;
    }


    protected DefaultWorkflowService getNewestWorkflowService()
    {
        return this.newestWorkflowService;
    }
}
