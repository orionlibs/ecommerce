package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.components;

import de.hybris.platform.cockpit.components.navigationarea.renderer.WorkflowSectionRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.impl.WorkflowItemsBrowserModel;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Treeitem;

public class WorkflowSelectEventListener implements EventListener
{
    private final WorkflowSectionRenderer workflowSectionRenderer;


    public WorkflowSelectEventListener(WorkflowSectionRenderer workflowSectionRenderer)
    {
        this.workflowSectionRenderer = workflowSectionRenderer;
    }


    public void onEvent(Event event) throws Exception
    {
        if(event instanceof SelectEvent)
        {
            SelectEvent selectEvent = (SelectEvent)event;
            if(!selectEvent.getSelectedItems().isEmpty())
            {
                Treeitem treeitem = selectEvent.getSelectedItems().iterator().next();
                TypedObject workflowTemplate = (TypedObject)treeitem.getValue();
                if(workflowTemplate != null && !(workflowTemplate.getObject() instanceof de.hybris.platform.workflow.model.WorkflowActionTemplateModel))
                {
                    WorkflowItemsBrowserModel workflowBrowserModel = new WorkflowItemsBrowserModel(workflowTemplate);
                    workflowBrowserModel.updateItems();
                    BrowserModel oldBrowser = this.workflowSectionRenderer.getNavigationArea().getPerspective().getBrowserArea().getFocusedBrowser();
                    this.workflowSectionRenderer.getNavigationArea().getPerspective().getBrowserArea()
                                    .replaceBrowser(oldBrowser, (BrowserModel)workflowBrowserModel);
                }
            }
        }
    }
}
