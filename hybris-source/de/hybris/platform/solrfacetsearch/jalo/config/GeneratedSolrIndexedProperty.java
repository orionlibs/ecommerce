package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.SolrSearchQueryProperty;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedSolrIndexedProperty extends GenericItem
{
    public static final String NAME = "name";
    public static final String DISPLAYNAME = "displayName";
    public static final String TYPE = "type";
    public static final String SORTABLETYPE = "sortableType";
    public static final String FACET = "facet";
    public static final String LOCALIZED = "localized";
    public static final String CURRENCY = "currency";
    public static final String MULTIVALUE = "multiValue";
    public static final String RANGESET = "rangeSet";
    public static final String FACETDISPLAYNAMEPROVIDER = "facetDisplayNameProvider";
    public static final String FACETTYPE = "facetType";
    public static final String FIELDVALUEPROVIDER = "fieldValueProvider";
    public static final String VALUEPROVIDERPARAMETER = "valueProviderParameter";
    public static final String VALUEPROVIDERPARAMETERS = "valueProviderParameters";
    public static final String EXPORTID = "exportId";
    public static final String USEFORSPELLCHECKING = "useForSpellchecking";
    public static final String USEFORAUTOCOMPLETE = "useForAutocomplete";
    public static final String PRIORITY = "priority";
    public static final String INCLUDEINRESPONSE = "includeInResponse";
    public static final String USEFORHIGHLIGHTING = "useForHighlighting";
    public static final String CUSTOMFACETSORTPROVIDER = "customFacetSortProvider";
    public static final String TOPVALUESPROVIDER = "topValuesProvider";
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
    public static final String CLASSATTRIBUTEASSIGNMENT = "classAttributeAssignment";
    public static final String CATEGORYFIELD = "categoryField";
    public static final String VISIBLE = "visible";
    public static final String SOLRINDEXEDTYPEPOS = "solrIndexedTypePOS";
    public static final String SOLRINDEXEDTYPE = "solrIndexedType";
    public static final String RANGESETS = "rangeSets";
    protected static String SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_SRC_ORDERED = "relation.SolrIndexedProperty2SolrValueRangeSetRelation.source.ordered";
    protected static String SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_TGT_ORDERED = "relation.SolrIndexedProperty2SolrValueRangeSetRelation.target.ordered";
    protected static String SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_MARKMODIFIED = "relation.SolrIndexedProperty2SolrValueRangeSetRelation.markmodified";
    public static final String SEARCHQUERYPROPERTIES = "searchQueryProperties";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrIndexedProperty> SOLRINDEXEDTYPEHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXEDPROPERTY, false, "solrIndexedType", "solrIndexedTypePOS", true, true, 2);
    protected static final OneToManyHandler<SolrSearchQueryProperty> SEARCHQUERYPROPERTIESHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHQUERYPROPERTY, true, "indexedProperty", "indexedPropertyPOS", true, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("displayName", Item.AttributeMode.INITIAL);
        tmp.put("type", Item.AttributeMode.INITIAL);
        tmp.put("sortableType", Item.AttributeMode.INITIAL);
        tmp.put("facet", Item.AttributeMode.INITIAL);
        tmp.put("localized", Item.AttributeMode.INITIAL);
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("multiValue", Item.AttributeMode.INITIAL);
        tmp.put("rangeSet", Item.AttributeMode.INITIAL);
        tmp.put("facetDisplayNameProvider", Item.AttributeMode.INITIAL);
        tmp.put("facetType", Item.AttributeMode.INITIAL);
        tmp.put("fieldValueProvider", Item.AttributeMode.INITIAL);
        tmp.put("valueProviderParameter", Item.AttributeMode.INITIAL);
        tmp.put("valueProviderParameters", Item.AttributeMode.INITIAL);
        tmp.put("exportId", Item.AttributeMode.INITIAL);
        tmp.put("useForSpellchecking", Item.AttributeMode.INITIAL);
        tmp.put("useForAutocomplete", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("includeInResponse", Item.AttributeMode.INITIAL);
        tmp.put("useForHighlighting", Item.AttributeMode.INITIAL);
        tmp.put("customFacetSortProvider", Item.AttributeMode.INITIAL);
        tmp.put("topValuesProvider", Item.AttributeMode.INITIAL);
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
        tmp.put("classAttributeAssignment", Item.AttributeMode.INITIAL);
        tmp.put("categoryField", Item.AttributeMode.INITIAL);
        tmp.put("visible", Item.AttributeMode.INITIAL);
        tmp.put("solrIndexedTypePOS", Item.AttributeMode.INITIAL);
        tmp.put("solrIndexedType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isCategoryField(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "categoryField");
    }


    public Boolean isCategoryField()
    {
        return isCategoryField(getSession().getSessionContext());
    }


    public boolean isCategoryFieldAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCategoryField(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCategoryFieldAsPrimitive()
    {
        return isCategoryFieldAsPrimitive(getSession().getSessionContext());
    }


    public void setCategoryField(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "categoryField", value);
    }


    public void setCategoryField(Boolean value)
    {
        setCategoryField(getSession().getSessionContext(), value);
    }


    public void setCategoryField(SessionContext ctx, boolean value)
    {
        setCategoryField(ctx, Boolean.valueOf(value));
    }


    public void setCategoryField(boolean value)
    {
        setCategoryField(getSession().getSessionContext(), value);
    }


    public ClassAttributeAssignment getClassAttributeAssignment(SessionContext ctx)
    {
        return (ClassAttributeAssignment)getProperty(ctx, "classAttributeAssignment");
    }


    public ClassAttributeAssignment getClassAttributeAssignment()
    {
        return getClassAttributeAssignment(getSession().getSessionContext());
    }


    public void setClassAttributeAssignment(SessionContext ctx, ClassAttributeAssignment value)
    {
        setProperty(ctx, "classAttributeAssignment", value);
    }


    public void setClassAttributeAssignment(ClassAttributeAssignment value)
    {
        setClassAttributeAssignment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOLRINDEXEDTYPEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isCurrency(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "currency");
    }


    public Boolean isCurrency()
    {
        return isCurrency(getSession().getSessionContext());
    }


    public boolean isCurrencyAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCurrency(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCurrencyAsPrimitive()
    {
        return isCurrencyAsPrimitive(getSession().getSessionContext());
    }


    public void setCurrency(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "currency", value);
    }


    public void setCurrency(Boolean value)
    {
        setCurrency(getSession().getSessionContext(), value);
    }


    public void setCurrency(SessionContext ctx, boolean value)
    {
        setCurrency(ctx, Boolean.valueOf(value));
    }


    public void setCurrency(boolean value)
    {
        setCurrency(getSession().getSessionContext(), value);
    }


    public String getCustomFacetSortProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "customFacetSortProvider");
    }


    public String getCustomFacetSortProvider()
    {
        return getCustomFacetSortProvider(getSession().getSessionContext());
    }


    public void setCustomFacetSortProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "customFacetSortProvider", value);
    }


    public void setCustomFacetSortProvider(String value)
    {
        setCustomFacetSortProvider(getSession().getSessionContext(), value);
    }


    public String getDisplayName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSolrIndexedProperty.getDisplayName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "displayName");
    }


    public String getDisplayName()
    {
        return getDisplayName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDisplayName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "displayName", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDisplayName()
    {
        return getAllDisplayName(getSession().getSessionContext());
    }


    public void setDisplayName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSolrIndexedProperty.setDisplayName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "displayName", value);
    }


    public void setDisplayName(String value)
    {
        setDisplayName(getSession().getSessionContext(), value);
    }


    public void setAllDisplayName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "displayName", value);
    }


    public void setAllDisplayName(Map<Language, String> value)
    {
        setAllDisplayName(getSession().getSessionContext(), value);
    }


    public String getExportId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "exportId");
    }


    public String getExportId()
    {
        return getExportId(getSession().getSessionContext());
    }


    public void setExportId(SessionContext ctx, String value)
    {
        setProperty(ctx, "exportId", value);
    }


    public void setExportId(String value)
    {
        setExportId(getSession().getSessionContext(), value);
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


    public String getFieldValueProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "fieldValueProvider");
    }


    public String getFieldValueProvider()
    {
        return getFieldValueProvider(getSession().getSessionContext());
    }


    public void setFieldValueProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "fieldValueProvider", value);
    }


    public void setFieldValueProvider(String value)
    {
        setFieldValueProvider(getSession().getSessionContext(), value);
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


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("SolrValueRangeSet");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_MARKMODIFIED);
        }
        return true;
    }


    public Boolean isLocalized(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "localized");
    }


    public Boolean isLocalized()
    {
        return isLocalized(getSession().getSessionContext());
    }


    public boolean isLocalizedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLocalized(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLocalizedAsPrimitive()
    {
        return isLocalizedAsPrimitive(getSession().getSessionContext());
    }


    public void setLocalized(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "localized", value);
    }


    public void setLocalized(Boolean value)
    {
        setLocalized(getSession().getSessionContext(), value);
    }


    public void setLocalized(SessionContext ctx, boolean value)
    {
        setLocalized(ctx, Boolean.valueOf(value));
    }


    public void setLocalized(boolean value)
    {
        setLocalized(getSession().getSessionContext(), value);
    }


    public Boolean isMultiValue(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "multiValue");
    }


    public Boolean isMultiValue()
    {
        return isMultiValue(getSession().getSessionContext());
    }


    public boolean isMultiValueAsPrimitive(SessionContext ctx)
    {
        Boolean value = isMultiValue(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isMultiValueAsPrimitive()
    {
        return isMultiValueAsPrimitive(getSession().getSessionContext());
    }


    public void setMultiValue(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "multiValue", value);
    }


    public void setMultiValue(Boolean value)
    {
        setMultiValue(getSession().getSessionContext(), value);
    }


    public void setMultiValue(SessionContext ctx, boolean value)
    {
        setMultiValue(ctx, Boolean.valueOf(value));
    }


    public void setMultiValue(boolean value)
    {
        setMultiValue(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    protected void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'name' is not changeable", 0);
        }
        setProperty(ctx, "name", value);
    }


    protected void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
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


    public SolrValueRangeSet getRangeSet(SessionContext ctx)
    {
        return (SolrValueRangeSet)getProperty(ctx, "rangeSet");
    }


    public SolrValueRangeSet getRangeSet()
    {
        return getRangeSet(getSession().getSessionContext());
    }


    public void setRangeSet(SessionContext ctx, SolrValueRangeSet value)
    {
        setProperty(ctx, "rangeSet", value);
    }


    public void setRangeSet(SolrValueRangeSet value)
    {
        setRangeSet(getSession().getSessionContext(), value);
    }


    public List<SolrValueRangeSet> getRangeSets(SessionContext ctx)
    {
        List<SolrValueRangeSet> items = getLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION, "SolrValueRangeSet", null,
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<SolrValueRangeSet> getRangeSets()
    {
        return getRangeSets(getSession().getSessionContext());
    }


    public long getRangeSetsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION, "SolrValueRangeSet", null);
    }


    public long getRangeSetsCount()
    {
        return getRangeSetsCount(getSession().getSessionContext());
    }


    public void setRangeSets(SessionContext ctx, List<SolrValueRangeSet> value)
    {
        setLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION, null, value,
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void setRangeSets(List<SolrValueRangeSet> value)
    {
        setRangeSets(getSession().getSessionContext(), value);
    }


    public void addToRangeSets(SessionContext ctx, SolrValueRangeSet value)
    {
        addLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void addToRangeSets(SolrValueRangeSet value)
    {
        addToRangeSets(getSession().getSessionContext(), value);
    }


    public void removeFromRangeSets(SessionContext ctx, SolrValueRangeSet value)
    {
        removeLinkedItems(ctx, true, GeneratedSolrfacetsearchConstants.Relations.SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SOLRINDEXEDPROPERTY2SOLRVALUERANGESETRELATION_MARKMODIFIED));
    }


    public void removeFromRangeSets(SolrValueRangeSet value)
    {
        removeFromRangeSets(getSession().getSessionContext(), value);
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


    public SolrIndexedType getSolrIndexedType(SessionContext ctx)
    {
        return (SolrIndexedType)getProperty(ctx, "solrIndexedType");
    }


    public SolrIndexedType getSolrIndexedType()
    {
        return getSolrIndexedType(getSession().getSessionContext());
    }


    public void setSolrIndexedType(SessionContext ctx, SolrIndexedType value)
    {
        SOLRINDEXEDTYPEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSolrIndexedType(SolrIndexedType value)
    {
        setSolrIndexedType(getSession().getSessionContext(), value);
    }


    Integer getSolrIndexedTypePOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "solrIndexedTypePOS");
    }


    Integer getSolrIndexedTypePOS()
    {
        return getSolrIndexedTypePOS(getSession().getSessionContext());
    }


    int getSolrIndexedTypePOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSolrIndexedTypePOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSolrIndexedTypePOSAsPrimitive()
    {
        return getSolrIndexedTypePOSAsPrimitive(getSession().getSessionContext());
    }


    void setSolrIndexedTypePOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "solrIndexedTypePOS", value);
    }


    void setSolrIndexedTypePOS(Integer value)
    {
        setSolrIndexedTypePOS(getSession().getSessionContext(), value);
    }


    void setSolrIndexedTypePOS(SessionContext ctx, int value)
    {
        setSolrIndexedTypePOS(ctx, Integer.valueOf(value));
    }


    void setSolrIndexedTypePOS(int value)
    {
        setSolrIndexedTypePOS(getSession().getSessionContext(), value);
    }


    public EnumerationValue getSortableType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "sortableType");
    }


    public EnumerationValue getSortableType()
    {
        return getSortableType(getSession().getSessionContext());
    }


    public void setSortableType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "sortableType", value);
    }


    public void setSortableType(EnumerationValue value)
    {
        setSortableType(getSession().getSessionContext(), value);
    }


    public String getTopValuesProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "topValuesProvider");
    }


    public String getTopValuesProvider()
    {
        return getTopValuesProvider(getSession().getSessionContext());
    }


    public void setTopValuesProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "topValuesProvider", value);
    }


    public void setTopValuesProvider(String value)
    {
        setTopValuesProvider(getSession().getSessionContext(), value);
    }


    public EnumerationValue getType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "type");
    }


    public EnumerationValue getType()
    {
        return getType(getSession().getSessionContext());
    }


    public void setType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "type", value);
    }


    public void setType(EnumerationValue value)
    {
        setType(getSession().getSessionContext(), value);
    }


    public Boolean isUseForAutocomplete(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "useForAutocomplete");
    }


    public Boolean isUseForAutocomplete()
    {
        return isUseForAutocomplete(getSession().getSessionContext());
    }


    public boolean isUseForAutocompleteAsPrimitive(SessionContext ctx)
    {
        Boolean value = isUseForAutocomplete(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUseForAutocompleteAsPrimitive()
    {
        return isUseForAutocompleteAsPrimitive(getSession().getSessionContext());
    }


    public void setUseForAutocomplete(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "useForAutocomplete", value);
    }


    public void setUseForAutocomplete(Boolean value)
    {
        setUseForAutocomplete(getSession().getSessionContext(), value);
    }


    public void setUseForAutocomplete(SessionContext ctx, boolean value)
    {
        setUseForAutocomplete(ctx, Boolean.valueOf(value));
    }


    public void setUseForAutocomplete(boolean value)
    {
        setUseForAutocomplete(getSession().getSessionContext(), value);
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


    public Boolean isUseForSpellchecking(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "useForSpellchecking");
    }


    public Boolean isUseForSpellchecking()
    {
        return isUseForSpellchecking(getSession().getSessionContext());
    }


    public boolean isUseForSpellcheckingAsPrimitive(SessionContext ctx)
    {
        Boolean value = isUseForSpellchecking(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUseForSpellcheckingAsPrimitive()
    {
        return isUseForSpellcheckingAsPrimitive(getSession().getSessionContext());
    }


    public void setUseForSpellchecking(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "useForSpellchecking", value);
    }


    public void setUseForSpellchecking(Boolean value)
    {
        setUseForSpellchecking(getSession().getSessionContext(), value);
    }


    public void setUseForSpellchecking(SessionContext ctx, boolean value)
    {
        setUseForSpellchecking(ctx, Boolean.valueOf(value));
    }


    public void setUseForSpellchecking(boolean value)
    {
        setUseForSpellchecking(getSession().getSessionContext(), value);
    }


    public String getValueProviderParameter(SessionContext ctx)
    {
        return (String)getProperty(ctx, "valueProviderParameter");
    }


    public String getValueProviderParameter()
    {
        return getValueProviderParameter(getSession().getSessionContext());
    }


    public void setValueProviderParameter(SessionContext ctx, String value)
    {
        setProperty(ctx, "valueProviderParameter", value);
    }


    public void setValueProviderParameter(String value)
    {
        setValueProviderParameter(getSession().getSessionContext(), value);
    }


    public Map<String, String> getAllValueProviderParameters(SessionContext ctx)
    {
        Map<String, String> map = (Map<String, String>)getProperty(ctx, "valueProviderParameters");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllValueProviderParameters()
    {
        return getAllValueProviderParameters(getSession().getSessionContext());
    }


    public void setAllValueProviderParameters(SessionContext ctx, Map<String, String> value)
    {
        setProperty(ctx, "valueProviderParameters", value);
    }


    public void setAllValueProviderParameters(Map<String, String> value)
    {
        setAllValueProviderParameters(getSession().getSessionContext(), value);
    }


    public Boolean isVisible(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "visible");
    }


    public Boolean isVisible()
    {
        return isVisible(getSession().getSessionContext());
    }


    public boolean isVisibleAsPrimitive(SessionContext ctx)
    {
        Boolean value = isVisible(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isVisibleAsPrimitive()
    {
        return isVisibleAsPrimitive(getSession().getSessionContext());
    }


    public void setVisible(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "visible", value);
    }


    public void setVisible(Boolean value)
    {
        setVisible(getSession().getSessionContext(), value);
    }


    public void setVisible(SessionContext ctx, boolean value)
    {
        setVisible(ctx, Boolean.valueOf(value));
    }


    public void setVisible(boolean value)
    {
        setVisible(getSession().getSessionContext(), value);
    }
}
