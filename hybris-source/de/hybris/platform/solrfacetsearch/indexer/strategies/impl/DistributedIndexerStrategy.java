package de.hybris.platform.solrfacetsearch.indexer.strategies.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.processing.distributed.defaultimpl.DistributedProcessHelper;
import de.hybris.platform.processing.distributed.simple.data.CollectionBasedCreationData;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.SolrIndexerDistributedProcessModel;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DistributedIndexerStrategy extends AbstractIndexerStrategy
{
    private static final Logger LOG = Logger.getLogger(DistributedIndexerStrategy.class);
    private static final String DISTRIBUTED_PROCESS_HANDLER = "indexerDistributedProcessHandler";
    private static final int SLEEP_DURATION_MILLIS = 5000;
    private DistributedProcessService distributedProcessService;
    private ModelService modelService;


    protected void doExecute(IndexerContext indexerContext) throws IndexerException
    {
        try
        {
            RevertibleUpdate revertibleUpdate = markThreadAsSuspendable();
            try
            {
                CollectionBasedCreationData indexerProcessData = buildIndexerCreationData(indexerContext);
                SolrIndexerDistributedProcessModel distributedIndexerProcess = createDistributedIndexerProcess(indexerProcessData, indexerContext);
                this.distributedProcessService.start(distributedIndexerProcess.getCode());
                waitForDistributedIndexer(distributedIndexerProcess.getCode());
                if(revertibleUpdate != null)
                {
                    revertibleUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(revertibleUpdate != null)
                {
                    try
                    {
                        revertibleUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(Exception e)
        {
            throw new IndexerException(e);
        }
    }


    protected RevertibleUpdate markThreadAsSuspendable()
    {
        return OperationInfo.updateThread(OperationInfo.builder().withTenant(resolveTenantId())
                        .withStatusInfo("Starting distributed indexing process as suspendable thread...").asSuspendableOperation().build());
    }


    protected CollectionBasedCreationData buildIndexerCreationData(IndexerContext indexerContext)
    {
        IndexConfig indexConfig = indexerContext.getFacetSearchConfig().getIndexConfig();
        int batchSize = indexConfig.getBatchSize();
        int maxRetries = indexConfig.getMaxRetries();
        CollectionBasedCreationData.Builder indexerCreationDataBuilder = CollectionBasedCreationData.builder().withElements(indexerContext.getPks()).withProcessId(String.valueOf(indexerContext.getIndexOperationId())).withHandlerId("indexerDistributedProcessHandler").withBatchSize(batchSize)
                        .withNumOfRetries(maxRetries).withProcessModelClass(SolrIndexerDistributedProcessModel.class);
        if(StringUtils.isNotEmpty(indexConfig.getNodeGroup()))
        {
            indexerCreationDataBuilder.withNodeGroup(indexConfig.getNodeGroup());
        }
        return indexerCreationDataBuilder.build();
    }


    protected SolrIndexerDistributedProcessModel createDistributedIndexerProcess(CollectionBasedCreationData indexerProcessData, IndexerContext indexerContext)
    {
        UserModel sessionUser = resolveSessionUser();
        LanguageModel sessionLanguage = resolveSessionLanguage();
        CurrencyModel sessionCurrency = resolveSessionCurrency();
        Collection<String> indexedProperties = new ArrayList<>();
        for(IndexedProperty indexedProperty : indexerContext.getIndexedProperties())
        {
            indexedProperties.add(indexedProperty.getName());
        }
        SolrIndexerDistributedProcessModel distributedIndexerProcess = (SolrIndexerDistributedProcessModel)this.distributedProcessService.create((ProcessCreationData)indexerProcessData);
        distributedIndexerProcess.setIndexOperationId(indexerContext.getIndexOperationId());
        distributedIndexerProcess.setIndexOperation(IndexerOperationValues.valueOf(indexerContext.getIndexOperation().name()));
        distributedIndexerProcess.setExternalIndexOperation(indexerContext.isExternalIndexOperation());
        distributedIndexerProcess.setFacetSearchConfig(indexerContext.getFacetSearchConfig().getName());
        distributedIndexerProcess.setIndexedType(indexerContext.getIndexedType().getUniqueIndexedTypeCode());
        distributedIndexerProcess.setIndexedProperties(indexedProperties);
        distributedIndexerProcess.setIndexerHints(indexerContext.getIndexerHints());
        distributedIndexerProcess.setIndex(indexerContext.getIndex().getQualifier());
        distributedIndexerProcess.setSessionUser(sessionUser.getUid());
        distributedIndexerProcess.setSessionCurrency((sessionCurrency == null) ? null : sessionCurrency.getIsocode());
        distributedIndexerProcess.setSessionLanguage((sessionLanguage == null) ? null : sessionLanguage.getIsocode());
        this.modelService.save(distributedIndexerProcess);
        return distributedIndexerProcess;
    }


    protected void waitForDistributedIndexer(String processCode) throws IndexerException, InterruptedException
    {
        while(true)
        {
            try
            {
                SolrIndexerDistributedProcessModel process = (SolrIndexerDistributedProcessModel)this.distributedProcessService.wait(processCode, 5L);
                if(DistributedProcessHelper.isFinished((DistributedProcessModel)process) && DistributedProcessState.FAILED.equals(process.getState()))
                {
                    throw new IndexerException("Indexing process has failed");
                }
                if(DistributedProcessHelper.isFinished((DistributedProcessModel)process))
                {
                    return;
                }
            }
            catch(SystemIsSuspendedException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("The system has been suspended. Retrying in 5 seconds.", (Throwable)e);
                }
                Thread.sleep(5000L);
            }
        }
    }


    public DistributedProcessService getDistributedProcessService()
    {
        return this.distributedProcessService;
    }


    @Required
    public void setDistributedProcessService(DistributedProcessService distributedProcessService)
    {
        this.distributedProcessService = distributedProcessService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
