package de.hybris.platform.solrfacetsearch.indexer.strategies.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FlexibleSearchQuerySpec;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeFlexibleSearchQuery;
import de.hybris.platform.solrfacetsearch.config.factories.FlexibleSearchQuerySpecFactory;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueriesExecutor;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.UndefinedIndexerQuery;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexOperationIdGenerator;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerStrategy;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrIndexNotFoundException;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StopWatch;

public abstract class AbstractIndexerStrategy implements IndexerStrategy
{
    private static final Logger LOG = Logger.getLogger(AbstractIndexerStrategy.class);
    private SessionService sessionService;
    private UserService userService;
    private CommonI18NService commonI18NService;
    private TenantService tenantService;
    private FlexibleSearchService flexibleSearchService;
    private FlexibleSearchQuerySpecFactory flexibleSearchQuerySpecFactory;
    private IndexerQueriesExecutor indexerQueriesExecutor;
    private IndexOperationIdGenerator indexOperationIdGenerator;
    private IndexerContextFactory<?> indexerContextFactory;
    private SolrIndexService solrIndexService;
    private SolrSearchProviderFactory solrSearchProviderFactory;
    private IndexOperation indexOperation;
    private FacetSearchConfig facetSearchConfig;
    private IndexedType indexedType;
    private Collection<IndexedProperty> indexedProperties;
    private List<PK> pks;
    private Index index;
    private Map<String, String> indexerHints;


    public void execute() throws IndexerException
    {
        validateRequiredFields();
        RevertibleUpdate revertibleInfo = null;
        StopWatch operationTimer = new StopWatch();
        operationTimer.start();
        logStrategyStart();
        try
        {
            revertibleInfo = registerOrUpdateNonSuspendableThread();
            createLocalSessionContext();
            Index resolvedIndex = resolveIndex();
            if(resolvedIndex == null && this.indexOperation != IndexOperation.FULL)
            {
                LOG.info("No active index found, FULL indexer operation must be performed before any other operation");
                return;
            }
            long indexOperationId = this.indexOperationIdGenerator.generate(this.facetSearchConfig, this.indexedType, resolvedIndex);
            boolean isExternalIndexOperation = true;
            if(this.pks == null)
            {
                isExternalIndexOperation = false;
            }
            doExecute(resolvedIndex, indexOperationId, isExternalIndexOperation);
            operationTimer.stop();
            logStrategySuccess(operationTimer);
        }
        catch(IndexerException | RuntimeException e)
        {
            operationTimer.stop();
            logStrategyError(operationTimer);
            throw e;
        }
        finally
        {
            revertOperationInfo(revertibleInfo);
            removeLocalSessionContext();
        }
    }


    protected void doExecute(Index resolvedIndex, long indexOperationId, boolean isExternalIndexOperation) throws IndexerException
    {
        try
        {
            Collection<IndexedProperty> resolveIndexedProperties = resolveIndexedProperties();
            Map<String, String> resolveIndexerHints = resolveIndexerHints();
            IndexerContext context = getIndexerContextFactory().createContext(indexOperationId, getIndexOperation(), isExternalIndexOperation,
                            getFacetSearchConfig(), getIndexedType(), resolveIndexedProperties);
            context.setIndex(resolvedIndex);
            context.getIndexerHints().putAll(resolveIndexerHints);
            getIndexerContextFactory().prepareContext();
            context.setPks(resolvePks());
            getIndexerContextFactory().initializeContext();
            if(CollectionUtils.isNotEmpty(context.getPks()))
            {
                doExecute(context);
            }
            getIndexerContextFactory().destroyContext();
        }
        catch(IndexerException | RuntimeException e)
        {
            getIndexerContextFactory().destroyContext(e);
            throw e;
        }
    }


    protected abstract void doExecute(IndexerContext paramIndexerContext) throws IndexerException;


