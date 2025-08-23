package com.hybris.datahub.client;

import com.hybris.datahub.PagedDataHubResponse;
import com.hybris.datahub.dto.metadata.TargetSystemData;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class TargetSystemClient extends RestClient
{
    public TargetSystemClient()
    {
        this(null, "");
    }


    public TargetSystemClient(String uri)
    {
        this(null, uri);
    }


    public TargetSystemClient(ClientConfiguration cfg, String uri)
    {
        super(cfg, uri);
    }


    public PagedDataHubResponse<TargetSystemData> getAllTargetSystems(int pageNumber, int pageSize)
    {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap();
        map.putSingle("pageNumber", Integer.toString(pageNumber));
        map.putSingle("pageSize", Integer.toString(pageSize));
        return getPaged("/target-systems/", (GenericType)new Object(this), (MultivaluedMap)map);
    }
}
