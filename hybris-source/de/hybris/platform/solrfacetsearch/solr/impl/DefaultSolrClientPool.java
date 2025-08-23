package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.config.SolrClientConfig;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.daos.SolrServerConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrClientPool;
import de.hybris.platform.solrfacetsearch.solr.SolrClientType;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.util.RedeployUtilities;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.solr.client.solrj.SolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSolrClientPool implements SolrClientPool, InitializingBean
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSolrClientPool.class);
    protected static final String INDEX_PARAM = "index";
    protected static final String SOLR_CLIENT_PARAM = "solrClientType";
    protected static final String CREATE_METHOD_PARAM = "createMethod";
    protected static final String CHECK_INTERVAL_PROPERTY = "solrfacetsearch.solrClientPool.checkInterval";
    protected static final long CHECK_INTERVAL_DEFAULT_VALUE = 300000L;
    protected static final String CHECK_THREAD_NAME_PREFIX = "solrclient-cleanup-";
    protected static final long SCHEDULER_TERMINATION_TIMEOUT = 10000L;
    private ModelService modelService;
    private ConfigurationService configurationService;
    private TenantService tenantService;
    private SessionService sessionService;
    private SolrServerConfigDao solrServerConfigDao;
    private Converter<SolrServerConfigModel, SolrConfig> solrServerConfigConverter;
    private String tenantId;
    private ConcurrentHashMap<String, SolrClientsWrapper> solrClients;
    private ScheduledExecutorService scheduler;


    protected String getTenantId()
    {
        return this.tenantId;
    }


    protected ScheduledExecutorService getScheduler()
    {
        return this.scheduler;
    }


    protected ConcurrentMap<String, SolrClientsWrapper> getSolrClients()
    {
        return this.solrClients;
    }


    public void afterPropertiesSet()
    {
        this.tenantId = this.tenantService.getCurrentTenantId();
        this.solrClients = new ConcurrentHashMap<>();
        Registry.registerTenantListener(createTenantListener());
    }


    public CachedSolrClient getOrCreate(Index index, SolrClientType clientType, Function<SolrConfig, SolrClient> createMethod) throws SolrServiceException
    {
        return getOrCreate(index, clientType, createMethod, null);
    }


    public CachedSolrClient getOrCreate(Index index, SolrClientType clientType, Function<SolrConfig, SolrClient> createMethod, Consumer<SolrClient> closeMethod) throws SolrServiceException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("index", index);
        ServicesUtil.validateParameterNotNullStandardMessage("solrClientType", clientType);
        ServicesUtil.validateParameterNotNullStandardMessage("createMethod", createMethod);
        SolrConfig solrConfig = index.getFacetSearchConfig().getSolrConfig();
        SolrClientsWrapper clientsWrapper = this.solrClients.compute(solrConfig.getName(), (solrConfigName, solrClientsWrapper) -> doGetOrCreate(solrConfig, solrClientsWrapper, clientType, createMethod, closeMethod));
        return resolveSolrClient(clientsWrapper, clientType);
    }


    public void invalidateAll()
    {
        LOG.debug("Invalidating pooled Solr clients ...");
        for(Iterator<String> iterator = this.solrClients.keySet().iterator(); iterator.hasNext(); )
        {
            String key = iterator.next();
            this.solrClients.computeIfPresent(key, (solrConfigName, clientsWrapper) -> {
                clientsWrapper.close();
                return null;
            });
        }
    }


    protected CachedSolrClient resolveSolrClient(SolrClientsWrapper clientsWrapper, SolrClientType solrClientType)
    {
        return (solrClientType == SolrClientType.INDEXING) ? clientsWrapper.getIndexClient() : clientsWrapper.getSearchClient();
    }


    protected SolrClientsWrapper doGetOrCreate(SolrConfig solrConfig, SolrClientsWrapper clientsWrapper, SolrClientType clientType, Function<SolrConfig, SolrClient> createMethod, Consumer<SolrClient> closeMethod)
    {
        SolrClientsWrapper newClientsWrapper;
        CachedSolrClient newSolrClient;
        if(clientsWrapper != null && Objects.equals(clientsWrapper.getConfigVersion(), solrConfig.getVersion()))
        {
            CachedSolrClient solrClient = resolveSolrClient(clientsWrapper, clientType);
            if(solrClient != null)
            {
                solrClient.addConsumer();
                return clientsWrapper;
            }
        }
        SolrServerConfigModel solrServerConfig = loadSolrServerConfig(solrConfig.getName());
        SolrConfig newSolrConfig = (SolrConfig)this.solrServerConfigConverter.convert(solrServerConfig);
        if(clientsWrapper != null && Objects.equals(clientsWrapper.getConfigVersion(), newSolrConfig.getVersion()))
        {
            newClientsWrapper = clientsWrapper;
            CachedSolrClient solrClient = resolveSolrClient(clientsWrapper, clientType);
            if(solrClient != null)
            {
                solrClient.addConsumer();
                return clientsWrapper;
            }
        }
        else
        {
            if(clientsWrapper != null)
            {
                LOG.info("New Solr config detected [config={}, newVersion={}, oldVersion={}]", new Object[] {solrConfig.getName(), newSolrConfig
                                .getVersion(), clientsWrapper.getConfigVersion()});
                clientsWrapper.close();
            }
            newClientsWrapper = new SolrClientsWrapper(newSolrConfig);
        }
        if(clientType == SolrClientType.INDEXING)
        {
            newSolrClient = createSolrClient(newSolrConfig, newSolrConfig.getIndexingClientConfig(), createMethod, closeMethod);
            newClientsWrapper.setIndexClient(newSolrClient);
        }
        else
        {
            newSolrClient = createSolrClient(newSolrConfig, newSolrConfig.getClientConfig(), createMethod, closeMethod);
            newClientsWrapper.setSearchClient(newSolrClient);
        }
        newSolrClient.addConsumer();
        return newClientsWrapper;
    }


    protected CachedSolrClient createSolrClient(SolrConfig solrConfig, SolrClientConfig solrClientConfig, Function<SolrConfig, SolrClient> createMethod, Consumer<SolrClient> closeMethod)
    {
        UsernamePasswordCredentials usernamePasswordCredentials;
        SolrClient solrClient = createMethod.apply(solrConfig);
        Credentials credentials = null;
        if(StringUtils.isNotBlank(solrClientConfig.getUsername()) && StringUtils.isNotBlank(solrClientConfig.getPassword()))
        {
            usernamePasswordCredentials = new UsernamePasswordCredentials(solrClientConfig.getUsername(), solrClientConfig.getPassword());
        }
        return new CachedSolrClient(solrClient, closeMethod, (Credentials)usernamePasswordCredentials);
    }


    protected TenantListener createTenantListener()
    {
        return (TenantListener)new SolrClientPoolTenantListener(this);
    }


    protected SolrServerConfigModel loadSolrServerConfig(String name)
    {
        SolrServerConfigModel solrServerConfig = this.solrServerConfigDao.findSolrServerConfigByName(name);
        this.modelService.refresh(solrServerConfig);
        return solrServerConfig;
    }


    protected void startCleanUpThread()
    {
        if(!JaloConnection.getInstance().isSystemInitialized() || RedeployUtilities.isShutdownInProgress())
        {
            return;
        }
        long checkInterval = this.configurationService.getConfiguration().getLong("solrfacetsearch.solrClientPool.checkInterval", 300000L);
        if(checkInterval <= 0L)
        {
            return;
        }
        LOG.info("Starting Solr clients clean-up thread for tenant {}", this.tenantId);
        Object object = new Object(this);
        this.scheduler = Executors.newScheduledThreadPool(1, (ThreadFactory)object);
        this.scheduler.scheduleAtFixedRate(() -> {
            try
            {
                try
                {
                    initializeSession();
                    checkAll();
                }
                finally
                {
                    destroySession();
                }
            }
            catch(Exception e)
            {
                LOG.warn(e.getMessage(), e);
            }
        }checkInterval, checkInterval, TimeUnit.MILLISECONDS);
    }


    protected void stopCleanUpThread()
    {
        if(this.scheduler != null)
        {
            LOG.info("Stopping Solr clients clean-up thread for tenant {}", this.tenantId);
            this.scheduler.shutdownNow();
            try
            {
                this.scheduler.awaitTermination(10000L, TimeUnit.MILLISECONDS);
                if(!this.scheduler.isShutdown())
                {
                    LOG.warn("Failed to stop Solr clients clean-up thread for tenant {}", this.tenantId);
                }
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }


    protected void initializeSession()
    {
        Tenant tenant = Registry.getTenantByID(this.tenantId);
        Registry.setCurrentTenant(tenant);
        this.sessionService.createNewSession();
    }


    protected void checkAll()
    {
        LOG.debug("Checking pooled Solr clients ...");
        for(String key : this.solrClients.keySet())
        {
            this.solrClients.computeIfPresent(key, this::check);
        }
    }


    protected SolrClientsWrapper check(String solrServerConfigName, SolrClientsWrapper clientsWrapper)
    {
        try
        {
            LOG.debug("Checking pooled Solr client [config={}]", solrServerConfigName);
            SolrServerConfigModel solrServerConfig = loadSolrServerConfig(solrServerConfigName);
            if(!StringUtils.equals(clientsWrapper.getConfigVersion(), solrServerConfig.getVersion()))
            {
                LOG.debug("New Solr config detected [config={}, newVersion={}, oldVersion={}]", new Object[] {solrServerConfigName, solrServerConfig
                                .getVersion(), clientsWrapper.getConfigVersion()});
                clientsWrapper.close();
                return null;
            }
        }
        catch(UnknownIdentifierException e)
        {
            LOG.debug("Solr config not found [config={}]", solrServerConfigName);
            clientsWrapper.close();
            return null;
        }
        catch(Exception e)
        {
            LOG.warn("Error checking Solr config [config={}, error={}]", solrServerConfigName, e);
        }
        return clientsWrapper;
    }


    protected void destroySession()
    {
        this.sessionService.closeCurrentSession();
        Registry.unsetCurrentTenant();
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


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
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


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public SolrServerConfigDao getSolrServerConfigDao()
    {
        return this.solrServerConfigDao;
    }


    @Required
    public void setSolrServerConfigDao(SolrServerConfigDao solrServerConfigDao)
    {
        this.solrServerConfigDao = solrServerConfigDao;
    }


    public Converter<SolrServerConfigModel, SolrConfig> getSolrServerConfigConverter()
    {
        return this.solrServerConfigConverter;
    }


    @Required
    public void setSolrServerConfigConverter(Converter<SolrServerConfigModel, SolrConfig> solrServerConfigConverter)
    {
        this.solrServerConfigConverter = solrServerConfigConverter;
    }
}
