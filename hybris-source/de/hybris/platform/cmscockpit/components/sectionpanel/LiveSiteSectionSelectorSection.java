package de.hybris.platform.cmscockpit.components.sectionpanel;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserArea;
import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserModel;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class LiveSiteSectionSelectorSection extends DefaultSectionSelectorSection
{
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
        CMSSiteModel siteModel = null;
        Object object = (getRelatedObject() == null) ? null : getRelatedObject().getObject();
        if(object instanceof CMSSiteModel)
        {
            siteModel = (CMSSiteModel)object;
        }
        AbstractUINavigationArea abstractUINavigationArea = getNavigationAreaModel().getNavigationArea();
        UIBrowserArea browserArea = abstractUINavigationArea.getPerspective().getBrowserArea();
        BrowserModel browserModel = browserArea.getFocusedBrowser();
        if(browserModel instanceof LiveEditBrowserModel)
        {
            LiveEditBrowserModel liveBrowserModel = (LiveEditBrowserModel)browserModel;
            liveBrowserModel.setActiveSite(siteModel);
            liveBrowserModel.setActiaveCatalogVersion(null);
        }
        getCmsAdminSiteService().setActiveCatalogVersion(null);
        getCmsAdminSiteService().setActiveSite(siteModel);
        if(browserArea instanceof LiveEditBrowserArea)
        {
            ((LiveEditBrowserArea)browserArea).refreshContent();
        }
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
