package de.hybris.platform.europe1.jalo.impex;

import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.SingleValueTranslator;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.util.DateRange;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.util.Utilities;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public abstract class Europe1RowTranslator extends SingleValueTranslator
{
    private SimpleDateFormat dateFormat;
    private NumberFormat numberFormat;
    private Locale loc;
    private final Map currenciesISOs = new HashMap<>();
    private final Map currenciesSymbols = new HashMap<>();


    public Europe1RowTranslator()
    {
        this(null, null, null);
    }


    public Europe1RowTranslator(SimpleDateFormat dateFormat, NumberFormat numberFormat, Locale loc)
    {
        this.dateFormat = dateFormat;
        this.numberFormat = numberFormat;
        this.loc = loc;
        Collection cur = C2LManager.getInstance().getAllCurrencies();
        for(Iterator<Currency> it = cur.iterator(); it.hasNext(); )
        {
            Currency currency = it.next();
            this.currenciesISOs.put(currency.getIsoCode().toLowerCase(), currency);
            if(currency.getSymbol() != null)
            {
                this.currenciesSymbols.put(currency.getSymbol().toLowerCase(), currency);
            }
        }
    }


    public void init(StandardColumnDescriptor columnDescriptor)
    {
        super.init(columnDescriptor);
        this.loc = getColumnDescriptor().getHeader().getReader().getLocale();
        if(getDateFormatString((AbstractDescriptor)columnDescriptor) != null)
        {
            this.dateFormat = Utilities.getSimpleDateFormat(getDateFormatString((AbstractDescriptor)columnDescriptor), this.loc);
        }
        else
        {
            this.dateFormat = (SimpleDateFormat)Utilities.getDateTimeInstance(2, 2, this.loc);
        }
        if(getNumberFormatString((AbstractDescriptor)columnDescriptor) != null)
        {
            this.numberFormat = Utilities.getDecimalFormat(getNumberFormatString((AbstractDescriptor)columnDescriptor), this.loc);
        }
        else
        {
            this.numberFormat = Utilities.getNumberInstance(this.loc);
        }
    }


    protected DateRange parseDateRange(String valueExpr)
    {
        StandardDateRange standardDateRange;
        DateRange dateRange = null;
        int startPosition = valueExpr.indexOf('[');
        int endPosition = valueExpr.indexOf(']');
        boolean dateRangeIsWelldefined = true;
        if(startPosition != -1 || endPosition != -1)
        {
            dateRangeIsWelldefined = false;
            if(endPosition > startPosition)
            {
                String dateRangeExpr = valueExpr.substring(startPosition + 1, endPosition);
                int sepPos = dateRangeExpr.indexOf(',');
                if(sepPos != -1)
                {
                    try
                    {
                        String start = dateRangeExpr.substring(0, sepPos).trim();
                        String end = dateRangeExpr.substring(sepPos + 1, dateRangeExpr.length()).trim();
                        Date startDate = transformStartDate(this.dateFormat.parse(start));
                        Date endDate = transformEndDate(this.dateFormat.parse(end));
                        standardDateRange = new StandardDateRange(startDate, endDate);
                        dateRangeIsWelldefined = true;
                    }
                    catch(ParseException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(!dateRangeIsWelldefined)
        {
            throw new JaloInvalidParameterException("Invalid daterange definition!", 123);
        }
        return (DateRange)standardDateRange;
    }


    protected Date transformStartDate(Date startDate)
    {
        return startDate;
    }


    protected Date transformEndDate(Date endDate)
    {
        Calendar cal = Utilities.getDefaultCalendar(this.loc);
        cal.setTime(endDate);
        if(cal.get(11) == 0 && cal.get(12) == 0 && cal.get(13) == 0)
        {
            cal.set(11, 23);
            cal.set(12, 59);
            cal.set(13, 59);
        }
        return cal.getTime();
    }


    protected ParsedCurrency parseCurrency(String valueExpr)
    {
        int startOfCurrencyDef = -1;
        Currency currency2use = null;
        for(Iterator<String> iterator1 = this.currenciesISOs.keySet().iterator(); iterator1.hasNext() && currency2use == null; )
        {
            String iso = ((String)iterator1.next()).toLowerCase();
            startOfCurrencyDef = valueExpr.toLowerCase().indexOf(iso);
            if(startOfCurrencyDef != -1)
            {
                currency2use = (Currency)this.currenciesISOs.get(iso);
            }
        }
        for(Iterator<String> it = this.currenciesSymbols.keySet().iterator(); it.hasNext() && currency2use == null; )
        {
            String symbol = ((String)it.next()).toLowerCase();
            startOfCurrencyDef = valueExpr.toLowerCase().indexOf(symbol);
            if(startOfCurrencyDef != -1)
            {
                currency2use = (Currency)this.currenciesSymbols.get(symbol);
            }
        }
        return new ParsedCurrency(currency2use, startOfCurrencyDef);
    }


    protected DateFormat getDateFormat()
    {
        return this.dateFormat;
    }


    protected NumberFormat getNumberFormat()
    {
        return this.numberFormat;
    }


    protected Map getCurrenciesISOs()
    {
        return this.currenciesISOs;
    }


    protected Map getCurrenciesSymbols()
    {
        return this.currenciesSymbols;
    }


    protected String getDateFormatString(AbstractDescriptor columnDescriptor)
    {
        String format = (columnDescriptor != null) ? columnDescriptor.getDescriptorData().getModifier("dateformat") : null;
        return (format != null && format.length() > 0) ? format : "dd.MM.yyyy";
    }


    protected String getNumberFormatString(AbstractDescriptor columnDescriptor)
    {
        String format = (columnDescriptor != null) ? columnDescriptor.getDescriptorData().getModifier("numberformat") : null;
        return (format != null && format.length() > 0) ? format : null;
    }
}
