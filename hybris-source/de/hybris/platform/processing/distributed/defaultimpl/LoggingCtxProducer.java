package de.hybris.platform.processing.distributed.defaultimpl;

import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.logging.TaskLoggingCtx;

public interface LoggingCtxProducer
{
    boolean isDefinedForTask(TaskModel paramTaskModel);


    TaskLoggingCtx createLoggingCtx(TaskModel paramTaskModel);
}
