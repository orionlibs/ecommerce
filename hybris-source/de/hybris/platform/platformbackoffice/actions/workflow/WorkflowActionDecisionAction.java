package de.hybris.platform.platformbackoffice.actions.workflow;

import com.hybris.backoffice.workflow.WorkflowDecisionMaker;
import com.hybris.cockpitng.actions.AbstractStatefulAction;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import org.springframework.beans.factory.annotation.Autowired;

@Deprecated(since = "6.6.0.0", forRemoval = true)
public class WorkflowActionDecisionAction extends AbstractStatefulAction<WorkflowActionModel, WorkflowDecisionModel>
{
    protected static final String PARAMETER_SELECTED_DECISION = "selectedDecision";
    protected WorkflowDecisionMaker workflowDecisionMaker;


    public ActionResult perform(ActionContext<WorkflowActionModel> ctx)
    {
        WorkflowDecisionModel selectedDecision = (WorkflowDecisionModel)getValue(ctx, "selectedDecision");
        WidgetInstanceManager widgetInstanceManager = (WidgetInstanceManager)ctx.getParameter("wim");
        if(!shouldPerform(widgetInstanceManager, selectedDecision))
        {
            return new ActionResult("error", selectedDecision);
        }
        getWorkflowDecisionMaker().makeDecision(selectedDecision.getAction(), selectedDecision, widgetInstanceManager);
        return new ActionResult("success", selectedDecision);
    }


    protected boolean shouldPerform(WidgetInstanceManager widgetInstanceManager, WorkflowDecisionModel selectedDecision)
    {
        return (widgetInstanceManager != null && selectedDecision != null && selectedDecision.getAction() != null && selectedDecision
                        .getAction().getStatus() == WorkflowActionStatus.IN_PROGRESS);
    }


    protected WorkflowDecisionMaker getWorkflowDecisionMaker()
    {
        return this.workflowDecisionMaker;
    }


    @Autowired
    public void setWorkflowDecisionMaker(WorkflowDecisionMaker workflowDecisionMaker)
    {
        this.workflowDecisionMaker = workflowDecisionMaker;
    }
}
