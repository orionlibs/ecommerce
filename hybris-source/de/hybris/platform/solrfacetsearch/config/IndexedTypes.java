package de.hybris.platform.solrfacetsearch.config;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.loader.ModelLoader;
import de.hybris.platform.solrfacetsearch.provider.IdentityProvider;
import de.hybris.platform.solrfacetsearch.provider.IndexedTypeFieldsValuesProvider;
import de.hybris.platform.solrfacetsearch.search.impl.SolrResult;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class IndexedTypes
{
    public static IndexedTypeFieldsValuesProvider getFieldsValuesProvider(IndexedType indexType)
    {
        String name = indexType.getFieldsValuesProvider();
        return (name == null) ? null : (IndexedTypeFieldsValuesProvider)Registry.getGlobalApplicationContext().getBean(name, IndexedTypeFieldsValuesProvider.class);
    }


    public static IdentityProvider getIdentityProvider(IndexedType indexType)
    {
        String name = indexType.getIdentityProvider();
        return (name == null) ? null : (IdentityProvider)Registry.getGlobalApplicationContext().getBean(name, IdentityProvider.class);
    }


    public static ModelLoader getModelLoader(IndexedType indexType)
    {
        String name = indexType.getModelLoader();
        return (name == null) ? null : (ModelLoader)Registry.getGlobalApplicationContext().getBean(name, ModelLoader.class);
    }


    public static Converter<SolrResult, ?> getSolrResultConverter(IndexedType indexType)
    {
        String name = indexType.getSolrResultConverter();
        return (name == null) ? null : (Converter<SolrResult, ?>)Registry.getGlobalApplicationContext().getBean(name, Converter.class);
    }


    public static IndexedType createIndexedType(ComposedTypeModel composedType, boolean variant, Collection<IndexedProperty> indexedProperties, Map<IndexOperation, IndexedTypeFlexibleSearchQuery> flexibleSearchQueries, String identityProvider, String modelLoader, Set<String> typeFacets,
                    String fieldsValuesProvider, String indexName, String indexNameFromConfig, String solrResultConverter)
    {
        IndexedType indexType = new IndexedType();
        indexType.setComposedType(composedType);
        indexType.setVariant(variant);
        indexType.setIndexedProperties(new HashMap<>());
        for(IndexedProperty indexedProperty : indexedProperties)
        {
            indexType.getIndexedProperties().put(indexedProperty.getName(), indexedProperty);
        }
        indexType.setIdentityProvider(identityProvider);
        indexType.setModelLoader(modelLoader);
        indexType.setTypeFacets(typeFacets);
        indexType.setFieldsValuesProvider(fieldsValuesProvider);
        indexType.setIndexName(indexName);
        indexType.setIndexNameFromConfig(indexNameFromConfig);
        indexType.setStaged(false);
        indexType.setSolrResultConverter(solrResultConverter);
        indexType.setFlexibleSearchQueries(flexibleSearchQueries);
        return indexType;
    }


    public static IndexedType createIndexedType(ComposedTypeModel composedType, boolean variant, Collection<IndexedProperty> indexedProperties, Map<IndexOperation, IndexedTypeFlexibleSearchQuery> flexibleSearchQueries, String identityProvider, String modelLoader, Set<String> typeFacets,
                    String fieldsValuesProvider, String indexName)
    {
        return createIndexedType(composedType, variant, indexedProperties, flexibleSearchQueries, identityProvider, modelLoader, typeFacets, fieldsValuesProvider, indexName, null, null);
    }


    public static IndexedType createIndexedType(ComposedTypeModel composedType, boolean variant, Collection<IndexedProperty> indexedProperties, Map<IndexOperation, IndexedTypeFlexibleSearchQuery> flexibleSearchQueries, String identityProvider, String modelLoader, Set<String> typeFacets,
                    String fieldsValuesProvider, String indexName, String indexNameFromConfig)
    {
        return createIndexedType(composedType, variant, indexedProperties, flexibleSearchQueries, identityProvider, modelLoader, typeFacets, fieldsValuesProvider, indexName, indexNameFromConfig, null);
    }
}
