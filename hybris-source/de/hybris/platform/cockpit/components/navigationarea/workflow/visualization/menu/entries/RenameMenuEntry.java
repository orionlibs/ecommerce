package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.menu.entries;

import de.hybris.platform.cockpit.components.navigationarea.renderer.WorkflowSectionRenderer;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowFacade;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.Registry;
import de.hybris.platform.workflow.model.WorkflowModel;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Treecell;

public class RenameMenuEntry extends Menuitem
{
    private final WorkflowSectionRenderer parentSectionRenderer;


    public RenameMenuEntry(TypedObject workflowObject, Treecell treecell, WorkflowSectionRenderer parentSectionRenderer)
    {
        super(Labels.getLabel("general.rename"));
        this.parentSectionRenderer = parentSectionRenderer;
        Object workflow = workflowObject.getObject();
        addEventListener("onClick", (EventListener)new Object(this, workflow, treecell, workflowObject));
        setDisabled(!getWorkflowFacade().isPlanned((WorkflowModel)workflow));
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return (WorkflowFacade)Registry.getApplicationContext().getBean("workflowFacade", WorkflowFacade.class);
    }
}
