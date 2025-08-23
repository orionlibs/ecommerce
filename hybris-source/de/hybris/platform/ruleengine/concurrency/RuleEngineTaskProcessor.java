package de.hybris.platform.ruleengine.concurrency;

import java.util.List;
import java.util.function.Consumer;

public interface RuleEngineTaskProcessor<I extends de.hybris.platform.core.model.ItemModel, T extends TaskResult>
{
    TaskExecutionFuture<T> execute(List<I> paramList, Consumer<List<I>> paramConsumer);


    TaskExecutionFuture<T> execute(List<I> paramList, Consumer<List<I>> paramConsumer, long paramLong);
}
