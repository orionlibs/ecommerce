package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.DayOfWeek;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.util.StandardDateRange;
import java.util.Date;
import java.util.List;

public class TriggerModel extends ItemModel
{
    public static final String _TYPECODE = "Trigger";
    public static final String _JOBTRIGGERRELATION = "JobTriggerRelation";
    public static final String _CRONJOBTRIGGERRELATION = "CronJobTriggerRelation";
    public static final String ACTIVE = "active";
    public static final String SECOND = "second";
    public static final String MINUTE = "minute";
    public static final String HOUR = "hour";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String RELATIVE = "relative";
    public static final String DAYSOFWEEK = "daysOfWeek";
    public static final String WEEKINTERVAL = "weekInterval";
    public static final String DATERANGE = "dateRange";
    public static final String ACTIVATIONTIME = "activationTime";
    public static final String TIMETABLE = "timeTable";
    public static final String CRONEXPRESSION = "cronExpression";
    public static final String MAXACCEPTABLEDELAY = "maxAcceptableDelay";
    public static final String JOB = "job";
    public static final String CRONJOB = "cronJob";


    public TriggerModel()
    {
    }


    public TriggerModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public TriggerModel(CronJobModel _cronJob, JobModel _job, ItemModel _owner)
    {
        setCronJob(_cronJob);
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "activationTime", type = Accessor.Type.GETTER)
    public Date getActivationTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("activationTime");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "cronExpression", type = Accessor.Type.GETTER)
    public String getCronExpression()
    {
        return (String)getPersistenceContext().getPropertyValue("cronExpression");
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
    public CronJobModel getCronJob()
    {
        return (CronJobModel)getPersistenceContext().getPropertyValue("cronJob");
    }


    @Accessor(qualifier = "dateRange", type = Accessor.Type.GETTER)
    public StandardDateRange getDateRange()
    {
        return (StandardDateRange)getPersistenceContext().getPropertyValue("dateRange");
    }


    @Accessor(qualifier = "day", type = Accessor.Type.GETTER)
    public Integer getDay()
    {
        return (Integer)getPersistenceContext().getPropertyValue("day");
    }


    @Accessor(qualifier = "daysOfWeek", type = Accessor.Type.GETTER)
    public List<DayOfWeek> getDaysOfWeek()
    {
        return (List<DayOfWeek>)getPersistenceContext().getPropertyValue("daysOfWeek");
    }


    @Accessor(qualifier = "hour", type = Accessor.Type.GETTER)
    public Integer getHour()
    {
        return (Integer)getPersistenceContext().getPropertyValue("hour");
    }


    @Accessor(qualifier = "job", type = Accessor.Type.GETTER)
    public JobModel getJob()
    {
        return (JobModel)getPersistenceContext().getPropertyValue("job");
    }


    @Accessor(qualifier = "maxAcceptableDelay", type = Accessor.Type.GETTER)
    public Integer getMaxAcceptableDelay()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxAcceptableDelay");
    }


    @Accessor(qualifier = "minute", type = Accessor.Type.GETTER)
    public Integer getMinute()
    {
        return (Integer)getPersistenceContext().getPropertyValue("minute");
    }


    @Accessor(qualifier = "month", type = Accessor.Type.GETTER)
    public Integer getMonth()
    {
        return (Integer)getPersistenceContext().getPropertyValue("month");
    }


    @Accessor(qualifier = "relative", type = Accessor.Type.GETTER)
    public Boolean getRelative()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("relative");
    }


    @Accessor(qualifier = "second", type = Accessor.Type.GETTER)
    public Integer getSecond()
    {
        return (Integer)getPersistenceContext().getPropertyValue("second");
    }


    @Accessor(qualifier = "timeTable", type = Accessor.Type.GETTER)
    public String getTimeTable()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "timeTable");
    }


    @Accessor(qualifier = "weekInterval", type = Accessor.Type.GETTER)
    public Integer getWeekInterval()
    {
        return (Integer)getPersistenceContext().getPropertyValue("weekInterval");
    }


    @Accessor(qualifier = "year", type = Accessor.Type.GETTER)
    public Integer getYear()
    {
        return (Integer)getPersistenceContext().getPropertyValue("year");
    }


    @Accessor(qualifier = "activationTime", type = Accessor.Type.SETTER)
    public void setActivationTime(Date value)
    {
        getPersistenceContext().setPropertyValue("activationTime", value);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "cronExpression", type = Accessor.Type.SETTER)
    public void setCronExpression(String value)
    {
        getPersistenceContext().setPropertyValue("cronExpression", value);
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
    public void setCronJob(CronJobModel value)
    {
        getPersistenceContext().setPropertyValue("cronJob", value);
    }


    @Accessor(qualifier = "dateRange", type = Accessor.Type.SETTER)
    public void setDateRange(StandardDateRange value)
    {
        getPersistenceContext().setPropertyValue("dateRange", value);
    }


    @Accessor(qualifier = "day", type = Accessor.Type.SETTER)
    public void setDay(Integer value)
    {
        getPersistenceContext().setPropertyValue("day", value);
    }


    @Accessor(qualifier = "daysOfWeek", type = Accessor.Type.SETTER)
    public void setDaysOfWeek(List<DayOfWeek> value)
    {
        getPersistenceContext().setPropertyValue("daysOfWeek", value);
    }


    @Accessor(qualifier = "hour", type = Accessor.Type.SETTER)
    public void setHour(Integer value)
    {
        getPersistenceContext().setPropertyValue("hour", value);
    }


    @Accessor(qualifier = "job", type = Accessor.Type.SETTER)
    public void setJob(JobModel value)
    {
        getPersistenceContext().setPropertyValue("job", value);
    }


    @Accessor(qualifier = "maxAcceptableDelay", type = Accessor.Type.SETTER)
    public void setMaxAcceptableDelay(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxAcceptableDelay", value);
    }


    @Accessor(qualifier = "minute", type = Accessor.Type.SETTER)
    public void setMinute(Integer value)
    {
        getPersistenceContext().setPropertyValue("minute", value);
    }


    @Accessor(qualifier = "month", type = Accessor.Type.SETTER)
    public void setMonth(Integer value)
    {
        getPersistenceContext().setPropertyValue("month", value);
    }


    @Accessor(qualifier = "relative", type = Accessor.Type.SETTER)
    public void setRelative(Boolean value)
    {
        getPersistenceContext().setPropertyValue("relative", value);
    }


    @Accessor(qualifier = "second", type = Accessor.Type.SETTER)
    public void setSecond(Integer value)
    {
        getPersistenceContext().setPropertyValue("second", value);
    }


    @Accessor(qualifier = "weekInterval", type = Accessor.Type.SETTER)
    public void setWeekInterval(Integer value)
    {
        getPersistenceContext().setPropertyValue("weekInterval", value);
    }


    @Accessor(qualifier = "year", type = Accessor.Type.SETTER)
    public void setYear(Integer value)
    {
        getPersistenceContext().setPropertyValue("year", value);
    }
}
