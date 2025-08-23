package de.hybris.platform.solrfacetsearch.indexer.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.common.ListenersFactory;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.ExtendedIndexerListener;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.IndexerListener;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexerContextFactory implements IndexerContextFactory<DefaultIndexerContext>
{
    private static final Logger LOG = Logger.getLogger(DefaultIndexerContextFactory.class);
    public static final String LISTENERS_KEY = "solrfacetsearch.indexerListeners";
    public static final String EXTENDED_LISTENERS_KEY = "solrfacetsearch.extendedIndexerListener";
    private final ThreadLocal<DefaultIndexerContext> indexerContext = new ThreadLocal<>();
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


    public DefaultIndexerContext createContext(long indexOperationId, IndexOperation indexOperation, boolean externalIndexOperation, FacetSearchConfig facetSearchConfig, IndexedType indexedType, Collection<IndexedProperty> indexedProperties)
    {
        DefaultIndexerContext context = new DefaultIndexerContext();
        context.setIndexOperationId(indexOperationId);
        context.setIndexOperation(indexOperation);
        context.setExternalIndexOperation(externalIndexOperation);
        context.setFacetSearchConfig(facetSearchConfig);
        context.setIndexedType(indexedType);
        context.setIndexedProperties(indexedProperties);
        createLocalSessionContext();
        this.indexerContext.set(context);
        return context;
    }


    public void prepareContext() throws IndexerException
    {
        DefaultIndexerContext context = getContext();
        context.setStatus(IndexerContext.Status.CREATED);
        executeAfterPrepareListeners(context);
    }


    public void initializeContext() throws IndexerException
    {
        DefaultIndexerContext context = getContext();
        if(IndexerContext.Status.CREATED != context.getStatus())
        {
            throw new IllegalStateException("Context not in status CREATED");
        }
        context.setStatus(IndexerContext.Status.STARTING);
        executeBeforeIndexListeners(context);
        context.setStatus(IndexerContext.Status.EXECUTING);
    }


    public DefaultIndexerContext getContext()
    {
        DefaultIndexerContext context = this.indexerContext.get();
        if(context == null)
        {
            throw new IllegalStateException("There is no current indexer context");
        }
        return context;
    }


    public void destroyContext() throws IndexerException
    {
        DefaultIndexerContext context = getContext();
        context.setStatus(IndexerContext.Status.STOPPING);
        executeAfterIndexListeners(context);
        context.setStatus(IndexerContext.Status.COMPLETED);
        this.indexerContext.remove();
        removeLocalSessionContext();
    }


    public void destroyContext(Exception failureException)
    {
        try
        {
            DefaultIndexerContext context = getContext();
            context.addFailureException(failureException);
            context.setStatus(IndexerContext.Status.FAILED);
            executeAfterIndexErrorListeners(context);
        }
        finally
        {
            this.indexerContext.remove();
            removeLocalSessionContext();
        }
    }


    protected void executeAfterPrepareListeners(DefaultIndexerContext context) throws IndexerException
    {
        List<ExtendedIndexerListener> listeners = getExtendedListeners(context);
        for(ExtendedIndexerListener listener : listeners)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running afterPrepareContext listener for: " + listener.getClass().getCanonicalName());
            }
            listener.afterPrepareContext((IndexerContext)context);
        }
    }


    protected void executeBeforeIndexListeners(DefaultIndexerContext context) throws IndexerException
    {
        List<IndexerListener> listeners = getListeners(context);
        for(IndexerListener listener : listeners)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running beforeIndex listener for: " + listener.getClass().getCanonicalName());
            }
            listener.beforeIndex((IndexerContext)context);
        }
    }


    protected void executeAfterIndexListeners(DefaultIndexerContext context) throws IndexerException
    {
        List<IndexerListener> listeners = getListeners(context);
        for(IndexerListener listener : Lists.reverse(listeners))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running afterIndex listener for: " + listener.getClass().getCanonicalName());
            }
            listener.afterIndex((IndexerContext)context);
        }
    }


    protected void executeAfterIndexErrorListeners(DefaultIndexerContext context)
    {
        List<IndexerListener> listeners = getListeners(context);
        for(IndexerListener listener : Lists.reverse(listeners))
        {
            try
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Running afterIndexError listener for: " + listener.getClass().getCanonicalName());
                }
                listener.afterIndexError((IndexerContext)context);
            }
            catch(Exception exception)
            {
                context.addFailureException(exception);
            }
        }
    }


    protected List<ExtendedIndexerListener> getExtendedListeners(DefaultIndexerContext context)
    {
        List<ExtendedIndexerListener> listeners = (List<ExtendedIndexerListener>)context.getAttributes().get("solrfacetsearch.extendedIndexerListener");
        if(listeners == null)
        {
            FacetSearchConfig contextFacetSearchConfig = context.getFacetSearchConfig();
            IndexedType contextIndexedType = context.getIndexedType();
            listeners = this.listenersFactory.getListeners(contextFacetSearchConfig, contextIndexedType, ExtendedIndexerListener.class);
            context.getAttributes().put("solrfacetsearch.extendedIndexerListener", listeners);
        }
        return listeners;
    }


    protected List<IndexerListener> getListeners(DefaultIndexerContext context)
    {
        List<IndexerListener> listeners = (List<IndexerListener>)context.getAttributes().get("solrfacetsearch.indexerListeners");
        if(listeners == null)
        {
            FacetSearchConfig facetSearchConfig = context.getFacetSearchConfig();
            IndexedType indexedType = context.getIndexedType();
            listeners = this.listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerListener.class);
            context.getAttributes().put("solrfacetsearch.indexerListeners", listeners);
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
