package de.hybris.platform.servicelayer.event;

import java.io.Serializable;
import java.util.Objects;

public interface ClusterAwareEvent extends Serializable
{
    @Deprecated(since = "ages")
    default boolean publish(int sourceNodeId, int targetNodeId)
    {
        throw new UnsupportedOperationException("This method is deprecated. Use #canPublish(PublishEventContext) method instead.");
    }


    default boolean canPublish(PublishEventContext publishEventContext)
    {
        Objects.requireNonNull(publishEventContext, "publishEventContext is required");
        return publish(publishEventContext.getSourceNodeId(), publishEventContext.getTargetNodeId());
    }
}
