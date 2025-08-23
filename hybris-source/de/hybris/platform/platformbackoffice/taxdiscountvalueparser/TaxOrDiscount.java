package de.hybris.platform.platformbackoffice.taxdiscountvalueparser;

import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

public class TaxOrDiscount
{
    private String code;
    private double value;
    private double appliedValue;
    private boolean absolute;
    private String currencyIsoCode;


    public TaxOrDiscount(Object initialValue)
    {
        if(initialValue instanceof TaxValue)
        {
            TaxValue taxValue = (TaxValue)initialValue;
            this.code = taxValue.getCode();
            this.value = taxValue.getValue();
            this.appliedValue = taxValue.getAppliedValue();
            this.absolute = taxValue.isAbsolute();
            this.currencyIsoCode = taxValue.getCurrencyIsoCode();
        }
        else if(initialValue instanceof DiscountValue)
        {
            DiscountValue discountValue = (DiscountValue)initialValue;
            this.code = discountValue.getCode();
            this.value = discountValue.getValue();
            this.appliedValue = discountValue.getAppliedValue();
            this.absolute = discountValue.isAbsolute();
            this.currencyIsoCode = discountValue.getCurrencyIsoCode();
        }
    }


    public TaxOrDiscount(String code, double value, boolean isAbsolute, double appliedValue, String currencyIsoCode)
    {
        this.code = code;
        this.value = value;
        this.absolute = isAbsolute;
        this.appliedValue = appliedValue;
        this.currencyIsoCode = currencyIsoCode;
    }


    public String getCode()
    {
        return this.code;
    }


    public double getValue()
    {
        return this.value;
    }


    public double getAppliedValue()
    {
        return this.appliedValue;
    }


    public boolean isAbsolute()
    {
        return this.absolute;
    }


    public String getCurrencyIsoCode()
    {
        return this.currencyIsoCode;
    }


    public TaxValue toTaxValue()
    {
        return new TaxValue(this.code, this.value, isAbsolute(), this.appliedValue, (this.currencyIsoCode == null) ? "%" : this.currencyIsoCode);
    }


    public DiscountValue toDiscountValue()
    {
        return new DiscountValue(this.code, this.value, isAbsolute(), this.appliedValue, (this.currencyIsoCode == null) ? "%" : this.currencyIsoCode);
    }
}
