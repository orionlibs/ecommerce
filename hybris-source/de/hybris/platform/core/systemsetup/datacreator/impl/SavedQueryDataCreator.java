package de.hybris.platform.core.systemsetup.datacreator.impl;

import de.hybris.platform.core.systemsetup.datacreator.internal.CoreDataCreator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.SavedQuery;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class SavedQueryDataCreator implements CoreDataCreator
{
    private static final Logger LOG = Logger.getLogger(SavedQueryDataCreator.class);


    public void populateDatabase()
    {
        try
        {
            ComposedType savedQueryType = getTypeManager().getComposedType(SavedQuery.class);
            createCodeOrNameSavedQuery(savedQueryType);
            createModifiedItemSavedQuery(savedQueryType);
        }
        catch(Exception e)
        {
            throw new SystemException(e.getMessage(), e);
        }
    }


    private void createCodeOrNameSavedQuery(ComposedType sqt)
    {
        SavedQuery savedQuery = getFlexibleSearch().getSavedQuery("CodeOrNameQuery");
        if(savedQuery == null)
        {
            try
            {
                Map<Object, Object> attributes = new HashMap<>();
                attributes.put("code", "CodeOrNameQuery");
                attributes.put("resultType", getTypeManager().getComposedType(Product.class));
                attributes.put("query", "SELECT {p:" + Item.PK + "} FROM {$$$ AS p} WHERE {p:code} LIKE ?txt OR {p:name:o} LIKE ?txt ORDER BY {p:code} ASC");
                attributes.put("params",
                                Collections.singletonMap("txt", getTypeManager().getRootAtomicType(String.class)));
                savedQuery = (SavedQuery)sqt.newInstance(attributes);
            }
            catch(JaloGenericCreationException e)
            {
                LOG.error("Error creating SavedQuery 'Product: Name or code'", (Throwable)e);
            }
            catch(JaloAbstractTypeException e)
            {
                LOG.error("Error creating SavedQuery 'Product: Name or code'", (Throwable)e);
            }
        }
    }


    private void createModifiedItemSavedQuery(ComposedType sqt) throws Exception
    {
        SavedQuery savedQuery = getFlexibleSearch().getSavedQuery("ModifiedItemQuery");
        if(savedQuery == null)
        {
            try
            {
                AtomicType dateType = getTypeManager().getRootAtomicType(Date.class);
                Map<Object, Object> attributes = new HashMap<>();
                Map<Object, Object> params = new HashMap<>();
                params.put("startDate", dateType);
                params.put("endDate", dateType);
                attributes.put("code", "ModifiedItemQuery");
                attributes.put("resultType", TypeManager.getInstance().getComposedType(Item.class));
                attributes
                                .put("query", "SELECT {" + Item.PK + "} FROM {$$$} WHERE {" + Item.MODIFIED_TIME + "} >= ?startDate AND {" + Item.MODIFIED_TIME + "} <=?endDate ORDER BY {" + Item.MODIFIED_TIME + "} DESC");
                attributes.put("params", params);
                savedQuery = (SavedQuery)sqt.newInstance(attributes);
            }
            catch(JaloGenericCreationException e)
            {
                LOG.error("Error creating SavedQuery 'Product: Modified between'", (Throwable)e);
            }
            catch(JaloAbstractTypeException e)
            {
                LOG.error("Error creating SavedQuery 'Product: Modified between'", (Throwable)e);
            }
        }
    }


    protected FlexibleSearch getFlexibleSearch()
    {
        return FlexibleSearch.getInstance();
    }


    protected TypeManager getTypeManager()
    {
        return TypeManager.getInstance();
    }
}
