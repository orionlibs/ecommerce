package de.hybris.platform.servicelayer.i18n.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFormatFactory implements FormatFactory
{
    private I18NService i18nService;
    private CommonI18NService commonI18NService;


    public NumberFormat createCurrencyFormat()
    {
        NumberFormat ret = NumberFormat.getCurrencyInstance(this.i18nService.getCurrentLocale());
        CurrencyModel currencyModel = this.commonI18NService.getCurrentCurrency();
        if(currencyModel != null)
        {
            adjustDigits((DecimalFormat)ret, currencyModel);
            adjustSymbol((DecimalFormat)ret, currencyModel);
        }
        return ret;
    }


    public DateFormat createDateTimeFormat(int dateStyle, int timeStyle)
    {
        DateFormat format = null;
        Locale locale = this.i18nService.getCurrentLocale();
        if(timeStyle == -1 && dateStyle != -1)
        {
            format = DateFormat.getDateInstance(dateStyle, locale);
        }
        else if(dateStyle == -1 && timeStyle != -1)
        {
            format = DateFormat.getTimeInstance(timeStyle, locale);
        }
        else if(dateStyle != -1 && timeStyle != -1)
        {
            format = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        }
        else
        {
            throw new IllegalArgumentException("Both time style and date style can not be -1");
        }
        format.setCalendar(Calendar.getInstance(this.i18nService.getCurrentTimeZone(), locale));
        return format;
    }


    public DateFormat createDateTimeFormat(String dateFormat)
    {
        Locale locale = this.i18nService.getCurrentLocale();
        return new SimpleDateFormat(dateFormat, locale);
    }


    public NumberFormat createNumberFormat(String pattern)
    {
        NumberFormat numberFormat = createNumberFormat();
        if(numberFormat instanceof DecimalFormat)
        {
            ((DecimalFormat)numberFormat).applyPattern(pattern);
        }
        return numberFormat;
    }


    public NumberFormat createIntegerFormat()
    {
        return NumberFormat.getIntegerInstance(this.i18nService
                        .getCurrentLocale());
    }


    public NumberFormat createNumberFormat()
    {
        return NumberFormat.getNumberInstance(this.i18nService.getCurrentLocale());
    }


    public NumberFormat createPercentFormat()
    {
        return NumberFormat.getPercentInstance(this.i18nService.getCurrentLocale());
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    public I18NService getI18nService()
    {
        return this.i18nService;
    }


    private DecimalFormat adjustDigits(DecimalFormat format, CurrencyModel currencyModel)
    {
        int tempDigits;
        if(currencyModel.getDigits() == null)
        {
            tempDigits = 0;
        }
        else
        {
            tempDigits = currencyModel.getDigits().intValue();
        }
        int digits = Math.max(0, tempDigits);
        format.setMaximumFractionDigits(digits);
        format.setMinimumFractionDigits(digits);
        if(digits == 0)
        {
            format.setDecimalSeparatorAlwaysShown(false);
        }
        return format;
    }


    private DecimalFormat adjustSymbol(DecimalFormat format, CurrencyModel currencyModel)
    {
        String symbol = currencyModel.getSymbol();
        if(symbol != null)
        {
            DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
            String iso = currencyModel.getIsocode();
            boolean changed = false;
            if(!iso.equalsIgnoreCase(symbols.getInternationalCurrencySymbol()))
            {
                symbols.setInternationalCurrencySymbol(iso);
                changed = true;
            }
            if(!symbol.equals(symbols.getCurrencySymbol()))
            {
                symbols.setCurrencySymbol(symbol);
                changed = true;
            }
            if(changed)
            {
                format.setDecimalFormatSymbols(symbols);
            }
        }
        return format;
    }
}
