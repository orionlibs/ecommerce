package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.util.CSVWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

public class SyncScheduleWriter extends CSVWriter implements Flushable
{
    private volatile int count = 0;
    private volatile int deadlockCount = 0;


    public SyncScheduleWriter(Writer writer)
    {
        this(writer, 0, 0);
    }


    public SyncScheduleWriter(Writer writer, int count, int deadlockCount)
    {
        super(writer);
        this.count = count;
        this.deadlockCount = deadlockCount;
    }


    public void write(SyncSchedule schedule) throws IOException
    {
        write(schedule.toCsv());
        this.count++;
        if(schedule.isDeadlockVictim())
        {
            this.deadlockCount++;
        }
        flush();
    }


    public void flush() throws IOException
    {
        getWriter().flush();
    }


    public int getCount()
    {
        return this.count;
    }


    public int getDeadlockCount()
    {
        return this.deadlockCount;
    }
}
