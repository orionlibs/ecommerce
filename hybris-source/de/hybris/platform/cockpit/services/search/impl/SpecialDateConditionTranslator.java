package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.model.advancedsearch.ConditionValue;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpecialDateConditionTranslator extends DefaultGenericQueryConditionTranslator
{
    private static final Logger LOG = LoggerFactory.getLogger(SpecialDateConditionTranslator.class);


    protected GenericCondition createCondition(GenericQuery query, ConditionValue conditionValue, SearchParameterDescriptor descriptor)
    {
        GenericCondition ret = null;
        Operator operator = conditionValue.getOperator();
        String typeCode = getTypeCode((GenericSearchParameterDescriptor)descriptor);
        String attribute = getAttributeQuali((GenericSearchParameterDescriptor)descriptor);
        preprocess(query, conditionValue, (GenericSearchParameterDescriptor)descriptor);
        Date firstValue = null;
        Date secondValue = null;
        Date currentDateWithoutTime = DateUtils.truncate(Calendar.getInstance().getTime(), 5);
        if("today".equalsIgnoreCase(operator.getQualifier()))
        {
            firstValue = currentDateWithoutTime;
            secondValue = DateUtils.addDays(firstValue, 1);
        }
        else if("tomorrow".equalsIgnoreCase(operator.getQualifier()))
        {
            firstValue = DateUtils.addDays(currentDateWithoutTime, 1);
            secondValue = DateUtils.addDays(firstValue, 1);
        }
        else if("yesterday".equalsIgnoreCase(operator.getQualifier()))
        {
            firstValue = DateUtils.addDays(currentDateWithoutTime, -1);
            secondValue = currentDateWithoutTime;
        }
        else if("thisweek".equalsIgnoreCase(operator.getQualifier()))
        {
            firstValue = truncateWeek(currentDateWithoutTime);
            secondValue = DateUtils.addWeeks(firstValue, 1);
        }
        else if("lastweek".equalsIgnoreCase(operator.getQualifier()))
        {
            secondValue = truncateWeek(currentDateWithoutTime);
            firstValue = DateUtils.addWeeks(secondValue, -1);
        }
        else if("thismonth".equalsIgnoreCase(operator.getQualifier()))
        {
            firstValue = DateUtils.truncate(currentDateWithoutTime, 2);
            secondValue = DateUtils.addMonths(firstValue, 1);
        }
        else if("lastmonth".equalsIgnoreCase(operator.getQualifier()))
        {
            secondValue = DateUtils.truncate(currentDateWithoutTime, 2);
            firstValue = DateUtils.addMonths(secondValue, -1);
        }
        else if("thisyear".equalsIgnoreCase(operator.getQualifier()))
        {
            firstValue = DateUtils.truncate(currentDateWithoutTime, 1);
            secondValue = DateUtils.addYears(firstValue, 1);
        }
        else if("lastyear".equalsIgnoreCase(operator.getQualifier()))
        {
            secondValue = DateUtils.truncate(currentDateWithoutTime, 1);
            firstValue = DateUtils.addYears(secondValue, -1);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported operator " + operator + " for descriptor " + this);
        }
        List<GenericCondition> conditions = new ArrayList<>();
        GenericSearchField field = new GenericSearchField(typeCode, attribute);
        conditions.add(GenericCondition.greaterOrEqual(field, firstValue));
        conditions.add(GenericCondition.lessOrEqual(field, secondValue));
        GenericConditionList genericConditionList = GenericCondition.and(conditions);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Searching between " +
                            DateFormat.getDateTimeInstance(3, 3, Locale.GERMANY).format(firstValue) + " and " +
                            DateFormat.getDateTimeInstance().format(secondValue));
        }
        return (GenericCondition)genericConditionList;
    }


    private Date truncateWeek(Date date)
    {
        int day = date.getDay() - 1;
        if(day < 0)
        {
            day = 6;
        }
        return DateUtils.addDays(date, -day);
    }
}
