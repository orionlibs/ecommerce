package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.components;

import de.hybris.platform.cockpit.components.navigationarea.renderer.WorkflowSectionRenderer;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.MyWorkflowsTreeRenderer;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowFacade;
import de.hybris.platform.core.Registry;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeitemRenderer;

public class MyWorkflowTree extends Tree
{
    WorkflowSectionRenderer tabPanel;


    public MyWorkflowTree(WorkflowSectionRenderer tabPanel, Object rootDummy, Div myWorkflowsMainDiv)
    {
        this.tabPanel = tabPanel;
        setMultiple(true);
        setTreeitemRenderer((TreeitemRenderer)new MyWorkflowsTreeRenderer(rootDummy, tabPanel, this, myWorkflowsMainDiv));
        setZclass("z-dottree");
        setSclass("workflows-tree");
        addEventListener("onSelect", (EventListener)new WorkflowSelectEventListener(tabPanel));
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return (WorkflowFacade)Registry.getApplicationContext().getBean("workflowFacade", WorkflowFacade.class);
    }
}
