package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.listview.impl.AddMultiItemCommentAction;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.PageableBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Image;

public class WorkflowPagerToolbarBrowserComponent extends PagerToolbarBrowserComponent
{
    private ActionColumnConfiguration actionConfig;
    private final String commentTypeCode = "workflow";
    private TypeService typeService;
    private WorkflowService workflowService;


    public WorkflowPagerToolbarBrowserComponent(PageableBrowserModel model, AbstractContentBrowser contentBrowser)
    {
        super(model, contentBrowser);
    }


    protected ActionColumnConfiguration getActionConfig()
    {
        if(this.actionConfig == null)
        {
            this.actionConfig = (ActionColumnConfiguration)SpringUtil.getBean("WorkflowItemsContentBrowserActionColumn");
        }
        return this.actionConfig;
    }


    protected void updateCommentToolbarSlot()
    {
        if(getCommentToolbarSlot() != null)
        {
            getCommentToolbarSlot().getChildren().clear();
            if(!getModel().getSelectedItems().isEmpty())
            {
                ListViewAction.Context context = new ListViewAction.Context();
                context.setBrowserModel((BrowserModel)getModel());
                if("LIST".equals(getModel().getViewMode()) || "MULTI_TYPE_LIST"
                                .equals(getModel().getViewMode()))
                {
                    context.setModel((TableModel)getModel().getTableModel());
                }
                List<ItemModel> selectedItemAttachments = UISessionUtils.getCurrentSession().getTypeService().unwrapItems(getModel().getSelectedItems());
                List<ItemModel> selectedItems = new ArrayList<>();
                for(ItemModel model : selectedItemAttachments)
                {
                    if(model instanceof WorkflowItemAttachmentModel)
                    {
                        boolean permission = UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(getTypeService().getObjectType("Comment").getCode(), "create");
                        if(permission && !getWorkflowService().isTerminated(((WorkflowItemAttachmentModel)model).getWorkflow()))
                        {
                            selectedItems.add(((WorkflowItemAttachmentModel)model).getItem());
                        }
                    }
                }
                if(!selectedItems.isEmpty())
                {
                    AddMultiItemCommentAction addMultiItemCommentAction = new AddMultiItemCommentAction(UISessionUtils.getCurrentSession().getTypeService().wrapItems(selectedItems));
                    addMultiItemCommentAction.setCommentTypeCode("workflow");
                    String multiSelectImageURI = addMultiItemCommentAction.getMultiSelectImageURI(context);
                    if(multiSelectImageURI != null)
                    {
                        Image actionImg = new Image(multiSelectImageURI);
                        EventListener listener = addMultiItemCommentAction.getMultiSelectEventListener(context);
                        if(listener != null)
                        {
                            actionImg.addEventListener("onClick", listener);
                        }
                        if(!StringUtils.isBlank(addMultiItemCommentAction.getTooltip(context)))
                        {
                            actionImg.setTooltiptext(addMultiItemCommentAction.getTooltip(context));
                        }
                        getCommentToolbarSlot().appendChild((Component)actionImg);
                    }
                }
            }
        }
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    public WorkflowService getWorkflowService()
    {
        if(this.workflowService == null)
        {
            this.workflowService = (WorkflowService)Registry.getApplicationContext().getBean("newestWorkflowService");
        }
        return this.workflowService;
    }
}
