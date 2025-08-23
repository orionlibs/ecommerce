package com.hybris.datahub.client;

import com.hybris.datahub.dto.count.CanonicalItemStatusCountData;
import com.hybris.datahub.dto.count.CanonicalPublicationStatusCountData;
import com.hybris.datahub.dto.count.ItemStatusCountData;
import com.hybris.datahub.dto.count.RawItemStatusCountData;

public class StatusCountClient extends RestClient
{
    private static final String STATUS_COUNTS = "/status-counts/";
    private static final String POOL_URL_PREFIX = "/pools/";


    public StatusCountClient(String baseUrl)
    {
        this(null, baseUrl);
    }


    public StatusCountClient(ClientConfiguration cfg, String url)
    {
        super(cfg, url);
    }


    public RawItemStatusCountData getRawItemStatusCount(String poolName)
    {
        return (RawItemStatusCountData)get("/pools/" + poolName + "/status-counts/raw", RawItemStatusCountData.class);
    }


    public RawItemStatusCountData getRawItemStatusCount()
    {
        return (RawItemStatusCountData)get("/status-counts/raw", RawItemStatusCountData.class);
    }


    public CanonicalItemStatusCountData getCanonicalItemStatusCount(String poolName)
    {
        return (CanonicalItemStatusCountData)get("/pools/" + poolName + "/status-counts/canonical", CanonicalItemStatusCountData.class);
    }


    public CanonicalPublicationStatusCountData getCanonicalPublicationStatusCount()
    {
        return (CanonicalPublicationStatusCountData)get("/status-counts/canonical-publication", CanonicalPublicationStatusCountData.class);
    }


    public CanonicalPublicationStatusCountData getCanonicalPublicationStatusCount(String poolName)
    {
        return (CanonicalPublicationStatusCountData)get("/pools/" + poolName + "/status-counts/canonical-publication", CanonicalPublicationStatusCountData.class);
    }


    public CanonicalItemStatusCountData getCanonicalItemStatusCount()
    {
        return (CanonicalItemStatusCountData)get("/status-counts/canonical", CanonicalItemStatusCountData.class);
    }


    public ItemStatusCountData getAllStatusCounts()
    {
        return (ItemStatusCountData)get("/status-counts", ItemStatusCountData.class);
    }


    public ItemStatusCountData getAllStatusCounts(String poolName)
    {
        return (ItemStatusCountData)get("/pools/" + poolName + "/status-counts", ItemStatusCountData.class);
    }
}