    protected void validateRequiredFields()
    {
        if(this.indexOperation == null)
        {
            throw new IllegalStateException("indexOperation field not set");
        }
        if(this.facetSearchConfig == null)
        {
            throw new IllegalStateException("facetSearchConfig field not set");
        }
        if(this.indexedType == null)
        {
            throw new IllegalStateException("indexedType field not set");
        }
    }


    protected Index resolveIndex() throws IndexerException
    {
        if(this.index != null)
        {
            return this.index;
        }
        try
        {
            SolrIndexModel activeIndex = this.solrIndexService.getActiveIndex(this.facetSearchConfig.getName(), this.indexedType
                            .getIdentifier());
            SolrSearchProvider searchProvider = this.solrSearchProviderFactory.getSearchProvider(this.facetSearchConfig, this.indexedType);
            return searchProvider.resolveIndex(this.facetSearchConfig, this.indexedType, activeIndex.getQualifier());
        }
        catch(SolrIndexNotFoundException e)
        {
            LOG.debug(e);
            return null;
        }
        catch(SolrServiceException e)
        {
            throw new IndexerException(e);
        }
    }


    protected FlexibleSearchQuerySpec createIndexerQuery() throws IndexerException
    {
        IndexedTypeFlexibleSearchQuery query = (IndexedTypeFlexibleSearchQuery)this.indexedType.getFlexibleSearchQueries().get(this.indexOperation);
        if(query == null)
        {
            throw new UndefinedIndexerQuery("" + this.indexOperation + " query not defined in configuration.");
        }
        try
        {
            return this.flexibleSearchQuerySpecFactory.createIndexQuery(query, this.indexedType, this.facetSearchConfig);
        }
        catch(SolrServiceException e)
        {
            throw new IndexerException(e);
        }
    }


    protected List<PK> executeIndexerQuery(FacetSearchConfig facetSearchConfig, IndexedType indexedType, String query, Map<String, Object> queryParameters) throws IndexerException
    {
        return this.indexerQueriesExecutor.getPks(facetSearchConfig, indexedType, query, queryParameters);
    }


    protected void createLocalSessionContext()
    {
        Session session = this.sessionService.getCurrentSession();
        JaloSession jaloSession = (JaloSession)this.sessionService.getRawSession(session);
        jaloSession.createLocalSessionContext();
    }


    protected void removeLocalSessionContext()
    {
        Session session = this.sessionService.getCurrentSession();
        JaloSession jaloSession = (JaloSession)this.sessionService.getRawSession(session);
        jaloSession.removeLocalSessionContext();
    }


    protected RevertibleUpdate registerOrUpdateNonSuspendableThread()
    {
        RevertibleUpdate revertibleInfo = null;
        OperationInfo operationInfo = OperationInfo.builder().withTenant(resolveTenantId()).withStatusInfo("Creating a context for indexing as non suspendable...").asNotSuspendableOperation().build();
        try
        {
            RegistrableThread.registerThread(operationInfo);
        }
        catch(IllegalStateException e)
        {
            LOG.debug("Thread has already been registered. Updating operation info...", e);
            revertibleInfo = OperationInfo.updateThread(operationInfo);
        }
        return revertibleInfo;
    }


    protected void revertOperationInfo(RevertibleUpdate revertibleInfo)
    {
        if(revertibleInfo == null)
        {
            RegistrableThread.unregisterThread();
        }
        else
        {
            revertibleInfo.revert();
        }
    }


