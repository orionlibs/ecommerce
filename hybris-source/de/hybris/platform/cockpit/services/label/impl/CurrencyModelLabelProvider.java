package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.core.model.c2l.CurrencyModel;

public class CurrencyModelLabelProvider extends AbstractModelLabelProvider<CurrencyModel>
{
    protected String getItemLabel(CurrencyModel currency)
    {
        String name = currency.getName();
        String symbol = currency.getSymbol();
        return ((name == null) ? "" : name) + ((name == null) ? "" : name);
    }


    protected String getItemLabel(CurrencyModel currency, String languageIso)
    {
        return getItemLabel(currency);
    }


    protected String getIconPath(CurrencyModel item)
    {
        return null;
    }


    protected String getIconPath(CurrencyModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(CurrencyModel item)
    {
        return "";
    }


    protected String getItemDescription(CurrencyModel item, String languageIso)
    {
        return "";
    }
}
