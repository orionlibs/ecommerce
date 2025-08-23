package de.hybris.platform.processing.distributed.defaultimpl;

import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.logging.TaskLoggingCtx;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class DistributedProcessLoggingCtxFactory
{
    @Autowired
    private List<LoggingCtxProducer> loggingCtxProducers;


    public TaskLoggingCtx createLoggingCtxForTask(TaskModel task)
    {
        for(LoggingCtxProducer producer : this.loggingCtxProducers)
        {
            if(producer.isDefinedForTask(task))
            {
                return producer.createLoggingCtx(task);
            }
        }
        return () -> {
        };
    }
}
