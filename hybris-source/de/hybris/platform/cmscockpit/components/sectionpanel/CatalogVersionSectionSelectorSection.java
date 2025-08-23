package de.hybris.platform.cmscockpit.components.sectionpanel;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cmscockpit.session.CmsCatalogBrowserModelFactory;
import de.hybris.platform.cmscockpit.session.impl.CatalogBrowserArea;
import de.hybris.platform.cmscockpit.session.impl.CmsCatalogBrowserModel;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.AbstractSectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.AdvancedBrowserModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;
import de.hybris.platform.cockpit.session.impl.AbstractUIPerspective;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class CatalogVersionSectionSelectorSection extends DefaultSectionSelectorSection
{
    private static final Logger LOG = Logger.getLogger(CatalogVersionSectionSelectorSection.class);
    private CMSAdminSiteService cmsAdminSiteService = null;
    private CmsCatalogBrowserModelFactory cmsCatalogBrowserModelFactory;


    public void selectionChanged()
    {
        closeEditorArea();
        CatalogVersionModel activeCatalogVersion = informCmsAboutCurrentCatalogVersion();
        AbstractUINavigationArea abstractUINavigationArea = getNavigationAreaModel().getNavigationArea();
        UIBrowserArea browserArea = abstractUINavigationArea.getPerspective().getBrowserArea();
        SectionPanelModel sectionPanelModel = abstractUINavigationArea.getSectionModel();
        if(sectionPanelModel instanceof AbstractSectionPanelModel)
        {
            ((AbstractSectionPanelModel)sectionPanelModel).sectionUpdated((Section)getRootSection());
        }
        if(browserArea instanceof CatalogBrowserArea)
        {
            CatalogBrowserArea catalogBrowserArea = (CatalogBrowserArea)browserArea;
            catalogBrowserArea.setActiveSite((CMSSiteModel)getParentSection().getRelatedObject().getObject());
            catalogBrowserArea.setActiveCatalogVersion(activeCatalogVersion);
            AdvancedBrowserModel welcomeBrowserModel = catalogBrowserArea.getWelcomeBrowserModel();
            if(welcomeBrowserModel == null)
            {
                LOG.error("Current browser area needs to provide a welcome browser model.");
            }
            else
            {
                catalogBrowserArea.closeOthers((BrowserModel)welcomeBrowserModel);
                if(browserArea instanceof AbstractBrowserArea && ((AbstractBrowserArea)browserArea).providesDefaultBrowser() &&
                                getCmsAdminSiteService().getActiveCatalogVersion() != null)
                {
                    BrowserModel currentBrowserModel = createRequiredBrowserModel();
                    setAsVisible(browserArea, currentBrowserModel);
                }
            }
        }
    }


    public List<TypedObject> getItems()
    {
        TypedObject selectedSiteObject = getParentSection().getRelatedObject();
        CMSSiteModel selectedSite = (CMSSiteModel)selectedSiteObject.getObject();
        List<TypedObject> ret = new ArrayList<>();
        if(selectedSite == null)
        {
            LOG.warn("It is not possible to retrieve content catalogs for empty stotre!");
        }
        else
        {
            for(CatalogModel contentCatalog : selectedSite.getContentCatalogs())
            {
                ret.addAll(getTypeService().wrapItems(contentCatalog.getCatalogVersions()));
            }
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
                TypedObject catalog = getTypeService().wrapItem(cmsNavigationEvent.getCatalog());
                this.selectedItems.clear();
                setRelatedObject(catalog);
                setSelectedItem(catalog);
            }
        }
    }


    protected void closeEditorArea()
    {
        TypedObject currentEditorObject = UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea().getCurrentObject();
        if(currentEditorObject != null)
        {
            AbstractUIPerspective currentPerspective = (AbstractUIPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective();
            currentPerspective.setActiveItem(null);
            currentPerspective.getEditorArea().setCurrentObject(null);
            ((BaseUICockpitPerspective)currentPerspective).collapseEditorArea();
        }
    }


    protected void setAsVisible(UIBrowserArea browserArea, BrowserModel browserModel)
    {
        if(browserArea.addVisibleBrowser(0, browserModel))
        {
            browserArea.update();
        }
        browserModel.focus();
    }


    protected CatalogVersionModel informCmsAboutCurrentCatalogVersion()
    {
        CatalogVersionModel catalogVersionModel = null;
        Object object = (getRelatedObject() == null) ? null : getRelatedObject().getObject();
        if(object instanceof CatalogVersionModel)
        {
            catalogVersionModel = (CatalogVersionModel)object;
        }
        getCmsAdminSiteService().setActiveSite((CMSSiteModel)getParentSection().getRelatedObject().getObject());
        getCmsAdminSiteService().setActiveCatalogVersion(catalogVersionModel);
        return catalogVersionModel;
    }


    protected BrowserModel createRequiredBrowserModel()
    {
        ObjectTemplate rootType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("AbstractPage");
        CmsCatalogBrowserModel cmsCatalogBrowserModel = this.cmsCatalogBrowserModelFactory.getInstance(rootType);
        cmsCatalogBrowserModel.setSimpleQuery("");
        cmsCatalogBrowserModel.updateItems();
        return (BrowserModel)cmsCatalogBrowserModel;
    }


    public void setSelectedItem(TypedObject selectedItem)
    {
        List<TypedObject> items = new ArrayList<>(getItems());
        if(items.contains(selectedItem) && ((
                        getSelectedItem() == null && selectedItem != null) || (getSelectedItem() != null &&
                        !getSelectedItem().equals(selectedItem))))
        {
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


    @Required
    public void setCmsCatalogBrowserModelFactory(CmsCatalogBrowserModelFactory cmsCatalogBrowserModelFactory)
    {
        this.cmsCatalogBrowserModelFactory = cmsCatalogBrowserModelFactory;
    }
}
