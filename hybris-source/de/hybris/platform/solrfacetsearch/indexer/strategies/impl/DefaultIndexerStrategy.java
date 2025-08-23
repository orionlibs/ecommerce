package de.hybris.platform.solrfacetsearch.indexer.strategies.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.workers.IndexerWorker;
import de.hybris.platform.solrfacetsearch.indexer.workers.IndexerWorkerFactory;
import de.hybris.platform.solrfacetsearch.indexer.workers.IndexerWorkerParameters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIndexerStrategy extends AbstractIndexerStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultIndexerStrategy.class);
    private IndexerWorkerFactory indexerWorkerFactory;


    protected void doExecute(IndexerContext indexerContext) throws IndexerException
    {
        IndexConfig indexConfig = indexerContext.getFacetSearchConfig().getIndexConfig();
        List<PK> pks = indexerContext.getPks();
        List<IndexerWorkerWrapper> workers = new ArrayList<>();
        int batchSize = indexConfig.getBatchSize();
        int numberOfThreads = indexConfig.getNumberOfThreads();
        int numberOfWorkers = (int)Math.ceil(pks.size() / batchSize);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Batch size: {}", Integer.valueOf(batchSize));
            LOG.debug("Number of threads: {}", Integer.valueOf(numberOfThreads));
            LOG.debug("Number of workers: {}", Integer.valueOf(numberOfWorkers));
        }
        ExecutorService executorService = createIndexerWorkersPool(numberOfThreads);
        ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);
        int maxRetries = Math.max(0, indexConfig.getMaxRetries());
        int maxBatchRetries = Math.max(0, indexConfig.getMaxBatchRetries());
        try
        {
            RevertibleUpdate revertibleUpdate = markThreadAsSuspendable();
            try
            {
                int start;
                for(int index = 0; index < numberOfWorkers; index++, start += batchSize)
                {
                    int end = Math.min(start + batchSize, pks.size());
                    int workerNumber = index;
                    List<PK> workerPks = pks.subList(start, end);
                    IndexerWorker indexerWorker = createIndexerWorker(indexerContext, workerNumber, workerPks);
                    IndexerWorkerWrapper indexerWorkerWrapper = new IndexerWorkerWrapper(indexerWorker, Integer.valueOf(workerNumber), maxBatchRetries, workerPks);
                    workers.add(indexerWorkerWrapper);
                }
                runWorkers(indexerContext, completionService, workers, maxRetries);
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
        catch(Exception exception)
        {
            throw new IndexerException(exception);
        }
        finally
        {
            executorService.shutdownNow();
        }
    }


    protected RevertibleUpdate markThreadAsSuspendable()
    {
        return OperationInfo.updateThread(OperationInfo.builder().withTenant(resolveTenantId())
                        .withStatusInfo("Starting default indexing process as suspendable thread...").asSuspendableOperation().build());
    }


    protected void runWorkers(IndexerContext indexerContext, ExecutorCompletionService<Integer> completionService, List<IndexerWorkerWrapper> workers, int retriesLeft) throws IndexerException
    {
        int currentRetriesLeft = retriesLeft;
        Map<Integer, IndexerWorkerWrapper> failedWorkers = new HashMap<>();
        LOG.debug("Submitting indexer workers (retries left: {})", Integer.valueOf(retriesLeft));
        for(IndexerWorkerWrapper worker : workers)
        {
            completionService.submit((Runnable)worker.getIndexerWorker(), worker.getWorkerNumber());
            failedWorkers.put(worker.getWorkerNumber(), worker);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Worker {} has been submitted (retries left: {}", worker
                                .getWorkerNumber(), Integer.valueOf(worker.getRetriesLeft()));
            }
        }
        for(int i = 0; i < workers.size(); i++)
        {
            try
            {
                Future<Integer> future = completionService.take();
                Integer workerNumber = future.get();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Worker {} finished", workerNumber);
                }
                failedWorkers.remove(workerNumber);
            }
            catch(ExecutionException e)
            {
                if(currentRetriesLeft <= 0)
                {
                    throw new IndexerException("Indexer worker failed. Max number of retries in total has been reached", e);
                }
                currentRetriesLeft--;
            }
            catch(InterruptedException e)
            {
                throw new IndexerException("Indexer worker was interrupted.", e);
            }
        }
        if(!failedWorkers.isEmpty())
        {
            List<IndexerWorkerWrapper> rerunWorkers = new ArrayList<>();
            for(IndexerWorkerWrapper indexerWorkerWrapper : failedWorkers.values())
            {
                if(indexerWorkerWrapper.getRetriesLeft() <= 0)
                {
                    throw new IndexerException("Indexer worker " + indexerWorkerWrapper.getWorkerNumber() + " failed. Max number of retries per worker has been reached");
                }
                IndexerWorker indexerWorker = createIndexerWorker(indexerContext, indexerWorkerWrapper.getWorkerNumber().intValue(), indexerWorkerWrapper
                                .getWorkerPks());
                indexerWorkerWrapper.setIndexerWorker(indexerWorker);
                indexerWorkerWrapper.setRetriesLeft(indexerWorkerWrapper.getRetriesLeft() - 1);
                rerunWorkers.add(indexerWorkerWrapper);
            }
            runWorkers(indexerContext, completionService, rerunWorkers, currentRetriesLeft);
        }
    }


    protected ExecutorService createIndexerWorkersPool(int numberOfThreads)
    {
        Object object = new Object(this);
        return Executors.newFixedThreadPool(numberOfThreads, (ThreadFactory)object);
    }


    protected IndexerWorker createIndexerWorker(IndexerContext indexerContext, long workerNumber, List<PK> workerPks) throws IndexerException
    {
        Collection<String> indexedProperties = new ArrayList<>();
        for(IndexedProperty indexedProperty : indexerContext.getIndexedProperties())
        {
            indexedProperties.add(indexedProperty.getName());
        }
        IndexerWorkerParameters workerParameters = new IndexerWorkerParameters();
        workerParameters.setWorkerNumber(workerNumber);
        workerParameters.setIndexOperationId(indexerContext.getIndexOperationId());
        workerParameters.setIndexOperation(indexerContext.getIndexOperation());
        workerParameters.setExternalIndexOperation(indexerContext.isExternalIndexOperation());
        workerParameters.setFacetSearchConfig(indexerContext.getFacetSearchConfig().getName());
        workerParameters.setIndexedType(indexerContext.getIndexedType().getUniqueIndexedTypeCode());
        workerParameters.setIndexedProperties(indexedProperties);
        workerParameters.setPks(workerPks);
        workerParameters.setIndexerHints(indexerContext.getIndexerHints());
        workerParameters.setIndex(indexerContext.getIndex().getQualifier());
        String tenantId = resolveTenantId();
        UserModel sessionUser = resolveSessionUser();
        LanguageModel sessionLanguage = resolveSessionLanguage();
        CurrencyModel sessionCurrency = resolveSessionCurrency();
        boolean sessionUseReadOnlyDataSource = resolveSessionUseReadOnlyDataSource();
        workerParameters.setTenant(tenantId);
        workerParameters.setSessionUser(sessionUser.getUid());
        workerParameters.setSessionLanguage((sessionLanguage == null) ? null : sessionLanguage.getIsocode());
        workerParameters.setSessionCurrency((sessionCurrency == null) ? null : sessionCurrency.getIsocode());
        workerParameters.setSessionUseReadOnlyDataSource(sessionUseReadOnlyDataSource);
        IndexerWorker indexerWorker = this.indexerWorkerFactory.createIndexerWorker(getFacetSearchConfig());
        indexerWorker.initialize(workerParameters);
        return indexerWorker;
    }


    public IndexerWorkerFactory getIndexerWorkerFactory()
    {
        return this.indexerWorkerFactory;
    }


    public void setIndexerWorkerFactory(IndexerWorkerFactory indexerWorkerFactory)
    {
        this.indexerWorkerFactory = indexerWorkerFactory;
    }
}
