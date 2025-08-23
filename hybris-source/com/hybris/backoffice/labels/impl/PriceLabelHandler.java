/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.labels.impl;

import com.hybris.backoffice.labels.LabelHandler;
import com.hybris.cockpitng.core.util.Validate;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class PriceLabelHandler implements LabelHandler<Double, CurrencyModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(PriceLabelHandler.class);
    private I18NService i18NService;


    @Override
    public String getLabel(final Double price, final CurrencyModel currencyModel)
    {
        Validate.notNull("Price may not be null", price);
        Validate.notNull("Currency may not be null", currencyModel);
        final String currencyIsoCode = currencyModel.getIsocode();
        Currency javaCurrency = null;
        try
        {
            javaCurrency = getI18NService().getBestMatchingJavaCurrency(currencyIsoCode);
        }
        catch(final IllegalArgumentException iae)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.warn(String.format("Could not find Java currency for iso code: '%s'.", currencyIsoCode), iae);
            }
            else
            {
                LOG.warn("Could not find Java currency for iso code: '{}'.", currencyIsoCode);
            }
        }
        try
        {
            if(javaCurrency == null)
            {
                return getNumberFormatForNonJavaCurrency(currencyModel).format(price);
            }
            else
            {
                return getNumberFormatter(javaCurrency, currencyModel).format(price.doubleValue());
            }
        }
        catch(final RuntimeException re)
        {
            LOG.warn(String.format("Could not format the given price '%d' in the given currency '%s'", price,
                            currencyModel.getSymbol()), re);
            return StringUtils.EMPTY;
        }
    }


    protected NumberFormat getNumberFormatForNonJavaCurrency(final CurrencyModel currencyModel)
    {
        final Locale currentLocale = getI18NService().getCurrentLocale();
        final NumberFormat df = NumberFormat.getCurrencyInstance(currentLocale);
        final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol(ObjectUtils.defaultIfNull(currencyModel.getSymbol(), StringUtils.EMPTY));
        dfs.setMonetaryDecimalSeparator('.');
        ((DecimalFormat)df).setDecimalFormatSymbols(dfs);
        return df;
    }


    protected NumberFormat getNumberFormatter(final Currency javaCurrency, final CurrencyModel currencyModel)
    {
        final Locale currentLocale = getI18NService().getCurrentLocale();
        final NumberFormat formatter = NumberFormat.getCurrencyInstance(currentLocale);
        if(formatter instanceof DecimalFormat)
        {
            adjustDecimalFormatter(javaCurrency, currencyModel, currentLocale, (DecimalFormat)formatter);
        }
        return formatter;
    }


    protected void adjustDecimalFormatter(final Currency javaCurrency, final CurrencyModel currencyModel,
                    final Locale currentLocale, final DecimalFormat decimalFormatter)
    {
        final DecimalFormatSymbols decimalSymbols = getDecimalFormatSymbols(javaCurrency, currencyModel.getSymbol(), currentLocale);
        decimalFormatter.setDecimalFormatSymbols(decimalSymbols);
        adjustFractionPart(currencyModel.getDigits(), decimalFormatter);
    }


    protected DecimalFormatSymbols getDecimalFormatSymbols(final Currency currency, final String currencySymbol,
                    final Locale currentLocale)
    {
        final DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance(currentLocale);
        decimalSymbols.setCurrency(currency);
        if(StringUtils.isNotBlank(currencySymbol))
        {
            decimalSymbols.setCurrencySymbol(currencySymbol);
        }
        return decimalSymbols;
    }


    protected void adjustFractionPart(final Integer digits, final DecimalFormat decimalFormat)
    {
        decimalFormat.setMaximumFractionDigits(digits);
        decimalFormat.setMinimumFractionDigits(digits);
        decimalFormat.setDecimalSeparatorAlwaysShown(digits > 0);
    }


    public I18NService getI18NService()
    {
        return i18NService;
    }


    @Required
    public void setI18NService(final I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
