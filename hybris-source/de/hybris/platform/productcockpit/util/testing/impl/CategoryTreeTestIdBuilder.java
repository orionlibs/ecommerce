package de.hybris.platform.productcockpit.util.testing.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.util.testing.TestIdBuilder;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryTreeTestIdBuilder implements TestIdBuilder<CategoryTreeTestIdContext>
{
    private static final Logger LOG = LoggerFactory.getLogger(CategoryTreeTestIdBuilder.class);


    public String buildTestId(CategoryTreeTestIdContext context)
    {
        MacFinderTreeNode treeNode = context.getTreeNode();
        int column = treeNode.getContainingColumn().getIndex();
        int row = treeNode.getContainingColumn().getChildren().indexOf(treeNode);
        StringBuilder labelBuilder = new StringBuilder("CategoryBrowser_Column" + column + "_Row" + row + "_");
        if(treeNode instanceof de.hybris.platform.productcockpit.model.macfinder.node.MacFinderTreeNodeChildable)
        {
            CatalogVersionModel catVer = context.getBrowserModel().getCatalogVersion();
            if(catVer instanceof de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel)
            {
                labelBuilder.append("ClassificationElement");
            }
            else
            {
                labelBuilder.append("CategoryElement");
            }
        }
        else if(treeNode instanceof de.hybris.platform.productcockpit.model.macfinder.node.LeafNode)
        {
            labelBuilder.append("LeafElement");
        }
        else
        {
            String displayedLabel = "";
            try
            {
                displayedLabel = treeNode.getDisplayedLabel();
            }
            catch(JaloSecurityException e)
            {
                LOG.error("Error getting displayed label, reason: ", (Throwable)e);
            }
            labelBuilder.append(StringUtils.deleteWhitespace(displayedLabel));
        }
        return labelBuilder.toString();
    }


    public Class getContextClass()
    {
        return CategoryTreeTestIdContext.class;
    }
}
