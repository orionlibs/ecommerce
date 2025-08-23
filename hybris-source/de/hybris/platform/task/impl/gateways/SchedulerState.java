package de.hybris.platform.task.impl.gateways;

import java.time.Instant;
import java.util.Date;

public class SchedulerState
{
    private final Instant lastActiveTs;
    private final Instant dbNow;
    private final int version;


    public SchedulerState(Instant lastActiveTs, Instant dbNow, int version)
    {
        this.lastActiveTs = lastActiveTs;
        this.dbNow = dbNow;
        this.version = version;
    }


    @Deprecated(since = "2105", forRemoval = true)
    public SchedulerState(Date lastActiveTs, int version)
    {
        this(lastActiveTs.toInstant(), Instant.now(), version);
    }


    public int getVersion()
    {
        return this.version;
    }


    @Deprecated(since = "2105", forRemoval = true)
    public Date getTimestamp()
    {
        return Date.from(this.lastActiveTs);
    }


    public Instant getLastActiveTs()
    {
        return this.lastActiveTs;
    }


    public Instant getDbNow()
    {
        return this.dbNow;
    }
}
