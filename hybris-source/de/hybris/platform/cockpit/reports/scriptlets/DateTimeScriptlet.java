package de.hybris.platform.cockpit.reports.scriptlets;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.ReadableInstant;

public class DateTimeScriptlet extends JRDefaultScriptlet
{
    private static final String DATE_PATTERN_WITH_MONTH = "MMMM yyyy";
    private static final String DATE_PATTERN_WITH_DAY = "dd MMMM yyyy";
    private static final String SPECIFICDAY = "SPECIFICDAY";
    private static final String USERDEFINED = "USERDEFINED";
    private static final String LAST12MONTHS = "LAST12MONTHS";
    private static final String LAST52WEEKS = "LAST52WEEKS";
    private static final String LASTMONTH = "LASTMONTH";
    private static final String LAST4WEEKS = "LAST4WEEKS";
    private static final String LAST7DAYS = "LAST7DAYS";


    public Date getLast7DaysFrom()
    {
        return (new DateMidnight()).minusDays(7).toDate();
    }


    public Date getLast7DaysTo()
    {
        return (new DateMidnight()).toDate();
    }


    public Date getLast4WeeksFrom()
    {
        return (new DateMidnight()).withDayOfWeek(1).minusWeeks(4).toDate();
    }


    public Date getLast4WeeksTo()
    {
        return (new DateMidnight()).withDayOfWeek(1).toDate();
    }


    public Date getLastMonthFrom()
    {
        return (new DateMidnight()).minusMonths(1).withDayOfMonth(1).toDate();
    }


    public Date getLastMonthTo()
    {
        return (new DateMidnight()).withDayOfMonth(1).toDate();
    }


    public Date getLast52WeeksFrom()
    {
        return (new DateMidnight()).withDayOfWeek(1).minusWeeks(52).toDate();
    }


    public Date getLast52WeeksTo()
    {
        return (new DateMidnight()).withDayOfWeek(1).toDate();
    }


    public Date getLast12MonthsFrom()
    {
        return (new DateMidnight()).minusMonths(12).withDayOfMonth(1).toDate();
    }


    public Date getLast12MonthsTo()
    {
        return (new DateMidnight()).withDayOfMonth(1).toDate();
    }


    public Date getSpecificDayFrom(Date specificDay)
    {
        return (new DateMidnight(specificDay)).toDate();
    }


    public Date getSpecificDayTo(Date specificDay)
    {
        return (new DateMidnight(specificDay)).plusDays(1).toDate();
    }


    public Date getYearAgoDate(Date specificDay)
    {
        return (new DateMidnight(specificDay)).minusWeeks(52).toDate();
    }


    public Date getDayBeforeFrom(Date specificDay)
    {
        return (new DateMidnight(specificDay)).minusDays(1).toDate();
    }


    public Date getDayBeforeTo(Date specificDay)
    {
        return (new DateMidnight(specificDay)).toDate();
    }


    public Date getLastMonthFrom(Date specificDay)
    {
        return (new DateMidnight(specificDay)).minusMonths(1).withDayOfMonth(1).toDate();
    }


    public Date getLastMonthTo(Date specificDay)
    {
        return (new DateMidnight(specificDay)).withDayOfMonth(1).toDate();
    }


    public Date currentDateFromHour(Long hour)
    {
        return (new DateTime()).withHourOfDay(hour.intValue()).toDate();
    }


    public String getLabelForItem(Long pk)
    {
        ItemModel item = (ItemModel)UISessionUtils.getCurrentSession().getModelService().get(PK.fromLong(pk.longValue()));
        return UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(
                        UISessionUtils.getCurrentSession().getTypeService().wrapItem(item));
    }


    public String getLabelForItem(long pk)
    {
        return getLabelForItem(Long.valueOf(pk));
    }


    public boolean renderChartForHourGranularity(String timerange)
    {
        if("SPECIFICDAY".equals(timerange))
        {
            return true;
        }
        return false;
    }


    public boolean renderChartForDayGranularity(String timerange, Date dateFrom, Date dateTo)
    {
        if("LAST7DAYS".equals(timerange))
        {
            return true;
        }
        if("USERDEFINED".equals(timerange) && getDaysBetween(dateFrom, dateTo) < 8)
        {
            return true;
        }
        return false;
    }


    public boolean renderChartForWeekGranularity(String timerange, Date dateFrom, Date dateTo)
    {
        int days = getDaysBetween(dateFrom, dateTo);
        if("LASTMONTH".equals(timerange) || "LAST52WEEKS".equals(timerange) || "LAST12MONTHS".equals(timerange) || "LAST4WEEKS"
                        .equals(timerange))
        {
            return true;
        }
        if("USERDEFINED".equals(timerange) && days > 7 && days < 365)
        {
            return true;
        }
        return false;
    }


    public boolean renderChartForMonthGranularity(String timerange, Date dateFrom, Date dateTo)
    {
        if("USERDEFINED".equals(timerange) && getDaysBetween(dateFrom, dateTo) >= 365)
        {
            return true;
        }
        return false;
    }


    public int getDaysBetween(Date dateFrom, Date dateTo)
    {
        return Days.daysBetween((ReadableInstant)new DateMidnight(dateFrom), (ReadableInstant)new DateMidnight(dateTo)).getDays();
    }


