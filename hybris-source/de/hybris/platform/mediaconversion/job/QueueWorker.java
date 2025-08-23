package de.hybris.platform.mediaconversion.job;

import java.util.Queue;
import org.apache.log4j.Logger;

class QueueWorker implements Runnable
{
    private static final Logger LOG = Logger.getLogger(QueueWorker.class);
    private final Queue<Runnable> queue;
    private final ExceptionCollector collector;


    QueueWorker(Queue<Runnable> queue, ExceptionCollector collector)
    {
        this.queue = queue;
        if(this.queue == null)
        {
            throw new IllegalArgumentException("Queue must not be null.");
        }
        this.collector = collector;
        if(this.collector == null)
        {
            throw new IllegalArgumentException("Exception collector must not be null.");
        }
    }


    public void run()
    {
        Runnable next = null;
        while((next = nextFromQueue()) != null)
        {
            try
            {
                next.run();
            }
            catch(RuntimeException e)
            {
                LOG.error("Failed to execute runnable '" + next + "'.", e);
                this.collector.collect(e);
            }
        }
        LOG.debug("Nothing more to do.");
    }


    private Runnable nextFromQueue()
    {
        synchronized(this.queue)
        {
            return this.queue.poll();
        }
    }
}
