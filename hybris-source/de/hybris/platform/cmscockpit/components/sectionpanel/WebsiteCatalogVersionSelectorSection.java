package de.hybris.platform.cmscockpit.components.sectionpanel;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cmscockpit.session.impl.SiteVersionAware;
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
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class WebsiteCatalogVersionSelectorSection extends DefaultSectionSelectorSection
{
    private static final Logger LOG = Logger.getLogger(WebsiteCatalogVersionSelectorSection.class);


    public void selectionChanged()
    {
        closeEditorArea();
        informBrowserAreaAboutCurrentCatalogVersion();
        AbstractUINavigationArea abstractUINavigationArea = getNavigationAreaModel().getNavigationArea();
        SectionPanelModel sectionPanelModel = abstractUINavigationArea.getSectionModel();
        if(sectionPanelModel instanceof AbstractSectionPanelModel)
        {
            ((AbstractSectionPanelModel)sectionPanelModel).sectionUpdated((Section)getRootSection());
        }
        refreshView();
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
        if(!ret.isEmpty())
        {
            setItems(getTypeService().wrapItems(ret));
        }
        else
        {
            setItems(Collections.EMPTY_LIST);
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


    protected void setAsVisible(UIBrowserArea browserArea, BrowserModel browserModel)
    {
        if(browserArea.addVisibleBrowser(0, browserModel))
        {
            browserArea.update();
        }
        browserModel.focus();
    }


    protected CatalogVersionModel informBrowserAreaAboutCurrentCatalogVersion()
    {
        CatalogVersionModel catalogVersionModel = null;
        Object object = (getRelatedObject() == null) ? null : getRelatedObject().getObject();
        if(object instanceof CatalogVersionModel)
        {
            catalogVersionModel = (CatalogVersionModel)object;
        }
        UIBrowserArea browserArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea();
        if(browserArea instanceof SiteVersionAware)
        {
            SiteVersionAware naviNodeBrowserArea = (SiteVersionAware)browserArea;
            naviNodeBrowserArea.setActiveSite((CMSSiteModel)getParentSection().getRelatedObject().getObject());
            naviNodeBrowserArea.setActiveCatalogVersion(catalogVersionModel);
            BrowserModel defaultBrowserModel = ((AbstractBrowserArea)browserArea).createNewDefaultBrowser();
            browserArea.addVisibleBrowser(0, defaultBrowserModel);
            browserArea.closeOthers(defaultBrowserModel);
        }
        return catalogVersionModel;
    }


    public void refreshView()
    {
        BrowserModel browser = getNavigationAreaModel().getNavigationArea().getPerspective().getBrowserArea().getFocusedBrowser();
        browser.updateItems();
        super.refreshView();
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


    protected TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }
}
