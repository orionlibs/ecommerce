package de.hybris.platform.servicelayer.time;

import java.util.Date;

public interface TimeService
{
    void setCurrentTime(Date paramDate);


    Date getCurrentTime();


    Date getCurrentDateWithTimeNormalized();


    long getTimeOffset();


    void resetTimeOffset();


    void setTimeOffset(long paramLong);
}
