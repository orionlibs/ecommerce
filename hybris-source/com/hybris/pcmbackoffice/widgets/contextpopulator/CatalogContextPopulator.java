package com.hybris.pcmbackoffice.widgets.contextpopulator;

import com.hybris.backoffice.tree.model.UncategorizedNode;
import com.hybris.backoffice.widgets.contextpopulator.ContextPopulator;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CatalogContextPopulator implements ContextPopulator
{
    private static final Logger LOG = LoggerFactory.getLogger(CatalogContextPopulator.class);
    public static final String CHILD_TYPE_CODE = "childTypeCode";
    public static final String SELECTED_TYPE_CODE = "selectedTypeCode";
    private TypeFacade typeFacade;


    public Map<String, Object> populate(Object data)
    {
        Map<String, Object> context = new HashMap<>();
        context.put("selectedObject", data);
        if(data instanceof com.hybris.cockpitng.widgets.common.explorertree.data.PartitionNodeData)
        {
            return context;
        }
        try
        {
            context.put("selectedTypeCode", this.typeFacade.getType(data));
        }
        catch(RuntimeException e)
        {
            String errorMessage = String.format("Could not resolve type for : [%s]", new Object[] {data});
            if(LOG.isDebugEnabled())
            {
                LOG.warn(errorMessage, e);
            }
            else
            {
                LOG.warn(errorMessage);
            }
        }
        if(data instanceof de.hybris.platform.catalog.model.CatalogModel)
        {
            context.put("childTypeCode", "CatalogVersion");
            context.put("catalog", data);
        }
        else if(data instanceof CatalogVersionModel)
        {
            context.put("childTypeCode", "Category");
            context.put("catalog", ((CatalogVersionModel)data).getCatalog());
            context.put("catalogVersion", data);
        }
        else if(data instanceof CategoryModel)
        {
            CategoryModel category = (CategoryModel)data;
            context.put("childTypeCode", "Category");
            if(category.getCatalogVersion() != null)
            {
                context.put("catalog", category.getCatalogVersion().getCatalog());
            }
            context.put("catalogVersion", category.getCatalogVersion());
            context.put("supercategories", Collections.singletonList(category));
        }
        else if(data instanceof UncategorizedNode)
        {
            Object parentItem = ((UncategorizedNode)data).getParentItem();
            if(parentItem instanceof CatalogVersionModel)
            {
                context.put("childTypeCode", "Category");
                context.put("catalog", ((CatalogVersionModel)parentItem).getCatalog());
                context.put("catalogVersion", parentItem);
            }
        }
        else
        {
            context.put("childTypeCode", "Catalog");
        }
        return context;
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
