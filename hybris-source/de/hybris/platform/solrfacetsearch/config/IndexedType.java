package de.hybris.platform.solrfacetsearch.config;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndexedType implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String identifier;
    private Map<String, IndexedProperty> indexedProperties;
    private Map<IndexOperation, IndexedTypeFlexibleSearchQuery> flexibleSearchQueries;
    private ComposedTypeModel composedType;
    private boolean variant;
    private boolean staged;
    private String identityProvider;
    private String modelLoader;
    private String indexName;
    private String indexNameFromConfig;
    private Set<String> typeFacets;
    private String defaultFieldValueProvider;
    private String fieldsValuesProvider;
    private String solrResultConverter;
    private String code;
    private String uniqueIndexedTypeCode;
    private boolean group;
    private String groupFieldName;
    private int groupLimit;
    private boolean groupFacets;
    private Collection<String> listeners;
    private String configSet;
    private String ftsQueryBuilder;
    private Map<String, String> ftsQueryBuilderParameters;
    private Map<String, SearchQueryTemplate> searchQueryTemplates;
    private Map<String, String> additionalParameters;
    private List<IndexedTypeSort> sorts;
    private Map<String, IndexedTypeSort> sortsByCode;


    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    public String getIdentifier()
    {
        return this.identifier;
    }


    public void setIndexedProperties(Map<String, IndexedProperty> indexedProperties)
    {
        this.indexedProperties = indexedProperties;
    }


    public Map<String, IndexedProperty> getIndexedProperties()
    {
        return this.indexedProperties;
    }


    public void setFlexibleSearchQueries(Map<IndexOperation, IndexedTypeFlexibleSearchQuery> flexibleSearchQueries)
    {
        this.flexibleSearchQueries = flexibleSearchQueries;
    }


    public Map<IndexOperation, IndexedTypeFlexibleSearchQuery> getFlexibleSearchQueries()
    {
        return this.flexibleSearchQueries;
    }


    public void setComposedType(ComposedTypeModel composedType)
    {
        this.composedType = composedType;
    }


    public ComposedTypeModel getComposedType()
    {
        return this.composedType;
    }


    public void setVariant(boolean variant)
    {
        this.variant = variant;
    }


    public boolean isVariant()
    {
        return this.variant;
    }


    public void setStaged(boolean staged)
    {
        this.staged = staged;
    }


    public boolean isStaged()
    {
        return this.staged;
    }


    public void setIdentityProvider(String identityProvider)
    {
        this.identityProvider = identityProvider;
    }


    public String getIdentityProvider()
    {
        return this.identityProvider;
    }


    public void setModelLoader(String modelLoader)
    {
        this.modelLoader = modelLoader;
    }


    public String getModelLoader()
    {
        return this.modelLoader;
    }


    public void setIndexName(String indexName)
    {
        this.indexName = indexName;
    }


    public String getIndexName()
    {
        return this.indexName;
    }


    public void setIndexNameFromConfig(String indexNameFromConfig)
    {
        this.indexNameFromConfig = indexNameFromConfig;
    }


    public String getIndexNameFromConfig()
    {
        return this.indexNameFromConfig;
    }


    public void setTypeFacets(Set<String> typeFacets)
    {
        this.typeFacets = typeFacets;
    }


    public Set<String> getTypeFacets()
    {
        return this.typeFacets;
    }


    public void setDefaultFieldValueProvider(String defaultFieldValueProvider)
    {
        this.defaultFieldValueProvider = defaultFieldValueProvider;
    }


    public String getDefaultFieldValueProvider()
    {
        return this.defaultFieldValueProvider;
    }


    public void setFieldsValuesProvider(String fieldsValuesProvider)
    {
        this.fieldsValuesProvider = fieldsValuesProvider;
    }


    public String getFieldsValuesProvider()
    {
        return this.fieldsValuesProvider;
    }


    public void setSolrResultConverter(String solrResultConverter)
    {
        this.solrResultConverter = solrResultConverter;
    }


    public String getSolrResultConverter()
    {
        return this.solrResultConverter;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setUniqueIndexedTypeCode(String uniqueIndexedTypeCode)
    {
        this.uniqueIndexedTypeCode = uniqueIndexedTypeCode;
    }


    public String getUniqueIndexedTypeCode()
    {
        return this.uniqueIndexedTypeCode;
    }


    public void setGroup(boolean group)
    {
        this.group = group;
    }


    public boolean isGroup()
    {
        return this.group;
    }


    public void setGroupFieldName(String groupFieldName)
    {
        this.groupFieldName = groupFieldName;
    }


    public String getGroupFieldName()
    {
        return this.groupFieldName;
    }


    public void setGroupLimit(int groupLimit)
    {
        this.groupLimit = groupLimit;
    }


    public int getGroupLimit()
    {
        return this.groupLimit;
    }


    public void setGroupFacets(boolean groupFacets)
    {
        this.groupFacets = groupFacets;
    }


    public boolean isGroupFacets()
    {
        return this.groupFacets;
    }


    public void setListeners(Collection<String> listeners)
    {
        this.listeners = listeners;
    }


    public Collection<String> getListeners()
    {
        return this.listeners;
    }


    public void setConfigSet(String configSet)
    {
        this.configSet = configSet;
    }


    public String getConfigSet()
    {
        return this.configSet;
    }


    public void setFtsQueryBuilder(String ftsQueryBuilder)
    {
        this.ftsQueryBuilder = ftsQueryBuilder;
    }


    public String getFtsQueryBuilder()
    {
        return this.ftsQueryBuilder;
    }


    public void setFtsQueryBuilderParameters(Map<String, String> ftsQueryBuilderParameters)
    {
        this.ftsQueryBuilderParameters = ftsQueryBuilderParameters;
    }


    public Map<String, String> getFtsQueryBuilderParameters()
    {
        return this.ftsQueryBuilderParameters;
    }


    public void setSearchQueryTemplates(Map<String, SearchQueryTemplate> searchQueryTemplates)
    {
        this.searchQueryTemplates = searchQueryTemplates;
    }


    public Map<String, SearchQueryTemplate> getSearchQueryTemplates()
    {
        return this.searchQueryTemplates;
    }


    public void setAdditionalParameters(Map<String, String> additionalParameters)
    {
        this.additionalParameters = additionalParameters;
    }


    public Map<String, String> getAdditionalParameters()
    {
        return this.additionalParameters;
    }


    public void setSorts(List<IndexedTypeSort> sorts)
    {
        this.sorts = sorts;
    }


    public List<IndexedTypeSort> getSorts()
    {
        return this.sorts;
    }


    public void setSortsByCode(Map<String, IndexedTypeSort> sortsByCode)
    {
        this.sortsByCode = sortsByCode;
    }


    public Map<String, IndexedTypeSort> getSortsByCode()
    {
        return this.sortsByCode;
    }
}
