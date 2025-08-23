package de.hybris.platform.productcockpit.util.testing.impl;

import de.hybris.platform.cockpit.util.testing.TestIdContext;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import de.hybris.platform.productcockpit.session.impl.CategoryTreeBrowserModel;

public class CategoryTreeTestIdContext implements TestIdContext
{
    private final CategoryTreeBrowserModel browserModel;
    private final MacFinderTreeNode treeNode;


    public CategoryTreeTestIdContext(CategoryTreeBrowserModel browserModel, MacFinderTreeNode treeNode)
    {
        this.browserModel = browserModel;
        this.treeNode = treeNode;
    }


    public CategoryTreeBrowserModel getBrowserModel()
    {
        return this.browserModel;
    }


    public MacFinderTreeNode getTreeNode()
    {
        return this.treeNode;
    }
}
