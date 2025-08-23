package de.hybris.platform.commercewebservices.core.storesession.data;

import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import java.io.Serializable;
import java.util.Collection;

public class CurrencyDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Collection<CurrencyData> currencies;


    public void setCurrencies(Collection<CurrencyData> currencies)
    {
        this.currencies = currencies;
    }


    public Collection<CurrencyData> getCurrencies()
    {
        return this.currencies;
    }
}
