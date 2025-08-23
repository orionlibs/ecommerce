package com.hybris.datahub.client;

import com.hybris.datahub.PagedDataHubResponse;
import com.hybris.datahub.dto.pool.PoolData;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class DataPoolClient extends RestClient
{
    private static final String URL_PREFIX = "/pools/";


    public DataPoolClient()
    {
        this(null, "");
    }


    public DataPoolClient(String uri)
    {
        this(null, uri);
    }


    public DataPoolClient(ClientConfiguration cfg, String uri)
    {
        super(cfg, uri);
    }


    public PoolData getPool(String poolName)
    {
        return (PoolData)get("/pools/" + poolName, PoolData.class);
    }


    public PagedDataHubResponse<PoolData> getAllPools(int pageNumber, int pageSize)
    {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap();
        map.putSingle("pageNumber", String.valueOf(pageNumber));
        map.putSingle("pageSize", String.valueOf(pageSize));
        return getPaged("/pools/", (GenericType)new Object(this), (MultivaluedMap)map);
    }


    public PoolData createDataPool(String poolName)
    {
        return createDataPool(toPoolData(poolName));
    }


    public PoolData createDataPool(PoolData pool)
    {
        Response response = createPool(pool);
        verifyErrorInResponse(response);
        return (PoolData)response.readEntity(PoolData.class);
    }


    private Response createPool(PoolData poolData)
    {
        log().info("Creating data pool '{}'", poolData.getPoolName());
        return post("/pools/", poolData);
    }


    private static PoolData toPoolData(String name)
    {
        PoolData poolData = new PoolData();
        poolData.setPoolName(name);
        return poolData;
    }
}
