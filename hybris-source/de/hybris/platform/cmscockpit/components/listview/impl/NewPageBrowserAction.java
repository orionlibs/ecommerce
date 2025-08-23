package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.testing.TestIdEnabled;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Menupopup;

public class NewPageBrowserAction extends AbstractCmscockpitListViewAction implements TestIdEnabled
{
    private static final String NEW_IMAGE_URI = "/cockpit/images/icon_func_new_available.png";


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        Object object = null;
        if(isActionAllowed())
        {
            object = new Object(this);
        }
        return (EventListener)object;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        if(isActionAllowed())
        {
            return "/cockpit/images/icon_func_new_available.png";
        }
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("cmscockpit.create_item");
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    protected boolean isActionAllowed()
    {
        CatalogVersionModel catalogVersion = ((CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService")).getActiveCatalogVersion();
        if(catalogVersion != null)
        {
            if(UISessionUtils.getCurrentSession().getUiAccessRightService()
                            .canWrite(UISessionUtils.getCurrentSession().getUser(), catalogVersion))
            {
                return true;
            }
        }
        return false;
    }


    public String getTestId()
    {
        return "BrowserArea_AddPage_button";
    }
}
