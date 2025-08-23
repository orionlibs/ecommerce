package de.hybris.platform.solrfacetsearch.indexer.workers.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerRuntimeException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerBatchStrategy;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerBatchStrategyFactory;
import de.hybris.platform.solrfacetsearch.indexer.workers.IndexerWorker;
import de.hybris.platform.solrfacetsearch.indexer.workers.IndexerWorkerParameters;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexerWorker implements IndexerWorker
{
    private static final Logger LOG = Logger.getLogger(DefaultIndexerWorker.class);
    private static final int SLEEP_DURATION_MILLIS = 5000;
    private SessionService sessionService;
    private UserService userService;
    private CommonI18NService commonI18NService;
    private FacetSearchConfigService facetSearchConfigService;
    private IndexerBatchStrategyFactory indexerBatchStrategyFactory;
    private SolrSearchProviderFactory solrSearchProviderFactory;
    private IndexerWorkerParameters workerParameters;


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public FacetSearchConfigService getFacetSearchConfigService()
    {
        return this.facetSearchConfigService;
    }


    @Required
    public void setFacetSearchConfigService(FacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    public IndexerBatchStrategyFactory getIndexerBatchStrategyFactory()
    {
        return this.indexerBatchStrategyFactory;
    }


    @Required
    public void setIndexerBatchStrategyFactory(IndexerBatchStrategyFactory indexerBatchStrategyFactory)
    {
        this.indexerBatchStrategyFactory = indexerBatchStrategyFactory;
    }


    public SolrSearchProviderFactory getSolrSearchProviderFactory()
    {
        return this.solrSearchProviderFactory;
    }


    public void setSolrSearchProviderFactory(SolrSearchProviderFactory solrSearchProviderFactory)
    {
        this.solrSearchProviderFactory = solrSearchProviderFactory;
    }


    public void initialize(IndexerWorkerParameters workerParameters)
    {
        ServicesUtil.validateParameterNotNull(workerParameters, "workerParameters must not be null");
        this.workerParameters = workerParameters;
    }


    public boolean isInitialized()
    {
        return (this.workerParameters != null);
    }


    public void run()
    {
        if(!isInitialized())
        {
            throw new IllegalStateException("Indexer worker was not initialized");
        }
        try
        {
            registerNonSuspendableThread();
            initializeSession();
            logWorkerStart();
            doRun();
            logWorkerSuccess();
        }
        catch(IndexerException | FacetConfigServiceException | SolrServiceException e)
        {
            logWorkerError(e);
            throw new IndexerRuntimeException(e);
        }
        catch(InterruptedException e)
        {
            logWorkerInterrupted();
            Thread.currentThread().interrupt();
        }
        finally
        {
            RegistrableThread.unregisterThread();
            destroySession();
        }
    }


    protected void doRun() throws IndexerException, FacetConfigServiceException, SolrServiceException, InterruptedException
    {
        FacetSearchConfig facetSearchConfig = this.facetSearchConfigService.getConfiguration(this.workerParameters.getFacetSearchConfig());
        IndexedType indexedType = this.facetSearchConfigService.resolveIndexedType(facetSearchConfig, this.workerParameters
                        .getIndexedType());
        List<IndexedProperty> indexedProperties = this.facetSearchConfigService.resolveIndexedProperties(facetSearchConfig, indexedType, this.workerParameters
                        .getIndexedProperties());
        SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
        Index index = solrSearchProvider.resolveIndex(facetSearchConfig, indexedType, this.workerParameters.getIndex());
        IndexerBatchStrategy indexerBatchStrategy = this.indexerBatchStrategyFactory.createIndexerBatchStrategy(facetSearchConfig);
        indexerBatchStrategy.setIndexOperationId(this.workerParameters.getIndexOperationId());
        indexerBatchStrategy.setIndexOperation(this.workerParameters.getIndexOperation());
        indexerBatchStrategy.setExternalIndexOperation(this.workerParameters.isExternalIndexOperation());
        indexerBatchStrategy.setFacetSearchConfig(facetSearchConfig);
        indexerBatchStrategy.setIndexedType(indexedType);
        indexerBatchStrategy.setIndexedProperties(indexedProperties);
        indexerBatchStrategy.setIndex(index);
        indexerBatchStrategy.setIndexerHints(this.workerParameters.getIndexerHints());
        indexerBatchStrategy.setPks(this.workerParameters.getPks());
        indexerBatchStrategy.execute();
    }


    protected void initializeSession()
    {
        Tenant tenant = Registry.getTenantByID(this.workerParameters.getTenant());
        Registry.setCurrentTenant(tenant);
        this.sessionService.createNewSession();
        this.sessionService.setAttribute("ctx.enable.fs.on.read-replica", Boolean.valueOf(this.workerParameters.isSessionUseReadOnlyDataSource()));
        UserModel user = this.userService.getUserForUID(this.workerParameters.getSessionUser());
        this.userService.setCurrentUser(user);
        LanguageModel language = this.commonI18NService.getLanguage(this.workerParameters.getSessionLanguage());
        this.commonI18NService.setCurrentLanguage(language);
        CurrencyModel currency = this.commonI18NService.getCurrency(this.workerParameters.getSessionCurrency());
        this.commonI18NService.setCurrentCurrency(currency);
    }


    protected void destroySession()
    {
        this.sessionService.closeCurrentSession();
        Registry.unsetCurrentTenant();
    }


    protected void registerNonSuspendableThread() throws InterruptedException
    {
        OperationInfo operationInfo = OperationInfo.builder().withTenant(this.workerParameters.getTenant()).withStatusInfo("Starting indexer worker " + this.workerParameters.getWorkerNumber()).asNotSuspendableOperation().build();
        while(true)
        {
            try
            {
                RegistrableThread.registerThread(operationInfo);
                return;
            }
            catch(SystemIsSuspendedException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(
                                    getTaskName() + " - System is suspended. The worker cannot be registered as non suspendable at the moment. Retrying in 5 sec...", (Throwable)e);
                }
                Thread.sleep(5000L);
            }
        }
    }


    protected String getTaskName()
    {
        return "Indexer worker " + this.workerParameters.getWorkerNumber() + " (" + this.workerParameters.getIndexOperation() + " index operation on " + this.workerParameters
                        .getFacetSearchConfig() + "/" + this.workerParameters.getIndexedType() + ")";
    }


    protected void logWorkerStart()
    {
        if(LOG.isDebugEnabled())
        {
            String taskName = getTaskName();
            LOG.debug("[" + taskName + "] started");
            LOG.debug("[" + taskName + "] tenant:" + Registry.getCurrentTenant());
            LOG.debug("[" + taskName + "] session ID: " + this.sessionService.getCurrentSession().getSessionId());
            LOG.debug("[" + taskName + "] session user: " + this.userService.getCurrentUser().getUid());
            LOG.debug("[" + taskName + "] session language: " + this.commonI18NService.getCurrentLanguage().getIsocode());
            LOG.debug("[" + taskName + "] session currency: " + this.commonI18NService.getCurrentCurrency().getIsocode());
            LOG.debug("[" + taskName + "] items count: " + this.workerParameters.getPks().size());
        }
    }


    protected void logWorkerSuccess()
    {
        if(LOG.isDebugEnabled())
        {
            String taskName = getTaskName();
            LOG.debug("[" + taskName + "] completed");
        }
    }


    protected void logWorkerError(Exception exception)
    {
        String taskName = getTaskName();
        LOG.error("[" + taskName + "] failed to process index items due to " + exception.getMessage(), exception);
    }


    protected void logWorkerInterrupted()
    {
        if(LOG.isDebugEnabled())
        {
            String taskName = getTaskName();
            LOG.debug("[" + taskName + "] interrupted");
        }
    }
}
