package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.helpers.ModelHelper;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class ApprovalAction extends ApprovalStatus
{
    private static final Logger LOG = LoggerFactory.getLogger(ApprovalAction.class);
    protected String ICON_FUNC_APPROVAL_ACTION_AVAILABLE = "cockpit/images/icon_func_approval.png";
    protected String ICON_FUNC_APPROVAL_ACTION_UNAVAILABLE = "cockpit/images/icon_func_approval_unavailable.png";
    private ModelHelper modelHelper;


    public Menupopup getMultiSelectPopup(ListViewAction.Context context)
    {
        Menupopup popupMenu = null;
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() >= 1)
        {
            popupMenu = new Menupopup();
            List<String> approvalStatusList = getProductCockpitProductService().getAllApprovalStatusCodes();
            for(String approvalStatusCode : approvalStatusList)
            {
                String label = getProductCockpitProductService().getApprovalStatusName(approvalStatusCode);
                if(label == null)
                {
                    label = approvalStatusCode;
                }
                Menuitem menuItem = new Menuitem(label);
                menuItem.setParent((Component)popupMenu);
                UITools.addBusyListener((Component)menuItem, "onClick", (EventListener)new Object(this, selectedItems, approvalStatusCode, context), null, "busy.approval");
            }
        }
        return popupMenu;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        Menupopup popupMenu = new Menupopup();
        List<String> approvalStatusList = getProductCockpitProductService().getAllApprovalStatusCodes();
        for(String approvalStatusCode : approvalStatusList)
        {
            String label = getProductCockpitProductService().getApprovalStatusName(approvalStatusCode);
            if(label == null)
            {
                label = approvalStatusCode;
            }
            Menuitem menuItem = new Menuitem(label);
            menuItem.setParent((Component)popupMenu);
            if(UISessionUtils.getCurrentSession().isUsingTestIDs())
            {
                String id = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(context.getItem());
                id = id.replaceAll("\\W", "") + "_" + id.replaceAll("\\W", "");
                id = id + "." + id;
                id = "Menuitem_" + id;
                UITools.applyTestID((Component)menuItem, id);
            }
            UITools.addBusyListener((Component)menuItem, "onClick", (EventListener)new Object(this, context, approvalStatusCode), null, "busy.approval");
        }
        return popupMenu;
    }


    protected void doChangeStatus(TypedObject selectedItem, String approvalStatusCode)
    {
        getProductCockpitProductService().setApprovalStatus(selectedItem, approvalStatusCode);
        try
        {
            this.modelHelper.saveModel((ItemModel)selectedItem.getObject(), true, true);
        }
        catch(ValueHandlerException e)
        {
            LOG.error(e.getMessage(), (Throwable)e);
        }
    }


    public String getImageURI(ListViewAction.Context context)
    {
        return this.ICON_FUNC_APPROVAL_ACTION_AVAILABLE;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() >= 1)
        {
            return this.ICON_FUNC_APPROVAL_ACTION_AVAILABLE;
        }
        return this.ICON_FUNC_APPROVAL_ACTION_UNAVAILABLE;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("gridview.item.approval.action.tooltip");
    }


    public void setModelHelper(ModelHelper modelHelper)
    {
        this.modelHelper = modelHelper;
    }
}
