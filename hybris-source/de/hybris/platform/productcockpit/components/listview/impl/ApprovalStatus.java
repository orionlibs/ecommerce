package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.product.ProductModel;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class ApprovalStatus extends AbstractProductAction
{
    protected static final String UNKNOWN_STATE_ICON = "cockpit/images/icon_status_approve_unknown.png";
    protected static final String UNAPPROVED_STATE_ICON = "cockpit/images/icon_status_approve_not_x.png";
    protected static final String APPROVED_STATE_ICON = "cockpit/images/icon_status_approve_ok.png";
    private TypeService typeService = null;


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        String uri = "cockpit/images/icon_status_approve_unknown.png";
        String approvalStatus = getStatusCode(context);
        if(approvalStatus != null)
        {
            if("unapproved".equals(approvalStatus))
            {
                uri = "cockpit/images/icon_status_approve_not_x.png";
            }
            else if("approved".equals(approvalStatus))
            {
                uri = "cockpit/images/icon_status_approve_ok.png";
            }
        }
        return uri;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getMultiSelectPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getStatusCode(ListViewAction.Context context)
    {
        if(getTypeService().getBaseType("Product").isAssignableFrom((ObjectType)context.getItem().getType()))
        {
            return getProductCockpitProductService().getApprovalStatusCode(context.getItem());
        }
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        String approvalStatus = getApprovalStatus(context.getItem());
        return (approvalStatus == null) ? Labels.getLabel("gridview.item.approvalstatus.tooltip") : approvalStatus;
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    private String getApprovalStatus(TypedObject typedObject)
    {
        String result = null;
        if(typedObject != null)
        {
            Object object = typedObject.getObject();
            if(object instanceof ProductModel && ((ProductModel)object).getApprovalStatus() != null)
            {
                result = Labels.getLabel("general.status") + ": " + Labels.getLabel("general.status");
            }
        }
        return result;
    }
}
