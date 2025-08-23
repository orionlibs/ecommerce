package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.util.CSVReader;
import java.io.Reader;
import java.util.Map;

public class SyncScheduleReader extends CSVReader
{
    private final int count;


    public SyncScheduleReader(Reader reader, int count)
    {
        super(reader);
        this.count = count;
    }


    public int getScheduleCount()
    {
        return this.count;
    }


    public SyncSchedule getScheduleFromLine()
    {
        return createSchedule(getLine());
    }


    protected SyncSchedule createSchedule(Map<Integer, String> line)
    {
        return new SyncSchedule(line);
    }
}
