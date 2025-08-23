package de.hybris.platform.b2bwebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "ScheduleReplenishmentForm", description = "Representation of a Schedule Replenishment Form")
public class ScheduleReplenishmentFormWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "replenishmentStartDate", value = "First day for replenishment order", example = "2020-12-31T09:00:00+0000")
    private Date replenishmentStartDate;
    @ApiModelProperty(name = "numberOfDays", value = "How frequent replenishment should be activated expressed in days. Mandatory with 'recurrencePeriod=DAILY'.", example = "14")
    private String numberOfDays;
    @ApiModelProperty(name = "numberOfWeeks", value = "How frequent replenishment should be activated expressed in weeks. Mandatory with 'recurrencePeriod=WEEKLY'.", example = "1")
    private String numberOfWeeks;
    @ApiModelProperty(name = "nthDayOfMonth", value = "On which date of month replenishment should be activated. Mandatory with 'recurrencePeriod=MONTHLY'.", example = "1")
    private String nthDayOfMonth;
    @ApiModelProperty(name = "recurrencePeriod", value = "Replenishment recurrence period. Available values are DAILY, WEEKLY and MONTHLY. DAILY requires 'numberOfDays'. WEEKLY requires 'daysOfWeek' AND 'numberOfWeeks'. MONTHLY requires 'nthDayOfMonth'.", example = "WEEKLY")
    private String recurrencePeriod;
    @ApiModelProperty(name = "daysOfWeek", value = "List of days of week on which replenishment should occur. Mandatory with 'recurrencePeriod=WEEKLY'.")
    private List<DayOfWeekWsDTO> daysOfWeek;


    public void setReplenishmentStartDate(Date replenishmentStartDate)
    {
        this.replenishmentStartDate = replenishmentStartDate;
    }


    public Date getReplenishmentStartDate()
    {
        return this.replenishmentStartDate;
    }


    public void setNumberOfDays(String numberOfDays)
    {
        this.numberOfDays = numberOfDays;
    }


    public String getNumberOfDays()
    {
        return this.numberOfDays;
    }


    public void setNumberOfWeeks(String numberOfWeeks)
    {
        this.numberOfWeeks = numberOfWeeks;
    }


    public String getNumberOfWeeks()
    {
        return this.numberOfWeeks;
    }


    public void setNthDayOfMonth(String nthDayOfMonth)
    {
        this.nthDayOfMonth = nthDayOfMonth;
    }


    public String getNthDayOfMonth()
    {
        return this.nthDayOfMonth;
    }


    public void setRecurrencePeriod(String recurrencePeriod)
    {
        this.recurrencePeriod = recurrencePeriod;
    }


    public String getRecurrencePeriod()
    {
        return this.recurrencePeriod;
    }


    public void setDaysOfWeek(List<DayOfWeekWsDTO> daysOfWeek)
    {
        this.daysOfWeek = daysOfWeek;
    }


    public List<DayOfWeekWsDTO> getDaysOfWeek()
    {
        return this.daysOfWeek;
    }
}
