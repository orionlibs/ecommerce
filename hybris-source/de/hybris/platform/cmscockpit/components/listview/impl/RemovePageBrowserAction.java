package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.servicelayer.security.permissions.PermissionCheckingService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Menupopup;

public class RemovePageBrowserAction extends AbstractCmscockpitListViewAction
{
    private static final Logger LOG = Logger.getLogger(RemovePageBrowserAction.class);
    private static final String IMAGE_URI = "/cockpit/images/icon_func_delete.png";
    private static final String MULTISELECT_IMAGE_URI = "/cockpit/images/icon_func_delete_22.png";
    private static final String MULTISELECT_IMAGE_URI_UNAVAILABLE = "/cockpit/images/icon_func_delete_unavailable_22.png";
    private CMSAdminPageService cmsAdminPageService;
    private PermissionCheckingService permissionCheckingService;


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        EventListener ret = null;
        return (EventListener)new Object(this, context);
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        if(isComponentLockedForUser(context.getItem()) || !isWritableCatalog(context.getItem()) ||
                        !getSystemService().checkPermissionOn("AbstractPage", "remove"))
        {
            return null;
        }
        return "/cockpit/images/icon_func_delete.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.remove");
    }


    public CMSAdminPageService getAdminContentElementService()
    {
        if(this.cmsAdminPageService == null)
        {
            this.cmsAdminPageService = (CMSAdminPageService)SpringUtil.getBean("cmsAdminPageService");
        }
        return this.cmsAdminPageService;
    }


    public PermissionCheckingService getPermissionCheckingService()
    {
        if(this.permissionCheckingService == null)
        {
            this.permissionCheckingService = (PermissionCheckingService)SpringUtil.getBean("permissionCheckingService");
        }
        return this.permissionCheckingService;
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        EventListener ret = null;
        if(CollectionUtils.isNotEmpty(context.getBrowserModel().getSelectedIndexes()) && context
                        .getBrowserModel().getSelectedIndexes().size() == 1)
        {
            if(context.getItem() == null && context.getBrowserModel().getSelectedItems().iterator().next() != null)
            {
                context.setItem(context.getBrowserModel().getSelectedItems().iterator().next());
            }
            ret = getEventListener(context);
        }
        return ret;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        if(CollectionUtils.isNotEmpty(context.getBrowserModel().getSelectedIndexes()) && context
                        .getBrowserModel().getSelectedIndexes().size() == 1)
        {
            return "/cockpit/images/icon_func_delete_22.png";
        }
        return "/cockpit/images/icon_func_delete_unavailable_22.png";
    }
}
