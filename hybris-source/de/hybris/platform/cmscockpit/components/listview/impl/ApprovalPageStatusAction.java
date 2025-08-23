package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.helpers.ModelHelper;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class ApprovalPageStatusAction extends AbstractCmscockpitListViewAction
{
    private static final Logger LOG = Logger.getLogger(ApprovalPageStatusAction.class);
    protected static final String IMAGE_URL_MULTISELECT_AVAILABLE = "/cockpit/images/icon_func_approval.png";
    protected static final String IMAGE_URL_MULTISELECT_UNAVAILABLE = "/cockpit/images/icon_func_approval_unavailable.png";
    private CmsCockpitService cmsCockpitService;
    private ModelHelper modelHelper;


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        String uri = "/cmscockpit/images//icon_product_approval_default.gif";
        String approvalStatus = getStatusCode(context);
        if(approvalStatus != null)
        {
            if("unapproved".equals(approvalStatus))
            {
                uri = "/cmscockpit/images//icon_product_approval_unapproved.gif";
            }
            else if("approved".equals(approvalStatus))
            {
                uri = "/cmscockpit/images//icon_product_approval_approved.gif";
            }
        }
        return uri;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        Menupopup popupMenu = null;
        if(!isComponentLockedForUser(context.getItem()) && getUIAccessRightService().isWritable((ObjectType)context.getItem().getType(),
                        getTypeService().getPropertyDescriptor("AbstractPage.approvalStatus"), false) &&
                        isWritableCatalog(context.getItem()))
        {
            popupMenu = new Menupopup();
            List<String> approvalStatusList = getCmsCockpitService().getAllApprovalStatusCodes();
            for(String approvalStatusCode : approvalStatusList)
            {
                String label = getCmsCockpitService().getApprovalStatusName(approvalStatusCode);
                if(label == null)
                {
                    label = approvalStatusCode;
                }
                Menuitem menuItem = new Menuitem(label);
                menuItem.setParent((Component)popupMenu);
                if(UISessionUtils.getCurrentSession().isUsingTestIDs())
                {
                    String id = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(context.getItem());
                    id = id.replaceAll("\\W", "") + "_" + id.replaceAll("\\W", "");
                    id = id + "." + id;
                    id = "Menuitem_" + id;
                    UITools.applyTestID((Component)menuItem, id);
                }
                UITools.addBusyListener((Component)menuItem, "onClick", (EventListener)new Object(this, context, approvalStatusCode), null, "busy.approval");
            }
        }
        return popupMenu;
    }


    protected void doChangeStatus(TypedObject selectedItem, String approvalStatusCode)
    {
        getCmsCockpitService().setApprovalStatus(selectedItem, approvalStatusCode);
        try
        {
            this.modelHelper.saveModel((ItemModel)selectedItem.getObject(), true, true);
        }
        catch(ValueHandlerException e)
        {
            LOG.error(e, (Throwable)e);
        }
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("cmspage.approvalstatus.tooltip");
    }


    public String getStatusCode(ListViewAction.Context context)
    {
        if(getTypeService().getBaseType(GeneratedCms2Constants.TC.ABSTRACTPAGE).isAssignableFrom((ObjectType)context.getItem().getType()))
        {
            return getCmsCockpitService().getApprovalStatusCode(context.getItem());
        }
        return null;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        if(context.getBrowserModel().getSelectedItems().isEmpty())
        {
            return "/cockpit/images/icon_func_approval_unavailable.png";
        }
        return "/cockpit/images/icon_func_approval.png";
    }


    public Menupopup getMultiSelectPopup(ListViewAction.Context context)
    {
        Menupopup popupMenu = new Menupopup();
        List<String> approvalStatusList = getCmsCockpitService().getAllApprovalStatusCodes();
        for(String approvalStatusCode : approvalStatusList)
        {
            String label = getCmsCockpitService().getApprovalStatusName(approvalStatusCode);
            if(label == null)
            {
                label = approvalStatusCode;
            }
            Menuitem menuItem = new Menuitem(label);
            menuItem.setParent((Component)popupMenu);
            UITools.addBusyListener((Component)menuItem, "onClick", (EventListener)new Object(this, context, approvalStatusCode), null, "busy.approval");
        }
        return popupMenu;
    }


    public CmsCockpitService getCmsCockpitService()
    {
        if(this.cmsCockpitService == null)
        {
            this.cmsCockpitService = (CmsCockpitService)SpringUtil.getBean("cmsCockpitService");
        }
        return this.cmsCockpitService;
    }


    public void setModelHelper(ModelHelper modelHelper)
    {
        this.modelHelper = modelHelper;
    }
}
