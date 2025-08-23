package de.hybris.platform.solrfacetsearch.solr.impl;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.client.HttpClient;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpClientUtil;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;

public class PatchedLBHttpSolrClient extends PatchedLBSolrClient
{
    private final HttpClient httpClient;
    private final boolean clientIsInternal;
    private final ConcurrentHashMap<String, HttpSolrClient> urlToClient = new ConcurrentHashMap<>();
    private final HttpSolrClient.Builder httpSolrClientBuilder;
    private Integer connectionTimeout;
    private volatile Integer soTimeout;


    @Deprecated
    protected PatchedLBHttpSolrClient(HttpSolrClient.Builder httpSolrClientBuilder, HttpClient httpClient, String... solrServerUrl)
    {
        this(((Builder)(new Builder())
                        .withHttpSolrClientBuilder(httpSolrClientBuilder)
                        .withHttpClient(httpClient))
                        .withBaseSolrUrls(solrServerUrl));
    }


    @Deprecated
    protected PatchedLBHttpSolrClient(HttpClient httpClient, ResponseParser parser, String... solrServerUrl)
    {
        this((Builder)((Builder)(new Builder())
                        .withBaseSolrUrls(solrServerUrl)
                        .withResponseParser(parser))
                        .withHttpClient(httpClient));
    }


    protected PatchedLBHttpSolrClient(Builder builder)
    {
        super(builder.baseSolrUrls);
        this.clientIsInternal = (builder.getHttpClient() == null);
        this.httpSolrClientBuilder = builder.httpSolrClientBuilder;
        this.httpClient = (builder.getHttpClient() == null) ? constructClient((String[])builder.baseSolrUrls.toArray((Object[])new String[builder.baseSolrUrls.size()])) : builder.getHttpClient();
        this.connectionTimeout = builder.getConnectionTimeoutMillis();
        this.soTimeout = builder.getSocketTimeoutMillis();
        this.parser = builder.getResponseParser();
        for(String baseUrl : builder.baseSolrUrls)
        {
            this.urlToClient.put(baseUrl, makeSolrClient(baseUrl));
        }
    }


    private HttpClient constructClient(String[] solrServerUrl)
    {
        ModifiableSolrParams params = new ModifiableSolrParams();
        if(solrServerUrl != null && solrServerUrl.length > 1)
        {
            params.set("retry", false);
        }
        else
        {
            params.set("retry", true);
        }
        return (HttpClient)HttpClientUtil.createClient((SolrParams)params);
    }


    protected HttpSolrClient makeSolrClient(String server)
    {
        HttpSolrClient client;
        if(this.httpSolrClientBuilder != null)
        {
            synchronized(this)
            {
                this.httpSolrClientBuilder
                                .withBaseSolrUrl(server)
                                .withHttpClient(this.httpClient);
                if(this.connectionTimeout != null)
                {
                    this.httpSolrClientBuilder.withConnectionTimeout(this.connectionTimeout.intValue());
                }
                if(this.soTimeout != null)
                {
                    this.httpSolrClientBuilder.withSocketTimeout(this.soTimeout.intValue());
                }
                client = this.httpSolrClientBuilder.build();
            }
        }
        else
        {
            HttpSolrClient.Builder clientBuilder = (HttpSolrClient.Builder)((HttpSolrClient.Builder)(new HttpSolrClient.Builder(server)).withHttpClient(this.httpClient)).withResponseParser(this.parser);
            if(this.connectionTimeout != null)
            {
                clientBuilder.withConnectionTimeout(this.connectionTimeout.intValue());
            }
            if(this.soTimeout != null)
            {
                clientBuilder.withSocketTimeout(this.soTimeout.intValue());
            }
            client = clientBuilder.build();
        }
        if(this.requestWriter != null)
        {
            client.setRequestWriter(this.requestWriter);
        }
        if(this.queryParams != null)
        {
            client.setQueryParams(this.queryParams);
        }
        return client;
    }


    @Deprecated
    public void setConnectionTimeout(int timeout)
    {
        this.connectionTimeout = Integer.valueOf(timeout);
        this.urlToClient.values().forEach(client -> client.setConnectionTimeout(timeout));
    }


    @Deprecated
    public void setSoTimeout(int timeout)
    {
        this.soTimeout = Integer.valueOf(timeout);
        this.urlToClient.values().forEach(client -> client.setSoTimeout(timeout));
    }


    @Deprecated
    public Rsp request(Req req) throws SolrServerException, IOException
    {
        PatchedLBSolrClient.Rsp rsp = request((PatchedLBSolrClient.Req)req);
        Rsp result = new Rsp();
        result.rsp = rsp.rsp;
        result.server = rsp.server;
        return result;
    }


    protected SolrClient getClient(String baseUrl)
    {
        HttpSolrClient client = this.urlToClient.get(baseUrl);
        if(client == null)
        {
            return (SolrClient)makeSolrClient(baseUrl);
        }
        return (SolrClient)client;
    }


    public String removeSolrServer(String server)
    {
        this.urlToClient.remove(server);
        return super.removeSolrServer(server);
    }


    public void close()
    {
        super.close();
        if(this.clientIsInternal)
        {
            HttpClientUtil.close(this.httpClient);
        }
    }


    public HttpClient getHttpClient()
    {
        return this.httpClient;
    }
}
