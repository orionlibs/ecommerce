package com.hybris.backoffice.dates;

import java.util.Date;
import org.apache.commons.lang.time.DateUtils;

public final class DayUtils
{
    public static boolean isToday(Date now, Date referenceDate)
    {
        return DateUtils.isSameDay(referenceDate, now);
    }


    public static boolean isYesterday(Date now, Date referenceDate)
    {
        return DateUtils.isSameDay(DateUtils.addDays(referenceDate, 1), now);
    }
}
