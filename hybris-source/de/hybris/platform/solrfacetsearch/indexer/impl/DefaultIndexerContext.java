package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.solr.Index;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultIndexerContext implements IndexerContext
{
    private long indexOperationId;
    private IndexOperation indexOperation;
    private boolean externalIndexOperation;
    private FacetSearchConfig facetSearchConfig;
    private IndexedType indexedType;
    private Collection<IndexedProperty> indexedProperties;
    private List<PK> pks;
    private Index index;
    private final Map<String, String> indexerHints = new HashMap<>();
    private final Map<String, Object> attributes = new HashMap<>();
    private IndexerContext.Status status;
    private final List<Exception> failureExceptions = new ArrayList<>();


    public long getIndexOperationId()
    {
        return this.indexOperationId;
    }


    public void setIndexOperationId(long indexOperationId)
    {
        this.indexOperationId = indexOperationId;
    }


    public IndexOperation getIndexOperation()
    {
        return this.indexOperation;
    }


    public void setIndexOperation(IndexOperation indexOperation)
    {
        this.indexOperation = indexOperation;
    }


    public boolean isExternalIndexOperation()
    {
        return this.externalIndexOperation;
    }


    public void setExternalIndexOperation(boolean externalIndexOperation)
    {
        this.externalIndexOperation = externalIndexOperation;
    }


    public FacetSearchConfig getFacetSearchConfig()
    {
        return this.facetSearchConfig;
    }


    public void setFacetSearchConfig(FacetSearchConfig facetSearchConfig)
    {
        this.facetSearchConfig = facetSearchConfig;
    }


    public IndexedType getIndexedType()
    {
        return this.indexedType;
    }


    public void setIndexedType(IndexedType indexedType)
    {
        this.indexedType = indexedType;
    }


    public Collection<IndexedProperty> getIndexedProperties()
    {
        return this.indexedProperties;
    }


    public void setIndexedProperties(Collection<IndexedProperty> indexedProperties)
    {
        this.indexedProperties = indexedProperties;
    }


    public List<PK> getPks()
    {
        return Collections.unmodifiableList(this.pks);
    }


    public void setPks(List<PK> pks)
    {
        if(IndexerContext.Status.CREATED != this.status && IndexerContext.Status.STARTING != this.status)
        {
            throw new IllegalStateException("expecting status CREATED or STARTING but it was " + this.status);
        }
        this.pks = pks;
    }


    public Index getIndex()
    {
        return this.index;
    }


    public void setIndex(Index index)
    {
        this.index = index;
    }


    public Map<String, String> getIndexerHints()
    {
        return this.indexerHints;
    }


    public Map<String, Object> getAttributes()
    {
        return this.attributes;
    }


    public IndexerContext.Status getStatus()
    {
        return this.status;
    }


    public void setStatus(IndexerContext.Status status)
    {
        this.status = status;
    }


    public void addFailureException(Exception exception)
    {
        this.failureExceptions.add(exception);
    }


    public List<Exception> getFailureExceptions()
    {
        return Collections.unmodifiableList(this.failureExceptions);
    }
}
