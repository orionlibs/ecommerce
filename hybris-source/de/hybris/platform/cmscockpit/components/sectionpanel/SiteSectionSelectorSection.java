package de.hybris.platform.cmscockpit.components.sectionpanel;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cmscockpit.session.impl.CatalogBrowserArea;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class SiteSectionSelectorSection extends DefaultSectionSelectorSection
{
    private static final Logger LOG = Logger.getLogger(SiteSectionSelectorSection.class);
    private boolean initialized = false;
    private CMSAdminSiteService cmsAdminSiteService = null;
    private CmsCockpitService cmsCockpitService;


    public void selectionChanged()
    {
        informCmsAboutCurrentSiteChanged();
        closeEditorArea();
        refreshView();
    }


    public List<TypedObject> getItems()
    {
        if(!this.initialized)
        {
            List<CMSSiteModel> sites = new ArrayList<>(getCmsCockpitService().getSites());
            if(!sites.isEmpty())
            {
                setItems(getTypeService().wrapItems(sites));
            }
            this.initialized = true;
        }
        return super.getItems();
    }


    public void updateItems()
    {
        List<CMSSiteModel> sites = new ArrayList<>(getCmsCockpitService().getSites());
        if(!sites.isEmpty())
        {
            setItems(getTypeService().wrapItems(sites));
        }
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof CmsNavigationEvent)
        {
            CmsNavigationEvent cmsNavigationEvent = (CmsNavigationEvent)event;
            if(cmsNavigationEvent.getSite() != null)
            {
                if(!event.getSource().equals(getNavigationAreaModel().getNavigationArea().getPerspective()))
                {
                    return;
                }
                setRelatedObject(getTypeService().wrapItem(cmsNavigationEvent.getSite()));
                setSelectedItem(getTypeService().wrapItem(cmsNavigationEvent.getSite()));
                for(Section section : getSubSections())
                {
                    if(section instanceof CockpitEventAcceptor)
                    {
                        CockpitEventAcceptor sectionAcceptor = (CockpitEventAcceptor)section;
                        if(cmsNavigationEvent.getCatalog() != null)
                        {
                            sectionAcceptor.onCockpitEvent(event);
                        }
                    }
                }
            }
        }
    }


    protected void closeEditorArea()
    {
        TypedObject currentEditorObject = UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea().getCurrentObject();
        if(currentEditorObject != null)
        {
            UICockpitPerspective currentPerspective = UISessionUtils.getCurrentSession().getCurrentPerspective();
            currentPerspective.getEditorArea().setCurrentObject(null);
            ((BaseUICockpitPerspective)currentPerspective).collapseEditorArea();
        }
    }


    protected void informCmsAboutCurrentSiteChanged()
    {
        Object object = (getRelatedObject() == null) ? null : getRelatedObject().getObject();
        CMSSiteModel siteModel = null;
        if(object instanceof CMSSiteModel)
        {
            siteModel = (CMSSiteModel)object;
        }
        getCmsAdminSiteService().setActiveSite(siteModel);
        getCmsAdminSiteService().setActiveCatalogVersion(null);
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof CatalogBrowserArea)
        {
            CatalogBrowserArea catalogBrowserArea = (CatalogBrowserArea)browserArea;
            catalogBrowserArea.setActiveSite(siteModel);
            catalogBrowserArea.setActiveCatalogVersion(null);
            AdvancedBrowserModel welcomeBrowserModel = catalogBrowserArea.getWelcomeBrowserModel();
            if(welcomeBrowserModel == null)
            {
                LOG.error("Current browser area needs to provide a welcome browser model.");
            }
            else
            {
                catalogBrowserArea.addVisibleBrowser(0, (BrowserModel)welcomeBrowserModel);
                catalogBrowserArea.closeOthers((BrowserModel)welcomeBrowserModel);
            }
        }
    }


    public void refreshView()
    {
        List<CMSSiteModel> sites = new ArrayList<>(getCmsCockpitService().getSites());
        if(!sites.isEmpty())
        {
            setItems(getTypeService().wrapItems(sites));
        }
        super.refreshView();
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService siteService)
    {
        this.cmsAdminSiteService = siteService;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.cmsAdminSiteService;
    }


    protected TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }


    protected CmsCockpitService getCmsCockpitService()
    {
        return this.cmsCockpitService;
    }


    @Required
    public void setCmsCockpitService(CmsCockpitService cmsCockpitService)
    {
        this.cmsCockpitService = cmsCockpitService;
    }
}
