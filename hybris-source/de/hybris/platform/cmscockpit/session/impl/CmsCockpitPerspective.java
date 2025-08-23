package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.events.impl.CmsPerspectiveInitEvent;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.EditorAreaController;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;

public class CmsCockpitPerspective extends BaseUICockpitPerspective
{
    private static final Logger LOG = Logger.getLogger(CmsCockpitPerspective.class);
    private CMSAdminSiteService adminSiteService = null;
    private CmsCockpitService cmsCockpitService;
    protected static final String CATALOGVERSION = ".catalogVersion";
    private String activeSiteCode = null;


    public void initialize(Map<String, Object> params)
    {
        UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new CmsPerspectiveInitEvent(this));
        if(getCmsAdminSiteService().getActiveSite() == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No active site available.");
            }
            if(StringUtils.isBlank(this.activeSiteCode))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("No active site has been configured for this perspective. Please make sure you have set the 'activeSiteCode' property in your Spring configuration correctly.");
                }
            }
            else
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("Activating site '" + this.activeSiteCode + "'.");
                }
                try
                {
                    if(!StringUtils.isBlank(this.activeSiteCode))
                    {
                        getCmsAdminSiteService().setActiveSiteForId(this.activeSiteCode);
                    }
                }
                catch(AmbiguousIdentifierException | de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException cinfe)
                {
                    LOG.warn("Could not set active site.", (Throwable)cinfe);
                }
            }
        }
        super.initialize(params);
    }


    public void createNewItem(ObjectTemplate template, Map<String, Object> initValues, boolean loadDefaultValues)
    {
        Map<String, Object> initialValues = new HashMap<>();
        initialValues.putAll(initValues);
        if(!initialValues.containsKey("CMSItem.catalogVersion"))
        {
            TypedObject catalogVersion = getTypeService().wrapItem(getCmsAdminSiteService().getActiveCatalogVersion());
            initialValues.put("CMSItem.catalogVersion", catalogVersion);
        }
        getEditorArea().reset();
        setActiveItem(null);
        getEditorArea().setCurrentObject(getActiveItem());
        for(BrowserModel b : getBrowserArea().getVisibleBrowsers())
        {
            getBrowserArea().updateActivation(b);
        }
        EditorAreaController eac = getEditorArea().getEditorAreaController();
        eac.setCreateFromTemplate((ObjectType)template, initialValues, loadDefaultValues);
        eac.resetSectionPanelModel();
    }


    protected static ThreadLocal<Boolean> activationFallBack = new ThreadLocal<>();


    public void activateItemInEditorFallback(TypedObject activeItem)
    {
        try
        {
            activationFallBack.set(Boolean.TRUE);
            activateItemInEditor(activeItem);
        }
        finally
        {
            activationFallBack.set(Boolean.FALSE);
        }
    }


    protected CmsPageBrowserModel newCmsPageBrowserModel()
    {
        return (CmsPageBrowserModel)SpringUtil.getBean("cmsPageBrowserModel");
    }


    private CmsPageBrowserModel getPageBrowserFor(TypedObject page)
    {
        for(BrowserModel browser : getBrowserArea().getBrowsers())
        {
            if(browser instanceof CmsPageBrowserModel)
            {
                CmsPageBrowserModel pageBrowser = (CmsPageBrowserModel)browser;
                if(page.equals(pageBrowser.getCurrentPageObject()))
                {
                    return pageBrowser;
                }
            }
        }
        return null;
    }


    protected void activateItemInEditorArea(TypedObject activeItem)
    {
        if(!Boolean.TRUE.equals(activationFallBack.get()))
        {
            if(activeItem != null && getTypeService().checkItemAlive(activeItem) && UISessionUtils.getCurrentSession()
                            .getTypeService().getBaseType(GeneratedCms2Constants.TC.ABSTRACTPAGE).isAssignableFrom((ObjectType)activeItem.getType()))
            {
                if(checkActiveSiteAndCatalog(activeItem))
                {
                    closeOtherBrowsers(activeItem);
                }
                CmsPageBrowserModel model = getPageBrowserFor(activeItem);
                if(model == null)
                {
                    model = newCmsPageBrowserModel();
                    model.setCurrentPageObject(activeItem);
                    model.initialize();
                }
                getBrowserArea().show((BrowserModel)model);
                if(getActiveItem() != null)
                {
                    super.activateItemInEditorArea(activeItem);
                }
            }
            else
            {
                super.activateItemInEditorArea(activeItem);
            }
        }
        else
        {
            super.activateItemInEditorArea(activeItem);
        }
    }


    protected boolean checkActiveSiteAndCatalog(TypedObject pageObject)
    {
        boolean changed = false;
        CatalogVersionModel currentCatalogVersion = getCmsAdminSiteService().getActiveCatalogVersion();
        AbstractPageModel page = (AbstractPageModel)pageObject.getObject();
        if(currentCatalogVersion == null || !currentCatalogVersion.equals(page.getCatalogVersion()))
        {
            changed = true;
            getCmsAdminSiteService().setActiveCatalogVersion(page.getCatalogVersion());
            Collection<CMSSiteModel> sites = getCmsCockpitService().getSites();
            for(CMSSiteModel site : sites)
            {
                if(site.getContentCatalogs().contains(page.getCatalogVersion().getCatalog()))
                {
                    getCmsAdminSiteService().setActiveSite(site);
                }
            }
        }
        return changed;
    }


    protected void closeOtherBrowsers(TypedObject item)
    {
        if(item != null)
        {
            Object object = item.getObject();
            if(object instanceof AbstractPageModel)
            {
                UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
                if(browserArea instanceof CMSBrowserArea)
                {
                    CMSBrowserArea cmsBrowserArea = (CMSBrowserArea)browserArea;
                    AdvancedBrowserModel welcomeBrowserModel = cmsBrowserArea.getWelcomeBrowserModel();
                    boolean containsWelcomeBrowser = false;
                    List<BrowserModel> allBrowsers = new ArrayList<>(cmsBrowserArea.getBrowsers());
                    for(BrowserModel browser : allBrowsers)
                    {
                        if(browser instanceof CmsPageBrowserModel)
                        {
                            cmsBrowserArea.close(browser);
                        }
                        if(browser.equals(welcomeBrowserModel))
                        {
                            containsWelcomeBrowser = true;
                        }
                    }
                    if(!containsWelcomeBrowser)
                    {
                        if(welcomeBrowserModel == null)
                        {
                            LOG.error("Current browser area needs to provide a welcome browser model.");
                        }
                        else
                        {
                            cmsBrowserArea.addVisibleBrowser(0, (BrowserModel)welcomeBrowserModel);
                        }
                    }
                }
            }
        }
    }


    public void setActiveSiteCode(String activeSiteCode)
    {
        this.activeSiteCode = activeSiteCode;
    }


    public String getActiveSiteCode()
    {
        return this.activeSiteCode;
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService adminSiteService)
    {
        this.adminSiteService = adminSiteService;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.adminSiteService;
    }


    protected CmsCockpitService getCmsCockpitService()
    {
        if(this.cmsCockpitService == null)
        {
            this.cmsCockpitService = (CmsCockpitService)SpringUtil.getBean("cmsCockpitService");
        }
        return this.cmsCockpitService;
    }


    public void setCmsCockpitService(CmsCockpitService cmsCockpitService)
    {
        this.cmsCockpitService = cmsCockpitService;
    }
}
