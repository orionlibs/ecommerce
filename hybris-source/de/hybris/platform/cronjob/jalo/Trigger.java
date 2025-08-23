package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.task.jalo.TriggerTask;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.localization.Localization;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronExpression;

public class Trigger extends GeneratedTrigger
{
    private static final int IGNORE_VALUE = -1;
    private static final Logger LOG = Logger.getLogger(Trigger.class.getName());
    private static final Map<String, Integer> DAYS_OF_WEEK_MAP;

    static
    {
        FastHashMap<String, Integer> fastHashMap = new FastHashMap();
        fastHashMap.put(GeneratedCronJobConstants.Enumerations.DayOfWeek.SUNDAY, Integer.valueOf(1));
        fastHashMap.put(GeneratedCronJobConstants.Enumerations.DayOfWeek.MONDAY, Integer.valueOf(2));
        fastHashMap.put(GeneratedCronJobConstants.Enumerations.DayOfWeek.TUESDAY, Integer.valueOf(3));
        fastHashMap.put(GeneratedCronJobConstants.Enumerations.DayOfWeek.WEDNESDAY, Integer.valueOf(4));
        fastHashMap.put(GeneratedCronJobConstants.Enumerations.DayOfWeek.THURSDAY, Integer.valueOf(5));
        fastHashMap.put(GeneratedCronJobConstants.Enumerations.DayOfWeek.FRIDAY, Integer.valueOf(6));
        fastHashMap.put(GeneratedCronJobConstants.Enumerations.DayOfWeek.SATURDAY, Integer.valueOf(7));
        DAYS_OF_WEEK_MAP = Collections.unmodifiableMap((Map<? extends String, ? extends Integer>)fastHashMap);
    }

