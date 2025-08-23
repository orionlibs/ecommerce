package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.navigationarea.model.TreeNodeWrapper;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.WorkflowItemsBrowserModel;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

public class WorkflowTemplateTreeitemRenderer implements TreeitemRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowTemplateTreeitemRenderer.class.getName());
    private final WorkflowSectionRenderer parentSectionRenderer;
    private TypeService typeService;


    public WorkflowTemplateTreeitemRenderer(WorkflowSectionRenderer parentSectionRenderer)
    {
        this.parentSectionRenderer = parentSectionRenderer;
    }


    public void render(Treeitem item, Object data) throws Exception
    {
        String sclass = "categoryTreeItem";
        TypedObject typedData = ((TreeNodeWrapper)data).getItem();
        Treerow treerow = item.getTreerow();
        if(treerow == null)
        {
            (treerow = new Treerow()).setParent((Component)item);
        }
        setCssStyle(item, treerow, typedData);
        try
        {
            TreeNodeWrapper child = (TreeNodeWrapper)item.getTree().getModel().getChild(item.getTree().getModel().getRoot(), 0);
            if(child.getItem().equals(typedData))
            {
                UITools.modifySClass((HtmlBasedComponent)treerow, "first_item", true);
            }
        }
        catch(Exception e)
        {
            LOG.warn("Could not set style class to first treeitem", e);
        }
        item.setValue(typedData);
        item.setLabel(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(typedData));
        BrowserModel browserModel = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser();
        if(browserModel instanceof WorkflowItemsBrowserModel)
        {
            item.setSelected(typedData.equals(((WorkflowItemsBrowserModel)browserModel).getWorkflowObject()));
        }
        Treecell cell = (Treecell)treerow.getFirstChild();
        if(cell == null)
        {
            (cell = new Treecell()).setParent((Component)treerow);
        }
        createAddButton(typedData, cell);
        cell.setDroppable("PerspectiveDND");
        cell.addEventListener("onDrop", (EventListener)new Object(this));
        UITools.modifySClass((HtmlBasedComponent)treerow, "categoryTreeItem", true);
    }


    private void createAddButton(TypedObject typedData, Treecell cell)
    {
        ItemModel itemModel = (ItemModel)typedData.getObject();
        if(itemModel instanceof WorkflowTemplateModel)
        {
            WorkflowTemplateModel workflowTemplate = (WorkflowTemplateModel)itemModel;
            Image addBtn = new Image("/cockpit/images/add_btn.gif");
            addBtn.setSclass("workflow-add-button");
            addBtn.addEventListener("onClick", (EventListener)new Object(this, workflowTemplate, cell));
            cell.appendChild((Component)addBtn);
        }
    }


    private WorkflowTemplateModel getWorkflowTemplate(TypedObject workflowTemplateObject)
    {
        WorkflowTemplateModel workflowTemplate = null;
        Object object = workflowTemplateObject.getObject();
        if(object instanceof WorkflowTemplateModel)
        {
            workflowTemplate = (WorkflowTemplateModel)object;
        }
        else if(object instanceof WorkflowActionTemplateModel)
        {
            workflowTemplate = ((WorkflowActionTemplateModel)object).getWorkflow();
        }
        return workflowTemplate;
    }


    private void setCssStyle(Treeitem item, Treerow treerow, TypedObject workflowTemplateObject)
    {
        Object object = workflowTemplateObject.getObject();
        if(object instanceof WorkflowTemplateModel)
        {
            treerow.setSclass("workflow-template");
            item.setImage("/cockpit/images/icon_workflow.png");
        }
        else if(object instanceof WorkflowActionTemplateModel)
        {
            treerow.setSclass("workflow-template-step");
        }
    }


    protected DragAndDropWrapper getDDWrapper()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective().getDragAndDropWrapperService().getWrapper();
    }


    public WorkflowService getWorkflowService()
    {
        return (WorkflowService)Registry.getApplicationContext().getBean("newestWorkflowService");
    }


    private TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    private ModelService getModelService()
    {
        return (ModelService)SpringUtil.getBean("modelService");
    }
}
