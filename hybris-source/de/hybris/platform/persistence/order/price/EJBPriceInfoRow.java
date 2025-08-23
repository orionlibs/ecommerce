package de.hybris.platform.persistence.order.price;

import java.util.Map;

public class EJBPriceInfoRow extends EJBInfoRow
{
    private final double price;


    public EJBPriceInfoRow(Map qualifiers, double price)
    {
        super(qualifiers);
        this.price = price;
    }


    public double getPrice()
    {
        return this.price;
    }
}
