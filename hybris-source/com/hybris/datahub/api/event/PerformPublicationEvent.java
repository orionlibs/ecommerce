package com.hybris.datahub.api.event;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.hybris.datahub.runtime.domain.PublicationAction;
import com.hybris.datahub.runtime.domain.TargetSystemPublication;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.concurrent.Immutable;

@Immutable
public class PerformPublicationEvent extends PublicationProcessEvent implements PerformProcessEvent
{
    private static final long serialVersionUID = 4626494689328477464L;
    private final String systemUrl;
    private final Set<Long> targetPublicationIds;


    public PerformPublicationEvent(long poolId, long actionId, Set<Long> targetPublicationIds, String systemUrl)
    {
        super(poolId, actionId);
        this.targetPublicationIds = targetPublicationIds;
        this.systemUrl = systemUrl;
    }


    public static PerformPublicationEvent createFor(PublicationAction action, String dataHubUrl)
    {
        Preconditions.checkArgument((action != null), "Publication action cannot be null");
        Preconditions.checkArgument((action.getPool() != null), "Data pool for the publication action cannot be null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(dataHubUrl), "Callback URL is required");
        Long actionId = action.getActionId();
        Long poolId = action.getPool().getPoolId();
        Preconditions.checkArgument((action.getActionId() != null), "Publication action must have an ID");
        Preconditions.checkArgument((poolId != null), "Data pool must have an ID");
        Set<Long> publicationIds = (action.getTargetSystemPublications() != null) ? (Set<Long>)action.getTargetSystemPublications().stream().map(TargetSystemPublication::getPublicationId).collect(Collectors.toSet()) : Collections.<Long>emptySet();
        return new PerformPublicationEvent(poolId.longValue(), actionId.longValue(), publicationIds, dataHubUrl);
    }


    public Set<Long> getTargetPublicationIds()
    {
        return this.targetPublicationIds;
    }


    public String getSystemUrl()
    {
        return this.systemUrl;
    }


    public String toString()
    {
        return "PerformPublicationEvent{actionId=" + getActionId() + ", poolId=" +
                        getPoolId() + ", systemUrl='" + this.systemUrl + "', targetPublicationIds=" + this.targetPublicationIds + "}";
    }
}
