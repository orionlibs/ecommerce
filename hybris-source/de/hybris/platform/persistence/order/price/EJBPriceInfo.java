package de.hybris.platform.persistence.order.price;

import java.io.Serializable;
import java.util.Collection;

public class EJBPriceInfo implements Serializable
{
    private final Collection prices;
    private final Collection discounts;
    private final Collection taxes;


    public EJBPriceInfo(Collection prices, Collection discounts, Collection taxes)
    {
        this.prices = prices;
        this.discounts = discounts;
        this.taxes = taxes;
    }


    public void addPriceInfoRow(EJBPriceInfoRow row)
    {
        this.prices.add(row);
    }


    public void addPriceInfoRows(Collection rows)
    {
        this.prices.addAll(rows);
    }


    public void removePriceInfoRow(EJBPriceInfoRow row)
    {
        this.prices.remove(row);
    }


    public void removeAllPriceInfoRow()
    {
        this.prices.clear();
    }


    public Collection getPriceInfoRows()
    {
        return this.prices;
    }


    public void addDiscountInfoRow(EJBDiscountInfoRow row)
    {
        this.prices.add(row);
    }


    public void addDiscountInfoRows(Collection rows)
    {
        this.prices.addAll(rows);
    }


    public void removeDiscountInfoRow(EJBDiscountInfoRow row)
    {
        this.prices.remove(row);
    }


    public void removeAllDiscountInfoRow()
    {
        this.prices.clear();
    }


    public Collection getDiscountInfoRows()
    {
        return this.discounts;
    }


    public void addTaxInfoRow(EJBTaxInfoRow row)
    {
        this.prices.add(row);
    }


    public void addTaxInfoRows(Collection rows)
    {
        this.prices.addAll(rows);
    }


    public void removeTaxInfoRow(EJBTaxInfoRow row)
    {
        this.prices.remove(row);
    }


    public void removeAllTaxInfoRow()
    {
        this.prices.clear();
    }


    public Collection getTaxInfoRows()
    {
        return this.taxes;
    }
}
