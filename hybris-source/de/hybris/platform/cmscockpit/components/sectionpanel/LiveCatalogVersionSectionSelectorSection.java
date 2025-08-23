package de.hybris.platform.cmscockpit.components.sectionpanel;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserArea;
import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserModel;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class LiveCatalogVersionSectionSelectorSection extends DefaultSectionSelectorSection
{
    private static final Logger LOG = Logger.getLogger(LiveCatalogVersionSectionSelectorSection.class);
    private CMSAdminSiteService cmsAdminSiteService = null;


    public void selectionChanged()
    {
        closeEditorArea();
        informCmsAboutCurrentCatalogVersion();
        AbstractUINavigationArea abstractUINavigationArea = getNavigationAreaModel().getNavigationArea();
        UIBrowserArea browserArea = abstractUINavigationArea.getPerspective().getBrowserArea();
        SectionPanelModel sectionPanelModel = abstractUINavigationArea.getSectionModel();
        if(sectionPanelModel instanceof AbstractSectionPanelModel)
        {
            ((AbstractSectionPanelModel)sectionPanelModel).sectionUpdated((Section)getRootSection());
        }
        if(browserArea instanceof LiveEditBrowserArea)
        {
            ((LiveEditBrowserArea)browserArea).refreshContent();
        }
    }


    public List<TypedObject> getItems()
    {
        TypedObject selectedSiteObject = getParentSection().getRelatedObject();
        CMSSiteModel selectedSite = (CMSSiteModel)selectedSiteObject.getObject();
        List<TypedObject> ret = new ArrayList<>();
        if(selectedSite != null)
        {
            for(CatalogModel contentCatalog : selectedSite.getContentCatalogs())
            {
                ret.addAll(getTypeService().wrapItems(contentCatalog.getCatalogVersions()));
            }
        }
        else
        {
            LOG.warn("It is not possible to retrieve content catalogs for empty stotre!");
        }
        if(ret.isEmpty())
        {
            setItems(Collections.EMPTY_LIST);
        }
        else
        {
            setItems(getTypeService().wrapItems(ret));
        }
        return super.getItems();
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof CmsNavigationEvent)
        {
            CmsNavigationEvent cmsNavigationEvent = (CmsNavigationEvent)event;
            if(cmsNavigationEvent.getCatalog() != null)
            {
                setOpen(true);
                setRelatedObject(getTypeService().wrapItem(cmsNavigationEvent.getCatalog()));
                setSelectedItem(getTypeService().wrapItem(cmsNavigationEvent.getCatalog()));
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


    protected void informCmsAboutCurrentCatalogVersion()
    {
        CatalogVersionModel catalogVersionModel = null;
        Object object = (getRelatedObject() == null) ? null : getRelatedObject().getObject();
        if(object instanceof CatalogVersionModel)
        {
            catalogVersionModel = (CatalogVersionModel)object;
        }
        AbstractUINavigationArea abstractUINavigationArea = getNavigationAreaModel().getNavigationArea();
        UIBrowserArea browserArea = abstractUINavigationArea.getPerspective().getBrowserArea();
        BrowserModel browserModel = browserArea.getFocusedBrowser();
        if(browserModel instanceof LiveEditBrowserModel)
        {
            LiveEditBrowserModel liveBrowserModel = (LiveEditBrowserModel)browserModel;
            liveBrowserModel.setActiveSite((CMSSiteModel)getParentSection().getRelatedObject().getObject());
            liveBrowserModel.setActiaveCatalogVersion(catalogVersionModel);
        }
        getCmsAdminSiteService().setActiveCatalogVersion(catalogVersionModel);
        getCmsAdminSiteService().setActiveSite((CMSSiteModel)getParentSection().getRelatedObject().getObject());
    }


    public void setSelectedItem(TypedObject selectedItem)
    {
        List<TypedObject> items = new ArrayList<>(getItems());
        if(items.contains(selectedItem) && ((
                        getSelectedItem() == null && selectedItem != null) || (getSelectedItem() != null &&
                        !getSelectedItem().equals(selectedItem))))
        {
            this.selectedItems.clear();
            this.selectedItems.add(selectedItem);
            selectionChanged();
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
}
