package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.events.impl.PageViewNavigationEvent;
import de.hybris.platform.cmscockpit.session.CmsCatalogBrowserModelFactory;
import de.hybris.platform.cockpit.components.contentbrowser.AbstractContentBrowser;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserArea;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Messagebox;

public class CatalogBrowserArea extends DefaultSearchBrowserArea implements CMSBrowserArea, SiteVersionAware
{
    private static final Logger LOG = Logger.getLogger(CatalogBrowserArea.class);
    private AdvancedBrowserModel welcomeBrowserModel = null;
    private CMSSiteModel activeSite;
    private CatalogVersionModel activeCatalogVersion;
    private CMSAdminSiteService cmsAdminSiteService;
    private CmsCatalogBrowserModelFactory cmsCatalogBrowserModelFactory;


    public void initialize(Map<String, Object> params)
    {
        if(!this.initialized)
        {
            if(this.visibleBrowsers == null || this.visibleBrowsers.isEmpty())
            {
                BrowserModel browserModel = null;
                if(this.browsers == null || this.browsers.isEmpty())
                {
                    AdvancedBrowserModel advancedBrowserModel = getWelcomeBrowserModel();
                    if(advancedBrowserModel == null)
                    {
                        ObjectTemplate rootType = null;
                        if(!StringUtils.isBlank(getRootSearchTypeCode()))
                        {
                            rootType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(getRootSearchTypeCode());
                        }
                        CmsCatalogBrowserModel cmsCatalogBrowserModel2 = this.cmsCatalogBrowserModelFactory.getInstance(rootType);
                        cmsCatalogBrowserModel2.updateItems();
                        CmsCatalogBrowserModel cmsCatalogBrowserModel1 = cmsCatalogBrowserModel2;
                    }
                }
                else
                {
                    browserModel = this.browsers.get(0);
                }
                addVisibleBrowser(browserModel);
                setFocusedBrowser(browserModel);
                browserModel.updateItems();
            }
            this.initialized = true;
        }
    }


    public AbstractContentBrowser getCorrespondingContentBrowser(BrowserModel browserModel)
    {
        return super.getCorrespondingContentBrowser(browserModel);
    }


    public BrowserModel createNewDefaultBrowser()
    {
        CmsCatalogBrowserModel cmsCatalogBrowserModel;
        BrowserModel ret = null;
        if(getCmsAdminSiteService().getActiveCatalogVersion() == null)
        {
            Clients.showBusy(null, false);
            try
            {
                Messagebox.show(Labels.getLabel("warn.catalog_version_info"), "Warning!", 1, "z-msgbox z-msgbox-information");
            }
            catch(InterruptedException e)
            {
                LOG.error("ZK related problem appears while displaying a messagebox", e);
            }
            ret = getFocusedBrowser();
        }
        else
        {
            ObjectTemplate rootType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(getRootSearchTypeCode());
            if(rootType != null)
            {
                CmsCatalogBrowserModel cmsCatalgoBrowserModel = this.cmsCatalogBrowserModelFactory.getInstance(rootType);
                cmsCatalgoBrowserModel.setSimpleQuery("");
                cmsCatalgoBrowserModel.updateItems();
                cmsCatalogBrowserModel = cmsCatalgoBrowserModel;
            }
        }
        return (BrowserModel)cmsCatalogBrowserModel;
    }


    public boolean providesDefaultBrowser()
    {
        return Boolean.TRUE.booleanValue();
    }


    public AdvancedBrowserModel getWelcomeBrowserModel()
    {
        return this.welcomeBrowserModel;
    }


    public void setWelcomeBrowserModel(AdvancedBrowserModel welcomeBrowserModel)
    {
        this.welcomeBrowserModel = welcomeBrowserModel;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        if(this.cmsAdminSiteService == null)
        {
            this.cmsAdminSiteService = (CMSAdminSiteService)SpringUtil.getBean("cmsAdminSiteService");
        }
        return this.cmsAdminSiteService;
    }


    public void update()
    {
        super.update();
        if(getPerspective().getActiveItem() != null)
        {
            BrowserModel browserModel = getFocusedBrowser();
            if(browserModel instanceof CmsPageBrowserModel)
            {
                TypedObject associatedPageTypeObject = ((CmsPageBrowserModel)browserModel).getCurrentPageObject();
                ((CmsCockpitPerspective)getPerspective()).activateItemInEditorFallback(associatedPageTypeObject);
            }
        }
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof de.hybris.platform.cmscockpit.events.impl.CmsPerspectiveInitEvent)
        {
            if(event.getSource() == null || !event.getSource().equals(getPerspective()))
            {
                return;
            }
            getCmsAdminSiteService().setActiveSite(getActiveSite());
            getCmsAdminSiteService().setActiveCatalogVersion(getActiveCatalogVersion());
        }
        else if(event instanceof PageViewNavigationEvent)
        {
            getCmsAdminSiteService().setActiveSite(((PageViewNavigationEvent)event).getSite());
            getCmsAdminSiteService().setActiveCatalogVersion(((PageViewNavigationEvent)event).getCatalog());
            UISessionUtils.getCurrentSession()
                            .getCurrentPerspective()
                            .activateItemInEditor(
                                            UISessionUtils.getCurrentSession().getTypeService().wrapItem(((PageViewNavigationEvent)event).getPagePk()));
        }
        else if(event instanceof ItemChangedEvent && ((ItemChangedEvent)event).getItem() != null && ((ItemChangedEvent)event)
                        .getItem().getObject() instanceof CMSSiteModel)
        {
            ItemChangedEvent changedEvent = (ItemChangedEvent)event;
            switch(null.$SwitchMap$de$hybris$platform$cockpit$events$impl$ItemChangedEvent$ChangeType[changedEvent.getChangeType().ordinal()])
            {
                case 1:
                    getWelcomeBrowserModel().updateItems();
                    break;
            }
        }
        super.onCockpitEvent(event);
    }


    public CMSSiteModel getActiveSite()
    {
        return this.activeSite;
    }


    public void setCmsAdminSiteService(CMSAdminSiteService cmsAdminSiteService)
    {
        this.cmsAdminSiteService = cmsAdminSiteService;
    }


    public void setActiveSite(CMSSiteModel activeSite)
    {
        this.activeSite = activeSite;
    }


    public void setActiveCatalogVersion(CatalogVersionModel activeCatalogVersion)
    {
        this.activeCatalogVersion = activeCatalogVersion;
    }


    public CatalogVersionModel getActiveCatalogVersion()
    {
        return this.activeCatalogVersion;
    }


    @Required
    public void setCmsCatalogBrowserModelFactory(CmsCatalogBrowserModelFactory cmsCatalogBrowserModelFactory)
    {
        this.cmsCatalogBrowserModelFactory = cmsCatalogBrowserModelFactory;
    }


    public void close(BrowserModel browserModel)
    {
        if(getBrowsers().size() == 1 && !browserModel.equals(getWelcomeBrowserModel()))
        {
            addVisibleBrowser((BrowserModel)getWelcomeBrowserModel());
        }
        super.close(browserModel);
    }
}
