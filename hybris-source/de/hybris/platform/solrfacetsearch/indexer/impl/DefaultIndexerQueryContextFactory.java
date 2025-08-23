package de.hybris.platform.solrfacetsearch.indexer.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.common.ListenersFactory;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueryListener;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexerQueryContextFactory implements IndexerQueryContextFactory<DefaultIndexerQueryContext>
{
    private static final Logger LOG = Logger.getLogger(DefaultIndexerQueryContextFactory.class);
    public static final String INDEXER_QUERY_LISTENERS_KEY = "solrfacetsearch.indexerQueryListeners";
    private final ThreadLocal<DefaultIndexerQueryContext> indexerQueryContext = new ThreadLocal<>();
    private SessionService sessionService;
    private ListenersFactory listenersFactory;


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public ListenersFactory getListenersFactory()
    {
        return this.listenersFactory;
    }


    @Required
    public void setListenersFactory(ListenersFactory listenersFactory)
    {
        this.listenersFactory = listenersFactory;
    }


    public DefaultIndexerQueryContext createContext(FacetSearchConfig facetSearchConfig, IndexedType indexedType, String query, Map<String, Object> queryParameters) throws IndexerException
    {
        DefaultIndexerQueryContext context = new DefaultIndexerQueryContext();
        context.setFacetSearchConfig(facetSearchConfig);
        context.setIndexedType(indexedType);
        context.setQuery(query);
        context.setQueryParameters(queryParameters);
        context.setStatus(IndexerQueryContext.Status.CREATED);
        createLocalSessionContext();
        this.indexerQueryContext.set(context);
        return context;
    }


    public void initializeContext() throws IndexerException
    {
        DefaultIndexerQueryContext context = getContext();
        if(IndexerQueryContext.Status.CREATED != context.getStatus())
        {
            throw new IllegalStateException("Context not in status CREATED");
        }
        context.setStatus(IndexerQueryContext.Status.STARTING);
        executeBeforeQueryListeners(context);
        context.setStatus(IndexerQueryContext.Status.EXECUTING);
    }


    public DefaultIndexerQueryContext getContext()
    {
        DefaultIndexerQueryContext context = this.indexerQueryContext.get();
        if(context == null)
        {
            throw new IllegalStateException("There is no current context");
        }
        return context;
    }


    public void destroyContext() throws IndexerException
    {
        DefaultIndexerQueryContext context = getContext();
        context.setStatus(IndexerQueryContext.Status.STOPPING);
        executeAfterQueryListeners(context);
        context.setStatus(IndexerQueryContext.Status.COMPLETED);
        this.indexerQueryContext.remove();
        removeLocalSessionContext();
    }


    public void destroyContext(Exception failureException)
    {
        try
        {
            DefaultIndexerQueryContext context = getContext();
            context.addFailureException(failureException);
            context.setStatus(IndexerQueryContext.Status.FAILED);
            executeAfterQueryErrorListeners(context);
        }
        finally
        {
            this.indexerQueryContext.remove();
            removeLocalSessionContext();
        }
    }


    protected void executeBeforeQueryListeners(DefaultIndexerQueryContext context) throws IndexerException
    {
        List<IndexerQueryListener> listeners = getListeners(context);
        for(IndexerQueryListener listener : listeners)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running beforeQuery listener for: " + listener.getClass().getCanonicalName());
            }
            listener.beforeQuery((IndexerQueryContext)context);
        }
    }


    protected void executeAfterQueryListeners(DefaultIndexerQueryContext context) throws IndexerException
    {
        List<IndexerQueryListener> listeners = getListeners(context);
        for(IndexerQueryListener listener : Lists.reverse(listeners))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running afterQuery listener for: " + listener.getClass().getCanonicalName());
            }
            listener.afterQuery((IndexerQueryContext)context);
        }
    }


    protected void executeAfterQueryErrorListeners(DefaultIndexerQueryContext context)
    {
        List<IndexerQueryListener> listeners = getListeners(context);
        for(IndexerQueryListener listener : Lists.reverse(listeners))
        {
            try
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Running afterQueryError listener for: " + listener.getClass().getCanonicalName());
                }
                listener.afterQueryError((IndexerQueryContext)context);
            }
            catch(Exception exception)
            {
                context.addFailureException(exception);
            }
        }
    }


    protected List<IndexerQueryListener> getListeners(DefaultIndexerQueryContext context)
    {
        List<IndexerQueryListener> listeners = (List<IndexerQueryListener>)context.getAttributes().get("solrfacetsearch.indexerQueryListeners");
        if(listeners == null)
        {
            FacetSearchConfig facetSearchConfig = context.getFacetSearchConfig();
            IndexedType indexedType = context.getIndexedType();
            listeners = this.listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerQueryListener.class);
            context.getAttributes().put("solrfacetsearch.indexerQueryListeners", listeners);
        }
        return listeners;
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
}
