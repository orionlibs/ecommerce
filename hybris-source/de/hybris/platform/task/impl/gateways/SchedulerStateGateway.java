package de.hybris.platform.task.impl.gateways;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public interface SchedulerStateGateway extends BaseGateway
{
    default boolean insertSchedulerRow(int version)
    {
        return insertSchedulerRow(Instant.now(), version);
    }


    @Deprecated(since = "2105", forRemoval = true)
    boolean insertSchedulerRow(Instant paramInstant, int paramInt);


    @Deprecated(since = "2105", forRemoval = true)
    boolean updateSchedulerRow(Instant paramInstant1, Instant paramInstant2);


    boolean updateSchedulerRow(Duration paramDuration);


    Optional<SchedulerState> getSchedulerTimestamp();


    default boolean deleteSchedulerRow()
    {
        return false;
    }
}
