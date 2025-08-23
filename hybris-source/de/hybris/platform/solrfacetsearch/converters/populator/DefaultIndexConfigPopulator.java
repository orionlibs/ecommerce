package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.CommitMode;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.OptimizeMode;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.enums.SolrCommitMode;
import de.hybris.platform.solrfacetsearch.enums.SolrOptimizeMode;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexerQueryModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class DefaultIndexConfigPopulator implements Populator<SolrFacetSearchConfigModel, IndexConfig>
{
    private Converter<SolrServerConfigModel, SolrConfig> solrServerConfigConverter;
    private Converter<SolrIndexedTypeModel, IndexedType> indexedTypeConverter;


    public void populate(SolrFacetSearchConfigModel source, IndexConfig target)
    {
        Collection<IndexedType> indexTypesFromItems;
        try
        {
            indexTypesFromItems = getIndexTypesFromItems(source.getSolrIndexedTypes(),
                            getSolrConfigFromItems(source.getSolrServerConfig()));
        }
        catch(FacetConfigServiceException e)
        {
            throw new ConversionException("Cannot get index types from items", e);
        }
        target.setIndexedTypes(new HashMap<>());
        for(IndexedType indexedType : indexTypesFromItems)
        {
            target.getIndexedTypes().put(indexedType.getUniqueIndexedTypeCode(), indexedType);
        }
        target.setCatalogVersions(Collections.unmodifiableCollection(source.getCatalogVersions()));
        target.setLanguages(Collections.unmodifiableCollection(source.getLanguages()));
        target.setCurrencies(Collections.unmodifiableCollection(source.getCurrencies()));
        target.setEnabledLanguageFallbackMechanism(source.isEnabledLanguageFallbackMechanism());
        target.setListeners(Collections.unmodifiableCollection(source.getListeners()));
        SolrIndexConfigModel solrIndexConfig = source.getSolrIndexConfig();
        target.setExportPath(solrIndexConfig.getExportPath());
        target.setBatchSize(solrIndexConfig.getBatchSize());
        target.setNumberOfThreads(solrIndexConfig.getNumberOfThreads());
        target.setIndexMode(solrIndexConfig.getIndexMode());
        target.setIgnoreErrors(solrIndexConfig.isIgnoreErrors());
        target.setLegacyMode(solrIndexConfig.isLegacyMode());
        target.setMaxRetries(solrIndexConfig.getMaxRetries());
        target.setMaxBatchRetries(solrIndexConfig.getMaxBatchRetries());
        target.setDistributedIndexing(solrIndexConfig.isDistributedIndexing());
        target.setNodeGroup(solrIndexConfig.getNodeGroup());
        SolrCommitMode solrCommitMode = solrIndexConfig.getCommitMode();
        target.setCommitMode((solrCommitMode == null) ? null : CommitMode.valueOf(solrCommitMode.toString()));
        SolrOptimizeMode solrOptimizeMode = solrIndexConfig.getOptimizeMode();
        target.setOptimizeMode((solrOptimizeMode == null) ? null : OptimizeMode.valueOf(solrOptimizeMode.toString()));
    }


    protected Collection<IndexedType> getIndexTypesFromItems(Collection<SolrIndexedTypeModel> itemTypes, SolrConfig solrConfig) throws FacetConfigServiceException
    {
        Collection<IndexedType> result = new ArrayList<>();
        for(SolrIndexedTypeModel itemType : itemTypes)
        {
            SolrIndexerQueryModel full = null;
            SolrIndexerQueryModel update = null;
            SolrIndexerQueryModel delete = null;
            for(SolrIndexerQueryModel query : itemType.getSolrIndexerQueries())
            {
                if(query.getType().equals(IndexerOperationValues.FULL))
                {
                    full = query;
                    continue;
                }
                if(query.getType().equals(IndexerOperationValues.UPDATE))
                {
                    update = query;
                    continue;
                }
                if(query.getType().equals(IndexerOperationValues.DELETE))
                {
                    delete = query;
                }
            }
            if(SolrServerMode.XML_EXPORT.equals(solrConfig.getMode()))
            {
                checkSingleQuery(full);
                checkSingleQuery(update);
                checkSingleQuery(delete);
            }
            IndexedType indexedType = (IndexedType)this.indexedTypeConverter.convert(itemType);
            result.add(indexedType);
        }
        return result;
    }


    protected void checkSingleQuery(SolrIndexerQueryModel query) throws FacetConfigServiceException
    {
        if(query.isInjectLastIndexTime())
        {
            throw new FacetConfigServiceException("Cannot use 'lastIndexTime' in solr indexer queries in XML_EXPORT mode");
        }
    }


    protected SolrConfig getSolrConfigFromItems(SolrServerConfigModel itemConfig)
    {
        return (SolrConfig)this.solrServerConfigConverter.convert(itemConfig);
    }


    public void setSolrServerConfigConverter(Converter<SolrServerConfigModel, SolrConfig> solrServerConfigConverter)
    {
        this.solrServerConfigConverter = solrServerConfigConverter;
    }


    public void setIndexedTypeConverter(Converter<SolrIndexedTypeModel, IndexedType> indexedTypeConverter)
    {
        this.indexedTypeConverter = indexedTypeConverter;
    }
}
