package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Menupopup;

public class RemoveContentElementAction extends AbstractCmscockpitListViewAction
{
    private static final String REMOVE_CONTENT_ELEMENT_ACTION_URL = "/cmscockpit/images/cnt_elem_remove_action.png";
    private CMSAdminComponentService adminComponentService = null;


    public CMSAdminComponentService getAdminComponentService()
    {
        if(this.adminComponentService == null)
        {
            this.adminComponentService = (CMSAdminComponentService)SpringUtil.getBean("cmsAdminComponentService");
        }
        return this.adminComponentService;
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        boolean canRemove = getSystemService().checkPermissionOn(context.getItem().getType().getCode(), "remove");
        if(!isEnabled() || isComponentLockedForUser(context.getItem()) || !isWritableCatalog(context.getItem()) ||
                        !getSystemService().checkPermissionOn("AbstractCMSComponent", "remove") || !canRemove)
        {
            return null;
        }
        return "/cmscockpit/images/cnt_elem_remove_action.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.remove");
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }
}
