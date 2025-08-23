package com.hybris.datahub.api.event;

import com.google.common.base.Preconditions;
import com.hybris.datahub.runtime.domain.CompositionAction;
import javax.annotation.concurrent.Immutable;

@Immutable
public class PerformCompositionEvent extends CompositionProcessEvent implements PerformProcessEvent
{
    private static final long serialVersionUID = 21140223151031708L;


    public PerformCompositionEvent(long poolId, long actionId)
    {
        super(poolId, actionId);
    }


    public static PerformCompositionEvent createFor(CompositionAction action)
    {
        Preconditions.checkArgument((action.getPool() != null), "Cannot perform composition for action with null data pool");
        Preconditions.checkArgument((action.getPool().getPoolId() != null), "Cannot perform composition in a data pool with null ID");
        Preconditions.checkArgument((action.getActionId() != null), "Cannot perform composition for action without ID");
        return new PerformCompositionEvent(action.getPool().getPoolId().longValue(), action.getActionId().longValue());
    }


    public String toString()
    {
        return "PerformCompositionEvent{actionId=" + getActionId() + ", poolId=" +
                        getPoolId() + "}";
    }
}
