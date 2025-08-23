package de.hybris.bootstrap.ddl;

import java.util.concurrent.TimeUnit;

public class TimeFormatter
{
    public static String format(long time)
    {
        return
                        String.format("%02dh:%02dm:%02ds:%02dms", new Object[] {Long.valueOf(TimeUnit.MILLISECONDS.toHours(time)),
                                        Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS
                                                        .toMinutes(TimeUnit.MILLISECONDS.toHours(time))),
                                        Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES
                                                        .toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))),
                                        Long.valueOf(TimeUnit.MILLISECONDS.toMillis(time) - TimeUnit.SECONDS
                                                        .toMillis(TimeUnit.MILLISECONDS.toSeconds(time)))});
    }
}
