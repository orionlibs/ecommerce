package de.hybris.platform.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.NavigationAreaListener;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.productcockpit.components.navigationarea.CatalogNavigationAreaModel;
import de.hybris.platform.productcockpit.model.favorites.impl.FavoriteCategory;
import de.hybris.platform.productcockpit.services.catalog.CatalogService;
import de.hybris.platform.productcockpit.services.catalog.FavoriteCategoryService;
import de.hybris.platform.productcockpit.session.CatalogNavigationAreaListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CatalogNavigationArea extends BaseUICockpitNavigationArea
{
    private static final Logger log = LoggerFactory.getLogger(CatalogNavigationArea.class);
    private CatalogService productCockpitCatalogService;
    private FavoriteCategoryService favoriteCategoryService;


    public void initialize(Map<String, Object> params)
    {
        super.initialize(params);
    }


    public void update()
    {
        super.update();
    }


    public CatalogService getProductCockpitCatalogService()
    {
        return this.productCockpitCatalogService;
    }


    @Required
    public void setProductCockpitCatalogService(CatalogService productCockpitCatalogService)
    {
        this.productCockpitCatalogService = productCockpitCatalogService;
    }


    public FavoriteCategoryService getFavoriteCategoryService()
    {
        return this.favoriteCategoryService;
    }


    @Required
    public void setFavoriteCategoryService(FavoriteCategoryService favoriteCategoryService)
    {
        this.favoriteCategoryService = favoriteCategoryService;
    }


    public SectionPanelModel getSectionModel()
    {
        if(super.getSectionModel() == null)
        {
            CatalogNavigationAreaModel m = new CatalogNavigationAreaModel(this);
            m.initialize();
            setSectionModel((SectionPanelModel)m);
        }
        return super.getSectionModel();
    }


    public UISession getSession()
    {
        return UISessionUtils.getCurrentSession();
    }


    public void deleteFavoriteCategory(FavoriteCategory favorite)
    {
        getFavoriteCategoryService().deleteFavoriteCategory(favorite);
        update();
    }


    public void createFavoriteCategory(String label, UserModel user)
    {
        getFavoriteCategoryService().createFavoriteCategory(label, user);
        update();
    }


    public CatalogVersionModel getSelectedCatalogVersion()
    {
        CatalogVersionModel ret;
        BrowserModel b = getPerspective().getBrowserArea().getFocusedBrowser();
        if(b instanceof CategoryTreeBrowserModel)
        {
            CategoryTreeBrowserModel qb = (CategoryTreeBrowserModel)b;
            ret = qb.getCatalogVersion();
        }
        else
        {
            ret = null;
            if(log.isDebugEnabled())
            {
                log.debug("No focused query browser available.");
            }
        }
        return ret;
    }


    public void setSelectedCatalogItems(CatalogVersionModel catalogVersion)
    {
        List<CatalogVersionModel> versions = new ArrayList<>();
        versions.add(catalogVersion);
        UISessionUtils.getCurrentSession().setSelectedCatalogVersions(new ArrayList<>(versions));
        fireCatalogItemSelectionChanged(catalogVersion);
    }


    protected void fireCatalogItemSelectionChanged(CatalogVersionModel catalogVersion)
    {
        for(NavigationAreaListener listener : getListeners())
        {
            ((CatalogNavigationAreaListener)listener).catalogItemSelectionChanged(catalogVersion);
        }
    }


    public void setSelectedFavoriteCategory()
    {
    }


    public List<CatalogVersionModel> getCatalogVersions()
    {
        UserModel currentUser = getSession().getUser();
        return getProductCockpitCatalogService().getSortedCatalogVersions(currentUser);
    }
}
