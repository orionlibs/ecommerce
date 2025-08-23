package de.hybris.platform.configurablebundlebackoffice.widgets.populator;

import com.google.common.collect.Lists;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.tree.model.CatalogTreeModelPopulator;
import de.hybris.platform.catalog.model.CatalogModel;
import java.util.List;

public class BundleTemplatesModelPopulator extends CatalogTreeModelPopulator
{
    protected List<NavigationNode> findChildrenNavigationNodes(NavigationNode node)
    {
        Object nodeData = node.getData();
        if(isCatalog(nodeData))
        {
            return prepareCatalogVersionNodes(node, (CatalogModel)nodeData);
        }
        if(isCatalogVersion(nodeData))
        {
            return Lists.newArrayList();
        }
        return prepareCatalogNodes(node);
    }


    protected boolean isCatalogVersion(Object nodeData)
    {
        return nodeData instanceof de.hybris.platform.catalog.model.CatalogVersionModel;
    }


    protected boolean isCatalog(Object nodeData)
    {
        return nodeData instanceof CatalogModel;
    }
}
