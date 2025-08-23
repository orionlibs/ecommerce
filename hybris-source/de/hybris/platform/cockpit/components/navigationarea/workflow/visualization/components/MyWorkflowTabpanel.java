package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.components;

import de.hybris.platform.cockpit.components.navigationarea.model.MyWorkflowsTreeModel;
import de.hybris.platform.cockpit.components.navigationarea.renderer.WorkflowSectionRenderer;
import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.WorkflowFacade;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.model.WorkflowModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;

public class MyWorkflowTabpanel
{
    public static final int DEFAULT_PAGE_SIZE = 8;
    private int pageSize;
    private final WorkflowSectionRenderer tabPanel;
    private final Object rootDummy;
    private final Tree myWorkflowsTree;
    private final Paging paging;


    public void refreshWorkflowPaging(int totalSize, int activePage)
    {
        this.paging.setTotalSize(totalSize);
        if(activePage < this.paging.getPageCount())
        {
            this.paging.setActivePage(activePage);
        }
        else
        {
            this.paging.setActivePage(0);
        }
    }


    public MyWorkflowTabpanel(Div div, WorkflowSectionRenderer tabPanel, Object rootDummy, int activePage, Div parent)
    {
        this.tabPanel = tabPanel;
        this.rootDummy = rootDummy;
        Div myWorkflowsMainDiv = new Div();
        myWorkflowsMainDiv.setClass("catalog_section_container");
        try
        {
            this
                            .pageSize = Integer.parseInt(UITools.getCockpitParameter("navigationarea.workflowtree.pageSize", Executions.getCurrent()));
        }
        catch(NumberFormatException ex)
        {
            this.pageSize = 8;
        }
        SearchResult<WorkflowModel> partOfWorkflows = getWorkflowFacade().getAllWorkflows(tabPanel
                        .getCurrentWorkflowViewOptions(), tabPanel.getCurrentAdhocWorkflowViewOptions(), activePage * this.pageSize, this.pageSize);
        MyWorkflowsTreeModel treeModel = new MyWorkflowsTreeModel(rootDummy, getWorkflowFacade().wrapItems(partOfWorkflows
                        .getResult()));
        this.myWorkflowsTree = (Tree)new MyWorkflowTree(tabPanel, rootDummy, myWorkflowsMainDiv);
        this.myWorkflowsTree.setModel((TreeModel)treeModel);
        this.paging = new Paging();
        this.paging.setClass("workflowpaging");
        this.paging.setAutohide(true);
        this.paging.setDetailed(false);
        this.paging.setVisible(true);
        this.paging.setPageSize(this.pageSize);
        this.paging.setTotalSize(partOfWorkflows.getTotalCount());
        if(activePage < this.paging.getPageCount())
        {
            this.paging.setActivePage(activePage);
        }
        else
        {
            this.paging.setActivePage(0);
        }
        this.paging.addEventListener("onPaging", (EventListener)new OnPagingEventListener(this));
        parent.appendChild((Component)this.paging);
        div.appendChild((Component)myWorkflowsMainDiv);
        myWorkflowsMainDiv.appendChild((Component)this.myWorkflowsTree);
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return (WorkflowFacade)Registry.getApplicationContext().getBean("workflowFacade", WorkflowFacade.class);
    }


    public int getActivePage()
    {
        return this.paging.getActivePage();
    }


    public int getPageSize()
    {
        return this.pageSize;
    }
}
