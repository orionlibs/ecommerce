package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.c2l.Currency;

@Deprecated
public class CurrencyLabelProvider extends AbstractObjectLabelProvider<Currency>
{
    protected String getItemLabel(Currency currency)
    {
        String name = currency.getName();
        String symbol = currency.getSymbol();
        return ((name == null) ? "" : name) + ((name == null) ? "" : name);
    }


    protected String getItemLabel(Currency currency, String languageIso)
    {
        return getItemLabel(currency);
    }


    protected String getIconPath(Currency item)
    {
        return null;
    }


    protected String getIconPath(Currency item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(Currency item)
    {
        return "";
    }


    protected String getItemDescription(Currency item, String languageIso)
    {
        return "";
    }
}
