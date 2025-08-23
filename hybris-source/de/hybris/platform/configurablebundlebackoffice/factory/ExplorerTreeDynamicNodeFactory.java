package de.hybris.platform.configurablebundlebackoffice.factory;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.cockpitng.tree.node.DynamicNode;
import com.hybris.cockpitng.tree.node.DynamicNodePopulator;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;

public class ExplorerTreeDynamicNodeFactory
{
    private DynamicNodePopulator populator;


    public ExplorerTreeDynamicNodeFactory(DynamicNodePopulator populator)
    {
        this.populator = populator;
    }


    public NavigationNode createAllCatalogsNode()
    {
        return (NavigationNode)new DynamicNode("allCatalogs", this.populator);
    }


    public NavigationNode createCatalogNode(CatalogModel catalog)
    {
        DynamicNode dynamicNode = new DynamicNode(catalog.getId(), this.populator);
        dynamicNode.setData(catalog);
        return (NavigationNode)dynamicNode;
    }


    public NavigationNode createCatalogVersionNode(CatalogVersionModel catalogVersionModel)
    {
        DynamicNode dynamicNode = new DynamicNode(catalogVersionModel.getVersion(), this.populator);
        dynamicNode.setData(catalogVersionModel);
        return (NavigationNode)dynamicNode;
    }
}