    private static final int PULSE_SECONDS = Config.getInt("cronjob.trigger.interval", 60);


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        adjustInitialAttributes(allAttributes);
        Object cronJobInstance = allAttributes.get("cronJob");
        Object jobInstance = allAttributes.get("job");
        if(cronJobInstance == null && jobInstance == null)
        {
            throw new JaloInvalidParameterException("Cannot create trigger! No value for CronJob OR Job is given. Need only one value!", 0);
        }
        if(cronJobInstance != null && jobInstance != null)
        {
            throw new JaloInvalidParameterException("Cannot create trigger! Too many values, provide a CronJob OR a Job value only!", 0);
        }
        if(jobInstance != null && !(jobInstance instanceof TriggerableJob) && !(jobInstance instanceof de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob))
        {
            throw new JaloInvalidParameterException("The given Job for this Trigger must implement the TriggerableJob interface or be a ServicelayerJob ! jobInstance.getClass():" + jobInstance
                            .getClass() + " jobInstance:" + jobInstance, 0);
        }
        return super.createItem(ctx, type, allAttributes);
    }


    private void adjustInitialAttributes(Item.ItemAttributeMap initialAttributes) throws JaloBusinessException
    {
        if(initialAttributes.get("active") == null)
        {
            initialAttributes.put("active", Boolean.FALSE);
        }
        if(initialAttributes.get("second") == null)
        {
            initialAttributes.put("second", Integer.valueOf(-1));
        }
        if(initialAttributes.get("minute") == null)
        {
            initialAttributes.put("minute", Integer.valueOf(-1));
        }
        if(initialAttributes.get("hour") == null)
        {
            initialAttributes.put("hour", Integer.valueOf(-1));
        }
        if(initialAttributes.get("day") == null)
        {
            initialAttributes.put("day", Integer.valueOf(-1));
        }
        if(initialAttributes.get("month") == null)
        {
            initialAttributes.put("month", Integer.valueOf(-1));
        }
        if(initialAttributes.get("year") == null)
        {
            initialAttributes.put("year", Integer.valueOf(-1));
        }
        if(initialAttributes.get("relative") == null)
        {
            initialAttributes.put("relative", Boolean.FALSE);
        }
        if(initialAttributes.get("activationTime") == null)
        {
            initialAttributes.put("activationTime", new Date(System.currentTimeMillis()));
        }
    }


    boolean activateForTest(long currentTime)
    {
        return activateInternal(currentTime, Mode.TESTING);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void activate()
    {
        activateInternal(System.currentTimeMillis(), Mode.REAL);
    }


    private boolean activateInternal(long currentTime, Mode mode)
    {
        Date activationTime = getActivationTime();
        CronJob cronJob = getCronJobForExecution();
        Integer maxOkDelaySec = getMaxAcceptableDelay();
        boolean delayOk = (maxOkDelaySec == null || maxOkDelaySec.intValue() == -1 || activationTime.getTime() + (maxOkDelaySec.intValue() * 1000) > currentTime);
        Date nextOccurrenceTime = null;
        if(delayOk)
        {
            if(StringUtils.isEmpty(getCronExpression()))
            {
                nextOccurrenceTime = getNextRunTime(activationTime, currentTime, cronJob, mode);
            }
            else
            {
                nextOccurrenceTime = getNextRunTimeBasedOnExpression(currentTime, cronJob, mode);
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Next cronJob time calculated '" + getCronExpression() + "' is :" + nextOccurrenceTime);
            }
            if(nextOccurrenceTime != null && mode == Mode.REAL)
            {
                if(LOG.isDebugEnabled())
                {
                    logDebug("Start CronJob " + cronJob.getCode() + " at " + (new Date(currentTime)).toString());
                }
                JaloSession js = getSession();
                SessionContext lctx = js.createLocalSessionContext();
                try
                {
                    lctx.setAttribute("cronjob.scheduled", Boolean.TRUE);
                    lctx.setAttribute("cronjob.scheduled.by", getPK());
                    cronJob.getJob().perform(cronJob, Job.Synchronicity.ASYNCHRONOUS);
                }
                finally
                {
                    js.removeLocalSessionContext();
                }
            }
        }
        if(StringUtils.isEmpty(getCronExpression()))
        {
            setTimeOfNextOccurrence(currentTime, mode, cronJob);
        }
        else if(nextOccurrenceTime != null)
        {
            setTimeOfNextOccurrence(nextOccurrenceTime.getTime(), mode, cronJob);
        }
        return (nextOccurrenceTime != null && delayOk);
    }


    private Date getNextRunTime(Date activationTime, long currentTime, CronJob cronJob, Mode testmode)
    {
        Job job = null;
        try
        {
            job = cronJob.getJob();
        }
        catch(JaloSystemException exp)
        {
            LOG.warn("CronJob '" + cronJob
                            .getCode() + "' cannot be triggered, because its Job is not valid. This could be due to a migration. Ask support@hybris.de for further information.");
            return null;
        }
        if(validateActivationTime(activationTime, currentTime, cronJob, testmode, job) == null)
        {
            return null;
        }
        return activationTime;
    }


    private Date getNextRunTimeBasedOnExpression(long currentTime, CronJob cronJob, Mode testmode)
    {
        Date activationTime = null;
        Job job = null;
        try
        {
            job = cronJob.getJob();
        }
        catch(JaloSystemException exp)
        {
            LOG.warn("CronJob '" + cronJob
                            .getCode() + "' cannot be triggered, because its Job is not valid. This could be due to a migration. Ask support@hybris.de for further information.");
            return null;
        }
        if(!StringUtils.isEmpty(getCronExpression()))
        {
            try
            {
                CronExpression expression = new CronExpression(getCronExpression());
                activationTime = expression.getNextValidTimeAfter(new Date(currentTime));
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Calculated next activation time for a expression '" + getCronExpression() + "' is :" + activationTime);
                }
            }
            catch(ParseException e)
            {
                LOG.error("Provided cronexpression '" + getCronExpression() + "' is not in correct format details, " + e
                                .getMessage());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e);
                }
            }
        }
        if(validateActivationTime(activationTime, currentTime, cronJob, testmode, job) == null)
        {
            return null;
        }
        return activationTime;
    }


    private Date validateActivationTime(Date activationTime, long currentTime, CronJob cronJob, Mode testmode, Job job)
    {
        if(activationTime == null)
        {
            logInfo("[PLA-5284,PLA-5357]: activation time was 'null'. trigger: " + getPK() + ", cronjob: " + getCronJob() + " .");
            return null;
        }
        if(StringUtils.isEmpty(getCronExpression()) && activationTime.getTime() > currentTime)
        {
            return null;
        }
        if(job == null)
        {
            throw new IllegalArgumentException("CronJob:  " + cronJob.getCode() + " -- missing assigned Job instance!");
        }
        if(testmode == Mode.REAL && !job.isPerformable(cronJob))
        {
            logDebug("CronJob " + cronJob.getCode() + " not started!");
            return null;
        }
        if(cronJob.isRunning())
        {
            logInfo("CronJob:  " + cronJob.getCode() + " has not yet finished so ignoring request to start it again..");
            return null;
        }
        return activationTime;
    }


    private void setTimeOfNextOccurrence(long currentTime, Mode testmode, CronJob cronJob)
    {
        long relativeTo = getActivationTime().getTime();
        long nextTime = getNextTime(relativeTo);
        while(nextTime > -1L && nextTime <= currentTime)
        {
            if(relativeTo >= nextTime)
            {
                if(testmode == Mode.TESTING)
                {
                    throw new IllegalStateException("endless cycle detected");
                }
                setActive(false);
                cronJob.addLog("calculation error getting next activation time relative to " + getActivationTime().getTime() + " - deactivating trigger " + this, cronJob
                                .getErrorLogLevel());
                nextTime = -1L;
                continue;
            }
            relativeTo = nextTime;
            nextTime = getNextTime(relativeTo);
        }
        if(nextTime > -1L)
        {
            setActivationTime(new Date(nextTime));
            logDebug("set new activation time to " + getActivationTime().toString());
        }
        else
        {
            setActivationTime(null);
            logDebug("set new activation time to null!");
        }
    }


    protected Calendar calculateNextTimeRelativeMode(Calendar relativeTo)
    {
        Calendar next = (Calendar)relativeTo.clone();
        if(-1 != getSecond().intValue())
        {
            next.add(13, getSecond().intValue());
        }
        if(-1 != getMinute().intValue())
        {
            next.add(12, getMinute().intValue());
        }
        if(-1 != getHour().intValue())
        {
            next.add(11, getHour().intValue());
        }
        if(-1 != getDay().intValue())
        {
            next.add(5, getDay().intValue());
        }
        if(-1 != getMonth().intValue())
        {
            next.add(2, getMonth().intValue());
        }
        if(-1 != getYear().intValue())
        {
            next.add(1, getYear().intValue());
        }
        return next;
    }


    protected Calendar calculateFixedTime(Calendar relativeTo)
    {
        Calendar next = (Calendar)relativeTo.clone();
        next.set(1, getYearAsPrimitive());
        next.set(2, hasMonth() ? getMonthAsPrimitive() : 0);
        next.set(5, hasDay() ? getDayAsPrimitive() : 1);
        next.set(11, hasHour() ? getHourAsPrimitive() : 0);
        next.set(12, hasMinute() ? getMinuteAsPrimitive() : 0);
        next.set(13, hasSecond() ? getSecondAsPrimitive() : 0);
        next.set(14, 0);
        return relativeTo.before(next) ? next : null;
    }


    private Calendar assertValidWeekday(Calendar relativeTo, Calendar calculatedTime)
    {
        if(calculatedTime != null && hasDayOfWeek())
        {
            int interval = hasWeekInterval() ? getWeekIntervalAsPrimitive() : 1;
            if(calculatedTime.before(relativeTo))
            {
                calculatedTime.set(1, relativeTo.get(1));
                calculatedTime.set(2, relativeTo.get(2));
                calculatedTime.set(5, relativeTo.get(5));
                if(calculatedTime.before(relativeTo))
                {
                    calculatedTime.add(5, 1);
                }
            }
            int lastWeekOfYear = relativeTo.get(3);
            int currentWeekOfYear = calculatedTime.get(3);
            if((currentWeekOfYear - lastWeekOfYear) % interval != 0)
            {
                calculatedTime.set(7, calculatedTime.getFirstDayOfWeek());
                calculatedTime.add(3, Math.abs(currentWeekOfYear - lastWeekOfYear) % interval);
            }
            List<EnumerationValue> days = getDaysOfWeekSorted(calculatedTime);
            int day = -1;
            long watchdogMarker = calculatedTime.getTimeInMillis();
            do
            {
                day = findDayInCurrentWeek(calculatedTime, days);
                if(day != -1)
                {
                    continue;
                }
                calculatedTime.set(7, calculatedTime.getFirstDayOfWeek());
                calculatedTime.add(3, interval);
                if(watchdogMarker >= calculatedTime.getTimeInMillis())
                {
                    throw new IllegalStateException("error in day-of-week calculation (" + watchdogMarker + ">=" + calculatedTime
                                    .getTimeInMillis());
                }
                watchdogMarker = calculatedTime.getTimeInMillis();
            }
            while(day == -1);
            calculatedTime.set(7, day);
        }
        return calculatedTime;
    }


    private int findDayInCurrentWeek(Calendar cal, List<EnumerationValue> allowedDays)
    {
        int currentDay = cal.get(7);
        DaysOfWeekComparator daysComp = new DaysOfWeekComparator(cal.getFirstDayOfWeek());
        for(EnumerationValue dowEnum : allowedDays)
        {
            int allowedDay = ((Integer)DAYS_OF_WEEK_MAP.get(dowEnum.getCode())).intValue();
            if(daysComp.compareDays(currentDay, allowedDay) <= 0)
            {
                return allowedDay;
            }
        }
        return -1;
    }


    private Calendar calculateRelativeTime(Calendar relativeTo, int iterativeField)
    {
        Calendar next = (Calendar)relativeTo.clone();
        switch(iterativeField)
        {
            case 1:
                next.set(2, hasMonth() ? getMonthAsPrimitive() : 0);
            case 2:
                next.set(5, hasDay() ? getDayAsPrimitive() : 1);
            case 5:
                next.set(11, hasHour() ? getHourAsPrimitive() : 0);
            case 11:
                next.set(12, hasMinute() ? getMinuteAsPrimitive() : 0);
            case 12:
                next.set(13, hasSecond() ? getSecondAsPrimitive() : 0);
                break;
            case 13:
                break;
            default:
                throw new IllegalArgumentException("invalid calendar field " + iterativeField);
        }
        if(!next.after(relativeTo))
        {
            next.add(iterativeField, 1);
        }
        return next;
    }


    private Calendar calculateYearlyTime(Calendar relativeTo)
    {
        return calculateRelativeTime(relativeTo, 1);
    }


    private Calendar calculateMonthlyTime(Calendar relativeTo)
    {
        return calculateRelativeTime(relativeTo, 2);
    }


    private Calendar calculateDailyTime(Calendar relativeTo)
    {
        return calculateRelativeTime(relativeTo, 5);
    }


    private Calendar calculateHourByHourTime(Calendar relativeTo)
    {
        return calculateRelativeTime(relativeTo, 11);
    }


    private Calendar calculateMinuteByMinuteTime(Calendar relativeTo)
    {
        return calculateRelativeTime(relativeTo, 12);
    }


    private Calendar calculateSecondBySecondTime(Calendar relativeTo)
    {
        return calculateRelativeTime(relativeTo, 13);
    }


    protected boolean hasYear()
    {
        Integer year = getYear();
        return (year != null && year.intValue() != -1);
    }


    protected boolean hasMonth()
    {
        Integer month = getMonth();
        return (month != null && month.intValue() != -1);
    }


    protected boolean hasDay()
    {
        Integer day = getDay();
        return (day != null && day.intValue() != -1);
    }


    protected boolean hasDayOfWeek()
    {
        Collection coll = getDaysOfWeek();
        return (coll != null && !coll.isEmpty());
    }


    protected boolean hasWeekInterval()
    {
        Integer week = getWeekInterval();
        return (week != null && week.intValue() != -1);
    }


    protected boolean hasHour()
    {
        Integer hour = getHour();
        return (hour != null && hour.intValue() != -1);
    }


    protected boolean hasMinute()
    {
        Integer minute = getMinute();
        return (minute != null && minute.intValue() != -1);
    }


    protected boolean hasSecond()
    {
        Integer second = getSecond();
        return (second != null && second.intValue() != -1);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public long getNextTime(long time)
    {
        Calendar relativeTo = Utilities.getDefaultCalendar();
        relativeTo.setTimeInMillis(time);
        return getNextTime(relativeTo);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public long getNextTime(Calendar relativeTo)
    {
        Calendar next;
        relativeTo.set(14, 0);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("" + relativeTo.get(5) + "/" + relativeTo.get(5) + "/" + relativeTo.get(2) + " " + relativeTo
                            .get(1) + ":" + relativeTo.get(11) + ":" + relativeTo
                            .get(12));
        }
        if(isRelativeAsPrimitive())
        {
            next = calculateNextTimeRelativeMode(relativeTo);
        }
        else if(hasYear())
        {
            next = assertValidWeekday(relativeTo, calculateFixedTime(relativeTo));
        }
        else if(hasMonth())
        {
            next = assertValidWeekday(relativeTo, calculateYearlyTime(relativeTo));
        }
        else if(hasDay())
        {
            next = assertValidWeekday(relativeTo, calculateMonthlyTime(relativeTo));
        }
        else if(hasHour())
        {
            next = assertValidWeekday(relativeTo, calculateDailyTime(relativeTo));
        }
        else if(hasMinute())
        {
            next = assertValidWeekday(relativeTo, calculateHourByHourTime(relativeTo));
        }
        else if(hasSecond())
        {
            next = assertValidWeekday(relativeTo, calculateMinuteByMinuteTime(relativeTo));
        }
        else
        {
            next = assertValidWeekday(relativeTo, calculateSecondBySecondTime(relativeTo));
        }
        if(next != null && getDateRange() != null)
        {
            Date date = next.getTime();
            return getDateRange().encloses(date) ? date.getTime() : -1L;
        }
        return (next != null) ? next.getTime().getTime() : -1L;
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Trigger[ ");
        if(getCronJob() != null)
        {
            stringBuilder.append("cronjob=" + getCronJob().getCode() + ", ");
            stringBuilder.append("job=" + getCronJob().getJob().getCode() + ", ");
        }
        else if(getJob() != null)
        {
            stringBuilder.append("job=" + getJob().getCode() + ", ");
        }
        stringBuilder.append("active=");
        stringBuilder.append(isActiveAsPrimitive());
        stringBuilder.append(", ");
        if(getSecond() != null && -1 != getSecond().intValue())
        {
            stringBuilder.append("second=");
            stringBuilder.append(getSecond().intValue());
            stringBuilder.append(", ");
        }
        if(getMinute() != null && -1 != getMinute().intValue())
        {
            stringBuilder.append("minute=");
            stringBuilder.append(getMinute().intValue());
            stringBuilder.append(", ");
        }
        if(getHour() != null && -1 != getHour().intValue())
        {
            stringBuilder.append("hour=");
            stringBuilder.append(getHour().intValue());
            stringBuilder.append(", ");
        }
        if(getDay() != null && -1 != getDay().intValue())
        {
            stringBuilder.append("day=");
            stringBuilder.append(getDay().intValue());
            stringBuilder.append(", ");
        }
        if(getMonth() != null && -1 != getMonth().intValue())
        {
            stringBuilder.append("month=");
            stringBuilder.append(getMonth().intValue());
            stringBuilder.append(", ");
        }
        if(getYear() != null && -1 != getYear().intValue())
        {
            stringBuilder.append("year=");
            stringBuilder.append(getYear().intValue());
            stringBuilder.append(", ");
        }
        stringBuilder.append("relative=");
        stringBuilder.append(isRelativeAsPrimitive());
        stringBuilder.append(", ");
        if(getDaysOfWeek() != null && !getDaysOfWeek().isEmpty())
        {
            stringBuilder.append("days of week=[");
            for(Iterator<EnumerationValue> iter = getDaysOfWeek().iterator(); iter.hasNext(); )
            {
                EnumerationValue enumVal = iter.next();
                stringBuilder.append(enumVal.getCode());
                stringBuilder.append(", ");
            }
            stringBuilder.append("] ");
        }
        if(-1 != getWeekIntervalAsPrimitive())
        {
            stringBuilder.append("week interval=");
            stringBuilder.append(getWeekIntervalAsPrimitive());
            stringBuilder.append(", ");
        }
        if(getDateRange() != null)
        {
            stringBuilder.append("date range=");
            stringBuilder.append(getDateRange().toString());
            stringBuilder.append(", ");
        }
        if(getActivationTime() != null)
        {
            stringBuilder.append("next activation=");
            stringBuilder.append(getActivationTime().toString());
            stringBuilder.append(" ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }


    private List<EnumerationValue> getDaysOfWeekSorted(Calendar relativeTo)
    {
        List<EnumerationValue> daysOfWeek = getDaysOfWeek();
        if(daysOfWeek != null)
        {
            List<EnumerationValue> sorted = new ArrayList<>(daysOfWeek);
            Collections.sort(sorted, (Comparator<? super EnumerationValue>)new DaysOfWeekComparator(relativeTo.getFirstDayOfWeek()));
            return sorted;
        }
        return null;
    }


    protected CronJob getCronJobForExecution()
    {
        CronJob cronJob = getCronJob();
        if(cronJob == null)
        {
            Job job = getJob();
            try
            {
                return job.createCronjob();
            }
            catch(UnsupportedOperationException e)
            {
                throw new IllegalStateException("illegal job " + job + " for job-only trigger " + this);
            }
            catch(JaloInvalidParameterException e)
            {
                throw new JaloSystemException(e);
            }
            catch(JaloBusinessException e)
            {
                throw new JaloSystemException(e);
            }
        }
        return cronJob;
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "something else")
    public void setActivationTime(SessionContext ctx, Date activationTime)
    {
        super.setActivationTime(ctx, activationTime);
        TriggerTask taskForTrigger = CronJobManager.getInstance().findTaskForTrigger(this);
        if(taskForTrigger == null)
        {
            LOG.error("No TriggerTask for the given trigger " + toString() + " found (maybe the task has been removed manually?). Please recreate the trigger.");
            return;
        }
        taskForTrigger.setExecutionDate(ctx, getActivationTime());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getTimeTable(SessionContext ctx)
    {
        Date activationTime = getActivationTime();
        if(activationTime == null)
        {
            return localize("trigger.timetable.activationtime.null");
        }
        StringBuilder timeTable = new StringBuilder();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        int day = getDayAsPrimitive();
        int month = getMonthAsPrimitive();
        int year = getYearAsPrimitive();
        int second = getSecondAsPrimitive();
        int hour = getHourAsPrimitive();
        int minute = getMinuteAsPrimitive();
        boolean showTime = true;
        if(!StringUtils.isEmpty(getCronExpression()))
        {
            try
            {
                CronExpression expression = new CronExpression(getCronExpression());
                timeTable.append(expression.getExpressionSummary());
            }
            catch(ParseException e)
            {
                LOG.warn("Unable to parse expression " + getCronExpression() + " for trigger ");
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e.getMessage(), e);
                }
            }
        }
        else if(isRelativeAsPrimitive())
        {
            if(-1 != day && -1 == month && -1 == year && -1 == second && -1 == minute && -1 == hour)
            {
                if(1 == day)
                {
                    timeTable.append(localize("trigger.timetable.dailyat"));
                }
                else
                {
                    timeTable.append(localize("trigger.timetable.all")).append(" ").append(day).append(" ")
                                    .append(localize("trigger.timetable.daysat"));
                }
            }
            else
            {
                timeTable.append(localize("trigger.timetable.intervalgap")).append(" ");
                if(0 < year)
                {
                    timeTable.append(year).append(" ")
                                    .append((year == 1) ? localize("trigger.timetable.year") : localize("trigger.timetable.years"));
                    timeTable.append(" ");
                }
                if(0 < month)
                {
                    timeTable.append(month).append(" ")
                                    .append((month == 1) ? localize("trigger.timetable.month") : localize("trigger.timetable.months"));
                    timeTable.append(" ");
                }
                if(0 < day)
                {
                    timeTable.append(day).append(" ")
                                    .append((day == 1) ? localize("trigger.timetable.day") : localize("trigger.timetable.days"));
                    timeTable.append(" ");
                }
                if(0 < hour)
                {
                    timeTable.append(hour).append(" ")
                                    .append((hour == 1) ? localize("trigger.timetable.hour") : localize("trigger.timetable.hours"));
                    timeTable.append(" ");
                }
                if(0 < minute)
                {
                    timeTable.append(minute).append(" ")
                                    .append((minute == 1) ? localize("trigger.timetable.minute") : localize("trigger.timetable.minutes"));
                    timeTable.append(" ");
                }
                if(0 < second)
                {
                    timeTable.append(second).append(" ")
                                    .append((second == 1) ? localize("trigger.timetable.second") : localize("trigger.timetable.seconds"));
                    timeTable.append(" ");
                }
                showTime = false;
            }
        }
        else
        {
            Collection daysOfWeek = getDaysOfWeek();
            if(daysOfWeek != null && !daysOfWeek.isEmpty())
            {
                int weekInterval = getWeekIntervalAsPrimitive();
                if(1 == weekInterval)
                {
                    timeTable.append(localize("trigger.timetable.every"));
                    timeTable.append(" ");
                }
                else
                {
                    timeTable.append(localize("trigger.timetable.every_week")).append(" ").append(weekInterval).append(" ")
                                    .append(localize("trigger.timetable.weekat")).append(" ");
                }
                for(Iterator<EnumerationValue> it = daysOfWeek.iterator(); it.hasNext(); )
                {
                    EnumerationValue enumValue = it.next();
                    timeTable.append(enumValue.getName());
                    if(it.hasNext())
                    {
                        timeTable.append(", ");
                        continue;
                    }
                    timeTable.append(" ").append(localize("trigger.timetable.at"));
                }
            }
            else if(day == -1 && hour != -1 && minute != -1 && second != -1)
            {
                timeTable.append(localize("trigger.timetable.dailyat"));
            }
            else if(-1 == month)
            {
                timeTable.append(localize("trigger.timetable.every"));
                timeTable.append(" ");
                timeTable.append(day).append(" ").append(localize("trigger.timetable.monthly"));
            }
            else
            {
                String formattedDate = dateFormat.format(activationTime);
                timeTable.append(localize("trigger.timetable.onceat")).append(" ").append(formattedDate).append(" ")
                                .append(localize("trigger.timetable.at"));
            }
        }
        if(showTime)
        {
            String formattedTime = timeFormat.format(activationTime);
            timeTable.append(" ").append(formattedTime);
        }
        return timeTable.toString().trim();
    }


    private String localize(String key)
    {
        return Localization.getLocalizedString(key);
    }


    private void logDebug(String debug)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(debug);
        }
    }


    private void logInfo(String info)
    {
        if(LOG.isInfoEnabled())
        {
            LOG.debug(info);
        }
    }


    private boolean validDelay(int delay)
    {
        if(delay == -1)
        {
            return true;
        }
        if(delay < PULSE_SECONDS)
        {
            logInfo("Trying to set a maxAcceptableDelayS of " + delay + " seconds but regular pulse is " + PULSE_SECONDS + " s.");
            return false;
        }
        return true;
    }


    @ForceJALO(reason = "something else")
    public void setMaxAcceptableDelay(SessionContext ctx, Integer value)
    {
        Integer innerValue = value;
        if(innerValue == null)
        {
            innerValue = Integer.valueOf(-1);
        }
        if(validDelay(innerValue.intValue()))
        {
            setProperty(ctx, "maxAcceptableDelay", innerValue);
        }
    }


    @ForceJALO(reason = "something else")
    public void setMaxAcceptableDelay(Integer value)
    {
        Integer innerValue = value;
        if(innerValue == null)
        {
            innerValue = Integer.valueOf(-1);
        }
        if(validDelay(innerValue.intValue()))
        {
            setMaxAcceptableDelay(getSession().getSessionContext(), innerValue);
        }
    }


    @ForceJALO(reason = "something else")
    public void setMaxAcceptableDelay(SessionContext ctx, int value)
    {
        if(validDelay(value))
        {
            setMaxAcceptableDelay(ctx, Integer.valueOf(value));
        }
    }


    @ForceJALO(reason = "something else")
    public void setMaxAcceptableDelay(int seconds)
    {
        if(validDelay(seconds))
        {
            setMaxAcceptableDelay(getSession().getSessionContext(), seconds);
        }
    }


    public static int getPulseseconds()
    {
        return PULSE_SECONDS;
    }
}
