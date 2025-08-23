package de.hybris.platform.productcockpit.dao;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zkplus.spring.SpringUtil;

public abstract class AbstractDao
{
    private TypeService typeService;


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }


    public abstract List<MacFinderTreeNode> getItems(Item paramItem, CatalogVersion paramCatalogVersion, boolean paramBoolean);


    public abstract int getItemCount(Item paramItem, CatalogVersion paramCatalogVersion, boolean paramBoolean);


    public abstract List<Item> getConnectedItems(Item paramItem, CatalogVersion paramCatalogVersion, boolean paramBoolean);


    protected abstract List<MacFinderTreeNode> wrapIntoConnectedItem(List<Item> paramList);


    protected List<Item> unwrapIntoConnectedItem(List<MacFinderTreeNode> sourceList)
    {
        List<Item> results = new ArrayList<>();
        for(MacFinderTreeNode leafProduct : sourceList)
        {
            results.add((Item)TypeTools.getModelService().getSource(leafProduct.getOriginalItem().getObject()));
        }
        return results;
    }
}
