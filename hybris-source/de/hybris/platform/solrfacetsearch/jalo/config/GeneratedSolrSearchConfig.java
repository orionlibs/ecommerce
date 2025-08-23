package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrSearchConfig extends GenericItem
{
    public static final String PAGESIZE = "pageSize";
    public static final String DEFAULTSORTORDER = "defaultSortOrder";
    public static final String DESCRIPTION = "description";
    public static final String RESTRICTFIELDSINRESPONSE = "restrictFieldsInResponse";
    public static final String ENABLEHIGHLIGHTING = "enableHighlighting";
    public static final String ALLFACETVALUESINRESPONSE = "allFacetValuesInResponse";
    public static final String LEGACYMODE = "legacyMode";
    public static final String GROUPINGPROPERTY = "groupingProperty";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("pageSize", Item.AttributeMode.INITIAL);
        tmp.put("defaultSortOrder", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("restrictFieldsInResponse", Item.AttributeMode.INITIAL);
        tmp.put("enableHighlighting", Item.AttributeMode.INITIAL);
        tmp.put("allFacetValuesInResponse", Item.AttributeMode.INITIAL);
        tmp.put("legacyMode", Item.AttributeMode.INITIAL);
        tmp.put("groupingProperty", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAllFacetValuesInResponse(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "allFacetValuesInResponse");
    }


    public Boolean isAllFacetValuesInResponse()
    {
        return isAllFacetValuesInResponse(getSession().getSessionContext());
    }


    public boolean isAllFacetValuesInResponseAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAllFacetValuesInResponse(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAllFacetValuesInResponseAsPrimitive()
    {
        return isAllFacetValuesInResponseAsPrimitive(getSession().getSessionContext());
    }


    public void setAllFacetValuesInResponse(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "allFacetValuesInResponse", value);
    }


    public void setAllFacetValuesInResponse(Boolean value)
    {
        setAllFacetValuesInResponse(getSession().getSessionContext(), value);
    }


    public void setAllFacetValuesInResponse(SessionContext ctx, boolean value)
    {
        setAllFacetValuesInResponse(ctx, Boolean.valueOf(value));
    }


    public void setAllFacetValuesInResponse(boolean value)
    {
        setAllFacetValuesInResponse(getSession().getSessionContext(), value);
    }


    public Collection<String> getDefaultSortOrder(SessionContext ctx)
    {
        Collection<String> coll = (Collection<String>)getProperty(ctx, "defaultSortOrder");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<String> getDefaultSortOrder()
    {
        return getDefaultSortOrder(getSession().getSessionContext());
    }


    public void setDefaultSortOrder(SessionContext ctx, Collection<String> value)
    {
        setProperty(ctx, "defaultSortOrder", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setDefaultSortOrder(Collection<String> value)
    {
        setDefaultSortOrder(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public Boolean isEnableHighlighting(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "enableHighlighting");
    }


    public Boolean isEnableHighlighting()
    {
        return isEnableHighlighting(getSession().getSessionContext());
    }


    public boolean isEnableHighlightingAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEnableHighlighting(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnableHighlightingAsPrimitive()
    {
        return isEnableHighlightingAsPrimitive(getSession().getSessionContext());
    }


    public void setEnableHighlighting(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "enableHighlighting", value);
    }


    public void setEnableHighlighting(Boolean value)
    {
        setEnableHighlighting(getSession().getSessionContext(), value);
    }


    public void setEnableHighlighting(SessionContext ctx, boolean value)
    {
        setEnableHighlighting(ctx, Boolean.valueOf(value));
    }


    public void setEnableHighlighting(boolean value)
    {
        setEnableHighlighting(getSession().getSessionContext(), value);
    }


    public SolrIndexedProperty getGroupingProperty(SessionContext ctx)
    {
        return (SolrIndexedProperty)getProperty(ctx, "groupingProperty");
    }


    public SolrIndexedProperty getGroupingProperty()
    {
        return getGroupingProperty(getSession().getSessionContext());
    }


    public void setGroupingProperty(SessionContext ctx, SolrIndexedProperty value)
    {
        setProperty(ctx, "groupingProperty", value);
    }


    public void setGroupingProperty(SolrIndexedProperty value)
    {
        setGroupingProperty(getSession().getSessionContext(), value);
    }


    public Boolean isLegacyMode(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "legacyMode");
    }


    public Boolean isLegacyMode()
    {
        return isLegacyMode(getSession().getSessionContext());
    }


    public boolean isLegacyModeAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLegacyMode(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLegacyModeAsPrimitive()
    {
        return isLegacyModeAsPrimitive(getSession().getSessionContext());
    }


    public void setLegacyMode(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "legacyMode", value);
    }


    public void setLegacyMode(Boolean value)
    {
        setLegacyMode(getSession().getSessionContext(), value);
    }


    public void setLegacyMode(SessionContext ctx, boolean value)
    {
        setLegacyMode(ctx, Boolean.valueOf(value));
    }


    public void setLegacyMode(boolean value)
    {
        setLegacyMode(getSession().getSessionContext(), value);
    }


    public Integer getPageSize(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "pageSize");
    }


    public Integer getPageSize()
    {
        return getPageSize(getSession().getSessionContext());
    }


    public int getPageSizeAsPrimitive(SessionContext ctx)
    {
        Integer value = getPageSize(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPageSizeAsPrimitive()
    {
        return getPageSizeAsPrimitive(getSession().getSessionContext());
    }


    public void setPageSize(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "pageSize", value);
    }


    public void setPageSize(Integer value)
    {
        setPageSize(getSession().getSessionContext(), value);
    }


    public void setPageSize(SessionContext ctx, int value)
    {
        setPageSize(ctx, Integer.valueOf(value));
    }


    public void setPageSize(int value)
    {
        setPageSize(getSession().getSessionContext(), value);
    }


    public Boolean isRestrictFieldsInResponse(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "restrictFieldsInResponse");
    }


    public Boolean isRestrictFieldsInResponse()
    {
        return isRestrictFieldsInResponse(getSession().getSessionContext());
    }


    public boolean isRestrictFieldsInResponseAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRestrictFieldsInResponse(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRestrictFieldsInResponseAsPrimitive()
    {
        return isRestrictFieldsInResponseAsPrimitive(getSession().getSessionContext());
    }


    public void setRestrictFieldsInResponse(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "restrictFieldsInResponse", value);
    }


    public void setRestrictFieldsInResponse(Boolean value)
    {
        setRestrictFieldsInResponse(getSession().getSessionContext(), value);
    }


    public void setRestrictFieldsInResponse(SessionContext ctx, boolean value)
    {
        setRestrictFieldsInResponse(ctx, Boolean.valueOf(value));
    }


    public void setRestrictFieldsInResponse(boolean value)
    {
        setRestrictFieldsInResponse(getSession().getSessionContext(), value);
    }
}
