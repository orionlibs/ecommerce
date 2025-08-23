package com.hybris.datahub.client;

import com.hybris.datahub.PagedDataHubResponse;
import com.hybris.datahub.dto.event.PoolActionData;
import com.hybris.datahub.dto.event.PublicationActionData;
import com.hybris.datahub.dto.filter.DataLoadingFilterDto;
import com.hybris.datahub.dto.filter.TargetSystemPublicationFilterDto;
import com.hybris.datahub.dto.item.ErrorData;
import com.hybris.datahub.dto.publication.CanonicalItemPublicationStatusData;
import com.hybris.datahub.dto.publication.TargetSystemPublicationData;
import com.hybris.datahub.paging.DataHubPage;
import com.hybris.datahub.paging.DataHubPageable;
import com.hybris.datahub.paging.DefaultDataHubPage;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublicationClient extends RestClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationClient.class);
    private static final String POOLS = "/pools/";
    private static final String PUBLICATIONS = "/publications/";
    private static final String TARGET_SYSTEM_PUBLICATIONS = "/target-system-publications/";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE_NUMBER = "pageNumber";


    public PublicationClient(String uri)
    {
        this(null, uri);
    }


    public PublicationClient(ClientConfiguration cfg, String uri)
    {
        super(cfg, uri);
    }


    public PublicationActionData triggerExportRetryToCoreSystem(List<String> targetSystemNames, String poolName)
    {
        return triggerPublication(targetSystemNames, poolName, PoolActionData.ActionType.RETRY_ITEMS_PUBLICATION);
    }


    public PublicationActionData triggerExportToCoreSystem(List<String> targetSystemNames, String poolName)
    {
        return triggerPublication(targetSystemNames, poolName, PoolActionData.ActionType.PUBLICATION);
    }


    private PublicationActionData triggerPublication(List<String> targetSystemNames, String poolName, PoolActionData.ActionType actionType)
    {
        PublicationActionData initialData = triggerPublicationAsync(targetSystemNames, poolName, actionType);
        try
        {
            return pollForPublicationCompletion(initialData.getActionId(), poolName);
        }
        catch(InterruptedException e)
        {
            LOGGER.error("Interrupted while polling for publication completion.", e);
            Thread.currentThread().interrupt();
            return initialData;
        }
        catch(Exception e)
        {
            LOGGER.error("Error polling for publication completion.", e);
            return initialData;
        }
    }


    public PublicationActionData triggerPublicationAsync(List<String> targetSystemNames, String poolName, PoolActionData.ActionType actionType)
    {
        PublicationActionData publicationData = toPublicationActionData(targetSystemNames, poolName, actionType);
        Response response = post("/pools/" + poolName + "/publications", publicationData);
        if(response.getStatus() == Response.Status.OK.getStatusCode())
        {
            return (PublicationActionData)response.readEntity(PublicationActionData.class);
        }
        String msg = (String)response.readEntity(String.class);
        throw new DataHubClientException(response.getStatusInfo().getReasonPhrase() + ": " + response.getStatusInfo().getReasonPhrase());
    }


    @Deprecated(since = "ages", forRemoval = true)
    public List<PublicationActionData> getAllPublications(String poolName)
    {
        List<PublicationActionData> actions = getPublications(poolName, null).getContent();
        actions.sort((action1, action2) -> (int)(action1.getActionId().longValue() - action2.getActionId().longValue()));
        return actions;
    }


    @NotNull
    public DataHubPage<PublicationActionData> getPublications(String poolName, DataHubPageable pageSpec)
    {
        MultivaluedMap<String, String> params = (new QueryParameters()).forPageable(pageSpec).asMap();
        PagedDataHubResponse<PublicationActionData> paged = retrieveItems(poolName, params);
        return DefaultDataHubPage.pageOf(PublicationActionData.class)
                        .forPageable(pageSpec)
                        .withTotalNumberOfElements(paged.getTotalCount())
                        .withData(paged.getItems())
                        .build();
    }


    private PagedDataHubResponse<PublicationActionData> retrieveItems(String poolName, MultivaluedMap<String, String> params)
    {
        String url = "/pools/" + poolName + "/publications";
        return getPaged(url, (GenericType)new Object(this), params);
    }


    public PublicationActionData getPublication(String poolName, Long actionId)
    {
        return (PublicationActionData)get("/pools/" + poolName + "/publications/" + actionId, PublicationActionData.class);
    }


    public List<TargetSystemPublicationData> getTargetSystemPublications(String poolName, Long actionId)
    {
        return get("/pools/" + poolName + "/publications/" + actionId + "/target-system-publications/", (GenericType)new Object(this));
    }


    public PagedDataHubResponse<TargetSystemPublicationData> getTargetSystemPublications(int pageNumber, int pageSize, TargetSystemPublicationFilterDto filter)
    {
        MultivaluedHashMap multivaluedHashMap = new MultivaluedHashMap();
        multivaluedHashMap.putSingle("pageNumber", Integer.toString(pageNumber));
        multivaluedHashMap.putSingle("pageSize", Integer.toString(pageSize));
        if(filter != null)
        {
            multivaluedHashMap.putSingle("q", QueryStringFilterFactory.createFromTargetSystemPublicationFilter(filter));
        }
        return getPaged("/target-system-publications/", (GenericType)new Object(this), (MultivaluedMap)multivaluedHashMap);
    }


    public PagedDataHubResponse<TargetSystemPublicationData> getAllTargetSystemPublications(int pageNumber, int pageSize)
    {
        return getTargetSystemPublications(pageNumber, pageSize, null);
    }


    public PagedDataHubResponse<ErrorData> getPublicationErrors(Long publicationId, int pageNumber, int pageSize)
    {
        MultivaluedHashMap multivaluedHashMap = new MultivaluedHashMap();
        multivaluedHashMap.putSingle("pageNumber", Integer.toString(pageNumber));
        multivaluedHashMap.putSingle("pageSize", Integer.toString(pageSize));
        return getPaged("/target-system-publications/" + publicationId + "/errors", (GenericType)new Object(this), (MultivaluedMap)multivaluedHashMap);
    }


    public PagedDataHubResponse<CanonicalItemPublicationStatusData> getPublicationCanonicalItemPublicationStatuses(String poolName, Long actionId, Long publicationId, int pageNumber, int pageSize)
    {
        return getPublicationCanonicalItemPublicationStatuses(poolName, actionId, publicationId, pageNumber, pageSize, (new DataLoadingFilterDto.Builder())
                        .build());
    }


    public PagedDataHubResponse<CanonicalItemPublicationStatusData> getPublicationCanonicalItemPublicationStatuses(String poolName, Long actionId, Long publicationId, int pageNumber, int pageSize, DataLoadingFilterDto filter)
    {
        MultivaluedHashMap multivaluedHashMap = new MultivaluedHashMap();
        multivaluedHashMap.putSingle("pageNumber", Integer.toString(pageNumber));
        multivaluedHashMap.putSingle("pageSize", Integer.toString(pageSize));
        if(filter != null)
        {
            multivaluedHashMap.putSingle("q", QueryStringFilterFactory.createFromStatusFilter(filter));
        }
        return getPaged("/pools/" + poolName + "/publications/" + actionId + "/target-system-publications/" + publicationId + "/item-statuses", (GenericType)new Object(this), (MultivaluedMap)multivaluedHashMap);
    }


    public int countPublications(String pool)
    {
        try
        {
            return getAllPublications(pool).size();
        }
        catch(Exception e)
        {
            LOGGER.trace("Pool '{}' not created yet.", pool, e);
            return 0;
        }
    }


    private static PublicationActionData toPublicationActionData(List<String> targetSystemNames, String poolName, PoolActionData.ActionType actionType)
    {
        PublicationActionData exportData = new PublicationActionData();
        exportData.setPoolName(poolName);
        exportData.setTargetSystemPublications(toTargetSystemPublications(targetSystemNames, exportData));
        exportData.setType(actionType.toString());
        return exportData;
    }


    private static List<TargetSystemPublicationData> toTargetSystemPublications(List<String> targetSystemNames, PublicationActionData exportData)
    {
        List<TargetSystemPublicationData> targetSystemPublicationList = new ArrayList<>();
        for(String targetSystemName : targetSystemNames)
        {
            TargetSystemPublicationData targetSystemPublication = new TargetSystemPublicationData();
            targetSystemPublication.setTargetSystemName(targetSystemName);
            targetSystemPublication.setPublicationId(exportData.getActionId());
            targetSystemPublicationList.add(targetSystemPublication);
        }
        return targetSystemPublicationList;
    }


    public PublicationActionData pollForPublicationCompletion(String poolName, int actionNum, int timeOutInSec) throws InterruptedException
    {
        long startTime = System.currentTimeMillis();
        int timeoutMillisec = timeOutInSec * 1000;
        PublicationActionData action = waitForPublicationToStart(poolName, actionNum, timeoutMillisec);
        long remainingTimeout = startTime + timeoutMillisec - System.currentTimeMillis();
        return (action != null) ? waitForCompletion(action.getActionId(), poolName, remainingTimeout) : null;
    }


    private PublicationActionData waitForPublicationToStart(String pool, int actionCnt, long timeout) throws InterruptedException
    {
        long startTime = System.currentTimeMillis();
        List<PublicationActionData> actions = getAllPublications(pool);
        long elapsedTime = 0L;
        for(; actions.size() < actionCnt && elapsedTime < timeout;
                        elapsedTime = System.currentTimeMillis() - startTime)
        {
            Thread.sleep(1000L);
            actions = getAllPublications(pool);
        }
        return (actions.size() < actionCnt) ? null : actions.get(actionCnt - 1);
    }


    public PublicationActionData pollForPublicationCompletion(Long actionId, String poolName) throws InterruptedException
    {
        return pollForPublicationCompletion(actionId, poolName, 60);
    }


    public PublicationActionData pollForPublicationCompletion(Long actionId, String poolName, int timeoutInSec) throws InterruptedException
    {
        return waitForCompletion(actionId, poolName, (timeoutInSec * 1000));
    }


    private PublicationActionData waitForCompletion(Long actionId, String poolName, long timeoutMillisec) throws InterruptedException
    {
        PublicationActionData data;
        long elapsedMillisec, startTime = System.currentTimeMillis();
        do
        {
            data = getPublication(poolName, actionId);
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug(data.toString());
            }
            Thread.sleep(500L);
            elapsedMillisec = System.currentTimeMillis() - startTime;
        }
        while(isPublicationInProgress(data) && elapsedMillisec < timeoutMillisec);
        return data;
    }


    private static boolean isPublicationInProgress(PublicationActionData action)
    {
        String status = action.getStatus();
        return (status == null || "IN_PROGRESS".equals(status) || "PENDING".equals(status));
    }
}
