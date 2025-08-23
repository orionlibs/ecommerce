package de.hybris.platform.catalog.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.ProductManager;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

public class CatalogVersionTools
{
    static final Logger LOG = Logger.getLogger(CatalogVersionTools.class.getName());


    public static Collection getSameProducts(Product product)
    {
        return ProductManager.getInstance().getProductsByCode(product.getCode());
    }


    public static Collection getSameCategories(Category category)
    {
        return CategoryManager.getInstance().getCategoriesByCode(category.getCode());
    }


    public static Keyword getSameKeyword(Keyword keyword)
    {
        return null;
    }


    public static Item.ItemAttributeMap getCopyableAttributes(ComposedType itemType, Item item)
    {
        Item.ItemAttributeMap copyableAttributes = new Item.ItemAttributeMap();
        try
        {
            Set attrdesc = itemType.getAttributeDescriptors();
            AttributeDescriptor attributeDescriptor = null;
            for(Iterator<AttributeDescriptor> it = attrdesc.iterator(); it.hasNext(); )
            {
                attributeDescriptor = it.next();
                if((!attributeDescriptor.isInitial() || attributeDescriptor.isWritable()) && !attributeDescriptor.isPartOf() &&
                                !attributeDescriptor.getQualifier().equals(Item.CREATION_TIME) &&
                                !attributeDescriptor.getQualifier().equals(Item.MODIFIED_TIME) &&
                                !attributeDescriptor.getQualifier().equals(Item.PK))
                {
                    LOG.debug("copyable attribute: " + attributeDescriptor.getQualifier() + " // " + item
                                    .getAttribute(attributeDescriptor.getQualifier()));
                    copyableAttributes.put(attributeDescriptor.getQualifier(), item
                                    .getAttribute(attributeDescriptor.getQualifier()));
                }
            }
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1716);
        }
        return copyableAttributes;
    }
}
