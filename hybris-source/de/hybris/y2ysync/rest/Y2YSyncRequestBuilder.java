package de.hybris.y2ysync.rest;

import de.hybris.y2ysync.rest.resources.DataStream;
import de.hybris.y2ysync.rest.resources.Y2YSyncRequest;
import de.hybris.y2ysync.task.runner.Y2YSyncContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Y2YSyncRequestBuilder
{
    private final List<DataStream> dataStreams = new ArrayList<>();
    private String syncExecutionId;
    private String homeUrl;
    private String feed;
    private String pool;
    private String autoPublishTargetSystems;


    public static Y2YSyncRequestBuilder builder()
    {
        return new Y2YSyncRequestBuilder();
    }


    public Y2YSyncRequestBuilder withSyncExecutionId(String syncExecutionId)
    {
        this.syncExecutionId = syncExecutionId;
        return this;
    }


    public Y2YSyncRequestBuilder withFeed(String feed)
    {
        this.feed = feed;
        return this;
    }


    public Y2YSyncRequestBuilder withPool(String pool)
    {
        this.pool = pool;
        return this;
    }


    public Y2YSyncRequestBuilder withHomeUrl(String homeUrl)
    {
        this.homeUrl = homeUrl;
        return this;
    }


    public Y2YSyncRequestBuilder withAutoPublishTargetSystems(String autoPublishTargetSystems)
    {
        this.autoPublishTargetSystems = autoPublishTargetSystems;
        return this;
    }


    public List<DataStream> getDataStreams()
    {
        return this.dataStreams;
    }


    public Y2YSyncRequest build()
    {
        Y2YSyncContext ctx = Y2YSyncContext.builder().withFeed(this.feed).withPool(this.pool).withAutoPublishTargetSystems(this.autoPublishTargetSystems).build();
        return new Y2YSyncRequest(this.syncExecutionId, this.homeUrl, this.dataStreams, ctx);
    }


    public Optional<DataStream> findDataStreamByColumnsAndType(String columns, String type, boolean delete)
    {
        return this.dataStreams.stream()
                        .filter(m -> (m.getColumns().equals(columns) && m.getItemType().equals(type) && m.isDelete() == delete))
                        .findFirst();
    }


    public void withDataStream(DataStream dataStream)
    {
        this.dataStreams.add(dataStream);
    }


    public void addAll(Y2YSyncRequestBuilder builder)
    {
        this.dataStreams.addAll(builder.getDataStreams());
    }
}
