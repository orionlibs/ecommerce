package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.components;

import de.hybris.platform.cockpit.components.navigationarea.model.WorkflowTemplateTreeModel;
import de.hybris.platform.cockpit.components.navigationarea.renderer.WorkflowSectionRenderer;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowFacade;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowTemplateTreeitemRenderer;
import de.hybris.platform.core.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeitemRenderer;

public class WorkflowTemplatesTabpanel
{
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowTemplatesTabpanel.class.getName());
    private final WorkflowSectionRenderer workflowSectionRenderer;


    public WorkflowTemplatesTabpanel(Div div, WorkflowSectionRenderer workflowSectionRenderer)
    {
        this.workflowSectionRenderer = workflowSectionRenderer;
        Div parentDiv = new Div();
        div.appendChild((Component)parentDiv);
        parentDiv.setClass("catalog_section_container");
        Tree workflowTemplatesTree = createTreeForWorkflowsTemplatesTabpanel();
        parentDiv.appendChild((Component)workflowTemplatesTree);
        createAdhocMenuEntry(parentDiv);
    }


    private Tree createTreeForWorkflowsTemplatesTabpanel()
    {
        Tree workflowTemplatesTree = new Tree();
        workflowTemplatesTree.setModel((TreeModel)new WorkflowTemplateTreeModel(WorkflowSectionRenderer.rootDummy));
        workflowTemplatesTree.setTreeitemRenderer((TreeitemRenderer)new WorkflowTemplateTreeitemRenderer(this.workflowSectionRenderer));
        workflowTemplatesTree.setZclass("z-dottree");
        workflowTemplatesTree.setSclass("workflowstemplates-tree");
        workflowTemplatesTree.addEventListener("onSelect", (EventListener)new WorkflowSelectEventListener(this.workflowSectionRenderer));
        return workflowTemplatesTree;
    }


    private void createAdhocMenuEntry(Div parentDiv)
    {
        Div cell = new Div();
        cell.setSclass("adhoc_wf_entry");
        cell.setDroppable("PerspectiveDND");
        cell.setParent((Component)parentDiv);
        cell.addEventListener("onDrop", (EventListener)new Object(this, cell));
        Div buttonContainer = new Div();
        cell.appendChild((Component)buttonContainer);
        Div center = new Div();
        buttonContainer.appendChild((Component)center);
        center.setSclass("create-adhoc-workflow-center");
        Hbox hbox = new Hbox();
        hbox.setWidths("20px,none,20px");
        center.appendChild((Component)hbox);
        Image cog = new Image("/cockpit/images/icon_workflow_adhoc.png");
        cog.setSclass("wf_icon");
        hbox.appendChild((Component)cog);
        Div labelCnt = new Div();
        labelCnt.setSclass("labelCnt");
        hbox.appendChild((Component)labelCnt);
        Label adhocLabel = new Label(Labels.getLabel("workflow.create.adhoc"));
        adhocLabel.setParent((Component)labelCnt);
        adhocLabel.setSclass("create-adhoc-workflow-label");
        createAdhocAddButton((Component)hbox);
    }


    private void createAdhocAddButton(Component parent)
    {
        Image addBtn = new Image("/cockpit/images/add_btn.gif");
        addBtn.setSclass("workflow-add-button");
        addBtn.addEventListener("onClick", (EventListener)new Object(this, parent));
        parent.appendChild((Component)addBtn);
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return (WorkflowFacade)Registry.getApplicationContext().getBean("workflowFacade", WorkflowFacade.class);
    }
}
