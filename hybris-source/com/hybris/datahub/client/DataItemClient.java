package com.hybris.datahub.client;

import com.hybris.datahub.PagedDataHubResponse;
import com.hybris.datahub.dto.feed.FeedData;
import com.hybris.datahub.dto.filter.DataItemFilterDto;
import com.hybris.datahub.dto.item.ItemData;
import com.hybris.datahub.dto.item.ResultData;
import com.hybris.datahub.paging.DataHubPage;
import com.hybris.datahub.paging.DataHubPageable;
import com.hybris.datahub.paging.DefaultDataHubPage;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class DataItemClient extends RestClient
{
    private DataItemQueryStringFactory queryStringFactory;


    public DataItemClient()
    {
        this(null, "");
    }


    public DataItemClient(String uri)
    {
        this(null, uri);
    }


    public DataItemClient(ClientConfiguration cfg, String uri)
    {
        super(cfg, uri);
        this.queryStringFactory = (DataItemQueryStringFactory)new LuceneQueryStringFactory();
    }


    public List<ItemData> findItems(String poolName, String itemType, DataItemFilterDto filter)
    {
        return findItems(poolName, itemType, filter, null).getContent();
    }


    public List<ItemData> findItems(String poolName, String itemType)
    {
        return findItems(poolName, itemType, (DataHubPageable)null).getContent();
    }


    @NotNull
    public DataHubPage<ItemData> findItems(String poolName, String itemType, DataHubPageable pageSpec)
    {
        return findItems(poolName, itemType, (DataItemFilterDto)null, pageSpec);
    }


    @NotNull
    public DataHubPage<ItemData> findItems(String poolName, String itemType, DataItemFilterDto filter, DataHubPageable pageSpec)
    {
        MultivaluedMap<String, String> params = (new QueryParameters(this.queryStringFactory)).forItemFilter(filter).forPageable(pageSpec).asMap();
        PagedDataHubResponse<ItemData> paged = retrieveItems(poolName, itemType, params);
        return DefaultDataHubPage.pageOf(ItemData.class)
                        .forPageable(pageSpec)
                        .withTotalNumberOfElements(paged.getTotalCount())
                        .withData(paged.getItems())
                        .build();
    }


    private PagedDataHubResponse<ItemData> retrieveItems(String poolName, String itemType, MultivaluedMap<String, String> params)
    {
        try
        {
            String url = "/pools/" + poolName + "/items/" + itemType + ".xml";
            return getPaged(url, (GenericType)new Object(this), params);
        }
        catch(Exception e)
        {
            String msg = String.format("Failed to retrieve %s items from pool %s by parameters %s", new Object[] {itemType, poolName, params});
            throw new DataHubClientException(msg, e);
        }
    }


    public List<ItemData> findItems(long targetPublicationId, String itemType)
    {
        String url = "/target-system-publications/" + targetPublicationId + "/items/" + itemType + ".json";
        return get(url, (GenericType)new Object(this));
    }


    public List<ItemData> findItems(String pool, String itemType, String attrName, Object attrValue)
    {
        List<ItemData> items = new LinkedList<>(findItems(pool, itemType, (new DataItemFilterDto.Builder()).build()));
        items.removeIf(e -> !attrValue.equals(e.getAttributeValue(attrName)));
        return items;
    }


    public ResultData deleteItem(String pool, String canonicalItemType, Map<String, String> keyFields)
    {
        String url = "/pools/" + pool + "/items/" + canonicalItemType;
        return deleteItemInternal(keyFields, url, "keyFields");
    }


    public ResultData deleteItemByFeed(FeedData feed, String rawItemType, Map<String, String> rawFields)
    {
        return deleteItemByFeed(feed.getName(), rawItemType, rawFields);
    }


    public ResultData deleteItemByFeed(String feed, String rawItemType, Map<String, String> rawFields)
    {
        String url = "/data-feeds/" + feed + "/types/" + rawItemType;
        return deleteItemInternal(rawFields, url, "rawFields");
    }


    public ResultData deleteItemByFeed(String feed, String rawItemType)
    {
        String url = "/data-feeds/" + feed + "/types/" + rawItemType;
        return delete(url);
    }


    private ResultData deleteItemInternal(Map<String, String> keyFields, String url, String paramName)
    {
        MultivaluedHashMap multivaluedHashMap = new MultivaluedHashMap();
        multivaluedHashMap.add(paramName, QueryStringFilterFactory.asUrlParameterValue(keyFields));
        return delete(url, (MultivaluedMap)multivaluedHashMap);
    }


    public ItemData getCanonicalItem(Long itemId)
    {
        String url = "/canonical-items/" + itemId + ".xml";
        return (ItemData)get(url, ItemData.class);
    }


    public ItemData getRawItem(Long itemId)
    {
        String url = "/raw-items/" + itemId + ".xml";
        return (ItemData)get(url, ItemData.class);
    }


    public void setQueryStringFactory(DataItemQueryStringFactory factory)
    {
        this.queryStringFactory = factory;
    }


    protected DataItemQueryStringFactory getQueryStringFactory()
    {
        return this.queryStringFactory;
    }
}
