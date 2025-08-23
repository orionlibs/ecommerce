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
import de.hybris.platform.solrfacetsearch.indexer.ExtendedIndexerBatchListener;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchListener;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultIndexerBatchContextFactory implements IndexerBatchContextFactory<DefaultIndexerBatchContext>
{
    private static final Logger LOG = Logger.getLogger(DefaultIndexerBatchContextFactory.class);
    public static final String LISTENERS_KEY = "solrfacetsearch.indexerBatchListeners";
    public static final String EXTENDED_LISTENERS_KEY = "solrfacetsearch.extendedIndexerListener";
    private final ThreadLocal<DefaultIndexerBatchContext> indexerBatchContext = new ThreadLocal<>();
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


    public DefaultIndexerBatchContext createContext(long indexOperationId, IndexOperation indexOperation, boolean externalIndexOperation, FacetSearchConfig facetSearchConfig, IndexedType indexedType, Collection<IndexedProperty> indexedProperties)
    {
        DefaultIndexerBatchContext context = new DefaultIndexerBatchContext();
        context.setIndexOperationId(indexOperationId);
        context.setIndexOperation(indexOperation);
        context.setExternalIndexOperation(externalIndexOperation);
        context.setFacetSearchConfig(facetSearchConfig);
        context.setIndexedType(indexedType);
        context.setIndexedProperties(indexedProperties);
        createLocalSessionContext();
        this.indexerBatchContext.set(context);
        return context;
    }


    public void prepareContext() throws IndexerException
    {
        DefaultIndexerBatchContext context = this.indexerBatchContext.get();
        context.setStatus(IndexerBatchContext.Status.CREATED);
        executeAfterPrepareListeners(context);
    }


    public void initializeContext() throws IndexerException
    {
        DefaultIndexerBatchContext context = getContext();
        if(IndexerBatchContext.Status.CREATED != context.getStatus())
        {
            throw new IllegalStateException("Context not in status CREATED");
        }
        context.setStatus(IndexerBatchContext.Status.STARTING);
        executeBeforeBatchListeners(context);
        context.setStatus(IndexerBatchContext.Status.EXECUTING);
    }


    public DefaultIndexerBatchContext getContext()
    {
        DefaultIndexerBatchContext context = this.indexerBatchContext.get();
        if(context == null)
        {
            throw new IllegalStateException("There is no current context");
        }
        return context;
    }


    public void destroyContext() throws IndexerException
    {
        DefaultIndexerBatchContext context = getContext();
        context.setStatus(IndexerBatchContext.Status.STOPPING);
        executeAfterBatchListeners(context);
        context.setStatus(IndexerBatchContext.Status.COMPLETED);
        this.indexerBatchContext.remove();
        removeLocalSessionContext();
    }


    public void destroyContext(Exception failureException)
    {
        try
        {
            DefaultIndexerBatchContext context = getContext();
            context.addFailureException(failureException);
            context.setStatus(IndexerBatchContext.Status.FAILED);
            executeAfterBatchErrorListeners(context);
        }
        finally
        {
            this.indexerBatchContext.remove();
            removeLocalSessionContext();
        }
    }


    protected void executeAfterPrepareListeners(DefaultIndexerBatchContext context) throws IndexerException
    {
        List<ExtendedIndexerBatchListener> listeners = getExtendedListeners(context);
        for(ExtendedIndexerBatchListener listener : listeners)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running afterPrepareContext listener for: " + listener.getClass().getCanonicalName());
            }
            listener.afterPrepareContext((IndexerBatchContext)context);
        }
    }


    protected void executeBeforeBatchListeners(DefaultIndexerBatchContext batchContext) throws IndexerException
    {
        List<IndexerBatchListener> listeners = getListeners(batchContext);
        for(IndexerBatchListener listener : listeners)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running beforeBatch listener for: " + listener.getClass().getCanonicalName());
            }
            listener.beforeBatch((IndexerBatchContext)batchContext);
        }
    }


    protected void executeAfterBatchListeners(DefaultIndexerBatchContext batchContext) throws IndexerException
    {
        List<IndexerBatchListener> listeners = getListeners(batchContext);
        for(IndexerBatchListener listener : Lists.reverse(listeners))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running afterBatch listener for: " + listener.getClass().getCanonicalName());
            }
            listener.afterBatch((IndexerBatchContext)batchContext);
        }
    }


    protected void executeAfterBatchErrorListeners(DefaultIndexerBatchContext batchContext)
    {
        List<IndexerBatchListener> listeners = getListeners(batchContext);
        for(IndexerBatchListener listener : Lists.reverse(listeners))
        {
            try
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Running afterBatchError listener for: " + listener.getClass().getCanonicalName());
                }
                listener.afterBatchError((IndexerBatchContext)batchContext);
            }
            catch(Exception exception)
            {
                batchContext.addFailureException(exception);
            }
        }
    }


    protected List<ExtendedIndexerBatchListener> getExtendedListeners(DefaultIndexerBatchContext context)
    {
        List<ExtendedIndexerBatchListener> listeners = (List<ExtendedIndexerBatchListener>)context.getAttributes().get("solrfacetsearch.extendedIndexerListener");
        if(listeners == null)
        {
            FacetSearchConfig contextFacetSearchConfig = context.getFacetSearchConfig();
            IndexedType contextIndexedType = context.getIndexedType();
            listeners = this.listenersFactory.getListeners(contextFacetSearchConfig, contextIndexedType, ExtendedIndexerBatchListener.class);
            context.getAttributes().put("solrfacetsearch.extendedIndexerListener", listeners);
        }
        return listeners;
    }


    protected List<IndexerBatchListener> getListeners(DefaultIndexerBatchContext batchContext)
    {
        List<IndexerBatchListener> listeners = (List<IndexerBatchListener>)batchContext.getAttributes().get("solrfacetsearch.indexerBatchListeners");
        if(listeners == null)
        {
            FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
            IndexedType indexedType = batchContext.getIndexedType();
            listeners = this.listenersFactory.getListeners(facetSearchConfig, indexedType, IndexerBatchListener.class);
            batchContext.getAttributes().put("solrfacetsearch.indexerBatchListeners", listeners);
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
