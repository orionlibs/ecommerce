package de.hybris.platform.acceleratorfacades.order.data;

import de.hybris.platform.commercefacades.product.data.PriceData;
import java.io.Serializable;

public class PriceRangeData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private PriceData minPrice;
    private PriceData maxPrice;


    public void setMinPrice(PriceData minPrice)
    {
        this.minPrice = minPrice;
    }


    public PriceData getMinPrice()
    {
        return this.minPrice;
    }


    public void setMaxPrice(PriceData maxPrice)
    {
        this.maxPrice = maxPrice;
    }


    public PriceData getMaxPrice()
    {
        return this.maxPrice;
    }
}
