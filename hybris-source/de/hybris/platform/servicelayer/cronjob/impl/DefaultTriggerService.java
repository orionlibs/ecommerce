package de.hybris.platform.servicelayer.cronjob.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.cronjob.enums.DayOfWeek;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.cronjob.jalo.TriggerableJob;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.servicelayer.cronjob.CronJobFactory;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.TriggerDao;
import de.hybris.platform.servicelayer.cronjob.TriggerService;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTriggerService implements TriggerService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTriggerService.class);
    private CronJobService cronJobService;
    private ModelService modelService;
    private TriggerDao triggerDao;
    private I18NService i18NService;
    private SessionService sessionService;
    private static final int CRONJOB_TRIGGER_INTERVAL_DEFAULT = 30;
    private static final Map<String, Integer> DAYS_OF_WEEK_MAP;
    public static final int IGNORE_VALUE = -1;

    static
    {
        FastHashMap<String, Integer> fastHashMap = new FastHashMap();
        fastHashMap.put(DayOfWeek.SUNDAY.getCode(), Integer.valueOf(1));
        fastHashMap.put(DayOfWeek.MONDAY.getCode(), Integer.valueOf(2));
        fastHashMap.put(DayOfWeek.TUESDAY.getCode(), Integer.valueOf(3));
        fastHashMap.put(DayOfWeek.WEDNESDAY.getCode(), Integer.valueOf(4));
        fastHashMap.put(DayOfWeek.THURSDAY.getCode(), Integer.valueOf(5));
        fastHashMap.put(DayOfWeek.FRIDAY.getCode(), Integer.valueOf(6));
        fastHashMap.put(DayOfWeek.SATURDAY.getCode(), Integer.valueOf(7));
        DAYS_OF_WEEK_MAP = Collections.unmodifiableMap((Map<? extends String, ? extends Integer>)fastHashMap);
    }

    @Required
    public void setCronJobService(CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTriggerDao(TriggerDao triggerDao)
    {
        this.triggerDao = triggerDao;
    }


    @Required
    public void setI18NService(I18NService i18nService)
    {
        this.i18NService = i18nService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public boolean activateForTest(TriggerModel trigger, long currentTime)
    {
        return activateInternal(trigger, currentTime, Mode.TESTING);
    }


    public void activate(TriggerModel trigger)
    {
        activateInternal(trigger, System.currentTimeMillis(), Mode.REAL);
    }


    private boolean activateInternal(TriggerModel trigger, long currentTime, Mode mode)
    {
        Date activationTime = trigger.getActivationTime();
        CronJobModel cronJob = getCronJobForExecution(trigger);
        Integer maxOkDelaySec = trigger.getMaxAcceptableDelay();
        boolean delayOk = (maxOkDelaySec == null || maxOkDelaySec.intValue() == -1 || activationTime.getTime() + (maxOkDelaySec.intValue() * 1000) > currentTime);
        boolean jobCanRun = true;
        if(trigger.getActive().booleanValue() && delayOk)
        {
            jobCanRun = jobCanRun(trigger, activationTime, currentTime, cronJob, mode);
            if(jobCanRun && mode == Mode.REAL)
            {
                this.sessionService.executeInLocalViewWithParams(
                                (Map)ImmutableMap.of("cronjob.scheduled", Boolean.TRUE, "cronjob.scheduled.by", trigger
                                                .getPk()), (SessionExecutionBody)new Object(this, cronJob, currentTime));
            }
        }
        if(StringUtils.isEmpty(trigger.getCronExpression()))
        {
            setTimeOfNextOccurence(trigger, currentTime, mode, cronJob);
        }
        else
        {
            setTimeOfNextOccurenceBasedOnExpression(trigger, trigger.getActivationTime().getTime());
        }
        return (jobCanRun && delayOk);
    }


    private boolean jobCanRun(TriggerModel trigger, Date activationTime, long currentTime, CronJobModel cronJob, Mode testmode)
    {
        JobModel job;
        try
        {
            job = cronJob.getJob();
        }
        catch(JaloSystemException exp)
        {
            LOG.warn("CronJob '{}' cannot be triggered, because its Job is not valid. This could be due to a migration. Ask support@hybris.de for further information.", cronJob
                            .getCode());
            return false;
        }
        if(!validateActivationTime(trigger, activationTime, currentTime, cronJob, testmode, job))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("validation failed to activate the trigger " + trigger + " activation time (" + activationTime + "), current time (" + (new Date(currentTime))
                                .toString() + ") for job " + job);
            }
            return false;
        }
        return true;
    }


    private void setTimeOfNextOccurenceBasedOnExpression(TriggerModel trigger, long currentTime)
    {
        try
        {
            CronExpression expression = new CronExpression(trigger.getCronExpression());
            Date activationTime = expression.getNextValidTimeAfter(new Date(currentTime));
            trigger.setActivationTime(activationTime);
            this.modelService.save(trigger);
            LOG.debug("Calculated next activation time for a expression '{}' is :{}", trigger.getCronExpression(), activationTime);
        }
        catch(ParseException e)
        {
            LOG.error("Provided cronexpression '{}' is not in correct format details, {}", trigger.getCronExpression(), e
                            .getMessage());
            LOG.debug("Provided cronexpression is not correct", e);
        }
    }


    private boolean validateActivationTime(TriggerModel trigger, Date activationTime, long currentTime, CronJobModel cronJob, Mode testmode, JobModel job)
    {
        if(activationTime == null)
        {
            LOG.info("Activation time was 'null'. trigger: {}, cronjob: {}", trigger.getPk(), trigger.getCronJob());
            return false;
        }
        if(activationTime.getTime() > currentTime)
        {
            return false;
        }
        if(job == null)
        {
            throw new IllegalArgumentException("CronJob:  " + cronJob.getCode() + " -- missing assigned Job instance!");
        }
        if(testmode == Mode.REAL && !this.cronJobService.isPerformable(cronJob))
        {
            LOG.debug("CronJob {} not started!", cronJob.getCode());
            return false;
        }
        if(this.cronJobService.isRunning(cronJob))
        {
            LOG.info("CronJob: {} has not yet finished so ignoring request to start it again..", cronJob.getCode());
            return false;
        }
        return true;
    }


    private CronJobModel getCronJobForExecution(TriggerModel trigger)
    {
        CronJobModel cronJob = trigger.getCronJob();
        if(cronJob == null)
        {
            JobModel job = trigger.getJob();
            try
            {
                return createCronjob(job);
            }
            catch(UnsupportedOperationException e)
            {
                throw new IllegalStateException("illegal job " + job + " for job-only trigger " + this);
            }
            catch(AttributeNotSupportedException e)
            {
                throw new SystemException(e);
            }
        }
        return cronJob;
    }


    private CronJobModel createCronjob(JobModel jobModel) throws AttributeNotSupportedException
    {
        Job job = (Job)this.modelService.getSource(jobModel);
        CronJobModel cronJobModel = null;
        if(job instanceof TriggerableJob)
        {
            cronJobModel = (CronJobModel)this.modelService.get(((TriggerableJob)job).newExecution());
        }
        else if(jobModel instanceof ServicelayerJobModel)
        {
            CronJobFactory cronJobFactory = this.cronJobService.getCronJobFactory((ServicelayerJobModel)jobModel);
            cronJobModel = cronJobFactory.createCronJob(jobModel);
        }
        else
        {
            throw new UnsupportedOperationException();
        }
        configureCronjob(jobModel, cronJobModel);
        if(Boolean.TRUE.equals(cronJobModel.getRetry()) && !this.cronJobService.isPerformable(cronJobModel))
        {
            Calendar cal = Utilities.getDefaultCalendar();
            cal.setTime(new Date());
            HashMap<Object, Object> map = new HashMap<>();
            map.put("cronJob", this.modelService.getSource(cronJobModel));
            map.put("second", Integer.valueOf(cal.get(13)));
            map.put("minute", Integer.valueOf(cal.get(12)));
            map.put("hour", Integer.valueOf(cal.get(11)));
            map.put("day", Integer.valueOf(cal.get(5)));
            map.put("month", Integer.valueOf(cal.get(2)));
            map.put("year", Integer.valueOf(cal.get(1)));
            map.put("relative", Boolean.FALSE);
            map.put("daysOfWeek", null);
            CronJobManager.getInstance().createTrigger(map);
        }
        return cronJobModel;
    }


    private void configureCronjob(JobModel job, CronJobModel cronjob) throws AttributeNotSupportedException
    {
        Map<String, String> values = getConfigAttributes();
        try
        {
            for(String jobMapping : values.keySet())
            {
                Object jobValueAttribute = this.modelService.getAttributeValue(job, jobMapping);
                if(jobValueAttribute != null)
                {
                    try
                    {
                        this.modelService.setAttributeValue(cronjob, values.get(jobMapping), jobValueAttribute);
                    }
                    catch(AttributeNotSupportedException e)
                    {
                        LOG.warn("Cannot rewrite attribute value from Job to CronJob, skipping. Job attribute name: '" + jobMapping + "', cronJob attribute name: '" + (String)values
                                        .get(jobMapping) + "', attribute value: '" + jobValueAttribute + "'. Reason: " + e
                                        .getMessage());
                    }
                }
            }
            this.modelService.save(cronjob);
        }
        catch(ModelSavingException e)
        {
            throw new SystemException("Error during the configuration of the cronjob: " + cronjob
                            .getClass() + " with code: " + cronjob.getCode(), e);
        }
        catch(RuntimeException e)
        {
            throw new SystemException("Error during the configuration of the cronjob: " + cronjob
                            .getClass() + " with code: " + cronjob.getCode(), e);
        }
    }


    protected Map<String, String> getConfigAttributes()
    {
        Map<String, String> mappingsJob2CronJob = new HashMap<>();
        mappingsJob2CronJob.put("active", "active");
        mappingsJob2CronJob.put("errorMode", "errorMode");
        mappingsJob2CronJob.put("logToFile", "logToFile");
        mappingsJob2CronJob.put("logLevelFile", "logLevelFile");
        mappingsJob2CronJob.put("logToDatabase", "logToDatabase");
        mappingsJob2CronJob.put("logLevelDatabase", "logLevelDatabase");
        mappingsJob2CronJob.put("sessionUser", "sessionUser");
        mappingsJob2CronJob.put("sessionLanguage", "sessionLanguage");
        mappingsJob2CronJob.put("sessionCurrency", "sessionCurrency");
        mappingsJob2CronJob.put("useReadOnlyDatasource", "useReadOnlyDatasource");
        mappingsJob2CronJob.put("emailAddress", "emailAddress");
        mappingsJob2CronJob.put("sendEmail", "sendEmail");
        mappingsJob2CronJob.put("emailNotificationTemplate", "emailNotificationTemplate");
        mappingsJob2CronJob.put("changeRecordingEnabled", "changeRecordingEnabled");
        mappingsJob2CronJob.put("removeOnExit", "removeOnExit");
        mappingsJob2CronJob.put("singleExecutable", "singleExecutable");
        mappingsJob2CronJob.put("retry", "retry");
        mappingsJob2CronJob.put("priority", "priority");
        mappingsJob2CronJob.put("alternativeDataSourceID", "alternativeDataSourceID");
        mappingsJob2CronJob.put("numberOfRetries", "numberOfRetries");
        return mappingsJob2CronJob;
    }


    private void setTimeOfNextOccurence(TriggerModel trigger, long currentTime, Mode testmode, CronJobModel cronJob)
    {
        Calendar relativeToCalendar = Calendar.getInstance(this.i18NService.getCurrentTimeZone(), this.i18NService
                        .getCurrentLocale());
        if(trigger.getActivationTime() != null)
        {
            relativeToCalendar.setTime(trigger.getActivationTime());
        }
        else
        {
            relativeToCalendar.setTimeInMillis(currentTime);
        }
        long relativeTo = relativeToCalendar.getTimeInMillis();
        long nextTime = getNextTime(trigger, relativeToCalendar).getTimeInMillis();
        while(nextTime > -1L && nextTime <= currentTime)
        {
            if(relativeTo >= nextTime)
            {
                if(testmode == Mode.TESTING)
                {
                    throw new IllegalStateException("endless cycle detected");
                }
                trigger.setActive(Boolean.FALSE);
                CronJob cronJobJalo = (CronJob)this.modelService.getSource(cronJob);
                cronJobJalo.addLog("calculation error getting next activation time relative to " + trigger
                                .getActivationTime().getTime() + " - deactivating trigger " + this, cronJobJalo
                                .getErrorLogLevel());
                nextTime = -1L;
                continue;
            }
            relativeTo = nextTime;
            nextTime = getNextTime(trigger, getCalendar(relativeTo)).getTime().getTime();
        }
        if(nextTime > -1L)
        {
            trigger.setActivationTime(new Date(nextTime));
            LOG.debug("set new activation time to {}", trigger.getActivationTime());
        }
        else
        {
            trigger.setActivationTime(null);
            LOG.debug("set new activation time to null!");
        }
        this.modelService.save(trigger);
    }


    public Calendar getNextTime(TriggerModel trigger, Calendar relativeTo)
    {
        if(StringUtils.isEmpty(trigger.getCronExpression()))
        {
            Calendar next;
            relativeTo.set(14, 0);
            LOG.debug("{}/{}/{} {}:{}:{}", new Object[] {Integer.valueOf(relativeTo.get(5)),
                            Integer.valueOf(relativeTo.get(2)),
                            Integer.valueOf(relativeTo.get(1)),
                            Integer.valueOf(relativeTo.get(11)),
                            Integer.valueOf(relativeTo.get(12)),
                            Integer.valueOf(relativeTo.get(13))});
            if(Boolean.TRUE.equals(trigger.getRelative()))
            {
                next = calculateNextTimeRelativeMode(trigger, relativeTo);
            }
            else if(hasYear(trigger))
            {
                next = assertValidWeekday(trigger, relativeTo, calculateFixedTime(trigger, relativeTo));
            }
            else if(hasMonth(trigger))
            {
                next = assertValidWeekday(trigger, relativeTo, calculateYearlyTime(trigger, relativeTo));
            }
            else if(hasDay(trigger))
            {
                next = assertValidWeekday(trigger, relativeTo, calculateMonthlyTime(trigger, relativeTo));
            }
            else if(hasHour(trigger))
            {
                next = assertValidWeekday(trigger, relativeTo, calculateDailyTime(trigger, relativeTo));
            }
            else if(hasMinute(trigger))
            {
                next = assertValidWeekday(trigger, relativeTo, calculateHourByHourTime(trigger, relativeTo));
            }
            else if(hasSecond(trigger))
            {
                next = assertValidWeekday(trigger, relativeTo, calculateMinuteByMinuteTime(trigger, relativeTo));
            }
            else
            {
                next = assertValidWeekday(trigger, relativeTo, calculateSecondBySecondTime(trigger, relativeTo));
            }
            if(next != null && trigger.getDateRange() != null)
            {
                Date date = next.getTime();
                return trigger.getDateRange().encloses(date) ? next : getCalendar(-1L);
            }
            return (next != null) ? next : getCalendar(-1L);
        }
        return getNextTimeForCronExpression(trigger, relativeTo);
    }


    private Calendar getNextTimeForCronExpression(TriggerModel trigger, Calendar relativeTo)
    {
        try
        {
            CronExpression expression = new CronExpression(trigger.getCronExpression());
            Calendar activationTimeCalendar = getCalendar(expression.getNextValidTimeAfter(relativeTo.getTime()).getTime());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Calculated next activation time for a expression '" + trigger.getCronExpression() + "' is :" + activationTimeCalendar);
            }
            return activationTimeCalendar;
        }
        catch(ParseException e)
        {
            LOG.error("Provided cronexpression '{}' is not in correct format details, {}", trigger.getCronExpression(), e
                            .getMessage());
            LOG.debug("Provided cronexpression is not correct", e);
            return getCalendar(-1L);
        }
    }


    private Calendar calculateFixedTime(TriggerModel trigger, Calendar relativeTo)
    {
        Calendar next = (Calendar)relativeTo.clone();
        next.set(1, getIntegerAsPrimitive(trigger.getYear()));
        next.set(2, hasMonth(trigger) ? getIntegerAsPrimitive(trigger.getMonth()) : 0);
        next.set(5, hasDay(trigger) ? getIntegerAsPrimitive(trigger.getDay()) : 1);
        next.set(11, hasHour(trigger) ? getIntegerAsPrimitive(trigger.getHour()) : 0);
        next.set(12, hasMinute(trigger) ? getIntegerAsPrimitive(trigger.getMinute()) : 0);
        next.set(13, hasSecond(trigger) ? getIntegerAsPrimitive(trigger.getSecond()) : 0);
        next.set(14, 0);
        return relativeTo.before(next) ? next : null;
    }


    private int getIntegerAsPrimitive(Integer value)
    {
        return (value != null) ? value.intValue() : 0;
    }


    private Calendar calculateNextTimeRelativeMode(TriggerModel trigger, Calendar relativeTo)
    {
        Calendar next = (Calendar)relativeTo.clone();
        if(-1 != trigger.getSecond().intValue())
        {
            next.add(13, trigger.getSecond().intValue());
        }
        if(-1 != trigger.getMinute().intValue())
        {
            next.add(12, trigger.getMinute().intValue());
        }
        if(-1 != trigger.getHour().intValue())
        {
            next.add(11, trigger.getHour().intValue());
        }
        if(-1 != trigger.getDay().intValue())
        {
            next.add(5, trigger.getDay().intValue());
        }
        if(-1 != trigger.getMonth().intValue())
        {
            next.add(2, trigger.getMonth().intValue());
        }
        if(-1 != trigger.getYear().intValue())
        {
            next.add(1, trigger.getYear().intValue());
        }
        return next;
    }


    private boolean hasYear(TriggerModel trigger)
    {
        Integer year = trigger.getYear();
        return (year != null && year.intValue() != -1);
    }


    private boolean hasMonth(TriggerModel trigger)
    {
        Integer month = trigger.getMonth();
        return (month != null && month.intValue() != -1);
    }


    private boolean hasDay(TriggerModel trigger)
    {
        Integer day = trigger.getDay();
        return (day != null && day.intValue() != -1);
    }


    private boolean hasDayOfWeek(TriggerModel trigger)
    {
        Collection coll = trigger.getDaysOfWeek();
        return (coll != null && !coll.isEmpty());
    }


    private boolean hasWeekInterval(TriggerModel trigger)
    {
        Integer week = trigger.getWeekInterval();
        return (week != null && week.intValue() != -1);
    }


    private boolean hasHour(TriggerModel trigger)
    {
        Integer hour = trigger.getHour();
        return (hour != null && hour.intValue() != -1);
    }


    private boolean hasMinute(TriggerModel trigger)
    {
        Integer minute = trigger.getMinute();
        return (minute != null && minute.intValue() != -1);
    }


    private boolean hasSecond(TriggerModel trigger)
    {
        Integer second = trigger.getSecond();
        return (second != null && second.intValue() != -1);
    }


    private Calendar calculateRelativeTime(TriggerModel trigger, Calendar relativeTo, int iterativeField)
    {
        Calendar next = (Calendar)relativeTo.clone();
        switch(iterativeField)
        {
            case 1:
                next.set(2, hasMonth(trigger) ? getIntegerAsPrimitive(trigger.getMonth()) : 0);
            case 2:
                next.set(5,
                                hasDay(trigger) ? getIntegerAsPrimitive(trigger.getDay()) : 1);
            case 5:
                next.set(11, hasHour(trigger) ? getIntegerAsPrimitive(trigger.getHour()) : 0);
            case 11:
                next.set(12, hasMinute(trigger) ? getIntegerAsPrimitive(trigger.getMinute()) : 0);
            case 12:
                next.set(13, hasSecond(trigger) ? getIntegerAsPrimitive(trigger.getSecond()) : 0);
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


    private Calendar calculateYearlyTime(TriggerModel trigger, Calendar relativeTo)
    {
        return calculateRelativeTime(trigger, relativeTo, 1);
    }


    private Calendar calculateMonthlyTime(TriggerModel trigger, Calendar relativeTo)
    {
        return calculateRelativeTime(trigger, relativeTo, 2);
    }


    private Calendar calculateDailyTime(TriggerModel trigger, Calendar relativeTo)
    {
        return calculateRelativeTime(trigger, relativeTo, 5);
    }


    private Calendar calculateHourByHourTime(TriggerModel trigger, Calendar relativeTo)
    {
        return calculateRelativeTime(trigger, relativeTo, 11);
    }


    private Calendar calculateMinuteByMinuteTime(TriggerModel trigger, Calendar relativeTo)
    {
        return calculateRelativeTime(trigger, relativeTo, 12);
    }


    private Calendar calculateSecondBySecondTime(TriggerModel trigger, Calendar relativeTo)
    {
        return calculateRelativeTime(trigger, relativeTo, 13);
    }


    private Calendar assertValidWeekday(TriggerModel trigger, Calendar relativeTo, Calendar calculatedTime)
    {
        if(calculatedTime != null && hasDayOfWeek(trigger))
        {
            int interval = hasWeekInterval(trigger) ? getIntegerAsPrimitive(trigger.getWeekInterval()) : 1;
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
            List<DayOfWeek> days = getDaysOfWeekSorted(trigger, calculatedTime);
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


    private List<DayOfWeek> getDaysOfWeekSorted(TriggerModel trigger, Calendar relativeTo)
    {
        List<DayOfWeek> daysOfWeek = trigger.getDaysOfWeek();
        if(daysOfWeek != null)
        {
            List<DayOfWeek> sorted = new ArrayList<>(daysOfWeek);
            Collections.sort(sorted, (Comparator<? super DayOfWeek>)new DaysOfWeekComparator(relativeTo.getFirstDayOfWeek()));
            return sorted;
        }
        return null;
    }


    private int findDayInCurrentWeek(Calendar cal, List<DayOfWeek> allowedDays)
    {
        int currentDay = cal.get(7);
        DaysOfWeekComparator daysComp = new DaysOfWeekComparator(cal.getFirstDayOfWeek());
        for(DayOfWeek dowEnum : allowedDays)
        {
            int allowedDay = ((Integer)DAYS_OF_WEEK_MAP.get(dowEnum.getCode())).intValue();
            if(daysComp.compareDays(currentDay, allowedDay) <= 0)
            {
                return allowedDay;
            }
        }
        return -1;
    }


    public List<TriggerModel> getActiveTriggers()
    {
        return getActiveTriggers(Calendar.getInstance(this.i18NService.getCurrentTimeZone(), this.i18NService.getCurrentLocale()));
    }


    public List<TriggerModel> getActiveTriggers(Calendar calendar)
    {
        return this.triggerDao.findActiveTriggers(calendar);
    }


    public int getPulseSeconds()
    {
        return Config.getInt("cronjob.trigger.interval", 30);
    }


    public void setPulseSeconds(int interval)
    {
        Config.setParameter("cronjob.trigger.interval", Integer.toString(interval));
    }


    private Calendar getCalendar(long millis)
    {
        Calendar calendar = Calendar.getInstance(this.i18NService.getCurrentTimeZone(), this.i18NService.getCurrentLocale());
        calendar.setTimeInMillis(millis);
        return calendar;
    }
}
