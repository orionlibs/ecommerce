package com.hybris.datahub.client;

import com.hybris.datahub.PagedDataHubResponse;
import com.hybris.datahub.dto.dataloading.DataLoadingActionData;
import com.hybris.datahub.dto.feed.FeedData;
import com.hybris.datahub.dto.filter.DataLoadingFilterDto;
import com.hybris.datahub.dto.pool.PoolData;
import java.util.List;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class DataFeedClient extends RestClient
{
    private final String urlPrefix = "/data-feeds/";


    public DataFeedClient()
    {
        this(null, "");
    }


    public DataFeedClient(String uri)
    {
        this(null, uri);
    }


    public DataFeedClient(ClientConfiguration cfg, String uri)
    {
        super(cfg, uri);
    }


    public FeedData createDataFeed(String feedName, String poolName)
    {
        FeedData feedData = toFeedData(feedName, poolName);
        createDataFeed(feedData);
        return feedData;
    }


    public FeedData createDataFeed(String feedName, String poolName, String poolingStrategy, String description)
    {
        FeedData feedData = toFeedData(feedName, poolName, poolingStrategy, description);
        createDataFeed(feedData);
        return feedData;
    }


    public FeedData toFeedData(String feedName, String poolName)
    {
        FeedData feedData = new FeedData();
        feedData.setName(feedName);
        feedData.setPoolingCondition(poolName);
        feedData.setPoolingStrategy("NAMED_POOL");
        return feedData;
    }


    public FeedData toFeedData(String feedName, String poolName, String poolingStrategy, String description)
    {
        FeedData feedData = new FeedData();
        feedData.setName(feedName);
        feedData.setPoolingCondition(poolName);
        feedData.setPoolingStrategy(poolingStrategy);
        feedData.setDescription(description);
        return feedData;
    }


    public Response createDataFeed(FeedData feedData)
    {
        log().info("Creating data feed '{}' for pool '{}'", feedData.getName(), feedData.getPoolingCondition());
        Response response = post("/data-feeds/", feedData);
        verifyErrorInResponse(response);
        return response;
    }


    public FeedData getDataFeed(String feedName)
    {
        return (FeedData)get("/data-feeds/" + feedName, FeedData.class);
    }


    public List<FeedData> getAllDataFeeds()
    {
        return get("/data-feeds/", MediaType.APPLICATION_XML_TYPE, (GenericType)new Object(this));
    }


    public List<PoolData> getPoolsForFeed(String feedName)
    {
        return get("/data-feeds/" + feedName + "/pools", MediaType.APPLICATION_XML_TYPE, (GenericType)new Object(this));
    }


    public PagedDataHubResponse<DataLoadingActionData> getDataLoadingActions(int pageNumber, int pageSize, DataLoadingFilterDto filter)
    {
        MultivaluedHashMap multivaluedHashMap = new MultivaluedHashMap();
        multivaluedHashMap.putSingle("pageNumber", Integer.toString(pageNumber));
        multivaluedHashMap.putSingle("pageSize", Integer.toString(pageSize));
        if(filter != null)
        {
            multivaluedHashMap.putSingle("q", QueryStringFilterFactory.createFromStatusFilter(filter));
        }
        return getPaged("/data-feeds/data-loads/", (GenericType)new Object(this), (MultivaluedMap)multivaluedHashMap);
    }
}
