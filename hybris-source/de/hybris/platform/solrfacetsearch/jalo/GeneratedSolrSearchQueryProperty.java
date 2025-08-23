package de.hybris.platform.solrfacetsearch.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedProperty;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrSearchQueryProperty extends GenericItem
{
    public static final String PRIORITY = "priority";
    public static final String INCLUDEINRESPONSE = "includeInResponse";
    public static final String USEFORHIGHLIGHTING = "useForHighlighting";
    public static final String FACET = "facet";
    public static final String FACETTYPE = "facetType";
    public static final String FACETDISPLAYNAMEPROVIDER = "facetDisplayNameProvider";
    public static final String FACETSORTPROVIDER = "facetSortProvider";
    public static final String FACETTOPVALUESPROVIDER = "facetTopValuesProvider";
    public static final String FTSQUERY = "ftsQuery";
    public static final String FTSQUERYMINTERMLENGTH = "ftsQueryMinTermLength";
    public static final String FTSQUERYBOOST = "ftsQueryBoost";
    public static final String FTSFUZZYQUERY = "ftsFuzzyQuery";
    public static final String FTSFUZZYQUERYMINTERMLENGTH = "ftsFuzzyQueryMinTermLength";
    public static final String FTSFUZZYQUERYFUZZINESS = "ftsFuzzyQueryFuzziness";
    public static final String FTSFUZZYQUERYBOOST = "ftsFuzzyQueryBoost";
    public static final String FTSWILDCARDQUERY = "ftsWildcardQuery";
    public static final String FTSWILDCARDQUERYMINTERMLENGTH = "ftsWildcardQueryMinTermLength";
    public static final String FTSWILDCARDQUERYTYPE = "ftsWildcardQueryType";
    public static final String FTSWILDCARDQUERYBOOST = "ftsWildcardQueryBoost";
    public static final String FTSPHRASEQUERY = "ftsPhraseQuery";
    public static final String FTSPHRASEQUERYSLOP = "ftsPhraseQuerySlop";
    public static final String FTSPHRASEQUERYBOOST = "ftsPhraseQueryBoost";
    public static final String INDEXEDPROPERTYPOS = "indexedPropertyPOS";
    public static final String INDEXEDPROPERTY = "indexedProperty";
    public static final String SEARCHQUERYTEMPLATEPOS = "searchQueryTemplatePOS";
    public static final String SEARCHQUERYTEMPLATE = "searchQueryTemplate";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrSearchQueryProperty> INDEXEDPROPERTYHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHQUERYPROPERTY, false, "indexedProperty", "indexedPropertyPOS", true, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedSolrSearchQueryProperty> SEARCHQUERYTEMPLATEHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHQUERYPROPERTY, false, "searchQueryTemplate", "searchQueryTemplatePOS", true, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("includeInResponse", Item.AttributeMode.INITIAL);
        tmp.put("useForHighlighting", Item.AttributeMode.INITIAL);
        tmp.put("facet", Item.AttributeMode.INITIAL);
        tmp.put("facetType", Item.AttributeMode.INITIAL);
        tmp.put("facetDisplayNameProvider", Item.AttributeMode.INITIAL);
        tmp.put("facetSortProvider", Item.AttributeMode.INITIAL);
        tmp.put("facetTopValuesProvider", Item.AttributeMode.INITIAL);
        tmp.put("ftsQuery", Item.AttributeMode.INITIAL);
        tmp.put("ftsQueryMinTermLength", Item.AttributeMode.INITIAL);
        tmp.put("ftsQueryBoost", Item.AttributeMode.INITIAL);
        tmp.put("ftsFuzzyQuery", Item.AttributeMode.INITIAL);
        tmp.put("ftsFuzzyQueryMinTermLength", Item.AttributeMode.INITIAL);
        tmp.put("ftsFuzzyQueryFuzziness", Item.AttributeMode.INITIAL);
        tmp.put("ftsFuzzyQueryBoost", Item.AttributeMode.INITIAL);
        tmp.put("ftsWildcardQuery", Item.AttributeMode.INITIAL);
        tmp.put("ftsWildcardQueryMinTermLength", Item.AttributeMode.INITIAL);
        tmp.put("ftsWildcardQueryType", Item.AttributeMode.INITIAL);
        tmp.put("ftsWildcardQueryBoost", Item.AttributeMode.INITIAL);
        tmp.put("ftsPhraseQuery", Item.AttributeMode.INITIAL);
        tmp.put("ftsPhraseQuerySlop", Item.AttributeMode.INITIAL);
        tmp.put("ftsPhraseQueryBoost", Item.AttributeMode.INITIAL);
        tmp.put("indexedPropertyPOS", Item.AttributeMode.INITIAL);
        tmp.put("indexedProperty", Item.AttributeMode.INITIAL);
        tmp.put("searchQueryTemplatePOS", Item.AttributeMode.INITIAL);
        tmp.put("searchQueryTemplate", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        INDEXEDPROPERTYHANDLER.newInstance(ctx, allAttributes);
        SEARCHQUERYTEMPLATEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isFacet(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "facet");
    }


    public Boolean isFacet()
    {
        return isFacet(getSession().getSessionContext());
    }


    public boolean isFacetAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFacet(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFacetAsPrimitive()
    {
        return isFacetAsPrimitive(getSession().getSessionContext());
    }


    public void setFacet(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "facet", value);
    }


    public void setFacet(Boolean value)
    {
        setFacet(getSession().getSessionContext(), value);
    }


    public void setFacet(SessionContext ctx, boolean value)
    {
        setFacet(ctx, Boolean.valueOf(value));
    }


    public void setFacet(boolean value)
    {
        setFacet(getSession().getSessionContext(), value);
    }


    public String getFacetDisplayNameProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "facetDisplayNameProvider");
    }


    public String getFacetDisplayNameProvider()
    {
        return getFacetDisplayNameProvider(getSession().getSessionContext());
    }


    public void setFacetDisplayNameProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "facetDisplayNameProvider", value);
    }


    public void setFacetDisplayNameProvider(String value)
    {
        setFacetDisplayNameProvider(getSession().getSessionContext(), value);
    }


    public String getFacetSortProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "facetSortProvider");
    }


    public String getFacetSortProvider()
    {
        return getFacetSortProvider(getSession().getSessionContext());
    }


    public void setFacetSortProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "facetSortProvider", value);
    }


    public void setFacetSortProvider(String value)
    {
        setFacetSortProvider(getSession().getSessionContext(), value);
    }


    public String getFacetTopValuesProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "facetTopValuesProvider");
    }


    public String getFacetTopValuesProvider()
    {
        return getFacetTopValuesProvider(getSession().getSessionContext());
    }


    public void setFacetTopValuesProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "facetTopValuesProvider", value);
    }


    public void setFacetTopValuesProvider(String value)
    {
        setFacetTopValuesProvider(getSession().getSessionContext(), value);
    }


    public EnumerationValue getFacetType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "facetType");
    }


    public EnumerationValue getFacetType()
    {
        return getFacetType(getSession().getSessionContext());
    }


    public void setFacetType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "facetType", value);
    }


    public void setFacetType(EnumerationValue value)
    {
        setFacetType(getSession().getSessionContext(), value);
    }


    public Boolean isFtsFuzzyQuery(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "ftsFuzzyQuery");
    }


    public Boolean isFtsFuzzyQuery()
    {
        return isFtsFuzzyQuery(getSession().getSessionContext());
    }


    public boolean isFtsFuzzyQueryAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFtsFuzzyQuery(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFtsFuzzyQueryAsPrimitive()
    {
        return isFtsFuzzyQueryAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsFuzzyQuery(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "ftsFuzzyQuery", value);
    }


    public void setFtsFuzzyQuery(Boolean value)
    {
        setFtsFuzzyQuery(getSession().getSessionContext(), value);
    }


    public void setFtsFuzzyQuery(SessionContext ctx, boolean value)
    {
        setFtsFuzzyQuery(ctx, Boolean.valueOf(value));
    }


    public void setFtsFuzzyQuery(boolean value)
    {
        setFtsFuzzyQuery(getSession().getSessionContext(), value);
    }


    public Float getFtsFuzzyQueryBoost(SessionContext ctx)
    {
        return (Float)getProperty(ctx, "ftsFuzzyQueryBoost");
    }


    public Float getFtsFuzzyQueryBoost()
    {
        return getFtsFuzzyQueryBoost(getSession().getSessionContext());
    }


    public float getFtsFuzzyQueryBoostAsPrimitive(SessionContext ctx)
    {
        Float value = getFtsFuzzyQueryBoost(ctx);
        return (value != null) ? value.floatValue() : 0.0F;
    }


    public float getFtsFuzzyQueryBoostAsPrimitive()
    {
        return getFtsFuzzyQueryBoostAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsFuzzyQueryBoost(SessionContext ctx, Float value)
    {
        setProperty(ctx, "ftsFuzzyQueryBoost", value);
    }


    public void setFtsFuzzyQueryBoost(Float value)
    {
        setFtsFuzzyQueryBoost(getSession().getSessionContext(), value);
    }


    public void setFtsFuzzyQueryBoost(SessionContext ctx, float value)
    {
        setFtsFuzzyQueryBoost(ctx, Float.valueOf(value));
    }


    public void setFtsFuzzyQueryBoost(float value)
    {
        setFtsFuzzyQueryBoost(getSession().getSessionContext(), value);
    }


    public Integer getFtsFuzzyQueryFuzziness(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "ftsFuzzyQueryFuzziness");
    }


    public Integer getFtsFuzzyQueryFuzziness()
    {
        return getFtsFuzzyQueryFuzziness(getSession().getSessionContext());
    }


    public int getFtsFuzzyQueryFuzzinessAsPrimitive(SessionContext ctx)
    {
        Integer value = getFtsFuzzyQueryFuzziness(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getFtsFuzzyQueryFuzzinessAsPrimitive()
    {
        return getFtsFuzzyQueryFuzzinessAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsFuzzyQueryFuzziness(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "ftsFuzzyQueryFuzziness", value);
    }


    public void setFtsFuzzyQueryFuzziness(Integer value)
    {
        setFtsFuzzyQueryFuzziness(getSession().getSessionContext(), value);
    }


    public void setFtsFuzzyQueryFuzziness(SessionContext ctx, int value)
    {
        setFtsFuzzyQueryFuzziness(ctx, Integer.valueOf(value));
    }


    public void setFtsFuzzyQueryFuzziness(int value)
    {
        setFtsFuzzyQueryFuzziness(getSession().getSessionContext(), value);
    }


    public Integer getFtsFuzzyQueryMinTermLength(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "ftsFuzzyQueryMinTermLength");
    }


    public Integer getFtsFuzzyQueryMinTermLength()
    {
        return getFtsFuzzyQueryMinTermLength(getSession().getSessionContext());
    }


    public int getFtsFuzzyQueryMinTermLengthAsPrimitive(SessionContext ctx)
    {
        Integer value = getFtsFuzzyQueryMinTermLength(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getFtsFuzzyQueryMinTermLengthAsPrimitive()
    {
        return getFtsFuzzyQueryMinTermLengthAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsFuzzyQueryMinTermLength(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "ftsFuzzyQueryMinTermLength", value);
    }


    public void setFtsFuzzyQueryMinTermLength(Integer value)
    {
        setFtsFuzzyQueryMinTermLength(getSession().getSessionContext(), value);
    }


    public void setFtsFuzzyQueryMinTermLength(SessionContext ctx, int value)
    {
        setFtsFuzzyQueryMinTermLength(ctx, Integer.valueOf(value));
    }


    public void setFtsFuzzyQueryMinTermLength(int value)
    {
        setFtsFuzzyQueryMinTermLength(getSession().getSessionContext(), value);
    }


    public Boolean isFtsPhraseQuery(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "ftsPhraseQuery");
    }


    public Boolean isFtsPhraseQuery()
    {
        return isFtsPhraseQuery(getSession().getSessionContext());
    }


    public boolean isFtsPhraseQueryAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFtsPhraseQuery(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFtsPhraseQueryAsPrimitive()
    {
        return isFtsPhraseQueryAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsPhraseQuery(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "ftsPhraseQuery", value);
    }


    public void setFtsPhraseQuery(Boolean value)
    {
        setFtsPhraseQuery(getSession().getSessionContext(), value);
    }


    public void setFtsPhraseQuery(SessionContext ctx, boolean value)
    {
        setFtsPhraseQuery(ctx, Boolean.valueOf(value));
    }


    public void setFtsPhraseQuery(boolean value)
    {
        setFtsPhraseQuery(getSession().getSessionContext(), value);
    }


    public Float getFtsPhraseQueryBoost(SessionContext ctx)
    {
        return (Float)getProperty(ctx, "ftsPhraseQueryBoost");
    }


    public Float getFtsPhraseQueryBoost()
    {
        return getFtsPhraseQueryBoost(getSession().getSessionContext());
    }


    public float getFtsPhraseQueryBoostAsPrimitive(SessionContext ctx)
    {
        Float value = getFtsPhraseQueryBoost(ctx);
        return (value != null) ? value.floatValue() : 0.0F;
    }


    public float getFtsPhraseQueryBoostAsPrimitive()
    {
        return getFtsPhraseQueryBoostAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsPhraseQueryBoost(SessionContext ctx, Float value)
    {
        setProperty(ctx, "ftsPhraseQueryBoost", value);
    }


    public void setFtsPhraseQueryBoost(Float value)
    {
        setFtsPhraseQueryBoost(getSession().getSessionContext(), value);
    }


    public void setFtsPhraseQueryBoost(SessionContext ctx, float value)
    {
        setFtsPhraseQueryBoost(ctx, Float.valueOf(value));
    }


    public void setFtsPhraseQueryBoost(float value)
    {
        setFtsPhraseQueryBoost(getSession().getSessionContext(), value);
    }


    public Float getFtsPhraseQuerySlop(SessionContext ctx)
    {
        return (Float)getProperty(ctx, "ftsPhraseQuerySlop");
    }


    public Float getFtsPhraseQuerySlop()
    {
        return getFtsPhraseQuerySlop(getSession().getSessionContext());
    }


    public float getFtsPhraseQuerySlopAsPrimitive(SessionContext ctx)
    {
        Float value = getFtsPhraseQuerySlop(ctx);
        return (value != null) ? value.floatValue() : 0.0F;
    }


    public float getFtsPhraseQuerySlopAsPrimitive()
    {
        return getFtsPhraseQuerySlopAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsPhraseQuerySlop(SessionContext ctx, Float value)
    {
        setProperty(ctx, "ftsPhraseQuerySlop", value);
    }


    public void setFtsPhraseQuerySlop(Float value)
    {
        setFtsPhraseQuerySlop(getSession().getSessionContext(), value);
    }


    public void setFtsPhraseQuerySlop(SessionContext ctx, float value)
    {
        setFtsPhraseQuerySlop(ctx, Float.valueOf(value));
    }


    public void setFtsPhraseQuerySlop(float value)
    {
        setFtsPhraseQuerySlop(getSession().getSessionContext(), value);
    }


    public Boolean isFtsQuery(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "ftsQuery");
    }


    public Boolean isFtsQuery()
    {
        return isFtsQuery(getSession().getSessionContext());
    }


    public boolean isFtsQueryAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFtsQuery(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFtsQueryAsPrimitive()
    {
        return isFtsQueryAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsQuery(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "ftsQuery", value);
    }


    public void setFtsQuery(Boolean value)
    {
        setFtsQuery(getSession().getSessionContext(), value);
    }


    public void setFtsQuery(SessionContext ctx, boolean value)
    {
        setFtsQuery(ctx, Boolean.valueOf(value));
    }


    public void setFtsQuery(boolean value)
    {
        setFtsQuery(getSession().getSessionContext(), value);
    }


    public Float getFtsQueryBoost(SessionContext ctx)
    {
        return (Float)getProperty(ctx, "ftsQueryBoost");
    }


    public Float getFtsQueryBoost()
    {
        return getFtsQueryBoost(getSession().getSessionContext());
    }


    public float getFtsQueryBoostAsPrimitive(SessionContext ctx)
    {
        Float value = getFtsQueryBoost(ctx);
        return (value != null) ? value.floatValue() : 0.0F;
    }


    public float getFtsQueryBoostAsPrimitive()
    {
        return getFtsQueryBoostAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsQueryBoost(SessionContext ctx, Float value)
    {
        setProperty(ctx, "ftsQueryBoost", value);
    }


    public void setFtsQueryBoost(Float value)
    {
        setFtsQueryBoost(getSession().getSessionContext(), value);
    }


    public void setFtsQueryBoost(SessionContext ctx, float value)
    {
        setFtsQueryBoost(ctx, Float.valueOf(value));
    }


    public void setFtsQueryBoost(float value)
    {
        setFtsQueryBoost(getSession().getSessionContext(), value);
    }


    public Integer getFtsQueryMinTermLength(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "ftsQueryMinTermLength");
    }


    public Integer getFtsQueryMinTermLength()
    {
        return getFtsQueryMinTermLength(getSession().getSessionContext());
    }


    public int getFtsQueryMinTermLengthAsPrimitive(SessionContext ctx)
    {
        Integer value = getFtsQueryMinTermLength(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getFtsQueryMinTermLengthAsPrimitive()
    {
        return getFtsQueryMinTermLengthAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsQueryMinTermLength(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "ftsQueryMinTermLength", value);
    }


    public void setFtsQueryMinTermLength(Integer value)
    {
        setFtsQueryMinTermLength(getSession().getSessionContext(), value);
    }


    public void setFtsQueryMinTermLength(SessionContext ctx, int value)
    {
        setFtsQueryMinTermLength(ctx, Integer.valueOf(value));
    }


    public void setFtsQueryMinTermLength(int value)
    {
        setFtsQueryMinTermLength(getSession().getSessionContext(), value);
    }


    public Boolean isFtsWildcardQuery(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "ftsWildcardQuery");
    }


    public Boolean isFtsWildcardQuery()
    {
        return isFtsWildcardQuery(getSession().getSessionContext());
    }


    public boolean isFtsWildcardQueryAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFtsWildcardQuery(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFtsWildcardQueryAsPrimitive()
    {
        return isFtsWildcardQueryAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsWildcardQuery(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "ftsWildcardQuery", value);
    }


    public void setFtsWildcardQuery(Boolean value)
    {
        setFtsWildcardQuery(getSession().getSessionContext(), value);
    }


    public void setFtsWildcardQuery(SessionContext ctx, boolean value)
    {
        setFtsWildcardQuery(ctx, Boolean.valueOf(value));
    }


    public void setFtsWildcardQuery(boolean value)
    {
        setFtsWildcardQuery(getSession().getSessionContext(), value);
    }


    public Float getFtsWildcardQueryBoost(SessionContext ctx)
    {
        return (Float)getProperty(ctx, "ftsWildcardQueryBoost");
    }


    public Float getFtsWildcardQueryBoost()
    {
        return getFtsWildcardQueryBoost(getSession().getSessionContext());
    }


    public float getFtsWildcardQueryBoostAsPrimitive(SessionContext ctx)
    {
        Float value = getFtsWildcardQueryBoost(ctx);
        return (value != null) ? value.floatValue() : 0.0F;
    }


    public float getFtsWildcardQueryBoostAsPrimitive()
    {
        return getFtsWildcardQueryBoostAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsWildcardQueryBoost(SessionContext ctx, Float value)
    {
        setProperty(ctx, "ftsWildcardQueryBoost", value);
    }


    public void setFtsWildcardQueryBoost(Float value)
    {
        setFtsWildcardQueryBoost(getSession().getSessionContext(), value);
    }


    public void setFtsWildcardQueryBoost(SessionContext ctx, float value)
    {
        setFtsWildcardQueryBoost(ctx, Float.valueOf(value));
    }


    public void setFtsWildcardQueryBoost(float value)
    {
        setFtsWildcardQueryBoost(getSession().getSessionContext(), value);
    }


    public Integer getFtsWildcardQueryMinTermLength(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "ftsWildcardQueryMinTermLength");
    }


    public Integer getFtsWildcardQueryMinTermLength()
    {
        return getFtsWildcardQueryMinTermLength(getSession().getSessionContext());
    }


    public int getFtsWildcardQueryMinTermLengthAsPrimitive(SessionContext ctx)
    {
        Integer value = getFtsWildcardQueryMinTermLength(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getFtsWildcardQueryMinTermLengthAsPrimitive()
    {
        return getFtsWildcardQueryMinTermLengthAsPrimitive(getSession().getSessionContext());
    }


    public void setFtsWildcardQueryMinTermLength(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "ftsWildcardQueryMinTermLength", value);
    }


    public void setFtsWildcardQueryMinTermLength(Integer value)
    {
        setFtsWildcardQueryMinTermLength(getSession().getSessionContext(), value);
    }


    public void setFtsWildcardQueryMinTermLength(SessionContext ctx, int value)
    {
        setFtsWildcardQueryMinTermLength(ctx, Integer.valueOf(value));
    }


    public void setFtsWildcardQueryMinTermLength(int value)
    {
        setFtsWildcardQueryMinTermLength(getSession().getSessionContext(), value);
    }


    public EnumerationValue getFtsWildcardQueryType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "ftsWildcardQueryType");
    }


    public EnumerationValue getFtsWildcardQueryType()
    {
        return getFtsWildcardQueryType(getSession().getSessionContext());
    }


    public void setFtsWildcardQueryType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "ftsWildcardQueryType", value);
    }


    public void setFtsWildcardQueryType(EnumerationValue value)
    {
        setFtsWildcardQueryType(getSession().getSessionContext(), value);
    }


    public Boolean isIncludeInResponse(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "includeInResponse");
    }


    public Boolean isIncludeInResponse()
    {
        return isIncludeInResponse(getSession().getSessionContext());
    }


    public boolean isIncludeInResponseAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIncludeInResponse(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIncludeInResponseAsPrimitive()
    {
        return isIncludeInResponseAsPrimitive(getSession().getSessionContext());
    }


    public void setIncludeInResponse(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "includeInResponse", value);
    }


    public void setIncludeInResponse(Boolean value)
    {
        setIncludeInResponse(getSession().getSessionContext(), value);
    }


    public void setIncludeInResponse(SessionContext ctx, boolean value)
    {
        setIncludeInResponse(ctx, Boolean.valueOf(value));
    }


    public void setIncludeInResponse(boolean value)
    {
        setIncludeInResponse(getSession().getSessionContext(), value);
    }


    public SolrIndexedProperty getIndexedProperty(SessionContext ctx)
    {
        return (SolrIndexedProperty)getProperty(ctx, "indexedProperty");
    }


    public SolrIndexedProperty getIndexedProperty()
    {
        return getIndexedProperty(getSession().getSessionContext());
    }


    protected void setIndexedProperty(SessionContext ctx, SolrIndexedProperty value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'indexedProperty' is not changeable", 0);
        }
        INDEXEDPROPERTYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setIndexedProperty(SolrIndexedProperty value)
    {
        setIndexedProperty(getSession().getSessionContext(), value);
    }


    Integer getIndexedPropertyPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "indexedPropertyPOS");
    }


    Integer getIndexedPropertyPOS()
    {
        return getIndexedPropertyPOS(getSession().getSessionContext());
    }


    int getIndexedPropertyPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getIndexedPropertyPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getIndexedPropertyPOSAsPrimitive()
    {
        return getIndexedPropertyPOSAsPrimitive(getSession().getSessionContext());
    }


    void setIndexedPropertyPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "indexedPropertyPOS", value);
    }


    void setIndexedPropertyPOS(Integer value)
    {
        setIndexedPropertyPOS(getSession().getSessionContext(), value);
    }


    void setIndexedPropertyPOS(SessionContext ctx, int value)
    {
        setIndexedPropertyPOS(ctx, Integer.valueOf(value));
    }


    void setIndexedPropertyPOS(int value)
    {
        setIndexedPropertyPOS(getSession().getSessionContext(), value);
    }


    public Integer getPriority(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "priority");
    }


    public Integer getPriority()
    {
        return getPriority(getSession().getSessionContext());
    }


    public int getPriorityAsPrimitive(SessionContext ctx)
    {
        Integer value = getPriority(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPriorityAsPrimitive()
    {
        return getPriorityAsPrimitive(getSession().getSessionContext());
    }


    public void setPriority(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "priority", value);
    }


    public void setPriority(Integer value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public void setPriority(SessionContext ctx, int value)
    {
        setPriority(ctx, Integer.valueOf(value));
    }


    public void setPriority(int value)
    {
        setPriority(getSession().getSessionContext(), value);
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


    public Boolean isUseForHighlighting(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "useForHighlighting");
    }


    public Boolean isUseForHighlighting()
    {
        return isUseForHighlighting(getSession().getSessionContext());
    }


    public boolean isUseForHighlightingAsPrimitive(SessionContext ctx)
    {
        Boolean value = isUseForHighlighting(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUseForHighlightingAsPrimitive()
    {
        return isUseForHighlightingAsPrimitive(getSession().getSessionContext());
    }


    public void setUseForHighlighting(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "useForHighlighting", value);
    }


    public void setUseForHighlighting(Boolean value)
    {
        setUseForHighlighting(getSession().getSessionContext(), value);
    }


    public void setUseForHighlighting(SessionContext ctx, boolean value)
    {
        setUseForHighlighting(ctx, Boolean.valueOf(value));
    }


    public void setUseForHighlighting(boolean value)
    {
        setUseForHighlighting(getSession().getSessionContext(), value);
    }
}
