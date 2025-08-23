package de.hybris.platform.productcockpit.components.navigationarea;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cockpit.components.navigationarea.DefaultNavigationAreaModel;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.productcockpit.session.impl.DefaultProductSearchBrowserModel;
import de.hybris.platform.productcockpit.session.impl.NavigationArea;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class NavigationAreaModel extends DefaultNavigationAreaModel
{
    private final List<ItemModel> tmpOpenedCatalogItems = new ArrayList<>();


    public NavigationAreaModel(NavigationArea area)
    {
        super((AbstractUINavigationArea)area);
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


    public NavigationArea getNavigationArea()
    {
        return (NavigationArea)super.getNavigationArea();
    }


    public List<ItemModel> getTmpOpenedCatalogItems()
    {
        return this.tmpOpenedCatalogItems;
    }


    public NavigationAreaModel()
    {
    }
}
