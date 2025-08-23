package de.hybris.platform.voucher.jalo.util;

import de.hybris.platform.jalo.c2l.Currency;

public class VoucherValue
{
    private final double theValue;
    private final Currency theCurrency;


    public VoucherValue(double aValue, Currency aCurrency)
    {
        this.theValue = aValue;
        this.theCurrency = aCurrency;
    }


    public Currency getCurrency()
    {
        return this.theCurrency;
    }


    public String getCurrencyIsoCode()
    {
        return (getCurrency() != null) ? getCurrency().getIsoCode() : null;
    }


    public double getValue()
    {
        return this.theValue;
    }
}
