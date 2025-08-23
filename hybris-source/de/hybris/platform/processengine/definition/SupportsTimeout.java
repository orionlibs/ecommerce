package de.hybris.platform.processengine.definition;

import de.hybris.platform.task.RetryLaterException;

public interface SupportsTimeout
{
    String handleTimeout(NodeExecutionContext paramNodeExecutionContext) throws RetryLaterException, NodeExecutionException;
}
