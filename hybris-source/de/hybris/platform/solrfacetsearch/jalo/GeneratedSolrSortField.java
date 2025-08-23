package de.hybris.platform.solrfacetsearch.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrSortField extends GenericItem
{
    public static final String FIELDNAME = "fieldName";
    public static final String ASCENDING = "ascending";
    public static final String SORTPOS = "sortPOS";
    public static final String SORT = "sort";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrSortField> SORTHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSORTFIELD, false, "sort", "sortPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("fieldName", Item.AttributeMode.INITIAL);
        tmp.put("ascending", Item.AttributeMode.INITIAL);
        tmp.put("sortPOS", Item.AttributeMode.INITIAL);
        tmp.put("sort", Item.AttributeMode.INITIAL);
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
        SORTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getFieldName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "fieldName");
    }


    public String getFieldName()
    {
        return getFieldName(getSession().getSessionContext());
    }


    public void setFieldName(SessionContext ctx, String value)
    {
        setProperty(ctx, "fieldName", value);
    }


    public void setFieldName(String value)
    {
        setFieldName(getSession().getSessionContext(), value);
    }


    public SolrSort getSort(SessionContext ctx)
    {
        return (SolrSort)getProperty(ctx, "sort");
    }


    public SolrSort getSort()
    {
        return getSort(getSession().getSessionContext());
    }


    public void setSort(SessionContext ctx, SolrSort value)
    {
        SORTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSort(SolrSort value)
    {
        setSort(getSession().getSessionContext(), value);
    }


    Integer getSortPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "sortPOS");
    }


    Integer getSortPOS()
    {
        return getSortPOS(getSession().getSessionContext());
    }


    int getSortPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSortPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSortPOSAsPrimitive()
    {
        return getSortPOSAsPrimitive(getSession().getSessionContext());
    }


    void setSortPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "sortPOS", value);
    }


    void setSortPOS(Integer value)
    {
        setSortPOS(getSession().getSessionContext(), value);
    }


    void setSortPOS(SessionContext ctx, int value)
    {
        setSortPOS(ctx, Integer.valueOf(value));
    }


    void setSortPOS(int value)
    {
        setSortPOS(getSession().getSessionContext(), value);
    }
}
