package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.core.model.c2l.CountryModel;

public class CountryModelLabelProvider extends AbstractModelLabelProvider<CountryModel>
{
    protected String getItemLabel(CountryModel currency)
    {
        String name = currency.getName();
        String symbol = currency.getIsocode();
        return ((name == null) ? "" : name) + ((name == null) ? "" : name);
    }


    protected String getItemLabel(CountryModel currency, String languageIso)
    {
        return getItemLabel(currency);
    }


    protected String getIconPath(CountryModel item)
    {
        return null;
    }


    protected String getIconPath(CountryModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(CountryModel item)
    {
        return "";
    }


    protected String getItemDescription(CountryModel item, String languageIso)
    {
        return "";
    }
}
