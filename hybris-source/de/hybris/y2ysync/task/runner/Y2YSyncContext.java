package de.hybris.y2ysync.task.runner;

public class Y2YSyncContext
{
    private final String syncExecutionId;
    private final String uri;
    private final String pool;
    private final String feed;
    private final String autoPublishTargetSystems;


    private Y2YSyncContext(String syncExecutionId, String uri, String feed, String pool, String autoPublishTargetSystems)
    {
        this.syncExecutionId = syncExecutionId;
        this.uri = uri;
        this.pool = pool;
        this.feed = feed;
        this.autoPublishTargetSystems = autoPublishTargetSystems;
    }


    public String getPool()
    {
        return this.pool;
    }


    public String getFeed()
    {
        return this.feed;
    }


    public String getAutoPublishTargetSystems()
    {
        return this.autoPublishTargetSystems;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public String getSyncExecutionId()
    {
        return this.syncExecutionId;
    }


    public String getUri()
    {
        return this.uri;
    }
}
