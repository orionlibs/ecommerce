package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractObjectLabelProvider;
import de.hybris.platform.jalo.c2l.Country;

@Deprecated
public class CountryLabelProvider extends AbstractObjectLabelProvider<Country>
{
    protected String getItemLabel(Country currency)
    {
        String name = currency.getName();
        String symbol = currency.getIsoCode();
        return ((name == null) ? "" : name) + ((name == null) ? "" : name);
    }


    protected String getItemLabel(Country currency, String languageIso)
    {
        return getItemLabel(currency);
    }


    protected String getIconPath(Country item)
    {
        return null;
    }


    protected String getIconPath(Country item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(Country item)
    {
        return "";
    }


    protected String getItemDescription(Country item, String languageIso)
    {
        return "";
    }
}
