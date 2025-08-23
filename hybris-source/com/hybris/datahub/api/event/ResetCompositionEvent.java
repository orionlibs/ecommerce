package com.hybris.datahub.api.event;

public class ResetCompositionEvent extends CompositionProcessEvent
{
    public ResetCompositionEvent(long poolId, long actionId)
    {
        super(poolId, actionId);
    }


    public String toString()
    {
        return "ResetCompositionEvent{poolId=" + getPoolId() + ", actionId=" +
                        getActionId() + ", eventName=" +
                        getEventName() + ", processId=" +
                        getProcessId() + "}";
    }
}
