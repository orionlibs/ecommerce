package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization;

import de.hybris.platform.cockpit.components.navigationarea.model.TreeNodeWrapper;
import de.hybris.platform.cockpit.components.navigationarea.renderer.WorkflowSectionRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.WorkflowItemsBrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

public class MyWorkflowsTreeRenderer implements TreeitemRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(MyWorkflowsTreeRenderer.class);
    private final WorkflowSectionRenderer parentSectionRenderer;
    private final Div parentDiv;
    private final Object rootDummy;
    private final Tree workflowTree;


    public MyWorkflowsTreeRenderer(Object rootDummy, WorkflowSectionRenderer parentSectionRenderer, Tree workflowTree, Div parentDiv)
    {
        this.rootDummy = rootDummy;
        this.parentSectionRenderer = parentSectionRenderer;
        this.workflowTree = workflowTree;
        this.parentDiv = parentDiv;
    }


    public void render(Treeitem item, Object data2) throws Exception
    {
        TypedObject typedData = ((TreeNodeWrapper)data2).getItem();
        Treerow treerow = item.getTreerow();
        if(treerow == null)
        {
            (treerow = new Treerow()).setParent((Component)item);
        }
        Treecell cell = (Treecell)treerow.getFirstChild();
        if(cell == null)
        {
            (cell = new Treecell()).setParent((Component)treerow);
        }
        if(typedData == null)
        {
            item.setLabel(Labels.getLabel("worfklow.noVisibleWorkflows"));
            item.setStyle("font-style: italic;");
        }
        else
        {
            setCssStyle(treerow, typedData);
            BrowserModel browserModel = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser();
            if(browserModel instanceof WorkflowItemsBrowserModel)
            {
                item.setSelected(typedData.equals(((WorkflowItemsBrowserModel)browserModel).getWorkflowObject()));
            }
            item.setValue(typedData);
            item.setImage(getWorkflowFacade().getStatusUrl(typedData));
            Label name = new Label(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(typedData));
            name.setClass("workflow_name");
            name.setParent((Component)cell);
            WorkflowModel workflow = (WorkflowModel)typedData.getObject();
            Popup popup = new Popup();
            popup.setSclass("workflow_popup");
            item.setTooltip(popup);
            Div div2 = new Div();
            div2.setSclass("workflow_image_div");
            div2.setParent((Component)popup);
            popup.addEventListener("onOpen", (EventListener)new Object(this, div2, workflow));
            Div iconDiv = createDateAndStatus(typedData);
            iconDiv.setParent((Component)cell);
            iconDiv.appendChild((Component)popup);
        }
        Treecell newCell = cell;
        newCell.addEventListener("onRightClick", (EventListener)new Object(this, item, newCell));
        if(typedData != null && GeneratedWorkflowConstants.TC.WORKFLOW.equals(typedData.getType().getCode()))
        {
            newCell.setDroppable("PerspectiveDND");
        }
        cell.addEventListener("onDrop", (EventListener)new Object(this, typedData));
        String sclass = "categoryTreeItem";
        UITools.modifySClass((HtmlBasedComponent)treerow, "categoryTreeItem", true);
    }


    private Object getWorkflowName(ItemModel modelData)
    {
        if(modelData instanceof WorkflowModel)
        {
            return ((WorkflowModel)modelData).getName();
        }
        return "";
    }


    private void setCssStyle(Treerow treerow, TypedObject workflow)
    {
        if(getWorkflowFacade().isAdhocWorkflow((WorkflowModel)workflow.getObject()))
        {
            treerow.setSclass("adhoc-workflow");
        }
        else
        {
            treerow.setSclass("workflow");
        }
    }


    private Div createDateAndStatus(TypedObject typedData)
    {
        Div iconDiv = new Div();
        iconDiv.setSclass("workflow-date-and-status");
        StringBuilder workflowDate = new StringBuilder(8);
        WorkflowModel workflow = (WorkflowModel)typedData.getObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", UISessionUtils.getCurrentSession().getLocale());
        if(getWorkflowFacade().isFinished(workflow))
        {
            Date endTime = workflow.getEndTime();
            workflowDate.append(dateFormat.format((endTime == null) ? workflow.getCreationtime() : endTime));
        }
        else
        {
            workflowDate.append(dateFormat.format(workflow.getCreationtime()));
        }
        iconDiv.appendChild((Component)new Label(workflowDate.toString()));
        return iconDiv;
    }


    private void resolveItemsToAdd(WorkflowModel workflowModel, List<ItemModel> suggestedItemsToAdd, List<ItemModel> itemsToAdd, List<ItemModel> existingItems)
    {
        List<WorkflowItemAttachmentModel> modelAttachments = workflowModel.getAttachments();
        List<ItemModel> allModelItems = new LinkedList<>();
        for(WorkflowItemAttachmentModel workflowItemAttachmentModel : modelAttachments)
        {
            ItemModel itemModel = workflowItemAttachmentModel.getItem();
            allModelItems.add(itemModel);
        }
        for(ItemModel itemToAdd : suggestedItemsToAdd)
        {
            if(allModelItems.contains(itemToAdd))
            {
                existingItems.add(itemToAdd);
                continue;
            }
            itemsToAdd.add(itemToAdd);
        }
    }


    protected DragAndDropWrapper getDDWrapper()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective().getDragAndDropWrapperService().getWrapper();
    }


    public WorkflowFacade getWorkflowFacade()
    {
        return (WorkflowFacade)Registry.getApplicationContext().getBean("workflowFacade", WorkflowFacade.class);
    }
}
