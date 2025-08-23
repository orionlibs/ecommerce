package com.hybris.datahub.api.event;

import com.hybris.datahub.composition.CompositionResult;
import javax.annotation.concurrent.Immutable;

@Immutable
public class CompositionCompletedEvent extends CompositionProcessEvent implements ProcessCompletedEvent
{
    private static final long serialVersionUID = 6964909168930039329L;
    private CompositionResult compositionResult;


    public CompositionCompletedEvent(long poolId, long actionId, CompositionResult compositionResult)
    {
        super(poolId, actionId);
        this.compositionResult = compositionResult;
    }


    public CompositionResult getCompositionResult()
    {
        return this.compositionResult;
    }


    public String toString()
    {
        return "CompositionCompletedEvent{actionId=" + getActionId() + ", poolId=" +
                        getPoolId() + ", compositionResult=" + this.compositionResult + "}";
    }
}
