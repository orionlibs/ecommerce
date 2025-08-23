package de.hybris.platform.personalizationservices.stub;

import de.hybris.platform.servicelayer.time.TimeService;
import java.util.Date;

public class MockTimeService implements TimeService
{
    private Date time;


    public MockTimeService()
    {
        setCurrentTime(new Date(0L));
    }


    public MockTimeService(Date instant)
    {
        setCurrentTime(instant);
    }


    public void setCurrentTime(Date instant)
    {
        this.time = instant;
    }


    public Date getCurrentTime()
    {
        return this.time;
    }


    public long getTimeOffset()
    {
        return 0L;
    }


    public void resetTimeOffset()
    {
    }


    public void setTimeOffset(long timeOffsetMillis)
    {
    }


    public Date getCurrentDateWithTimeNormalized()
    {
        return this.time;
    }
}
