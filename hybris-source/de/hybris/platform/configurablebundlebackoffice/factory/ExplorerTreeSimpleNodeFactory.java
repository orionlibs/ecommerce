package de.hybris.platform.configurablebundlebackoffice.factory;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;

public class ExplorerTreeSimpleNodeFactory
{
    public NavigationNode createAllCatalogsNode()
    {
        return (NavigationNode)new SimpleNode("allCatalogs");
    }


    public NavigationNode createCatalogNode(CatalogModel catalog)
    {
        SimpleNode catalogNode = new SimpleNode(catalog.getId());
        catalogNode.setData(catalog);
        return (NavigationNode)catalogNode;
    }


    public NavigationNode createCatalogVersionNode(CatalogVersionModel catalogVersionModel)
    {
        SimpleNode catalogNode = new SimpleNode(catalogVersionModel.getVersion());
        catalogNode.setData(catalogVersionModel);
        return (NavigationNode)catalogNode;
    }
}