    public Date getFrom(String timerange, Date from, Date specificDay)
    {
        if("LAST7DAYS".equals(timerange))
        {
            return getLast7DaysFrom();
        }
        if("LAST4WEEKS".equals(timerange))
        {
            return getLast4WeeksFrom();
        }
        if("LASTMONTH".equals(timerange))
        {
            return getLastMonthFrom();
        }
        if("LAST52WEEKS".equals(timerange))
        {
            return getLast52WeeksFrom();
        }
        if("LAST12MONTHS".equals(timerange))
        {
            return getLast12MonthsFrom();
        }
        if("USERDEFINED".equals(timerange))
        {
            return getSpecificDayFrom(from);
        }
        if("SPECIFICDAY".equals(timerange))
        {
            return getSpecificDayFrom(specificDay);
        }
        return new Date(0L);
    }


    public Date getTo(String timerange, Date dateTo, Date specificDay)
    {
        if("LAST7DAYS".equals(timerange))
        {
            return getLast7DaysTo();
        }
        if("LAST4WEEKS".equals(timerange))
        {
            return getLast4WeeksTo();
        }
        if("LASTMONTH".equals(timerange))
        {
            return getLastMonthTo();
        }
        if("LAST52WEEKS".equals(timerange))
        {
            return getLast52WeeksTo();
        }
        if("LAST12MONTHS".equals(timerange))
        {
            return getLast12MonthsTo();
        }
        if("USERDEFINED".equals(timerange))
        {
            return getSpecificDayTo(dateTo);
        }
        if("SPECIFICDAY".equals(timerange))
        {
            return getSpecificDayTo(specificDay);
        }
        return new Date(0L);
    }


    public Date getFromForPreviousPeriod(String timerange, Date dateFrom, Date dateTo, Date specificDay)
    {
        return (new DateMidnight(getFrom(timerange, dateFrom, specificDay))).minusDays(
                        Days.daysBetween((ReadableInstant)new DateMidnight(getFrom(timerange, dateFrom, specificDay)), (ReadableInstant)new DateMidnight(
                                        getTo(timerange, dateTo, specificDay))).getDays()).toDate();
    }


    public Date getToForPreviousPeriod(String timerange, Date dateFrom, Date dateTo, Date specificDay)
    {
        return (new DateMidnight(getTo(timerange, dateTo, specificDay))).minusDays(
                        Days.daysBetween((ReadableInstant)new DateMidnight(getFrom(timerange, dateFrom, specificDay)), (ReadableInstant)new DateMidnight(
                                        getTo(timerange, dateTo, specificDay))).getDays()).toDate();
    }


    public Date getFromYearAgo(String timerange, Date dateFrom, Date specificDay)
    {
        DateMidnight fromMidnight = new DateMidnight(getFrom(timerange, dateFrom, specificDay));
        return (new DateMidnight(fromMidnight)).withYear(fromMidnight.getYear() - 1).toDate();
    }


    public Date getToYearAgo(String timerange, Date dateTo, Date specificDay)
    {
        DateMidnight toMidnight = new DateMidnight(getTo(timerange, dateTo, specificDay));
        return (new DateMidnight(toMidnight)).withYear(toMidnight.getYear() - 1).toDate();
    }


    public String getCSMSeriesValue(String group, String timerange, Date dateFrom, Date dateTo)
    {
        if("GP1".equals(group))
        {
            if("LAST7DAYS".equals(timerange))
            {
                return getFormattedDate(dateFrom, "dd MMMM yyyy") + " - " + getFormattedDate(dateFrom, "dd MMMM yyyy");
            }
            if("LAST4WEEKS".equals(timerange))
            {
                return getFormattedDate(dateFrom, "dd MMMM yyyy") + " - " + getFormattedDate(dateFrom, "dd MMMM yyyy");
            }
            if("LASTMONTH".equals(timerange))
            {
                return getFormattedDate(dateFrom, "MMMM yyyy") + " - " + getFormattedDate(dateFrom, "MMMM yyyy");
            }
            if("LAST52WEEKS".equals(timerange))
            {
                return getFormattedDate(dateFrom, "dd MMMM yyyy") + " - " + getFormattedDate(dateFrom, "dd MMMM yyyy");
            }
            if("LAST12MONTHS".equals(timerange))
            {
                return getFormattedDate(dateFrom, "MMMM yyyy") + " - " + getFormattedDate(dateFrom, "MMMM yyyy");
            }
            if("USERDEFINED".equals(timerange))
            {
                return getFormattedDate(dateFrom, "dd MMMM yyyy") + " - " + getFormattedDate(dateFrom, "dd MMMM yyyy");
            }
            if("SPECIFICDAY".equals(timerange))
            {
                return getFormattedDate(dateFrom, "dd MMMM yyyy");
            }
        }
        else if("GP2".equals(group))
        {
            return getCSMSeriesValue("GP1", timerange, getYearAgoDate(dateFrom), getYearAgoDate(dateTo));
        }
        return group;
    }


    public String getFormattedDate(Date date, String format)
    {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }
}
