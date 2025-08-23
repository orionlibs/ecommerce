package de.hybris.platform.solrfacetsearch.solr.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.Credentials;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachedSolrClient extends SolrClient
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(CachedSolrClient.class);
    private final SolrClient delegate;
    private final transient Credentials credentials;
    private final transient Consumer<SolrClient> closeMethod;
    private final AtomicInteger consumers;


    public CachedSolrClient(SolrClient delegate)
    {
        this(delegate, null, null);
    }


    public CachedSolrClient(SolrClient delegate, Credentials credentials)
    {
        this(delegate, null, credentials);
    }


    public CachedSolrClient(SolrClient delegate, Consumer<SolrClient> closeMethod, Credentials credentials)
    {
        this.delegate = delegate;
        this.closeMethod = closeMethod;
        this.credentials = credentials;
        this.consumers = new AtomicInteger();
    }


    public NamedList<Object> request(SolrRequest solrRequest, String collection) throws SolrServerException, IOException
    {
        if(this.credentials != null)
        {
            String previousUser = solrRequest.getBasicAuthUser();
            String previousPassword = solrRequest.getBasicAuthPassword();
            try
            {
                solrRequest.setBasicAuthCredentials(this.credentials.getUserPrincipal().getName(), this.credentials.getPassword());
                return this.delegate.request(solrRequest, collection);
            }
            finally
            {
                solrRequest.setBasicAuthCredentials(previousUser, previousPassword);
            }
        }
        return this.delegate.request(solrRequest, collection);
    }


    public void close()
    {
        int consumersNumber = this.consumers.decrementAndGet();
        if(consumersNumber < 0)
        {
            IOUtils.closeQuietly((Closeable)this.delegate);
            try
            {
                if(this.closeMethod != null)
                {
                    this.closeMethod.accept(this.delegate);
                }
            }
            catch(Exception e)
            {
                LOG.error("An error ocurred while closing a Solr client", e);
            }
        }
    }


    public void addConsumer()
    {
        this.consumers.incrementAndGet();
    }


    public SolrClient getDelegate()
    {
        return this.delegate;
    }


    public Consumer<SolrClient> getCloseMethod()
    {
        return this.closeMethod;
    }


    protected Credentials getCredentials()
    {
        return this.credentials;
    }
}
