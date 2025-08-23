package de.hybris.platform.solrfacetsearch.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrSearchQuerySort extends GenericItem
{
    public static final String FIELD = "field";
    public static final String ASCENDING = "ascending";
    public static final String SEARCHQUERYTEMPLATEPOS = "searchQueryTemplatePOS";
    public static final String SEARCHQUERYTEMPLATE = "searchQueryTemplate";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrSearchQuerySort> SEARCHQUERYTEMPLATEHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHQUERYSORT, false, "searchQueryTemplate", "searchQueryTemplatePOS", true, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("field", Item.AttributeMode.INITIAL);
        tmp.put("ascending", Item.AttributeMode.INITIAL);
        tmp.put("searchQueryTemplatePOS", Item.AttributeMode.INITIAL);
        tmp.put("searchQueryTemplate", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAscending(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "ascending");
    }


    public Boolean isAscending()
    {
        return isAscending(getSession().getSessionContext());
    }


    public boolean isAscendingAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAscending(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAscendingAsPrimitive()
    {
        return isAscendingAsPrimitive(getSession().getSessionContext());
    }


    public void setAscending(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "ascending", value);
    }


    public void setAscending(Boolean value)
    {
        setAscending(getSession().getSessionContext(), value);
    }


    public void setAscending(SessionContext ctx, boolean value)
    {
        setAscending(ctx, Boolean.valueOf(value));
    }


    public void setAscending(boolean value)
    {
        setAscending(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SEARCHQUERYTEMPLATEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getField(SessionContext ctx)
    {
        return (String)getProperty(ctx, "field");
    }


    public String getField()
    {
        return getField(getSession().getSessionContext());
    }


    public void setField(SessionContext ctx, String value)
    {
        setProperty(ctx, "field", value);
    }


    public void setField(String value)
    {
        setField(getSession().getSessionContext(), value);
    }


    public SolrSearchQueryTemplate getSearchQueryTemplate(SessionContext ctx)
    {
        return (SolrSearchQueryTemplate)getProperty(ctx, "searchQueryTemplate");
    }


    public SolrSearchQueryTemplate getSearchQueryTemplate()
    {
        return getSearchQueryTemplate(getSession().getSessionContext());
    }


    protected void setSearchQueryTemplate(SessionContext ctx, SolrSearchQueryTemplate value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'searchQueryTemplate' is not changeable", 0);
        }
        SEARCHQUERYTEMPLATEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setSearchQueryTemplate(SolrSearchQueryTemplate value)
    {
        setSearchQueryTemplate(getSession().getSessionContext(), value);
    }


    Integer getSearchQueryTemplatePOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "searchQueryTemplatePOS");
    }


    Integer getSearchQueryTemplatePOS()
    {
        return getSearchQueryTemplatePOS(getSession().getSessionContext());
    }


    int getSearchQueryTemplatePOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSearchQueryTemplatePOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSearchQueryTemplatePOSAsPrimitive()
    {
        return getSearchQueryTemplatePOSAsPrimitive(getSession().getSessionContext());
    }


    void setSearchQueryTemplatePOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "searchQueryTemplatePOS", value);
    }


    void setSearchQueryTemplatePOS(Integer value)
    {
        setSearchQueryTemplatePOS(getSession().getSessionContext(), value);
    }


    void setSearchQueryTemplatePOS(SessionContext ctx, int value)
    {
        setSearchQueryTemplatePOS(ctx, Integer.valueOf(value));
    }


    void setSearchQueryTemplatePOS(int value)
    {
        setSearchQueryTemplatePOS(getSession().getSessionContext(), value);
    }
}
