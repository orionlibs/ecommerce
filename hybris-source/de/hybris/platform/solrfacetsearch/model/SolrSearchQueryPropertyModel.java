package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.SolrIndexedPropertyFacetType;
import de.hybris.platform.solrfacetsearch.enums.SolrWildcardType;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

public class SolrSearchQueryPropertyModel extends ItemModel
{
    public static final String _TYPECODE = "SolrSearchQueryProperty";
    public static final String _SOLRINDEXEDPROPERTY2SOLRSEARCHQUERYPROPERTY = "SolrIndexedProperty2SolrSearchQueryProperty";
    public static final String _SOLRSEARCHQUERYTEMPLATE2SOLRSEARCHQUERYPROPERTY = "SolrSearchQueryTemplate2SolrSearchQueryProperty";
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


    public SolrSearchQueryPropertyModel()
    {
    }


    public SolrSearchQueryPropertyModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSearchQueryPropertyModel(SolrIndexedPropertyModel _indexedProperty, SolrSearchQueryTemplateModel _searchQueryTemplate)
    {
        setIndexedProperty(_indexedProperty);
        setSearchQueryTemplate(_searchQueryTemplate);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrSearchQueryPropertyModel(SolrIndexedPropertyModel _indexedProperty, ItemModel _owner, SolrSearchQueryTemplateModel _searchQueryTemplate)
    {
        setIndexedProperty(_indexedProperty);
        setOwner(_owner);
        setSearchQueryTemplate(_searchQueryTemplate);
    }


    @Accessor(qualifier = "facetDisplayNameProvider", type = Accessor.Type.GETTER)
    public String getFacetDisplayNameProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("facetDisplayNameProvider");
    }


