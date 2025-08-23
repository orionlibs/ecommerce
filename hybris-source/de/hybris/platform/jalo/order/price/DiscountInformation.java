package de.hybris.platform.jalo.order.price;

import de.hybris.platform.util.DiscountValue;
import java.util.Map;

public class DiscountInformation extends QualifiedPricingInfo implements PDTInformation
{
    private final DiscountValue value;


    public DiscountInformation(DiscountValue value)
    {
        this.value = value;
    }


    public DiscountInformation(Map qualifiers, DiscountValue value)
    {
        super(qualifiers);
        this.value = value;
    }


    public DiscountValue getValue()
    {
        return getDiscountValue();
    }


    public DiscountValue getDiscountValue()
    {
        return this.value;
    }
}
