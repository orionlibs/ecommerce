package de.hybris.platform.cmscockpit.components.navigationarea;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.common.annotations.HybrisDeprecation;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.services.ContentCatalogService;
import de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel;
import de.hybris.platform.cockpit.components.navigationarea.AbstractSelectorSection;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "4.3", forRemoval = true)
@HybrisDeprecation(sinceVersion = "4.3")
public class CmsCatalogSelectorSection extends AbstractSelectorSection
{
    private static final Logger LOG = Logger.getLogger(CmsCatalogSelectorSection.class);
    private boolean initialized = false;
    private ContentCatalogService contentCatalogService = null;
    private CMSAdminSiteService siteService = null;
    private ModelService modelService = null;


    public void selectionChanged()
    {
        TypedObject selectedItem = getSelectedItem();
        if(selectedItem != null)
        {
            Object object = selectedItem.getObject();
            CatalogVersionModel catalogVersion = null;
            if(object instanceof CatalogVersionModel)
            {
                catalogVersion = (CatalogVersionModel)object;
            }
            if(catalogVersion == null)
            {
                LOG.error("Catalog version could not be activated. Reason: Selected item not valid");
            }
            else
            {
                CatalogModel catalog = catalogVersion.getCatalog();
                if(catalog == null || !(catalog instanceof ContentCatalogModel))
                {
                    LOG.error("Could not activate the corresponding site. Reason: Content catalog not available.");
                }
                else
                {
                    Collection<CMSSiteModel> sites = ((ContentCatalogModel)catalog).getCmsSites();
                    if(sites == null || sites.isEmpty())
                    {
                        LOG.error("Could not activate the corresponding site. Reason: Site not available.");
                    }
                    else
                    {
                        CMSSiteModel site = sites.iterator().next();
                        getCmsAdminSiteService().setActiveSite(site);
                    }
                }
                getCmsAdminSiteService().setActiveCatalogVersion(catalogVersion);
            }
            BrowserModel browser = getNavigationAreaModel().getNavigationArea().getPerspective().getBrowserArea().getFocusedBrowser();
            if(browser instanceof SearchBrowserModel)
            {
                SearchBrowserModel searchBrowser = (SearchBrowserModel)browser;
                searchBrowser.setSimpleQuery("");
                searchBrowser.updateItems(0);
            }
            else
            {
                LOG.warn("Handling of other browser types than Search browsers not implemented yet.");
            }
        }
    }


    @Required
    public void setNavigationAreaModel(AbstractNavigationAreaModel navAreaModel)
    {
        super.setNavigationAreaModel(navAreaModel);
    }


    public List<TypedObject> getItems()
    {
        if(!this.initialized)
        {
            List<CatalogVersionModel> catalogVersionModels = new ArrayList<>();
            Collection<ContentCatalogModel> contentCatalogs = getContentCatalogService().getContentCatalogs("name");
            if(contentCatalogs != null)
            {
                for(CatalogModel catalogModel : contentCatalogs)
                {
                    catalogVersionModels.addAll(catalogModel.getCatalogVersions());
                }
            }
            setItems(UISessionUtils.getCurrentSession().getTypeService().wrapItems(catalogVersionModels));
            this.initialized = true;
        }
        return super.getItems();
    }


    @Required
    public void setContentCatalogService(ContentCatalogService contentCatalogService)
    {
        this.contentCatalogService = contentCatalogService;
    }


    protected ContentCatalogService getContentCatalogService()
    {
        return this.contentCatalogService;
    }


    @Required
    public void setCmsAdminSiteService(CMSAdminSiteService siteService)
    {
        this.siteService = siteService;
    }


    protected CMSAdminSiteService getCmsAdminSiteService()
    {
        return this.siteService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public boolean isItemActive(TypedObject item)
    {
        Boolean active = Boolean.FALSE;
        if(item != null)
        {
            Object object = item.getObject();
            if(object instanceof CatalogVersionModel)
            {
                active = ((CatalogVersionModel)object).getActive();
            }
        }
        return (active == null) ? false : active.booleanValue();
    }
}
