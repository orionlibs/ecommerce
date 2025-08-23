package de.hybris.platform.servicelayer.time.impl;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.time.TimeService;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTimeService extends AbstractBusinessService implements TimeService
{
    private I18NService i18nService;


    public void setCurrentTime(Date instant)
    {
        if(instant != null)
        {
            long timeOffset = instant.getTime() - System.currentTimeMillis();
            setTimeOffset(timeOffset);
        }
        else
        {
            resetTimeOffset();
        }
    }


    public Date getCurrentTime()
    {
        Object timeOffsetAttr = getSessionService().getAttribute("timeoffset");
        long timeOffsetMillis = (timeOffsetAttr != null) ? ((Long)timeOffsetAttr).longValue() : 0L;
        return new Date(System.currentTimeMillis() + timeOffsetMillis);
    }


    public Date getCurrentDateWithTimeNormalized()
    {
        long now = getCurrentTime().getTime();
        Long cachedDateValidTo = (Long)getSessionService().getAttribute("currentdateValidTo");
        Long cachedCurrentDate = (Long)getSessionService().getAttribute("currentdate");
        if(cachedCurrentDate == null || cachedDateValidTo == null || cachedDateValidTo.longValue() < now)
        {
            Calendar cal = Calendar.getInstance(this.i18nService.getCurrentTimeZone(), this.i18nService.getCurrentLocale());
            cal.setTimeInMillis(now);
            cal.set(14, 0);
            cal.set(13, 0);
            cal.set(12, 0);
            cal.set(10, 0);
            cal.set(9, 0);
            cachedCurrentDate = Long.valueOf(cal.getTimeInMillis());
            cal.roll(5, true);
            cachedDateValidTo = Long.valueOf(cal.getTimeInMillis());
            getSessionService().setAttribute("currentdate", cachedCurrentDate);
            getSessionService().setAttribute("currentdateValidTo", cachedDateValidTo);
        }
        return new Date(cachedCurrentDate.longValue());
    }


    public long getTimeOffset()
    {
        Object timeOffsetAttr = getSessionService().getAttribute("timeoffset");
        return (timeOffsetAttr != null) ? ((Long)timeOffsetAttr).longValue() : 0L;
    }


    public void resetTimeOffset()
    {
        getSessionService().removeAttribute("timeoffset");
    }


    public void setTimeOffset(long timeOffsetMillis)
    {
        if(timeOffsetMillis != 0L)
        {
            getSessionService().setAttribute("timeoffset", Long.valueOf(timeOffsetMillis));
        }
        else
        {
            resetTimeOffset();
        }
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }
}
