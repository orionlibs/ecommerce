package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractBrowserComponent;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.components.contentbrowser.DefaultSearchContentBrowser;
import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.spring.SpringUtil;

public class CmsCatalogSearchContentBrowser extends DefaultSearchContentBrowser
{
    protected AbstractBrowserComponent createToolbarComponent()
    {
        return (AbstractBrowserComponent)new Object(this, getModel(), (AbstractContentBrowser)this);
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
    }


    public boolean update()
    {
        boolean ret = false;
        if(!UITools.isFromOtherDesktop((Component)this))
        {
            ret = super.update();
        }
        return ret;
    }
}
