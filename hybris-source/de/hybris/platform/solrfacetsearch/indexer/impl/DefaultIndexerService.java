package de.hybris.platform.solrfacetsearch.indexer.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerStrategy;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerStrategyFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexerService implements IndexerService
{
    private IndexerStrategyFactory indexerStrategyFactory;


    public IndexerStrategyFactory getIndexerStrategyFactory()
    {
        return this.indexerStrategyFactory;
    }


    @Required
    public void setIndexerStrategyFactory(IndexerStrategyFactory indexerStrategyFactory)
    {
        this.indexerStrategyFactory = indexerStrategyFactory;
    }


    public void performFullIndex(FacetSearchConfig facetSearchConfig) throws IndexerException
    {
        performFullIndex(facetSearchConfig, Collections.emptyMap());
    }


    public void performFullIndex(FacetSearchConfig facetSearchConfig, Map<String, String> indexerHints) throws IndexerException
    {
        IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
        for(IndexedType indexedType : indexConfig.getIndexedTypes().values())
        {
            IndexerStrategy indexerStrategy = createIndexerStrategy(facetSearchConfig);
            indexerStrategy.setIndexOperation(IndexOperation.FULL);
            indexerStrategy.setFacetSearchConfig(facetSearchConfig);
            indexerStrategy.setIndexedType(indexedType);
            indexerStrategy.setIndexerHints(indexerHints);
            indexerStrategy.execute();
        }
    }


    public void updateIndex(FacetSearchConfig facetSearchConfig) throws IndexerException
    {
        updateIndex(facetSearchConfig, Collections.emptyMap());
    }


    public void updateIndex(FacetSearchConfig facetSearchConfig, Map<String, String> indexerHints) throws IndexerException
    {
        IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
        for(IndexedType indexedType : indexConfig.getIndexedTypes().values())
        {
            IndexerStrategy indexerStrategy = createIndexerStrategy(facetSearchConfig);
            indexerStrategy.setIndexOperation(IndexOperation.UPDATE);
            indexerStrategy.setFacetSearchConfig(facetSearchConfig);
            indexerStrategy.setIndexedType(indexedType);
            indexerStrategy.setIndexerHints(indexerHints);
            indexerStrategy.execute();
        }
    }


    public void updateTypeIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType) throws IndexerException
    {
        updateTypeIndex(facetSearchConfig, indexedType, Collections.emptyMap());
    }


    public void updateTypeIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Map<String, String> indexerHints) throws IndexerException
    {
        IndexerStrategy indexerStrategy = createIndexerStrategy(facetSearchConfig);
        indexerStrategy.setIndexOperation(IndexOperation.UPDATE);
        indexerStrategy.setFacetSearchConfig(facetSearchConfig);
        indexerStrategy.setIndexedType(indexedType);
        indexerStrategy.setIndexerHints(indexerHints);
        indexerStrategy.execute();
    }


    public void updateTypeIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType, List<PK> pks) throws IndexerException
    {
        updateTypeIndex(facetSearchConfig, indexedType, pks, Collections.emptyMap());
    }


    public void updateTypeIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType, List<PK> pks, Map<String, String> indexerHints) throws IndexerException
    {
        IndexerStrategy indexerStrategy = createIndexerStrategy(facetSearchConfig);
        indexerStrategy.setIndexOperation(IndexOperation.UPDATE);
        indexerStrategy.setFacetSearchConfig(facetSearchConfig);
        indexerStrategy.setIndexedType(indexedType);
        indexerStrategy.setPks(pks);
        indexerStrategy.setIndexerHints(indexerHints);
        indexerStrategy.execute();
    }


    public void updatePartialTypeIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Collection<IndexedProperty> indexedProperties, List<PK> pks) throws IndexerException
    {
        updatePartialTypeIndex(facetSearchConfig, indexedType, indexedProperties, pks, Collections.emptyMap());
    }


    public void updatePartialTypeIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Collection<IndexedProperty> indexedProperties, List<PK> pks, Map<String, String> indexerHints) throws IndexerException
    {
        IndexerStrategy indexerStrategy = createIndexerStrategy(facetSearchConfig);
        indexerStrategy.setIndexOperation(IndexOperation.PARTIAL_UPDATE);
        indexerStrategy.setFacetSearchConfig(facetSearchConfig);
        indexerStrategy.setIndexedType(indexedType);
        indexerStrategy.setIndexedProperties(indexedProperties);
        indexerStrategy.setPks(pks);
        indexerStrategy.setIndexerHints(indexerHints);
        indexerStrategy.execute();
    }


    public void deleteFromIndex(FacetSearchConfig facetSearchConfig) throws IndexerException
    {
        deleteFromIndex(facetSearchConfig, Collections.emptyMap());
    }


    public void deleteFromIndex(FacetSearchConfig facetSearchConfig, Map<String, String> indexerHints) throws IndexerException
    {
        IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
        for(IndexedType indexedType : indexConfig.getIndexedTypes().values())
        {
            IndexerStrategy indexerStrategy = createIndexerStrategy(facetSearchConfig);
            indexerStrategy.setIndexOperation(IndexOperation.DELETE);
            indexerStrategy.setFacetSearchConfig(facetSearchConfig);
            indexerStrategy.setIndexedType(indexedType);
            indexerStrategy.setIndexerHints(indexerHints);
            indexerStrategy.execute();
        }
    }


    public void deleteTypeIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType) throws IndexerException
    {
        deleteTypeIndex(facetSearchConfig, indexedType, Collections.emptyMap());
    }


    public void deleteTypeIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType, Map<String, String> indexerHints) throws IndexerException
    {
        IndexerStrategy indexerStrategy = createIndexerStrategy(facetSearchConfig);
        indexerStrategy.setIndexOperation(IndexOperation.DELETE);
        indexerStrategy.setFacetSearchConfig(facetSearchConfig);
        indexerStrategy.setIndexedType(indexedType);
        indexerStrategy.setIndexerHints(indexerHints);
        indexerStrategy.execute();
    }


    public void deleteTypeIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType, List<PK> pks) throws IndexerException
    {
        deleteTypeIndex(facetSearchConfig, indexedType, pks, Collections.emptyMap());
    }


    public void deleteTypeIndex(FacetSearchConfig facetSearchConfig, IndexedType indexedType, List<PK> pks, Map<String, String> indexerHints) throws IndexerException
    {
        IndexerStrategy indexerStrategy = createIndexerStrategy(facetSearchConfig);
        indexerStrategy.setIndexOperation(IndexOperation.DELETE);
        indexerStrategy.setFacetSearchConfig(facetSearchConfig);
        indexerStrategy.setIndexedType(indexedType);
        indexerStrategy.setPks(pks);
        indexerStrategy.setIndexerHints(indexerHints);
        indexerStrategy.execute();
    }


    protected IndexerStrategy createIndexerStrategy(FacetSearchConfig facetSearchConfig) throws IndexerException
    {
        return this.indexerStrategyFactory.createIndexerStrategy(facetSearchConfig);
    }
}
