package de.hybris.platform.cockpit.helpers.impl;

import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowFacade;
import de.hybris.platform.cockpit.helpers.WorkflowHelper;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.workflow.model.WorkflowModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultWorkflowHelper implements WorkflowHelper
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWorkflowHelper.class);
    private WorkflowFacade workflowFacade;


    public String getStatusUrl(TypedObject typedData)
    {
        WorkflowModel workflow = (WorkflowModel)typedData.getObject();
        if(this.workflowFacade.isPlanned(workflow))
        {
            return "/cockpit/images/not_started_workflow.png";
        }
        if(this.workflowFacade.isRunning(workflow))
        {
            return "/cockpit/images/running_workflow.png";
        }
        if(this.workflowFacade.isFinished(workflow))
        {
            return "/cockpit/images/finished_workflow.png";
        }
        if(this.workflowFacade.isTerminated(workflow))
        {
            return "/cockpit/images/terminated_workflow.png";
        }
        LOG.warn("Picture for status: " + workflow.getStatus() + " not found, used: cockpit/images/stop_klein.jpg instead.");
        return "cockpit/images/stop_klein.jpg";
    }


    public void setWorkflowFacade(WorkflowFacade workflowFacade)
    {
        this.workflowFacade = workflowFacade;
    }
}
