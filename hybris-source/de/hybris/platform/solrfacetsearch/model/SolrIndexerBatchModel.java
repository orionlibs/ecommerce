package de.hybris.platform.solrfacetsearch.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.processing.model.SimpleBatchModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SolrIndexerBatchModel extends SimpleBatchModel
{
    public static final String _TYPECODE = "SolrIndexerBatch";


    public SolrIndexerBatchModel()
    {
    }


    public SolrIndexerBatchModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerBatchModel(String _executionId, String _id, DistributedProcessModel _process, int _retries, BatchType _type)
    {
        setExecutionId(_executionId);
        setId(_id);
        setProcess(_process);
        setRetries(_retries);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerBatchModel(String _executionId, String _id, ItemModel _owner, DistributedProcessModel _process, int _retries, BatchType _type)
    {
        setExecutionId(_executionId);
        setId(_id);
        setOwner(_owner);
        setProcess(_process);
        setRetries(_retries);
        setType(_type);
    }
}
