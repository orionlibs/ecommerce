package de.hybris.platform.catalog.jalo.synchronization;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

class SyncScheduleConcurrentWriter extends SyncScheduleWriter
{
    private static final Logger LOG = Logger.getLogger(SyncScheduleConcurrentWriter.class.getName());
    private static final int DEFAULT_QUEUE_FLUSH_SIZE = 512;
    private final ReentrantLock writerLock = new ReentrantLock();
    private final Queue queue = new ConcurrentLinkedQueue();
    private final AtomicLong estimatedQueueSize = new AtomicLong(0L);


    SyncScheduleConcurrentWriter(File file, boolean append, int count, int deadlockCount) throws UnsupportedEncodingException, FileNotFoundException
    {
        super(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file, append)), "UTF-8"), count, deadlockCount);
    }


    public void flush() throws IOException
    {
    }


    protected void flushQueue() throws IOException
    {
        while(true)
        {
            Object line = this.queue.poll();
            if(line == null)
            {
                break;
            }
            if(line instanceof Map)
            {
                super.write((Map)line);
            }
            else
            {
                super.writeSrcLine((String)line);
            }
            this.estimatedQueueSize.decrementAndGet();
        }
        getWriter().flush();
    }


    public void write(Map lineData) throws IOException
    {
        this.queue.offer(lineData);
        if(this.estimatedQueueSize.addAndGet(2L) > 512L)
        {
            try
            {
                this.writerLock.lock();
                flushQueue();
            }
            finally
            {
                this.writerLock.unlock();
            }
        }
    }


    public void writeSrcLine(String scrline) throws IOException
    {
        this.queue.offer(scrline);
        if(this.estimatedQueueSize.addAndGet(2L) > 512L)
        {
            try
            {
                this.writerLock.lock();
                flushQueue();
            }
            finally
            {
                this.writerLock.unlock();
            }
        }
    }


    public void close() throws IOException
    {
        try
        {
            this.writerLock.lock();
            flushQueue();
            super.close();
        }
        finally
        {
            this.writerLock.unlock();
        }
    }
}
