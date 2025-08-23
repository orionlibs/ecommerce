package de.hybris.platform.jalo.order.price;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ProductPriceInformations
{
    private final Collection<PriceInformation> prices;
    private final Collection<TaxInformation> taxes;
    private final Collection<DiscountInformation> discounts;


    public ProductPriceInformations(PriceInformation pinf)
    {
        this(Collections.singleton(pinf), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }


    public ProductPriceInformations(Collection<PriceInformation> prices)
    {
        this(prices, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }


    public ProductPriceInformations(Collection<PriceInformation> prices, Collection<TaxInformation> taxes)
    {
        this(prices, taxes, Collections.EMPTY_LIST);
    }


    public ProductPriceInformations(Collection<PriceInformation> prices, Collection<TaxInformation> taxes, Collection<DiscountInformation> discounts)
    {
        this.prices = Collections.unmodifiableCollection(new ArrayList<>(prices));
        this.taxes = Collections.unmodifiableCollection(new ArrayList<>(taxes));
        this.discounts = Collections.unmodifiableCollection(new ArrayList<>(discounts));
    }


    public Collection<PriceInformation> getPrices()
    {
        return this.prices;
    }


    public Collection<DiscountInformation> getDiscounts()
    {
        return this.discounts;
    }


    public Collection<TaxInformation> getTaxes()
    {
        return this.taxes;
    }
}
