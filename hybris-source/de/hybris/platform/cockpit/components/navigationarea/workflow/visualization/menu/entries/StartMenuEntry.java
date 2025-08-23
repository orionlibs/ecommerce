package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.menu.entries;

import de.hybris.platform.cockpit.components.navigationarea.renderer.WorkflowSectionRenderer;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.MyWorkflowsTreeRenderer;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowFacade;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.Registry;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menuitem;

public class StartMenuEntry extends Menuitem
{
    WorkflowSectionRenderer parentSectionRenderer;
    MyWorkflowsTreeRenderer myWorkflowsTreeRenderer;


    public StartMenuEntry(TypedObject typedWorkflow, WorkflowSectionRenderer parentSectionRenderer, MyWorkflowsTreeRenderer myWorkflowsTreeRenderer)
    {
        super(Labels.getLabel("general.start"));
        this.parentSectionRenderer = parentSectionRenderer;
        this.myWorkflowsTreeRenderer = myWorkflowsTreeRenderer;
        addEventListener("onClick", (EventListener)new Object(this, typedWorkflow));
        WorkflowModel workflow = (WorkflowModel)typedWorkflow.getObject();
        if(!getWorkflowFacade().isPlanned(workflow) || (
                        !workflow.getActions().isEmpty() && ((WorkflowActionModel)workflow.getActions().get(0)).getPrincipalAssigned() == null))
        {
            setDisabled(true);
        }
    }


    private EventListener createStartMenuEventListener(TypedObject typedWorkflow)
    {
        return (EventListener)new Object(this, typedWorkflow);
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return (WorkflowFacade)Registry.getApplicationContext().getBean("workflowFacade", WorkflowFacade.class);
    }
}
