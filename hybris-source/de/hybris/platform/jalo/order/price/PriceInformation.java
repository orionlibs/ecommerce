package de.hybris.platform.jalo.order.price;

import de.hybris.platform.util.PriceValue;
import java.util.Map;

public class PriceInformation extends QualifiedPricingInfo implements PDTInformation
{
    private final PriceValue price;


    public PriceInformation(PriceValue price)
    {
        this.price = price;
    }


    public PriceInformation(Map qualifiers, PriceValue price)
    {
        super(qualifiers);
        this.price = price;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public PriceValue getPrice()
    {
        return this.price;
    }


    public PriceValue getPriceValue()
    {
        return this.price;
    }


    public String toString()
    {
        StringBuilder toString = new StringBuilder();
        toString.append(super.toString()).append(" ").append(getPriceValue().toString());
        return toString.toString();
    }


    public boolean equalsWithoutPriceRow(PriceInformation otherPriceInformation)
    {
        if(getQualifierCount() != otherPriceInformation.getQualifierCount())
        {
            return false;
        }
        for(Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)getQualifiers().entrySet())
        {
            String key = entry.getKey();
            if("pricerow".equals(key))
            {
                continue;
            }
            if(!otherPriceInformation.getQualifiers().containsKey(key))
            {
                return false;
            }
            if(!otherPriceInformation.getQualifiers().get(key).equals(entry.getValue()))
            {
                return false;
            }
        }
        return true;
    }


    public PriceValue getValue()
    {
        return getPriceValue();
    }
}
