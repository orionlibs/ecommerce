package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.listview.impl.ContextAreaTableModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class AddItemCommentAction extends AbstractCommentAction
{
    private TypeService typeService;
    private String commentTypeCode;
    private WorkflowService serviceLayerWorkflowService;


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }


    public String getImageURI(ListViewAction.Context context)
    {
        if(context.getModel() instanceof ContextAreaTableModel && ((ContextAreaTableModel)context
                        .getModel()).getBrowserModel() instanceof AdvancedBrowserModel)
        {
            TypedObject contextRootItem = ((AdvancedBrowserModel)((ContextAreaTableModel)context.getModel()).getBrowserModel()).getContextRootItem();
            Object object = contextRootItem.getObject();
            if(object instanceof WorkflowActionModel)
            {
                WorkflowActionModel wfAction = (WorkflowActionModel)object;
                boolean permission = UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(getTypeService().getObjectType("Reply").getCode(), "create");
                if(permission && !getServiceLayerWorkflowService().isTerminated(wfAction.getWorkflow()))
                {
                    return "/cockpit/images/icon-addComment.png";
                }
            }
        }
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("comment.addComment");
    }


    public String getCommentTypeCode()
    {
        return this.commentTypeCode;
    }


    public void setCommentTypeCode(String commentTypeCode)
    {
        this.commentTypeCode = commentTypeCode;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    private WorkflowService getServiceLayerWorkflowService()
    {
        if(this.serviceLayerWorkflowService == null)
        {
            this
                            .serviceLayerWorkflowService = (WorkflowService)Registry.getApplicationContext().getBean("newestWorkflowService");
        }
        return this.serviceLayerWorkflowService;
    }
}
