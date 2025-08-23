package de.hybris.platform.cockpit.components.menu.impl;

import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowFacade;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.Registry;
import java.util.List;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listitem;

public class UsersAssignedListWorkflowRenderer extends AbstractUsersAssignedListRenderer
{
    private final TypedObject workflow;


    public UsersAssignedListWorkflowRenderer(List<TypedObject> assignedValuesList, TypedObject workflow)
    {
        super(assignedValuesList);
        this.workflow = workflow;
    }


    public EventListener getRemoveImageListener(Listitem item, Object data)
    {
        return (EventListener)new Object(this);
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return (WorkflowFacade)Registry.getApplicationContext().getBean("workflowFacade", WorkflowFacade.class);
    }
}