    @Accessor(qualifier = "facetSortProvider", type = Accessor.Type.GETTER)
    public String getFacetSortProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("facetSortProvider");
    }


    @Accessor(qualifier = "facetTopValuesProvider", type = Accessor.Type.GETTER)
    public String getFacetTopValuesProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("facetTopValuesProvider");
    }


    @Accessor(qualifier = "facetType", type = Accessor.Type.GETTER)
    public SolrIndexedPropertyFacetType getFacetType()
    {
        return (SolrIndexedPropertyFacetType)getPersistenceContext().getPropertyValue("facetType");
    }


    @Accessor(qualifier = "ftsFuzzyQueryBoost", type = Accessor.Type.GETTER)
    public Float getFtsFuzzyQueryBoost()
    {
        return (Float)getPersistenceContext().getPropertyValue("ftsFuzzyQueryBoost");
    }


    @Accessor(qualifier = "ftsFuzzyQueryFuzziness", type = Accessor.Type.GETTER)
    public Integer getFtsFuzzyQueryFuzziness()
    {
        return (Integer)getPersistenceContext().getPropertyValue("ftsFuzzyQueryFuzziness");
    }


    @Accessor(qualifier = "ftsFuzzyQueryMinTermLength", type = Accessor.Type.GETTER)
    public Integer getFtsFuzzyQueryMinTermLength()
    {
        return (Integer)getPersistenceContext().getPropertyValue("ftsFuzzyQueryMinTermLength");
    }


    @Accessor(qualifier = "ftsPhraseQueryBoost", type = Accessor.Type.GETTER)
    public Float getFtsPhraseQueryBoost()
    {
        return (Float)getPersistenceContext().getPropertyValue("ftsPhraseQueryBoost");
    }


    @Accessor(qualifier = "ftsPhraseQuerySlop", type = Accessor.Type.GETTER)
    public Float getFtsPhraseQuerySlop()
    {
        return (Float)getPersistenceContext().getPropertyValue("ftsPhraseQuerySlop");
    }


    @Accessor(qualifier = "ftsQueryBoost", type = Accessor.Type.GETTER)
    public Float getFtsQueryBoost()
    {
        return (Float)getPersistenceContext().getPropertyValue("ftsQueryBoost");
    }


    @Accessor(qualifier = "ftsQueryMinTermLength", type = Accessor.Type.GETTER)
    public Integer getFtsQueryMinTermLength()
    {
        return (Integer)getPersistenceContext().getPropertyValue("ftsQueryMinTermLength");
    }


    @Accessor(qualifier = "ftsWildcardQueryBoost", type = Accessor.Type.GETTER)
    public Float getFtsWildcardQueryBoost()
    {
        return (Float)getPersistenceContext().getPropertyValue("ftsWildcardQueryBoost");
    }


    @Accessor(qualifier = "ftsWildcardQueryMinTermLength", type = Accessor.Type.GETTER)
    public Integer getFtsWildcardQueryMinTermLength()
    {
        return (Integer)getPersistenceContext().getPropertyValue("ftsWildcardQueryMinTermLength");
    }


    @Accessor(qualifier = "ftsWildcardQueryType", type = Accessor.Type.GETTER)
    public SolrWildcardType getFtsWildcardQueryType()
    {
        return (SolrWildcardType)getPersistenceContext().getPropertyValue("ftsWildcardQueryType");
    }


    @Accessor(qualifier = "indexedProperty", type = Accessor.Type.GETTER)
    public SolrIndexedPropertyModel getIndexedProperty()
    {
        return (SolrIndexedPropertyModel)getPersistenceContext().getPropertyValue("indexedProperty");
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        return (Integer)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "searchQueryTemplate", type = Accessor.Type.GETTER)
    public SolrSearchQueryTemplateModel getSearchQueryTemplate()
    {
        return (SolrSearchQueryTemplateModel)getPersistenceContext().getPropertyValue("searchQueryTemplate");
    }


    @Accessor(qualifier = "useForHighlighting", type = Accessor.Type.GETTER)
    public Boolean getUseForHighlighting()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("useForHighlighting");
    }


    @Accessor(qualifier = "facet", type = Accessor.Type.GETTER)
    public boolean isFacet()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("facet"));
    }


    @Accessor(qualifier = "ftsFuzzyQuery", type = Accessor.Type.GETTER)
    public boolean isFtsFuzzyQuery()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("ftsFuzzyQuery"));
    }


    @Accessor(qualifier = "ftsPhraseQuery", type = Accessor.Type.GETTER)
    public boolean isFtsPhraseQuery()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("ftsPhraseQuery"));
    }


    @Accessor(qualifier = "ftsQuery", type = Accessor.Type.GETTER)
    public boolean isFtsQuery()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("ftsQuery"));
    }


    @Accessor(qualifier = "ftsWildcardQuery", type = Accessor.Type.GETTER)
    public boolean isFtsWildcardQuery()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("ftsWildcardQuery"));
    }


    @Accessor(qualifier = "includeInResponse", type = Accessor.Type.GETTER)
    public boolean isIncludeInResponse()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("includeInResponse"));
    }


    @Accessor(qualifier = "facet", type = Accessor.Type.SETTER)
    public void setFacet(boolean value)
    {
        getPersistenceContext().setPropertyValue("facet", toObject(value));
    }


    @Accessor(qualifier = "facetDisplayNameProvider", type = Accessor.Type.SETTER)
    public void setFacetDisplayNameProvider(String value)
    {
        getPersistenceContext().setPropertyValue("facetDisplayNameProvider", value);
    }


    @Accessor(qualifier = "facetSortProvider", type = Accessor.Type.SETTER)
    public void setFacetSortProvider(String value)
    {
        getPersistenceContext().setPropertyValue("facetSortProvider", value);
    }


    @Accessor(qualifier = "facetTopValuesProvider", type = Accessor.Type.SETTER)
    public void setFacetTopValuesProvider(String value)
    {
        getPersistenceContext().setPropertyValue("facetTopValuesProvider", value);
    }


    @Accessor(qualifier = "facetType", type = Accessor.Type.SETTER)
    public void setFacetType(SolrIndexedPropertyFacetType value)
    {
        getPersistenceContext().setPropertyValue("facetType", value);
    }


    @Accessor(qualifier = "ftsFuzzyQuery", type = Accessor.Type.SETTER)
    public void setFtsFuzzyQuery(boolean value)
    {
        getPersistenceContext().setPropertyValue("ftsFuzzyQuery", toObject(value));
    }


    @Accessor(qualifier = "ftsFuzzyQueryBoost", type = Accessor.Type.SETTER)
    public void setFtsFuzzyQueryBoost(Float value)
    {
        getPersistenceContext().setPropertyValue("ftsFuzzyQueryBoost", value);
    }


    @Accessor(qualifier = "ftsFuzzyQueryFuzziness", type = Accessor.Type.SETTER)
    public void setFtsFuzzyQueryFuzziness(Integer value)
    {
        getPersistenceContext().setPropertyValue("ftsFuzzyQueryFuzziness", value);
    }


    @Accessor(qualifier = "ftsFuzzyQueryMinTermLength", type = Accessor.Type.SETTER)
    public void setFtsFuzzyQueryMinTermLength(Integer value)
    {
        getPersistenceContext().setPropertyValue("ftsFuzzyQueryMinTermLength", value);
    }


    @Accessor(qualifier = "ftsPhraseQuery", type = Accessor.Type.SETTER)
    public void setFtsPhraseQuery(boolean value)
    {
        getPersistenceContext().setPropertyValue("ftsPhraseQuery", toObject(value));
    }


    @Accessor(qualifier = "ftsPhraseQueryBoost", type = Accessor.Type.SETTER)
    public void setFtsPhraseQueryBoost(Float value)
    {
        getPersistenceContext().setPropertyValue("ftsPhraseQueryBoost", value);
    }


    @Accessor(qualifier = "ftsPhraseQuerySlop", type = Accessor.Type.SETTER)
    public void setFtsPhraseQuerySlop(Float value)
    {
        getPersistenceContext().setPropertyValue("ftsPhraseQuerySlop", value);
    }


    @Accessor(qualifier = "ftsQuery", type = Accessor.Type.SETTER)
    public void setFtsQuery(boolean value)
    {
        getPersistenceContext().setPropertyValue("ftsQuery", toObject(value));
    }


    @Accessor(qualifier = "ftsQueryBoost", type = Accessor.Type.SETTER)
    public void setFtsQueryBoost(Float value)
    {
        getPersistenceContext().setPropertyValue("ftsQueryBoost", value);
    }


    @Accessor(qualifier = "ftsQueryMinTermLength", type = Accessor.Type.SETTER)
    public void setFtsQueryMinTermLength(Integer value)
    {
        getPersistenceContext().setPropertyValue("ftsQueryMinTermLength", value);
    }


    @Accessor(qualifier = "ftsWildcardQuery", type = Accessor.Type.SETTER)
    public void setFtsWildcardQuery(boolean value)
    {
        getPersistenceContext().setPropertyValue("ftsWildcardQuery", toObject(value));
    }


    @Accessor(qualifier = "ftsWildcardQueryBoost", type = Accessor.Type.SETTER)
    public void setFtsWildcardQueryBoost(Float value)
    {
        getPersistenceContext().setPropertyValue("ftsWildcardQueryBoost", value);
    }


    @Accessor(qualifier = "ftsWildcardQueryMinTermLength", type = Accessor.Type.SETTER)
    public void setFtsWildcardQueryMinTermLength(Integer value)
    {
        getPersistenceContext().setPropertyValue("ftsWildcardQueryMinTermLength", value);
    }


    @Accessor(qualifier = "ftsWildcardQueryType", type = Accessor.Type.SETTER)
    public void setFtsWildcardQueryType(SolrWildcardType value)
    {
        getPersistenceContext().setPropertyValue("ftsWildcardQueryType", value);
    }


    @Accessor(qualifier = "includeInResponse", type = Accessor.Type.SETTER)
    public void setIncludeInResponse(boolean value)
    {
        getPersistenceContext().setPropertyValue("includeInResponse", toObject(value));
    }


    @Accessor(qualifier = "indexedProperty", type = Accessor.Type.SETTER)
    public void setIndexedProperty(SolrIndexedPropertyModel value)
    {
        getPersistenceContext().setPropertyValue("indexedProperty", value);
    }


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "searchQueryTemplate", type = Accessor.Type.SETTER)
    public void setSearchQueryTemplate(SolrSearchQueryTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("searchQueryTemplate", value);
    }


    @Accessor(qualifier = "useForHighlighting", type = Accessor.Type.SETTER)
    public void setUseForHighlighting(Boolean value)
    {
        getPersistenceContext().setPropertyValue("useForHighlighting", value);
    }
}
