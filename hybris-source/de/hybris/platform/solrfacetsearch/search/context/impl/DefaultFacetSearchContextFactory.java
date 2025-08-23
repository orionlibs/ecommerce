package de.hybris.platform.solrfacetsearch.search.context.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.common.ListenersFactory;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContextFactory;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFacetSearchContextFactory implements FacetSearchContextFactory<DefaultFacetSearchContext>
{
    private static final Logger LOG = Logger.getLogger(DefaultFacetSearchContextFactory.class);
    public static final String FACET_SEARCH_LISTENERS_KEY = "solrfacetsearch.facetSearchListeners";
    private final ThreadLocal<DefaultFacetSearchContext> facetSearchContext = new ThreadLocal<>();
    private SessionService sessionService;
    private CatalogVersionService catalogVersionService;
    private ListenersFactory listenersFactory;


    public DefaultFacetSearchContext createContext(FacetSearchConfig facetSearchConfig, IndexedType indexedType, SearchQuery searchQuery)
    {
        Collection<CatalogVersionModel> catalogVersions = this.catalogVersionService.getSessionCatalogVersions();
        DefaultFacetSearchContext context = new DefaultFacetSearchContext();
        context.setFacetSearchConfig(facetSearchConfig);
        context.setIndexedType(indexedType);
        context.setSearchQuery(searchQuery);
        context.setParentSessionCatalogVersions(catalogVersions);
        context.setStatus(FacetSearchContext.Status.CREATED);
        if(CollectionUtils.isNotEmpty(indexedType.getSorts()))
        {
            context.setAvailableNamedSorts(new ArrayList(indexedType.getSorts()));
        }
        else
        {
            context.setAvailableNamedSorts(new ArrayList());
        }
        createLocalSessionContext();
        this.facetSearchContext.set(context);
        return context;
    }


    public void initializeContext() throws FacetSearchException
    {
        DefaultFacetSearchContext context = getContext();
        if(FacetSearchContext.Status.CREATED != context.getStatus())
        {
            throw new IllegalStateException("Context not in status CREATED");
        }
        context.setStatus(FacetSearchContext.Status.STARTING);
        executeBeforeFacetSearchListeners(context);
        context.setStatus(FacetSearchContext.Status.EXECUTING);
    }


    public DefaultFacetSearchContext getContext()
    {
        DefaultFacetSearchContext context = this.facetSearchContext.get();
        if(context == null)
        {
            throw new IllegalStateException("There is no current context");
        }
        return context;
    }


    public void destroyContext() throws FacetSearchException
    {
        DefaultFacetSearchContext context = getContext();
        context.setStatus(FacetSearchContext.Status.STOPPING);
        executeAfterFacetSearchListeners(context);
        context.setStatus(FacetSearchContext.Status.COMPLETED);
        this.facetSearchContext.remove();
        removeLocalSessionContext();
    }


    public void destroyContext(Exception failureException)
    {
        try
        {
            DefaultFacetSearchContext context = getContext();
            context.addFailureException(failureException);
            context.setStatus(FacetSearchContext.Status.FAILED);
            executeAfterFacetSearchErrorListeners(context);
        }
        finally
        {
            this.facetSearchContext.remove();
            removeLocalSessionContext();
        }
    }


    protected void executeBeforeFacetSearchListeners(DefaultFacetSearchContext context) throws FacetSearchException
    {
        List<FacetSearchListener> listeners = getListeners(context);
        for(FacetSearchListener listener : listeners)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running beforeFacetSearch listener for: " + listener.getClass().getCanonicalName());
            }
            listener.beforeSearch((FacetSearchContext)context);
        }
    }


    protected void executeAfterFacetSearchListeners(DefaultFacetSearchContext context) throws FacetSearchException
    {
        List<FacetSearchListener> listeners = getListeners(context);
        for(FacetSearchListener listener : Lists.reverse(listeners))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Running afterFacetSearch listener for: " + listener.getClass().getCanonicalName());
            }
            listener.afterSearch((FacetSearchContext)context);
        }
    }


    protected void executeAfterFacetSearchErrorListeners(DefaultFacetSearchContext context)
    {
        List<FacetSearchListener> listeners = getListeners(context);
        for(FacetSearchListener listener : Lists.reverse(listeners))
        {
            try
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Running afterFacetSearchError listener for: " + listener.getClass().getCanonicalName());
                }
                listener.afterSearchError((FacetSearchContext)context);
            }
            catch(Exception exception)
            {
                context.addFailureException(exception);
            }
        }
    }


    protected List<FacetSearchListener> getListeners(DefaultFacetSearchContext context)
    {
        List<FacetSearchListener> listeners = (List<FacetSearchListener>)context.getAttributes().get("solrfacetsearch.facetSearchListeners");
        if(listeners == null)
        {
            FacetSearchConfig facetSearchConfig = context.getFacetSearchConfig();
            IndexedType indexedType = context.getIndexedType();
            listeners = this.listenersFactory.getListeners(facetSearchConfig, indexedType, FacetSearchListener.class);
            context.getAttributes().put("solrfacetsearch.facetSearchListeners", listeners);
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


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
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
}
