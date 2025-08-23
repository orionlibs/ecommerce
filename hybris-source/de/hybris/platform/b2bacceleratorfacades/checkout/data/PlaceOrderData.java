package de.hybris.platform.b2bacceleratorfacades.checkout.data;

import de.hybris.platform.b2bacceleratorfacades.order.data.B2BReplenishmentRecurrenceEnum;
import de.hybris.platform.cronjob.enums.DayOfWeek;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PlaceOrderData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String securityCode;
    private Boolean termsCheck;
    private Boolean negotiateQuote;
    private String quoteRequestDescription;
    private Boolean replenishmentOrder;
    private Date replenishmentStartDate;
    private B2BReplenishmentRecurrenceEnum replenishmentRecurrence;
    private List<DayOfWeek> nDaysOfWeek;
    private String nDays;
    private String nWeeks;
    private String nthDayOfMonth;


    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
    }


    public String getSecurityCode()
    {
        return this.securityCode;
    }


    public void setTermsCheck(Boolean termsCheck)
    {
        this.termsCheck = termsCheck;
    }


    public Boolean getTermsCheck()
    {
        return this.termsCheck;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public void setNegotiateQuote(Boolean negotiateQuote)
    {
        this.negotiateQuote = negotiateQuote;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public Boolean getNegotiateQuote()
    {
        return this.negotiateQuote;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public void setQuoteRequestDescription(String quoteRequestDescription)
    {
        this.quoteRequestDescription = quoteRequestDescription;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public String getQuoteRequestDescription()
    {
        return this.quoteRequestDescription;
    }


    public void setReplenishmentOrder(Boolean replenishmentOrder)
    {
        this.replenishmentOrder = replenishmentOrder;
    }


    public Boolean getReplenishmentOrder()
    {
        return this.replenishmentOrder;
    }


    public void setReplenishmentStartDate(Date replenishmentStartDate)
    {
        this.replenishmentStartDate = replenishmentStartDate;
    }


    public Date getReplenishmentStartDate()
    {
        return this.replenishmentStartDate;
    }


    public void setReplenishmentRecurrence(B2BReplenishmentRecurrenceEnum replenishmentRecurrence)
    {
        this.replenishmentRecurrence = replenishmentRecurrence;
    }


    public B2BReplenishmentRecurrenceEnum getReplenishmentRecurrence()
    {
        return this.replenishmentRecurrence;
    }


    public void setNDaysOfWeek(List<DayOfWeek> nDaysOfWeek)
    {
        this.nDaysOfWeek = nDaysOfWeek;
    }


    public List<DayOfWeek> getNDaysOfWeek()
    {
        return this.nDaysOfWeek;
    }


    public void setNDays(String nDays)
    {
        this.nDays = nDays;
    }


    public String getNDays()
    {
        return this.nDays;
    }


    public void setNWeeks(String nWeeks)
    {
        this.nWeeks = nWeeks;
    }


    public String getNWeeks()
    {
        return this.nWeeks;
    }


    public void setNthDayOfMonth(String nthDayOfMonth)
    {
        this.nthDayOfMonth = nthDayOfMonth;
    }


    public String getNthDayOfMonth()
    {
        return this.nthDayOfMonth;
    }
}
