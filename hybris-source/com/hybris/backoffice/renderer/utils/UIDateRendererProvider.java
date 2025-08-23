/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.renderer.utils;

import com.hybris.backoffice.dates.DayUtils;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class UIDateRendererProvider
{
    protected static final String LABEL_LATERDATE = "com.hybris.backoffice.dates.laterdate";
    protected static final String LABEL_TODAY = "com.hybris.backoffice.dates.today";
    protected static final String LABEL_UNKNOWN = "com.hybris.backoffice.dates.unknown";
    protected static final String LABEL_YESTERDAY = "com.hybris.backoffice.dates.yesterday";


    /**
     * @param now
     * @param referenceDate
     * @return formatted value for Label. If <code>referenceDate</code> is today in reference to <code>now</code>, then the
     *         returned value is "Today - {time}", when is yesterday then returned value is "Yesterday - {time}", otherwise
     *         is {date} - {time}. "Today" and "Yesterday" labels can be localized. If one of the inputs is null then
     *         "Unknown" is returned.
     */
    public String getFormattedDateLabel(final Date now, final Date referenceDate)
    {
        if(now == null || referenceDate == null)
        {
            return Labels.getLabel(LABEL_UNKNOWN);
        }
        final DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locales.getCurrent());
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locales.getCurrent());
        final TimeZone formatTimeZone = getFormatTimeZone();
        if(formatTimeZone != null)
        {
            timeFormat.setTimeZone(formatTimeZone);
            dateFormat.setTimeZone(formatTimeZone);
        }
        final String labelKey = getI18NLabelKey(now, referenceDate);
        return getLabel(labelKey, timeFormat, dateFormat, referenceDate);
    }


    protected String getI18NLabelKey(final Date now, final Date referenceDate)
    {
        String labelKey = LABEL_LATERDATE;
        if(DayUtils.isYesterday(now, referenceDate))
        {
            labelKey = LABEL_YESTERDAY;
        }
        else if(DayUtils.isToday(now, referenceDate))
        {
            labelKey = LABEL_TODAY;
        }
        return labelKey;
    }


    protected String getLabel(final String labelKey, final DateFormat timeFormat, final DateFormat dateFormat,
                    final Date referenceDate)
    {
        return getLabel(labelKey, timeFormat.format(referenceDate), dateFormat.format(referenceDate));
    }


    protected String getLabel(final String labelKey, final String timeFormat, final String dateFormat)
    {
        return Labels.getLabel(labelKey, new String[]
                        {timeFormat, dateFormat});
    }


    /**
     * @return get preferred timezone of current session.
     *         If current session is null, then null is returned.
     */
    public TimeZone getFormatTimeZone()
    {
        final Session session = Sessions.getCurrent();
        if(session != null)
        {
            final Object timeZone = session.getAttribute(Attributes.PREFERRED_TIME_ZONE);
            if(timeZone instanceof TimeZone)
            {
                return (TimeZone)timeZone;
            }
        }
        return null;
    }
}
