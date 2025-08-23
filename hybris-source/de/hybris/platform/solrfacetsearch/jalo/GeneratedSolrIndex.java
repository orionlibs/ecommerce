package de.hybris.platform.solrfacetsearch.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrIndex extends GenericItem
{
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";
    public static final String INDEXEDTYPE = "indexedType";
    public static final String QUALIFIER = "qualifier";
    public static final String ACTIVE = "active";
    public static final String INDEXOPERATIONS = "indexOperations";
    protected static final OneToManyHandler<SolrIndexOperation> INDEXOPERATIONSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXOPERATION, true, "index", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("facetSearchConfig", Item.AttributeMode.INITIAL);
        tmp.put("indexedType", Item.AttributeMode.INITIAL);
        tmp.put("qualifier", Item.AttributeMode.INITIAL);
        tmp.put("active", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isActive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "active");
    }


    public Boolean isActive()
    {
        return isActive(getSession().getSessionContext());
    }


    public boolean isActiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isActive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive()
    {
        return isActiveAsPrimitive(getSession().getSessionContext());
    }


    public void setActive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "active", value);
    }


    public void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    public void setActive(boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public SolrFacetSearchConfig getFacetSearchConfig(SessionContext ctx)
    {
        return (SolrFacetSearchConfig)getProperty(ctx, "facetSearchConfig");
    }


    public SolrFacetSearchConfig getFacetSearchConfig()
    {
        return getFacetSearchConfig(getSession().getSessionContext());
    }


    protected void setFacetSearchConfig(SessionContext ctx, SolrFacetSearchConfig value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'facetSearchConfig' is not changeable", 0);
        }
        setProperty(ctx, "facetSearchConfig", value);
    }


    protected void setFacetSearchConfig(SolrFacetSearchConfig value)
    {
        setFacetSearchConfig(getSession().getSessionContext(), value);
    }


    public SolrIndexedType getIndexedType(SessionContext ctx)
    {
        return (SolrIndexedType)getProperty(ctx, "indexedType");
    }


    public SolrIndexedType getIndexedType()
    {
        return getIndexedType(getSession().getSessionContext());
    }


    protected void setIndexedType(SessionContext ctx, SolrIndexedType value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'indexedType' is not changeable", 0);
        }
        setProperty(ctx, "indexedType", value);
    }


    protected void setIndexedType(SolrIndexedType value)
    {
        setIndexedType(getSession().getSessionContext(), value);
    }


    public Collection<SolrIndexOperation> getIndexOperations(SessionContext ctx)
    {
        return INDEXOPERATIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<SolrIndexOperation> getIndexOperations()
    {
        return getIndexOperations(getSession().getSessionContext());
    }


    public void setIndexOperations(SessionContext ctx, Collection<SolrIndexOperation> value)
    {
        INDEXOPERATIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setIndexOperations(Collection<SolrIndexOperation> value)
    {
        setIndexOperations(getSession().getSessionContext(), value);
    }


    public void addToIndexOperations(SessionContext ctx, SolrIndexOperation value)
    {
        INDEXOPERATIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToIndexOperations(SolrIndexOperation value)
    {
        addToIndexOperations(getSession().getSessionContext(), value);
    }


    public void removeFromIndexOperations(SessionContext ctx, SolrIndexOperation value)
    {
        INDEXOPERATIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromIndexOperations(SolrIndexOperation value)
    {
        removeFromIndexOperations(getSession().getSessionContext(), value);
    }


    public String getQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "qualifier");
    }


    public String getQualifier()
    {
        return getQualifier(getSession().getSessionContext());
    }


    protected void setQualifier(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'qualifier' is not changeable", 0);
        }
        setProperty(ctx, "qualifier", value);
    }


    protected void setQualifier(String value)
    {
        setQualifier(getSession().getSessionContext(), value);
    }
}
