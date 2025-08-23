package com.hybris.datahub.client;

import com.hybris.datahub.PagedDataHubResponse;
import com.hybris.datahub.dto.event.CompositionActionData;
import com.hybris.datahub.dto.event.PoolActionData;
import com.hybris.datahub.dto.filter.CompositionFilterDto;
import com.hybris.datahub.dto.item.ErrorData;
import com.hybris.datahub.log.Log;
import java.util.List;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;
import org.slf4j.Logger;

public class PoolActionClient extends RestClient
{
    private static final Logger LOGGER = (Logger)Log.getLogger(PoolActionClient.class);
    private static final String URL_PREFIX = "/pools/";


    public PoolActionClient()
    {
        this(null, "");
    }


    public PoolActionClient(String uri)
    {
        this(null, uri);
    }


    public PoolActionClient(ClientConfiguration cfg, String uri)
    {
        super(cfg, uri);
    }


    public CompositionActionData initiateCompositionAction(String poolName)
    {
        CompositionActionData initialData = (CompositionActionData)post("/pools/" + poolName + "/compositions/", new CompositionActionData()).readEntity(CompositionActionData.class);
        try
        {
            return pollForCompositionCompletion(initialData.getActionId(), poolName);
        }
        catch(InterruptedException e)
        {
            LOGGER.error("Interrupted while polling for composition completion.", e);
            Thread.currentThread().interrupt();
            return initialData;
        }
        catch(Exception e)
        {
            LOGGER.error("Error polling for composition completion.", e);
            return initialData;
        }
    }


    public CompositionActionData initiateCompositionActionAsync(String poolName)
    {
        return (CompositionActionData)post("/pools/" + poolName + "/compositions/", new CompositionActionData()).readEntity(CompositionActionData.class);
    }


    public PagedDataHubResponse<CompositionActionData> getAllCompositions(int pageNumber, int pageSize, CompositionFilterDto filter)
    {
        MultivaluedMap<String, String> params = createPageParams(pageNumber, pageSize);
        if(filter != null)
        {
            params.putSingle("q", QueryStringFilterFactory.createFromCompositionFilter(filter));
        }
        return getPaged("/compositions/", (GenericType)new Object(this), params);
    }


    public CompositionActionData getComposition(String poolName, Long compositionActionId)
    {
        return (CompositionActionData)get("/pools/" + poolName + "/compositions/" + compositionActionId, CompositionActionData.class);
    }


    public List<PoolActionData> getAllPoolActions(String poolName)
    {
        return get("/pools/" + poolName + "/pool-history/", (GenericType)new Object(this));
    }


    public CompositionActionData pollForCompositionCompletion(Long actionId, String poolName) throws InterruptedException
    {
        CompositionActionData data;
        long startTime = System.currentTimeMillis();
        do
        {
            data = getComposition(poolName, actionId);
            Thread.sleep(500L);
        }
        while("PENDING".equals(data.getStatus()) || ("IN_PROGRESS".equals(data.getStatus()) &&
                        System.currentTimeMillis() - startTime < 60000L));
        return data;
    }


    public PagedDataHubResponse<ErrorData> getCompositionErrors(Long compositionActionId, int pageNumber, int pageSize)
    {
        return getPaged("/compositions/" + compositionActionId + "/errors", (GenericType)new Object(this),
                        createPageParams(pageNumber, pageSize));
    }
}
