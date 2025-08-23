package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.SolrSearchQueryTemplate;
import de.hybris.platform.solrfacetsearch.jalo.SolrSort;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedSolrIndexedType extends GenericItem
{
    public static final String IDENTIFIER = "identifier";
    public static final String TYPE = "type";
    public static final String VARIANT = "variant";
    public static final String IDENTITYPROVIDER = "identityProvider";
    public static final String MODELLOADER = "modelLoader";
    public static final String DEFAULTFIELDVALUEPROVIDER = "defaultFieldValueProvider";
    public static final String VALUESPROVIDER = "valuesProvider";
    public static final String INDEXNAME = "indexName";
    public static final String SOLRRESULTCONVERTER = "solrResultConverter";
    public static final String GROUP = "group";
    public static final String GROUPFIELDNAME = "groupFieldName";
    public static final String GROUPLIMIT = "groupLimit";
    public static final String GROUPFACETS = "groupFacets";
    public static final String LISTENERS = "listeners";
    public static final String CONFIGSET = "configSet";
    public static final String FTSQUERYBUILDER = "ftsQueryBuilder";
    public static final String FTSQUERYBUILDERPARAMETERS = "ftsQueryBuilderParameters";
    public static final String ADDITIONALPARAMETERS = "additionalParameters";
    public static final String SOLRINDEXERQUERIES = "solrIndexerQueries";
    public static final String SOLRINDEXEDPROPERTIES = "solrIndexedProperties";
    public static final String SOLRFACETSEARCHCONFIGPOS = "solrFacetSearchConfigPOS";
    public static final String SOLRFACETSEARCHCONFIG = "solrFacetSearchConfig";
    public static final String SEARCHQUERYTEMPLATES = "searchQueryTemplates";
    public static final String SORTS = "sorts";
    protected static final OneToManyHandler<SolrIndexerQuery> SOLRINDEXERQUERIESHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERQUERY, true, "solrIndexedType", "solrIndexedTypePOS", true, true, 2);
    protected static final OneToManyHandler<SolrIndexedProperty> SOLRINDEXEDPROPERTIESHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXEDPROPERTY, true, "solrIndexedType", "solrIndexedTypePOS", true, true, 2);
    protected static final BidirectionalOneToManyHandler<GeneratedSolrIndexedType> SOLRFACETSEARCHCONFIGHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXEDTYPE, false, "solrFacetSearchConfig", "solrFacetSearchConfigPOS", true, true, 2);
    protected static final OneToManyHandler<SolrSearchQueryTemplate> SEARCHQUERYTEMPLATESHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSEARCHQUERYTEMPLATE, true, "indexedType", "indexedTypePOS", true, true, 0);
    protected static final OneToManyHandler<SolrSort> SORTSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRSORT, true, "indexedType", "indexedTypePOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("identifier", Item.AttributeMode.INITIAL);
        tmp.put("type", Item.AttributeMode.INITIAL);
        tmp.put("variant", Item.AttributeMode.INITIAL);
        tmp.put("identityProvider", Item.AttributeMode.INITIAL);
        tmp.put("modelLoader", Item.AttributeMode.INITIAL);
        tmp.put("defaultFieldValueProvider", Item.AttributeMode.INITIAL);
        tmp.put("valuesProvider", Item.AttributeMode.INITIAL);
        tmp.put("indexName", Item.AttributeMode.INITIAL);
        tmp.put("solrResultConverter", Item.AttributeMode.INITIAL);
        tmp.put("group", Item.AttributeMode.INITIAL);
        tmp.put("groupFieldName", Item.AttributeMode.INITIAL);
        tmp.put("groupLimit", Item.AttributeMode.INITIAL);
        tmp.put("groupFacets", Item.AttributeMode.INITIAL);
        tmp.put("listeners", Item.AttributeMode.INITIAL);
        tmp.put("configSet", Item.AttributeMode.INITIAL);
        tmp.put("ftsQueryBuilder", Item.AttributeMode.INITIAL);
        tmp.put("ftsQueryBuilderParameters", Item.AttributeMode.INITIAL);
        tmp.put("additionalParameters", Item.AttributeMode.INITIAL);
        tmp.put("solrFacetSearchConfigPOS", Item.AttributeMode.INITIAL);
        tmp.put("solrFacetSearchConfig", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Map<String, String> getAllAdditionalParameters(SessionContext ctx)
    {
        Map<String, String> map = (Map<String, String>)getProperty(ctx, "additionalParameters");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllAdditionalParameters()
    {
        return getAllAdditionalParameters(getSession().getSessionContext());
    }


    public void setAllAdditionalParameters(SessionContext ctx, Map<String, String> value)
    {
        setProperty(ctx, "additionalParameters", value);
    }


    public void setAllAdditionalParameters(Map<String, String> value)
    {
        setAllAdditionalParameters(getSession().getSessionContext(), value);
    }


    public String getConfigSet(SessionContext ctx)
    {
        return (String)getProperty(ctx, "configSet");
    }


    public String getConfigSet()
    {
        return getConfigSet(getSession().getSessionContext());
    }


    public void setConfigSet(SessionContext ctx, String value)
    {
        setProperty(ctx, "configSet", value);
    }


    public void setConfigSet(String value)
    {
        setConfigSet(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOLRFACETSEARCHCONFIGHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDefaultFieldValueProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "defaultFieldValueProvider");
    }


    public String getDefaultFieldValueProvider()
    {
        return getDefaultFieldValueProvider(getSession().getSessionContext());
    }


    public void setDefaultFieldValueProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "defaultFieldValueProvider", value);
    }


    public void setDefaultFieldValueProvider(String value)
    {
        setDefaultFieldValueProvider(getSession().getSessionContext(), value);
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


    public String getGroupFieldName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "groupFieldName");
    }


    public String getGroupFieldName()
    {
        return getGroupFieldName(getSession().getSessionContext());
    }


    public void setGroupFieldName(SessionContext ctx, String value)
    {
        setProperty(ctx, "groupFieldName", value);
    }


    public void setGroupFieldName(String value)
    {
        setGroupFieldName(getSession().getSessionContext(), value);
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


    public String getIdentifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "identifier");
    }


    public String getIdentifier()
    {
        return getIdentifier(getSession().getSessionContext());
    }


    protected void setIdentifier(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'identifier' is not changeable", 0);
        }
        setProperty(ctx, "identifier", value);
    }


    protected void setIdentifier(String value)
    {
        setIdentifier(getSession().getSessionContext(), value);
    }


    public String getIdentityProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "identityProvider");
    }


    public String getIdentityProvider()
    {
        return getIdentityProvider(getSession().getSessionContext());
    }


    public void setIdentityProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "identityProvider", value);
    }


    public void setIdentityProvider(String value)
    {
        setIdentityProvider(getSession().getSessionContext(), value);
    }


    public String getIndexName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexName");
    }


    public String getIndexName()
    {
        return getIndexName(getSession().getSessionContext());
    }


    public void setIndexName(SessionContext ctx, String value)
    {
        setProperty(ctx, "indexName", value);
    }


    public void setIndexName(String value)
    {
        setIndexName(getSession().getSessionContext(), value);
    }


    public Collection<String> getListeners(SessionContext ctx)
    {
        Collection<String> coll = (Collection<String>)getProperty(ctx, "listeners");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<String> getListeners()
    {
        return getListeners(getSession().getSessionContext());
    }


    public void setListeners(SessionContext ctx, Collection<String> value)
    {
        setProperty(ctx, "listeners", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setListeners(Collection<String> value)
    {
        setListeners(getSession().getSessionContext(), value);
    }


    public String getModelLoader(SessionContext ctx)
    {
        return (String)getProperty(ctx, "modelLoader");
    }


    public String getModelLoader()
    {
        return getModelLoader(getSession().getSessionContext());
    }


    public void setModelLoader(SessionContext ctx, String value)
    {
        setProperty(ctx, "modelLoader", value);
    }


    public void setModelLoader(String value)
    {
        setModelLoader(getSession().getSessionContext(), value);
    }


    public Collection<SolrSearchQueryTemplate> getSearchQueryTemplates(SessionContext ctx)
    {
        return SEARCHQUERYTEMPLATESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<SolrSearchQueryTemplate> getSearchQueryTemplates()
    {
        return getSearchQueryTemplates(getSession().getSessionContext());
    }


    public void setSearchQueryTemplates(SessionContext ctx, Collection<SolrSearchQueryTemplate> value)
    {
        SEARCHQUERYTEMPLATESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSearchQueryTemplates(Collection<SolrSearchQueryTemplate> value)
    {
        setSearchQueryTemplates(getSession().getSessionContext(), value);
    }


    public void addToSearchQueryTemplates(SessionContext ctx, SolrSearchQueryTemplate value)
    {
        SEARCHQUERYTEMPLATESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSearchQueryTemplates(SolrSearchQueryTemplate value)
    {
        addToSearchQueryTemplates(getSession().getSessionContext(), value);
    }


    public void removeFromSearchQueryTemplates(SessionContext ctx, SolrSearchQueryTemplate value)
    {
        SEARCHQUERYTEMPLATESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSearchQueryTemplates(SolrSearchQueryTemplate value)
    {
        removeFromSearchQueryTemplates(getSession().getSessionContext(), value);
    }


    public SolrFacetSearchConfig getSolrFacetSearchConfig(SessionContext ctx)
    {
        return (SolrFacetSearchConfig)getProperty(ctx, "solrFacetSearchConfig");
    }


    public SolrFacetSearchConfig getSolrFacetSearchConfig()
    {
        return getSolrFacetSearchConfig(getSession().getSessionContext());
    }


    public void setSolrFacetSearchConfig(SessionContext ctx, SolrFacetSearchConfig value)
    {
        SOLRFACETSEARCHCONFIGHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSolrFacetSearchConfig(SolrFacetSearchConfig value)
    {
        setSolrFacetSearchConfig(getSession().getSessionContext(), value);
    }


    Integer getSolrFacetSearchConfigPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "solrFacetSearchConfigPOS");
    }


    Integer getSolrFacetSearchConfigPOS()
    {
        return getSolrFacetSearchConfigPOS(getSession().getSessionContext());
    }


    int getSolrFacetSearchConfigPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSolrFacetSearchConfigPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSolrFacetSearchConfigPOSAsPrimitive()
    {
        return getSolrFacetSearchConfigPOSAsPrimitive(getSession().getSessionContext());
    }


    void setSolrFacetSearchConfigPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "solrFacetSearchConfigPOS", value);
    }


    void setSolrFacetSearchConfigPOS(Integer value)
    {
        setSolrFacetSearchConfigPOS(getSession().getSessionContext(), value);
    }


    void setSolrFacetSearchConfigPOS(SessionContext ctx, int value)
    {
        setSolrFacetSearchConfigPOS(ctx, Integer.valueOf(value));
    }


    void setSolrFacetSearchConfigPOS(int value)
    {
        setSolrFacetSearchConfigPOS(getSession().getSessionContext(), value);
    }


    public List<SolrIndexedProperty> getSolrIndexedProperties(SessionContext ctx)
    {
        return (List<SolrIndexedProperty>)SOLRINDEXEDPROPERTIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrIndexedProperty> getSolrIndexedProperties()
    {
        return getSolrIndexedProperties(getSession().getSessionContext());
    }


    public void setSolrIndexedProperties(SessionContext ctx, List<SolrIndexedProperty> value)
    {
        SOLRINDEXEDPROPERTIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSolrIndexedProperties(List<SolrIndexedProperty> value)
    {
        setSolrIndexedProperties(getSession().getSessionContext(), value);
    }


    public void addToSolrIndexedProperties(SessionContext ctx, SolrIndexedProperty value)
    {
        SOLRINDEXEDPROPERTIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSolrIndexedProperties(SolrIndexedProperty value)
    {
        addToSolrIndexedProperties(getSession().getSessionContext(), value);
    }


    public void removeFromSolrIndexedProperties(SessionContext ctx, SolrIndexedProperty value)
    {
        SOLRINDEXEDPROPERTIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSolrIndexedProperties(SolrIndexedProperty value)
    {
        removeFromSolrIndexedProperties(getSession().getSessionContext(), value);
    }


    public List<SolrIndexerQuery> getSolrIndexerQueries(SessionContext ctx)
    {
        return (List<SolrIndexerQuery>)SOLRINDEXERQUERIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrIndexerQuery> getSolrIndexerQueries()
    {
        return getSolrIndexerQueries(getSession().getSessionContext());
    }


    public void setSolrIndexerQueries(SessionContext ctx, List<SolrIndexerQuery> value)
    {
        SOLRINDEXERQUERIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSolrIndexerQueries(List<SolrIndexerQuery> value)
    {
        setSolrIndexerQueries(getSession().getSessionContext(), value);
    }


    public void addToSolrIndexerQueries(SessionContext ctx, SolrIndexerQuery value)
    {
        SOLRINDEXERQUERIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSolrIndexerQueries(SolrIndexerQuery value)
    {
        addToSolrIndexerQueries(getSession().getSessionContext(), value);
    }


    public void removeFromSolrIndexerQueries(SessionContext ctx, SolrIndexerQuery value)
    {
        SOLRINDEXERQUERIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSolrIndexerQueries(SolrIndexerQuery value)
    {
        removeFromSolrIndexerQueries(getSession().getSessionContext(), value);
    }


    public String getSolrResultConverter(SessionContext ctx)
    {
        return (String)getProperty(ctx, "solrResultConverter");
    }


    public String getSolrResultConverter()
    {
        return getSolrResultConverter(getSession().getSessionContext());
    }


    public void setSolrResultConverter(SessionContext ctx, String value)
    {
        setProperty(ctx, "solrResultConverter", value);
    }


    public void setSolrResultConverter(String value)
    {
        setSolrResultConverter(getSession().getSessionContext(), value);
    }


    public List<SolrSort> getSorts(SessionContext ctx)
    {
        return (List<SolrSort>)SORTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrSort> getSorts()
    {
        return getSorts(getSession().getSessionContext());
    }


    public void setSorts(SessionContext ctx, List<SolrSort> value)
    {
        SORTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSorts(List<SolrSort> value)
    {
        setSorts(getSession().getSessionContext(), value);
    }


    public void addToSorts(SessionContext ctx, SolrSort value)
    {
        SORTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSorts(SolrSort value)
    {
        addToSorts(getSession().getSessionContext(), value);
    }


    public void removeFromSorts(SessionContext ctx, SolrSort value)
    {
        SORTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSorts(SolrSort value)
    {
        removeFromSorts(getSession().getSessionContext(), value);
    }


    public ComposedType getType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "type");
    }


    public ComposedType getType()
    {
        return getType(getSession().getSessionContext());
    }


    public void setType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "type", value);
    }


    public void setType(ComposedType value)
    {
        setType(getSession().getSessionContext(), value);
    }


    public String getValuesProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "valuesProvider");
    }


    public String getValuesProvider()
    {
        return getValuesProvider(getSession().getSessionContext());
    }


    public void setValuesProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "valuesProvider", value);
    }


    public void setValuesProvider(String value)
    {
        setValuesProvider(getSession().getSessionContext(), value);
    }


    public Boolean isVariant(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "variant");
    }


    public Boolean isVariant()
    {
        return isVariant(getSession().getSessionContext());
    }


    public boolean isVariantAsPrimitive(SessionContext ctx)
    {
        Boolean value = isVariant(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isVariantAsPrimitive()
    {
        return isVariantAsPrimitive(getSession().getSessionContext());
    }


    public void setVariant(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "variant", value);
    }


    public void setVariant(Boolean value)
    {
        setVariant(getSession().getSessionContext(), value);
    }


    public void setVariant(SessionContext ctx, boolean value)
    {
        setVariant(ctx, Boolean.valueOf(value));
    }


    public void setVariant(boolean value)
    {
        setVariant(getSession().getSessionContext(), value);
    }
}
