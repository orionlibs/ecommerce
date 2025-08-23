/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.labels.LabelProvider;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class DefaultDateLabelProvider implements LabelProvider<Date>
{
    @Override
    public String getLabel(final Date date)
    {
        if(date == null)
        {
            return StringUtils.EMPTY;
        }
        else
        {
            final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, getFormatLocale());
            final TimeZone timeZone = getFormatTimeZone();
            if(timeZone != null)
            {
                dateFormat.setTimeZone(timeZone);
            }
            return dateFormat.format(date);
        }
    }


    protected TimeZone getFormatTimeZone()
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


    protected Locale getFormatLocale()
    {
        return Locales.getCurrent();
    }


    @Override
    public String getDescription(final Date object)
    {
        return null;
    }


    @Override
    public String getIconPath(final Date object)
    {
        return null;
    }
}
