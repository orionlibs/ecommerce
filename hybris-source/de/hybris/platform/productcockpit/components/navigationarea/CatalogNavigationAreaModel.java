package de.hybris.platform.productcockpit.components.navigationarea;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel;
import de.hybris.platform.cockpit.components.navigationarea.NavigationPanelSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.productcockpit.model.favorites.impl.FavoriteCategory;
import de.hybris.platform.productcockpit.session.impl.CatalogNavigationArea;
import de.hybris.platform.productcockpit.session.impl.DefaultProductSearchBrowserModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CatalogNavigationAreaModel extends AbstractNavigationAreaModel
{
    private final List<ItemModel> tmpOpenedCatalogItems = new ArrayList<>();
    private NavigationPanelSection favoritesSection;
    private NavigationPanelSection openBrowsersSection;
    private boolean initialized = false;


    public CatalogNavigationAreaModel(CatalogNavigationArea area)
    {
        super((AbstractUINavigationArea)area);
    }


    public void initialize()
    {
        if(!this.initialized)
        {
            UISessionUtils.getCurrentSession().addSessionListener((UISessionListener)new MyUISessionListener(this));
            this.initialized = true;
        }
    }


    public void update()
    {
        Collection<ItemModel> openedItems = new HashSet<>();
        BrowserModel b = getNavigationArea().getPerspective().getBrowserArea().getFocusedBrowser();
        if(b instanceof DefaultProductSearchBrowserModel)
        {
            DefaultProductSearchBrowserModel searchBrowser = (DefaultProductSearchBrowserModel)b;
            for(CategoryModel category : searchBrowser.getSelectedCategories())
            {
                openedItems.add(getNavigationArea().getProductCockpitCatalogService().getCatalogVersion(category));
                openedItems.addAll(getNavigationArea().getProductCockpitCatalogService().getCategoryPath(category));
            }
        }
        this.tmpOpenedCatalogItems.addAll(openedItems);
        for(Section s : getSections())
        {
            sectionUpdated(s);
        }
        this.tmpOpenedCatalogItems.clear();
    }


    public NavigationPanelSection getOpenBrowsersSection()
    {
        return this.openBrowsersSection;
    }


    public void setOpenBrowsersSection(NavigationPanelSection openBrowsersSection)
    {
        this.openBrowsersSection = openBrowsersSection;
    }


    public NavigationPanelSection getFavoritesSection()
    {
        return this.favoritesSection;
    }


    public void setFavoritesSection(NavigationPanelSection favoritesSection)
    {
        this.favoritesSection = favoritesSection;
    }


    public List<ItemModel> getTmpOpenedCatalogItems()
    {
        return this.tmpOpenedCatalogItems;
    }


    public void hideSection(Section section)
    {
        if(section.isVisible())
        {
            ((NavigationPanelSection)section).setVisible(false);
            fireSectionHide(section);
        }
    }


    public void showSection(Section section)
    {
        if(!section.isVisible())
        {
            ((NavigationPanelSection)section).setVisible(true);
            fireSectionShow(section);
        }
    }


    public List<FavoriteCategory> getFavoriteCategoriesFromJalo()
    {
        return getNavigationArea().getFavoriteCategoryService().getFavoriteCategories(UISessionUtils.getCurrentSession().getUser());
    }


    public CatalogNavigationArea getNavigationArea()
    {
        return (CatalogNavigationArea)super.getNavigationArea();
    }


    public CatalogNavigationAreaModel()
    {
    }
}
