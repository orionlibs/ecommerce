package de.hybris.platform.servicelayer.cronjob.attributehandler;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.cronjob.enums.DayOfWeek;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.localization.Localization;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronExpression;

public class TriggerTimetable implements DynamicAttributeHandler<String, TriggerModel>
{
    private static final Logger LOG = Logger.getLogger(TriggerTimetable.class);
    public static final int IGNORE_VALUE = -1;
    private TypeService typeService;


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public String get(TriggerModel model)
    {
        Date activationTime = model.getActivationTime();
        if(activationTime == null)
        {
            return localize("trigger.timetable.activationtime.null");
        }
        StringBuilder timeTable = new StringBuilder();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        int day = model.getDay().intValue();
        int month = model.getMonth().intValue();
        int year = model.getYear().intValue();
        int second = model.getSecond().intValue();
        int hour = model.getHour().intValue();
        int minute = model.getMinute().intValue();
        boolean showTime = true;
        if(!StringUtils.isEmpty(model.getCronExpression()))
        {
            try
            {
                CronExpression expression = new CronExpression(model.getCronExpression());
                timeTable.append(expression.getExpressionSummary());
            }
            catch(ParseException e)
            {
                LOG.warn("Unable to parse expression " + model.getCronExpression() + " for trigger ");
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e.getMessage(), e);
                }
            }
        }
        else if(Boolean.TRUE.equals(model.getRelative()))
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
            List<DayOfWeek> daysOfWeek = model.getDaysOfWeek();
            if(daysOfWeek != null && !daysOfWeek.isEmpty())
            {
                int weekInterval = model.getWeekInterval().intValue();
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
                for(Iterator<DayOfWeek> it = daysOfWeek.iterator(); it.hasNext(); )
                {
                    DayOfWeek enumValue = it.next();
                    String dayOfWeekEnumName = getTypeService().getEnumerationValue((HybrisEnumValue)enumValue).getName();
                    timeTable.append(dayOfWeekEnumName);
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


    public void set(TriggerModel model, String value)
    {
        throw new UnsupportedOperationException("Timetable attribute of the Trigger model is read-only...");
    }
}
