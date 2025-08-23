package de.hybris.platform.solrfacetsearch.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedProperty;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrSearchQueryTemplate extends GenericItem
{
    public static final String NAME = "name";
    public static final String SHOWFACETS = "showFacets";
    public static final String RESTRICTFIELDSINRESPONSE = "restrictFieldsInResponse";
    public static final String ENABLEHIGHLIGHTING = "enableHighlighting";
    public static final String GROUP = "group";
    public static final String GROUPPROPERTY = "groupProperty";
    public static final String GROUPLIMIT = "groupLimit";
    public static final String GROUPFACETS = "groupFacets";
    public static final String PAGESIZE = "pageSize";
    public static final String FTSQUERYBUILDER = "ftsQueryBuilder";
    public static final String FTSQUERYBUILDERPARAMETERS = "ftsQueryBuilderParameters";
    public static final String INDEXEDTYPEPOS = "indexedTypePOS";
    public static final String INDEXEDTYPE = "indexedType";
    public static final String SEARCHQUERYPROPERTIES = "searchQueryProperties";
    public static final String SEARCHQUERYSORTS = "searchQuerySorts";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrSearchQueryTemplate> INDEXEDTYPEHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHQUERYTEMPLATE, false, "indexedType", "indexedTypePOS", true, true, 0);
    protected static final OneToManyHandler<SolrSearchQueryProperty> SEARCHQUERYPROPERTIESHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHQUERYPROPERTY, true, "searchQueryTemplate", "searchQueryTemplatePOS", true, true, 0);
    protected static final OneToManyHandler<SolrSearchQuerySort> SEARCHQUERYSORTSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHQUERYSORT, true, "searchQueryTemplate", "searchQueryTemplatePOS", true, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("showFacets", Item.AttributeMode.INITIAL);
        tmp.put("restrictFieldsInResponse", Item.AttributeMode.INITIAL);
        tmp.put("enableHighlighting", Item.AttributeMode.INITIAL);
        tmp.put("group", Item.AttributeMode.INITIAL);
        tmp.put("groupProperty", Item.AttributeMode.INITIAL);
        tmp.put("groupLimit", Item.AttributeMode.INITIAL);
        tmp.put("groupFacets", Item.AttributeMode.INITIAL);
        tmp.put("pageSize", Item.AttributeMode.INITIAL);
        tmp.put("ftsQueryBuilder", Item.AttributeMode.INITIAL);
        tmp.put("ftsQueryBuilderParameters", Item.AttributeMode.INITIAL);
        tmp.put("indexedTypePOS", Item.AttributeMode.INITIAL);
        tmp.put("indexedType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        INDEXEDTYPEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
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


    public String getFtsQueryBuilder(SessionContext ctx)
    {
        return (String)getProperty(ctx, "ftsQueryBuilder");
    }


    public String getFtsQueryBuilder()
    {
        return getFtsQueryBuilder(getSession().getSessionContext());
    }


    public void setFtsQueryBuilder(SessionContext ctx, String value)
    {
        setProperty(ctx, "ftsQueryBuilder", value);
    }


    public void setFtsQueryBuilder(String value)
    {
        setFtsQueryBuilder(getSession().getSessionContext(), value);
    }


    public Map<String, String> getAllFtsQueryBuilderParameters(SessionContext ctx)
    {
        Map<String, String> map = (Map<String, String>)getProperty(ctx, "ftsQueryBuilderParameters");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllFtsQueryBuilderParameters()
    {
        return getAllFtsQueryBuilderParameters(getSession().getSessionContext());
    }


    public void setAllFtsQueryBuilderParameters(SessionContext ctx, Map<String, String> value)
    {
        setProperty(ctx, "ftsQueryBuilderParameters", value);
    }


    public void setAllFtsQueryBuilderParameters(Map<String, String> value)
    {
        setAllFtsQueryBuilderParameters(getSession().getSessionContext(), value);
    }


    public Boolean isGroup(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "group");
    }


    public Boolean isGroup()
    {
        return isGroup(getSession().getSessionContext());
    }


    public boolean isGroupAsPrimitive(SessionContext ctx)
    {
        Boolean value = isGroup(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isGroupAsPrimitive()
    {
        return isGroupAsPrimitive(getSession().getSessionContext());
    }


    public void setGroup(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "group", value);
    }


    public void setGroup(Boolean value)
    {
        setGroup(getSession().getSessionContext(), value);
    }


    public void setGroup(SessionContext ctx, boolean value)
    {
        setGroup(ctx, Boolean.valueOf(value));
    }


    public void setGroup(boolean value)
    {
        setGroup(getSession().getSessionContext(), value);
    }


    public Boolean isGroupFacets(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "groupFacets");
    }


    public Boolean isGroupFacets()
    {
        return isGroupFacets(getSession().getSessionContext());
    }


    public boolean isGroupFacetsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isGroupFacets(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isGroupFacetsAsPrimitive()
    {
        return isGroupFacetsAsPrimitive(getSession().getSessionContext());
    }


    public void setGroupFacets(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "groupFacets", value);
    }


    public void setGroupFacets(Boolean value)
    {
        setGroupFacets(getSession().getSessionContext(), value);
    }


    public void setGroupFacets(SessionContext ctx, boolean value)
    {
        setGroupFacets(ctx, Boolean.valueOf(value));
    }


    public void setGroupFacets(boolean value)
    {
        setGroupFacets(getSession().getSessionContext(), value);
    }


    public Integer getGroupLimit(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "groupLimit");
    }


    public Integer getGroupLimit()
    {
        return getGroupLimit(getSession().getSessionContext());
    }


    public int getGroupLimitAsPrimitive(SessionContext ctx)
    {
        Integer value = getGroupLimit(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getGroupLimitAsPrimitive()
    {
        return getGroupLimitAsPrimitive(getSession().getSessionContext());
    }


    public void setGroupLimit(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "groupLimit", value);
    }


    public void setGroupLimit(Integer value)
    {
        setGroupLimit(getSession().getSessionContext(), value);
    }


    public void setGroupLimit(SessionContext ctx, int value)
    {
        setGroupLimit(ctx, Integer.valueOf(value));
    }


    public void setGroupLimit(int value)
    {
        setGroupLimit(getSession().getSessionContext(), value);
    }


    public SolrIndexedProperty getGroupProperty(SessionContext ctx)
    {
        return (SolrIndexedProperty)getProperty(ctx, "groupProperty");
    }


    public SolrIndexedProperty getGroupProperty()
    {
        return getGroupProperty(getSession().getSessionContext());
    }


    public void setGroupProperty(SessionContext ctx, SolrIndexedProperty value)
    {
        setProperty(ctx, "groupProperty", value);
    }


    public void setGroupProperty(SolrIndexedProperty value)
    {
        setGroupProperty(getSession().getSessionContext(), value);
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
        INDEXEDTYPEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setIndexedType(SolrIndexedType value)
    {
        setIndexedType(getSession().getSessionContext(), value);
    }


    Integer getIndexedTypePOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "indexedTypePOS");
    }


    Integer getIndexedTypePOS()
    {
        return getIndexedTypePOS(getSession().getSessionContext());
    }


    int getIndexedTypePOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getIndexedTypePOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getIndexedTypePOSAsPrimitive()
    {
        return getIndexedTypePOSAsPrimitive(getSession().getSessionContext());
    }


    void setIndexedTypePOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "indexedTypePOS", value);
    }


    void setIndexedTypePOS(Integer value)
    {
        setIndexedTypePOS(getSession().getSessionContext(), value);
    }


    void setIndexedTypePOS(SessionContext ctx, int value)
    {
        setIndexedTypePOS(ctx, Integer.valueOf(value));
    }


    void setIndexedTypePOS(int value)
    {
        setIndexedTypePOS(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
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


    public Collection<SolrSearchQueryProperty> getSearchQueryProperties(SessionContext ctx)
    {
        return SEARCHQUERYPROPERTIESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<SolrSearchQueryProperty> getSearchQueryProperties()
    {
        return getSearchQueryProperties(getSession().getSessionContext());
    }


    public void setSearchQueryProperties(SessionContext ctx, Collection<SolrSearchQueryProperty> value)
    {
        SEARCHQUERYPROPERTIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSearchQueryProperties(Collection<SolrSearchQueryProperty> value)
    {
        setSearchQueryProperties(getSession().getSessionContext(), value);
    }


    public void addToSearchQueryProperties(SessionContext ctx, SolrSearchQueryProperty value)
    {
        SEARCHQUERYPROPERTIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSearchQueryProperties(SolrSearchQueryProperty value)
    {
        addToSearchQueryProperties(getSession().getSessionContext(), value);
    }


    public void removeFromSearchQueryProperties(SessionContext ctx, SolrSearchQueryProperty value)
    {
        SEARCHQUERYPROPERTIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSearchQueryProperties(SolrSearchQueryProperty value)
    {
        removeFromSearchQueryProperties(getSession().getSessionContext(), value);
    }


    public Collection<SolrSearchQuerySort> getSearchQuerySorts(SessionContext ctx)
    {
        return SEARCHQUERYSORTSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<SolrSearchQuerySort> getSearchQuerySorts()
    {
        return getSearchQuerySorts(getSession().getSessionContext());
    }


    public void setSearchQuerySorts(SessionContext ctx, Collection<SolrSearchQuerySort> value)
    {
        SEARCHQUERYSORTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSearchQuerySorts(Collection<SolrSearchQuerySort> value)
    {
        setSearchQuerySorts(getSession().getSessionContext(), value);
    }


    public void addToSearchQuerySorts(SessionContext ctx, SolrSearchQuerySort value)
    {
        SEARCHQUERYSORTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSearchQuerySorts(SolrSearchQuerySort value)
    {
        addToSearchQuerySorts(getSession().getSessionContext(), value);
    }


    public void removeFromSearchQuerySorts(SessionContext ctx, SolrSearchQuerySort value)
    {
        SEARCHQUERYSORTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSearchQuerySorts(SolrSearchQuerySort value)
    {
        removeFromSearchQuerySorts(getSession().getSessionContext(), value);
    }


    public Boolean isShowFacets(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "showFacets");
    }


    public Boolean isShowFacets()
    {
        return isShowFacets(getSession().getSessionContext());
    }


    public boolean isShowFacetsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isShowFacets(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isShowFacetsAsPrimitive()
    {
        return isShowFacetsAsPrimitive(getSession().getSessionContext());
    }


    public void setShowFacets(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "showFacets", value);
    }


    public void setShowFacets(Boolean value)
    {
        setShowFacets(getSession().getSessionContext(), value);
    }


    public void setShowFacets(SessionContext ctx, boolean value)
    {
        setShowFacets(ctx, Boolean.valueOf(value));
    }


    public void setShowFacets(boolean value)
    {
        setShowFacets(getSession().getSessionContext(), value);
    }
}
