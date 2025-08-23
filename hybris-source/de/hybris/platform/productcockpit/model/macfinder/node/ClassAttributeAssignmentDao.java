package de.hybris.platform.productcockpit.model.macfinder.node;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.productcockpit.dao.AbstractDao;
import java.util.ArrayList;
import java.util.List;

public class ClassAttributeAssignmentDao extends AbstractDao
{
    public List<Item> getConnectedItems(Item category, CatalogVersion version, boolean withSubcategories)
    {
        List<Item> ret = new ArrayList<>();
        if(category instanceof ClassificationClass)
        {
            List<ClassAttributeAssignment> attrList = ((ClassificationClass)category).getDeclaredClassificationAttributeAssignments();
            for(ClassAttributeAssignment attr : attrList)
            {
                ret.add(attr);
            }
        }
        return ret;
    }


    public List<MacFinderTreeNode> getItems(Item category, CatalogVersion version, boolean withSubcategories)
    {
        return wrapIntoConnectedItem(getConnectedItems(category, version, withSubcategories));
    }


    protected List<MacFinderTreeNode> wrapIntoConnectedItem(List<Item> sourceList)
    {
        List<MacFinderTreeNode> results = new ArrayList<>();
        for(Item item : sourceList)
        {
            ClassAttributeAssignmentLeafNode classAttributeAssignmentLeafNode = new ClassAttributeAssignmentLeafNode(this);
            classAttributeAssignmentLeafNode.setOriginalItem(getTypeService().wrapItem(item.getPK()));
            results.add(classAttributeAssignmentLeafNode);
        }
        return results;
    }


    public int getItemCount(Item category, CatalogVersion version, boolean withSubcategories)
    {
        return getConnectedItems(category, version, withSubcategories).size();
    }
}
