package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminContentSlotService;
import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Menupopup;

public class RemoveContentSlotAction extends AbstractListViewAction
{
    private static final String REMOVE_CONTENT_ELEMENT_ACTION_URL = "/cmscockpit/images/cnt_elem_remove_action.png";
    private CMSAdminContentSlotService cmsAdminSlotService = null;


    public CMSAdminContentSlotService getCMSAdminContentSlotService()
    {
        if(this.cmsAdminSlotService == null)
        {
            this.cmsAdminSlotService = (CMSAdminContentSlotService)SpringUtil.getBean("cmsAdminContentSlotService");
        }
        return this.cmsAdminSlotService;
    }


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
        return "/cmscockpit/images/cnt_elem_remove_action.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.remove");
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }
}