    protected void logStrategyStart()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("" + this.indexOperation + " index operation started on " + this.indexOperation + "/" + this.facetSearchConfig.getName() + ": " + this.indexedType
                            .getUniqueIndexedTypeCode());
        }
    }


    protected void logStrategySuccess(StopWatch operationTimer)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("" + this.indexOperation + " index operation finished on " + this.indexOperation + "/" + this.facetSearchConfig.getName() + ": " + this.indexedType
                            .getUniqueIndexedTypeCode() + ", total time: " + new Date() + "s.");
        }
    }


    protected void logStrategyError(StopWatch operationTimer)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("" + this.indexOperation + " index operation failed on " + this.indexOperation + "/" + this.facetSearchConfig.getName() + ": " + this.indexedType
                            .getUniqueIndexedTypeCode() + ", total time: " + new Date() + "s.");
        }
    }


    protected List<PK> resolvePks() throws IndexerException
    {
        if(this.pks == null)
        {
            FlexibleSearchQuerySpec querySpec = createIndexerQuery();
            String userUID = querySpec.getUser();
            UserModel user = this.userService.getUserForUID(userUID);
            this.userService.setCurrentUser(user);
            String query = querySpec.getQuery();
            Map<String, Object> queryParameters = querySpec.createParameters();
            return executeIndexerQuery(this.facetSearchConfig, this.indexedType, query, queryParameters);
        }
        return this.pks;
    }


    protected Collection<IndexedProperty> resolveIndexedProperties()
    {
        if(this.indexedProperties == null)
        {
            return this.indexedType.getIndexedProperties().values();
        }
        return this.indexedProperties;
    }


    protected Map<String, String> resolveIndexerHints()
    {
        if(this.indexerHints == null)
        {
            return new HashMap<>();
        }
        return this.indexerHints;
    }


    protected String resolveTenantId()
    {
        return this.tenantService.getCurrentTenantId();
    }


    protected UserModel resolveSessionUser()
    {
        return this.userService.getCurrentUser();
    }


    protected LanguageModel resolveSessionLanguage()
    {
        return this.commonI18NService.getCurrentLanguage();
    }


    protected CurrencyModel resolveSessionCurrency()
    {
        return this.commonI18NService.getCurrentCurrency();
    }


    protected boolean resolveSessionUseReadOnlyDataSource()
    {
        return ((Boolean)this.flexibleSearchService.isReadOnlyDataSourceEnabled().orElse(Boolean.valueOf(false))).booleanValue();
    }


    public IndexOperation getIndexOperation()
    {
        return this.indexOperation;
    }


    public void setIndexOperation(IndexOperation indexOperation)
    {
        this.indexOperation = indexOperation;
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
        return this.pks;
    }


    public void setPks(List<PK> pks)
    {
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


    public void setIndexerHints(Map<String, String> indexerHints)
    {
        this.indexerHints = indexerHints;
    }


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


    public FlexibleSearchQuerySpecFactory getFlexibleSearchQuerySpecFactory()
    {
        return this.flexibleSearchQuerySpecFactory;
    }


    @Required
    public void setFlexibleSearchQuerySpecFactory(FlexibleSearchQuerySpecFactory flexibleSearchQuerySpecFactory)
    {
        this.flexibleSearchQuerySpecFactory = flexibleSearchQuerySpecFactory;
    }


    public IndexerQueriesExecutor getIndexerQueriesExecutor()
    {
        return this.indexerQueriesExecutor;
    }


    @Required
    public void setIndexerQueriesExecutor(IndexerQueriesExecutor indexerQueriesExecutor)
    {
        this.indexerQueriesExecutor = indexerQueriesExecutor;
    }


    public IndexOperationIdGenerator getIndexOperationIdGenerator()
    {
        return this.indexOperationIdGenerator;
    }


    @Required
    public void setIndexOperationIdGenerator(IndexOperationIdGenerator indexOperationIdGenerator)
    {
        this.indexOperationIdGenerator = indexOperationIdGenerator;
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


    public TenantService getTenantService()
    {
        return this.tenantService;
    }


    @Required
    public void setTenantService(TenantService tenantService)
    {
        this.tenantService = tenantService;
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public IndexerContextFactory getIndexerContextFactory()
    {
        return this.indexerContextFactory;
    }


    @Required
    public void setIndexerContextFactory(IndexerContextFactory<?> indexerContextFactory)
    {
        this.indexerContextFactory = indexerContextFactory;
    }


    public SolrIndexService getSolrIndexService()
    {
        return this.solrIndexService;
    }


    @Required
    public void setSolrIndexService(SolrIndexService solrIndexService)
    {
        this.solrIndexService = solrIndexService;
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
}
