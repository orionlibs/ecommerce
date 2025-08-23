package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.menu.entries;

import de.hybris.platform.cockpit.components.navigationarea.renderer.WorkflowSectionRenderer;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowFacade;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.Registry;
import de.hybris.platform.workflow.model.WorkflowModel;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Treeitem;

public class DeleteMenuEntry extends Menuitem
{
    public DeleteMenuEntry(Treeitem treeitem, TypedObject typedObject, WorkflowSectionRenderer parentSectionRenderer)
    {
        super(Labels.getLabel("general.delete"));
        addEventListener("onClick", (EventListener)new Object(this, treeitem, typedObject, parentSectionRenderer));
        if(!getWorkflowFacade().isPlanned((WorkflowModel)typedObject.getObject()))
        {
            setDisabled(true);
        }
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return (WorkflowFacade)Registry.getApplicationContext().getBean("workflowFacade", WorkflowFacade.class);
    }
}
