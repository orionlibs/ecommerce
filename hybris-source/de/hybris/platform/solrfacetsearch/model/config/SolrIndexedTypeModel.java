package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.SolrSearchQueryTemplateModel;
import de.hybris.platform.solrfacetsearch.model.SolrSortModel;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SolrIndexedTypeModel extends ItemModel
{
    public static final String _TYPECODE = "SolrIndexedType";
    public static final String _SOLRFACETSEARCHCONFIG2SOLRINDEXEDTYPE = "SolrFacetSearchConfig2SolrIndexedType";
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


    public SolrIndexedTypeModel()
    {
    }


    public SolrIndexedTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexedTypeModel(String _identifier, ComposedTypeModel _type)
    {
        setIdentifier(_identifier);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexedTypeModel(String _identifier, ItemModel _owner, ComposedTypeModel _type)
    {
        setIdentifier(_identifier);
        setOwner(_owner);
        setType(_type);
    }


    @Accessor(qualifier = "additionalParameters", type = Accessor.Type.GETTER)
    public Map<String, String> getAdditionalParameters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("additionalParameters");
    }


    @Accessor(qualifier = "configSet", type = Accessor.Type.GETTER)
    public String getConfigSet()
    {
        return (String)getPersistenceContext().getPropertyValue("configSet");
    }


    @Accessor(qualifier = "defaultFieldValueProvider", type = Accessor.Type.GETTER)
    public String getDefaultFieldValueProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("defaultFieldValueProvider");
    }


    @Accessor(qualifier = "ftsQueryBuilder", type = Accessor.Type.GETTER)
    public String getFtsQueryBuilder()
    {
        return (String)getPersistenceContext().getPropertyValue("ftsQueryBuilder");
    }


    @Accessor(qualifier = "ftsQueryBuilderParameters", type = Accessor.Type.GETTER)
    public Map<String, String> getFtsQueryBuilderParameters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("ftsQueryBuilderParameters");
    }


    @Accessor(qualifier = "groupFieldName", type = Accessor.Type.GETTER)
    public String getGroupFieldName()
    {
        return (String)getPersistenceContext().getPropertyValue("groupFieldName");
    }


    @Accessor(qualifier = "groupLimit", type = Accessor.Type.GETTER)
    public Integer getGroupLimit()
    {
        return (Integer)getPersistenceContext().getPropertyValue("groupLimit");
    }


    @Accessor(qualifier = "identifier", type = Accessor.Type.GETTER)
    public String getIdentifier()
    {
        return (String)getPersistenceContext().getPropertyValue("identifier");
    }


    @Accessor(qualifier = "identityProvider", type = Accessor.Type.GETTER)
    public String getIdentityProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("identityProvider");
    }


    @Accessor(qualifier = "indexName", type = Accessor.Type.GETTER)
    public String getIndexName()
    {
        return (String)getPersistenceContext().getPropertyValue("indexName");
    }


    @Accessor(qualifier = "listeners", type = Accessor.Type.GETTER)
    public Collection<String> getListeners()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("listeners");
    }


    @Accessor(qualifier = "modelLoader", type = Accessor.Type.GETTER)
    public String getModelLoader()
    {
        return (String)getPersistenceContext().getPropertyValue("modelLoader");
    }


    @Accessor(qualifier = "searchQueryTemplates", type = Accessor.Type.GETTER)
    public Collection<SolrSearchQueryTemplateModel> getSearchQueryTemplates()
    {
        return (Collection<SolrSearchQueryTemplateModel>)getPersistenceContext().getPropertyValue("searchQueryTemplates");
    }


    @Accessor(qualifier = "solrFacetSearchConfig", type = Accessor.Type.GETTER)
    public SolrFacetSearchConfigModel getSolrFacetSearchConfig()
    {
        return (SolrFacetSearchConfigModel)getPersistenceContext().getPropertyValue("solrFacetSearchConfig");
    }


    @Accessor(qualifier = "solrIndexedProperties", type = Accessor.Type.GETTER)
    public List<SolrIndexedPropertyModel> getSolrIndexedProperties()
    {
        return (List<SolrIndexedPropertyModel>)getPersistenceContext().getPropertyValue("solrIndexedProperties");
    }


    @Accessor(qualifier = "solrIndexerQueries", type = Accessor.Type.GETTER)
    public List<SolrIndexerQueryModel> getSolrIndexerQueries()
    {
        return (List<SolrIndexerQueryModel>)getPersistenceContext().getPropertyValue("solrIndexerQueries");
    }


    @Accessor(qualifier = "solrResultConverter", type = Accessor.Type.GETTER)
    public String getSolrResultConverter()
    {
        return (String)getPersistenceContext().getPropertyValue("solrResultConverter");
    }


    @Accessor(qualifier = "sorts", type = Accessor.Type.GETTER)
    public List<SolrSortModel> getSorts()
    {
        return (List<SolrSortModel>)getPersistenceContext().getPropertyValue("sorts");
    }


    @Accessor(qualifier = "type", type = Accessor.Type.GETTER)
    public ComposedTypeModel getType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("type");
    }


    @Accessor(qualifier = "valuesProvider", type = Accessor.Type.GETTER)
    public String getValuesProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("valuesProvider");
    }


    @Accessor(qualifier = "group", type = Accessor.Type.GETTER)
    public boolean isGroup()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("group"));
    }


    @Accessor(qualifier = "groupFacets", type = Accessor.Type.GETTER)
    public boolean isGroupFacets()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("groupFacets"));
    }


    @Accessor(qualifier = "variant", type = Accessor.Type.GETTER)
    public boolean isVariant()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("variant"));
    }


    @Accessor(qualifier = "additionalParameters", type = Accessor.Type.SETTER)
    public void setAdditionalParameters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("additionalParameters", value);
    }


    @Accessor(qualifier = "configSet", type = Accessor.Type.SETTER)
    public void setConfigSet(String value)
    {
        getPersistenceContext().setPropertyValue("configSet", value);
    }


    @Accessor(qualifier = "defaultFieldValueProvider", type = Accessor.Type.SETTER)
    public void setDefaultFieldValueProvider(String value)
    {
        getPersistenceContext().setPropertyValue("defaultFieldValueProvider", value);
    }


    @Accessor(qualifier = "ftsQueryBuilder", type = Accessor.Type.SETTER)
    public void setFtsQueryBuilder(String value)
    {
        getPersistenceContext().setPropertyValue("ftsQueryBuilder", value);
    }


    @Accessor(qualifier = "ftsQueryBuilderParameters", type = Accessor.Type.SETTER)
    public void setFtsQueryBuilderParameters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("ftsQueryBuilderParameters", value);
    }


    @Accessor(qualifier = "group", type = Accessor.Type.SETTER)
    public void setGroup(boolean value)
    {
        getPersistenceContext().setPropertyValue("group", toObject(value));
    }


    @Accessor(qualifier = "groupFacets", type = Accessor.Type.SETTER)
    public void setGroupFacets(boolean value)
    {
        getPersistenceContext().setPropertyValue("groupFacets", toObject(value));
    }


    @Accessor(qualifier = "groupFieldName", type = Accessor.Type.SETTER)
    public void setGroupFieldName(String value)
    {
        getPersistenceContext().setPropertyValue("groupFieldName", value);
    }


    @Accessor(qualifier = "groupLimit", type = Accessor.Type.SETTER)
    public void setGroupLimit(Integer value)
    {
        getPersistenceContext().setPropertyValue("groupLimit", value);
    }


    @Accessor(qualifier = "identifier", type = Accessor.Type.SETTER)
    public void setIdentifier(String value)
    {
        getPersistenceContext().setPropertyValue("identifier", value);
    }


    @Accessor(qualifier = "identityProvider", type = Accessor.Type.SETTER)
    public void setIdentityProvider(String value)
    {
        getPersistenceContext().setPropertyValue("identityProvider", value);
    }


    @Accessor(qualifier = "indexName", type = Accessor.Type.SETTER)
    public void setIndexName(String value)
    {
        getPersistenceContext().setPropertyValue("indexName", value);
    }


    @Accessor(qualifier = "listeners", type = Accessor.Type.SETTER)
    public void setListeners(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("listeners", value);
    }


    @Accessor(qualifier = "modelLoader", type = Accessor.Type.SETTER)
    public void setModelLoader(String value)
    {
        getPersistenceContext().setPropertyValue("modelLoader", value);
    }


    @Accessor(qualifier = "searchQueryTemplates", type = Accessor.Type.SETTER)
    public void setSearchQueryTemplates(Collection<SolrSearchQueryTemplateModel> value)
    {
        getPersistenceContext().setPropertyValue("searchQueryTemplates", value);
    }


    @Accessor(qualifier = "solrFacetSearchConfig", type = Accessor.Type.SETTER)
    public void setSolrFacetSearchConfig(SolrFacetSearchConfigModel value)
    {
        getPersistenceContext().setPropertyValue("solrFacetSearchConfig", value);
    }


    @Accessor(qualifier = "solrIndexedProperties", type = Accessor.Type.SETTER)
    public void setSolrIndexedProperties(List<SolrIndexedPropertyModel> value)
    {
        getPersistenceContext().setPropertyValue("solrIndexedProperties", value);
    }


    @Accessor(qualifier = "solrIndexerQueries", type = Accessor.Type.SETTER)
    public void setSolrIndexerQueries(List<SolrIndexerQueryModel> value)
    {
        getPersistenceContext().setPropertyValue("solrIndexerQueries", value);
    }


    @Accessor(qualifier = "solrResultConverter", type = Accessor.Type.SETTER)
    public void setSolrResultConverter(String value)
    {
        getPersistenceContext().setPropertyValue("solrResultConverter", value);
    }


    @Accessor(qualifier = "sorts", type = Accessor.Type.SETTER)
    public void setSorts(List<SolrSortModel> value)
    {
        getPersistenceContext().setPropertyValue("sorts", value);
    }


    @Accessor(qualifier = "type", type = Accessor.Type.SETTER)
    public void setType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("type", value);
    }


    @Accessor(qualifier = "valuesProvider", type = Accessor.Type.SETTER)
    public void setValuesProvider(String value)
    {
        getPersistenceContext().setPropertyValue("valuesProvider", value);
    }


    @Accessor(qualifier = "variant", type = Accessor.Type.SETTER)
    public void setVariant(boolean value)
    {
        getPersistenceContext().setPropertyValue("variant", toObject(value));
    }
}
