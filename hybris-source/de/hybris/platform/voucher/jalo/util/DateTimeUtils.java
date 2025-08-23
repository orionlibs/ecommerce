package de.hybris.platform.voucher.jalo.util;

import com.google.common.base.Preconditions;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import java.text.DateFormat;
import java.util.Calendar;

public class DateTimeUtils
{
    public static DateFormat getDateFormat(int dateStyle)
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        DateFormat df = DateFormat.getDateInstance(dateStyle, ctx.getLocale());
        df.setCalendar(Calendar.getInstance(ctx.getTimeZone(), ctx.getLocale()));
        return df;
    }
}
