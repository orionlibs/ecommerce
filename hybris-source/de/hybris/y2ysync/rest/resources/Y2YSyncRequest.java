package de.hybris.y2ysync.rest.resources;

import de.hybris.y2ysync.task.runner.Y2YSyncContext;
import java.util.List;

public class Y2YSyncRequest
{
    private final String syncExecutionId;
    private final String sourcePlatformUrl;
    private final List<DataStream> dataStreams;
    private final Y2YSyncContext syncContext;


    public Y2YSyncRequest(String syncExecutionId, String sourcePlatformUrl, List<DataStream> dataStreams)
    {
        this(syncExecutionId, sourcePlatformUrl, dataStreams, Y2YSyncContext.builder()
                        .withFeed("Y2YSYNC_FEED")
                        .withPool("Y2YSYNC_POOL")
                        .withAutoPublishTargetSystems("")
                        .build());
    }


    public Y2YSyncRequest(String syncExecutionId, String sourcePlatformUrl, List<DataStream> dataStreams, Y2YSyncContext syncContext)
    {
        this.syncExecutionId = syncExecutionId;
        this.sourcePlatformUrl = sourcePlatformUrl;
        this.dataStreams = dataStreams;
        this.syncContext = syncContext;
    }


    public String getSyncExecutionId()
    {
        return this.syncExecutionId;
    }


    public List<DataStream> getDataStreams()
    {
        return this.dataStreams;
    }


    public String toString()
    {
        return "Y2YSyncRequest{syncExecutionId='" + this.syncExecutionId + "', dataStreams='" + this.dataStreams + "'}";
    }


    public String getSourcePlatformUrl()
    {
        return this.sourcePlatformUrl;
    }


    public String getPool()
    {
        return this.syncContext.getPool();
    }


    public String getFeed()
    {
        return this.syncContext.getFeed();
    }


    public String getAutoPublishTargetSystems()
    {
        return this.syncContext.getAutoPublishTargetSystems();
    }
}
