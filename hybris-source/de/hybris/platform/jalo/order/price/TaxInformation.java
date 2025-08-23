package de.hybris.platform.jalo.order.price;

import de.hybris.platform.util.TaxValue;
import java.util.Map;

public class TaxInformation extends QualifiedPricingInfo implements PDTInformation
{
    private final TaxValue taxValue;


    public TaxInformation(TaxValue taxValue)
    {
        this.taxValue = taxValue;
    }


    public TaxInformation(Map qualifiers, TaxValue taxValue)
    {
        super(qualifiers);
        this.taxValue = taxValue;
    }


    public TaxValue getTaxValue()
    {
        return this.taxValue;
    }


    public TaxValue getValue()
    {
        return getTaxValue();
    }
}
