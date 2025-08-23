package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.spi.Exporter;
import de.hybris.platform.solrfacetsearch.indexer.spi.Indexer;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.IdentityProvider;
import de.hybris.platform.solrfacetsearch.provider.IndexedTypeFieldsValuesProvider;
import de.hybris.platform.solrfacetsearch.provider.RangeNameProvider;
import de.hybris.platform.solrfacetsearch.provider.TypeValueResolver;
import de.hybris.platform.solrfacetsearch.provider.ValueProviderSelectionStrategy;
import de.hybris.platform.solrfacetsearch.provider.ValueResolver;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexer implements Indexer, BeanFactoryAware
{
    private static final Logger LOG = Logger.getLogger("solrIndexThreadLogger");
    protected static final String VALUE_PROVIDERS_KEY = "solrfacetsearch.valueProviders";
    protected static final String INDEXED_FIELDS_KEY = "solrfacetsearch.indexedFields";
    private ModelService modelService;
    private TypeService typeService;
    private SolrSearchProviderFactory solrSearchProviderFactory;
    private IndexerBatchContextFactory<?> indexerBatchContextFactory;
    private FieldNameProvider fieldNameProvider;
    private RangeNameProvider rangeNameProvider;
    private ValueProviderSelectionStrategy valueProviderSelectionStrategy;
    private BeanFactory beanFactory;


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public SolrSearchProviderFactory getSolrSearchProviderFactory()
    {
        return this.solrSearchProviderFactory;
    }


    @Required
    public void setSolrSearchProviderFactory(SolrSearchProviderFactory solrSearchProviderFactory)
    {
        this.solrSearchProviderFactory = solrSearchProviderFactory;
    }


    public IndexerBatchContextFactory getIndexerBatchContextFactory()
    {
        return this.indexerBatchContextFactory;
    }


    @Required
    public void setIndexerBatchContextFactory(IndexerBatchContextFactory<?> indexerBatchContextFactory)
    {
        this.indexerBatchContextFactory = indexerBatchContextFactory;
    }


    public FieldNameProvider getFieldNameProvider()
    {
        return this.fieldNameProvider;
    }


    @Required
    public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
    {
        this.fieldNameProvider = fieldNameProvider;
    }


    public RangeNameProvider getRangeNameProvider()
    {
        return this.rangeNameProvider;
    }


    @Required
    public void setRangeNameProvider(RangeNameProvider rangeNameProvider)
    {
        this.rangeNameProvider = rangeNameProvider;
    }


    public ValueProviderSelectionStrategy getValueProviderSelectionStrategy()
    {
        return this.valueProviderSelectionStrategy;
    }


    @Required
    public void setValueProviderSelectionStrategy(ValueProviderSelectionStrategy valueProviderSelectionStrategy)
    {
        this.valueProviderSelectionStrategy = valueProviderSelectionStrategy;
    }


    protected BeanFactory getBeanFactory()
    {
        return this.beanFactory;
    }


    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }


    public Collection<SolrInputDocument> indexItems(Collection<ItemModel> items, FacetSearchConfig facetSearchConfig, IndexedType indexedType) throws IndexerException, InterruptedException
    {
        if(items == null)
        {
            return Collections.emptyList();
        }
        IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
        SolrConfig solrConfig = facetSearchConfig.getSolrConfig();
        Collection<SolrInputDocument> documents = new ArrayList<>(items.size());
        for(ItemModel itemModel : items)
        {
            if(Thread.interrupted())
            {
                throw new InterruptedException();
            }
            try
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Indexing item with PK " + itemModel.getPk());
                }
                SolrInputDocument solrDocument = createInputDocument(itemModel, indexConfig, indexedType);
                documents.add(solrDocument);
            }
            catch(FieldValueProviderException | RuntimeException e)
            {
                String message = "Failed to index item with PK " + itemModel.getPk() + ": " + e.getMessage();
                handleError(indexConfig, indexedType, message, e);
            }
        }
        SolrServerMode serverMode = solrConfig.getMode();
        Exporter exporter = getExporter(serverMode);
        exporter.exportToUpdateIndex(documents, facetSearchConfig, indexedType);
        return documents;
    }


    public Collection<SolrInputDocument> indexItems(Collection<ItemModel> items, FacetSearchConfig facetSearchConfig, IndexedType indexedType, Collection<IndexedProperty> indexedProperties) throws IndexerException, InterruptedException
    {
        if(items == null)
        {
            return Collections.emptyList();
        }
        IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
        SolrConfig solrConfig = facetSearchConfig.getSolrConfig();
        Collection<SolrInputDocument> documents = new ArrayList<>(items.size());
        for(ItemModel itemModel : items)
        {
            if(Thread.interrupted())
            {
                throw new InterruptedException();
            }
            try
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Indexing item with PK " + itemModel.getPk());
                }
                SolrInputDocument solrDocument = createInputDocument(itemModel, indexConfig, indexedType, indexedProperties);
                documents.add(solrDocument);
            }
            catch(FieldValueProviderException | RuntimeException e)
            {
                String message = "Failed to index item with PK " + itemModel.getPk() + ": " + e.getMessage();
                handleError(indexConfig, indexedType, message, e);
            }
        }
        SolrServerMode serverMode = solrConfig.getMode();
        Exporter exporter = getExporter(serverMode);
        exporter.exportToUpdateIndex(documents, facetSearchConfig, indexedType);
        return documents;
    }


    public void removeItemsByPk(Collection<PK> pks, FacetSearchConfig facetSearchConfig, IndexedType indexedType, Index index) throws IndexerException, InterruptedException
    {
        if(CollectionUtils.isEmpty(pks))
        {
            return;
        }
        SolrServerMode serverMode = facetSearchConfig.getSolrConfig().getMode();
        if(serverMode == SolrServerMode.XML_EXPORT)
        {
            Exporter exporter = getExporter(serverMode);
            List<String> pkValues = (List<String>)pks.stream().map(PK::getLongValueAsString).collect(Collectors.toList());
            exporter.exportToDeleteFromIndex(pkValues, facetSearchConfig, indexedType);
        }
        else
        {
            try
            {
                SolrSearchProvider searchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
                searchProvider.deleteDocumentsByPk(index, pks);
            }
            catch(SolrServiceException e)
            {
                throw new IndexerException(e.getMessage(), e);
            }
        }
    }


    protected void handleError(IndexConfig indexConfig, IndexedType indexedType, String message, Exception error) throws IndexerException
    {
        if(indexConfig.isIgnoreErrors())
        {
            LOG.warn(message);
        }
        else
        {
            throw new IndexerException(message, error);
        }
    }


    protected IdentityProvider<ItemModel> getIdentityProvider(IndexedType indexedType)
    {
        return (IdentityProvider<ItemModel>)this.beanFactory.getBean(indexedType.getIdentityProvider(), IdentityProvider.class);
    }


    protected Exporter getExporter(SolrServerMode serverMode)
    {
        String beanName = "solr.exporter." + serverMode.toString().toLowerCase(Locale.ROOT);
        return (Exporter)this.beanFactory.getBean(beanName, Exporter.class);
    }


    protected SolrInputDocument createInputDocument(ItemModel model, IndexConfig indexConfig, IndexedType indexedType) throws FieldValueProviderException
    {
        validateCommonRequiredParameters(model, indexConfig, indexedType);
        IndexerBatchContext batchContext = this.indexerBatchContextFactory.getContext();
        SolrInputDocument doc = new SolrInputDocument(new String[0]);
        DefaultSolrInputDocument wrappedDoc = createWrappedDocument(batchContext, doc);
        wrappedDoc.startDocument();
        doc.addField("indexOperationId", Long.valueOf(batchContext.getIndexOperationId()));
        addCommonFields(doc, batchContext, model);
        addIndexedPropertyFields((InputDocument)wrappedDoc, batchContext, model);
        addIndexedTypeFields((InputDocument)wrappedDoc, batchContext, model);
        wrappedDoc.endDocument();
        batchContext.getInputDocuments().add(wrappedDoc);
        return doc;
    }


    protected SolrInputDocument createInputDocument(ItemModel model, IndexConfig indexConfig, IndexedType indexedType, Collection<IndexedProperty> indexedProperties) throws FieldValueProviderException
    {
        validateCommonRequiredParameters(model, indexConfig, indexedType);
        IndexerBatchContext batchContext = this.indexerBatchContextFactory.getContext();
        Set<String> indexedFields = getIndexedFields(batchContext);
        SolrInputDocument doc = new SolrInputDocument(new String[0]);
        DefaultSolrInputDocument wrappedDoc = createWrappedDocumentForPartialUpdates(batchContext, doc, indexedFields);
        wrappedDoc.startDocument();
        addCommonFields(doc, batchContext, model);
        addIndexedPropertyFields((InputDocument)wrappedDoc, batchContext, model);
        wrappedDoc.endDocument();
        return doc;
    }


    protected void validateCommonRequiredParameters(ItemModel item, IndexConfig indexConfig, IndexedType indexedType)
    {
        if(item == null)
        {
            throw new IllegalArgumentException("item must not be null");
        }
        if(indexConfig == null)
        {
            throw new IllegalArgumentException("indexConfig must not be null");
        }
        if(indexedType == null)
        {
            throw new IllegalArgumentException("indexedType must not be null");
        }
    }


    protected DefaultSolrInputDocument createWrappedDocument(IndexerBatchContext batchContext, SolrInputDocument delegate)
    {
        return new DefaultSolrInputDocument(delegate, batchContext, this.fieldNameProvider, this.rangeNameProvider);
    }


    protected DefaultSolrInputDocument createWrappedDocumentForPartialUpdates(IndexerBatchContext batchContext, SolrInputDocument delegate, Set<String> indexedPropertiesFields)
    {
        return (DefaultSolrInputDocument)new DefaultSolrPartialUpdateInputDocument(delegate, batchContext, this.fieldNameProvider, this.rangeNameProvider, indexedPropertiesFields);
    }


    protected void addCommonFields(SolrInputDocument document, IndexerBatchContext batchContext, ItemModel model)
    {
        FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
        IndexedType indexedType = batchContext.getIndexedType();
        IdentityProvider<ItemModel> identityProvider = getIdentityProvider(indexedType);
        String id = identityProvider.getIdentifier(facetSearchConfig.getIndexConfig(), model);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Using SolrInputDocument id [" + id + "]");
        }
        document.addField("id", id);
        document.addField("pk", Long.valueOf(model.getPk().getLongValue()));
        ComposedTypeModel composedType = this.typeService.getComposedTypeForClass(model.getClass());
        if(Objects.equals(composedType.getCatalogItemType(), Boolean.TRUE))
        {
            AttributeDescriptorModel catalogAttDesc = composedType.getCatalogVersionAttribute();
            CatalogVersionModel catalogVersion = (CatalogVersionModel)this.modelService.getAttributeValue(model, catalogAttDesc.getQualifier());
            document.addField("catalogId", catalogVersion.getCatalog().getId());
            document.addField("catalogVersion", catalogVersion.getVersion());
        }
    }


    protected void addIndexedPropertyFields(InputDocument document, IndexerBatchContext batchContext, ItemModel model) throws FieldValueProviderException
    {
        Map<String, Collection<IndexedProperty>> valueProviders = resolveValueProviders(batchContext);
        for(Map.Entry<String, Collection<IndexedProperty>> entry : valueProviders.entrySet())
        {
            String valueProviderId = entry.getKey();
            Collection<IndexedProperty> indexedProperties = entry.getValue();
            Object valueProvider = this.valueProviderSelectionStrategy.getValueProvider(valueProviderId);
            if(valueProvider instanceof FieldValueProvider)
            {
                addIndexedPropertyFieldsForOldApi(document, batchContext, model, indexedProperties, valueProviderId, (FieldValueProvider)valueProvider);
                continue;
            }
            if(valueProvider instanceof ValueResolver)
            {
                addIndexedPropertyFieldsForNewApi(document, batchContext, model, indexedProperties, valueProviderId, (ValueResolver<ItemModel>)valueProvider);
                continue;
            }
            throw new FieldValueProviderException("Value provider is not of an expected type: " + valueProviderId);
        }
    }


    protected void addIndexedPropertyFieldsForOldApi(InputDocument document, IndexerBatchContext batchContext, ItemModel model, Collection<IndexedProperty> indexedProperties, String valueProviderId, FieldValueProvider valueProvider) throws FieldValueProviderException
    {
        FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
        for(IndexedProperty indexedProperty : indexedProperties)
        {
            try
            {
                Collection<FieldValue> fieldValues = valueProvider.getFieldValues(facetSearchConfig.getIndexConfig(), indexedProperty, model);
                for(FieldValue fieldValue : fieldValues)
                {
                    document.addField(fieldValue.getFieldName(), fieldValue.getValue());
                }
            }
            catch(FieldValueProviderException | RuntimeException e)
            {
                String message = "Failed to resolve values for item with PK: " + model.getPk() + ", by resolver: " + valueProviderId + ", for property: " + indexedProperty.getName() + ", reason: " + e.getMessage();
                handleError(facetSearchConfig.getIndexConfig(), message, e);
            }
        }
    }


    protected void addIndexedPropertyFieldsForNewApi(InputDocument document, IndexerBatchContext batchContext, ItemModel model, Collection<IndexedProperty> indexedProperties, String valueProviderId, ValueResolver<ItemModel> valueProvider) throws FieldValueProviderException
    {
        FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
        try
        {
            valueProvider.resolve(document, batchContext, indexedProperties, model);
        }
        catch(FieldValueProviderException | RuntimeException e)
        {
            ArrayList<String> indexedPropertiesNames = new ArrayList<>();
            for(IndexedProperty indexedProperty : indexedProperties)
            {
                indexedPropertiesNames.add(indexedProperty.getName());
            }
            String message = "Failed to resolve values for item with PK: " + model.getPk() + ", by resolver: " + valueProviderId + ", for properties: " + indexedPropertiesNames + ", reason: " + e.getMessage();
            handleError(facetSearchConfig.getIndexConfig(), message, e);
        }
    }


    protected void addIndexedTypeFields(InputDocument document, IndexerBatchContext batchContext, ItemModel model) throws FieldValueProviderException
    {
        IndexedType indexedType = batchContext.getIndexedType();
        String typeValueProviderBeanId = indexedType.getFieldsValuesProvider();
        if(typeValueProviderBeanId != null)
        {
            Object typeValueProvider = getTypeValueProvider(typeValueProviderBeanId);
            if(typeValueProvider instanceof IndexedTypeFieldsValuesProvider)
            {
                addIndexedTypeFieldsForOldApi(document, batchContext, model, typeValueProviderBeanId, (IndexedTypeFieldsValuesProvider)typeValueProvider);
            }
            else if(typeValueProvider instanceof TypeValueResolver)
            {
                addIndexedTypeFieldsForNewApi(document, batchContext, model, typeValueProviderBeanId, (TypeValueResolver<ItemModel>)typeValueProvider);
            }
            else
            {
                throw new FieldValueProviderException("Type value provider is not of an expected type: " + typeValueProviderBeanId);
            }
        }
    }


    protected void addIndexedTypeFieldsForOldApi(InputDocument document, IndexerBatchContext batchContext, ItemModel model, String typeValueProviderBeanId, IndexedTypeFieldsValuesProvider typeValueProvider) throws FieldValueProviderException
    {
        FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
        try
        {
            Collection<FieldValue> fieldValues = typeValueProvider.getFieldValues(facetSearchConfig.getIndexConfig(), model);
            for(FieldValue fieldValue : fieldValues)
            {
                document.addField(fieldValue.getFieldName(), fieldValue.getValue());
            }
        }
        catch(FieldValueProviderException | RuntimeException e)
        {
            String message = "Failed to resolve values for item with PK: " + model.getPk() + ", by resolver: " + typeValueProviderBeanId + ", reason: " + e.getMessage();
            handleError(facetSearchConfig.getIndexConfig(), message, e);
        }
    }


    protected void addIndexedTypeFieldsForNewApi(InputDocument document, IndexerBatchContext batchContext, ItemModel model, String typeValueProviderBeanId, TypeValueResolver<ItemModel> typeValueProvider) throws FieldValueProviderException
    {
        FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
        try
        {
            typeValueProvider.resolve(document, batchContext, model);
        }
        catch(FieldValueProviderException | RuntimeException e)
        {
            String message = "Failed to resolve values for item with PK: " + model.getPk() + ", by resolver: " + typeValueProviderBeanId + ", reason: " + e.getMessage();
            handleError(facetSearchConfig.getIndexConfig(), message, e);
        }
    }


    protected Map<String, Collection<IndexedProperty>> resolveValueProviders(IndexerBatchContext batchContext)
    {
        Map<String, Collection<IndexedProperty>> valueProviders = (Map<String, Collection<IndexedProperty>>)batchContext.getAttributes().get("solrfacetsearch.valueProviders");
        if(valueProviders == null)
        {
            valueProviders = this.valueProviderSelectionStrategy.resolveValueProviders(batchContext.getIndexedType(), batchContext
                            .getIndexedProperties());
            batchContext.getAttributes().put("solrfacetsearch.valueProviders", valueProviders);
        }
        return valueProviders;
    }


    protected Set<String> getIndexedFields(IndexerBatchContext batchContext) throws FieldValueProviderException
    {
        Set<String> indexedPropertiesFields = (Set<String>)batchContext.getAttributes().get("solrfacetsearch.indexedFields");
        if(CollectionUtils.isNotEmpty(indexedPropertiesFields))
        {
            return indexedPropertiesFields;
        }
        indexedPropertiesFields = new HashSet<>();
        Set<String> indexedPropertiesNames = new HashSet<>();
        for(IndexedProperty indexedProperty : batchContext.getIndexedProperties())
        {
            indexedPropertiesNames.add(indexedProperty.getName());
        }
        SolrClient solrClient = null;
        try
        {
            Index index = batchContext.getIndex();
            FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
            IndexedType indexedType = batchContext.getIndexedType();
            SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
            solrClient = solrSearchProvider.getClientForIndexing(index);
            Set<String> fields = getIndexedFields(index, solrClient);
            for(String field : fields)
            {
                String indexedPropertyName = this.fieldNameProvider.getPropertyName(field);
                if(indexedPropertiesNames.contains(indexedPropertyName))
                {
                    indexedPropertiesFields.add(field);
                }
            }
        }
        catch(IOException | SolrServerException | SolrServiceException e)
        {
            throw new FieldValueProviderException("Could not fetch fields from solr server", e);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)solrClient);
        }
        batchContext.getAttributes().put("solrfacetsearch.indexedFields", indexedPropertiesFields);
        return indexedPropertiesFields;
    }


    protected Set<String> getIndexedFields(Index index, SolrClient solrClient) throws SolrServerException, IOException
    {
        LukeRequest request = new LukeRequest();
        request.setNumTerms(0);
        LukeResponse response = (LukeResponse)request.process(solrClient, index.getName());
        Map<String, LukeResponse.FieldInfo> fields = response.getFieldInfo();
        if(fields != null)
        {
            return fields.keySet();
        }
        return Collections.emptySet();
    }


    protected void handleError(IndexConfig indexConfig, String message, Exception error) throws FieldValueProviderException
    {
        if(indexConfig.isIgnoreErrors())
        {
            LOG.warn(message);
        }
        else
        {
            throw new FieldValueProviderException(message, error);
        }
    }


    protected Object getTypeValueProvider(String beanName)
    {
        return this.beanFactory.getBean(beanName);
    }
}
