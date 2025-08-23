package de.hybris.platform.europe1.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.util.StandardDateRange;
import java.util.Date;

public class PriceRowBasicData
{
    private final PK pk;
    private final Date startTime;
    private final Date endTime;
    private final Currency currency;
    private final boolean giveAwayPrice;


    PriceRowBasicData(PK pk, Date startTime, Date endTime, Currency currency, boolean giveAwayPrice)
    {
        this.pk = pk;
        this.startTime = startTime;
        this.endTime = endTime;
        this.currency = currency;
        this.giveAwayPrice = giveAwayPrice;
    }


    public boolean isCurrencyValid(Currency curr, Currency base)
    {
        return (curr.equals(this.currency) || (base != null && base.equals(this.currency)));
    }


    public boolean isDateInRange(Date date)
    {
        return (this.startTime == null || this.endTime == null || (new StandardDateRange(this.startTime, this.endTime)).encloses(date));
    }


    public PK getPK()
    {
        return this.pk;
    }


    public boolean isGiveAwayModeValid(boolean giveAwayMode)
    {
        return (this.giveAwayPrice == giveAwayMode);
    }
}
