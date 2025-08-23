package de.hybris.platform.cockpit.helpers.impl;

import de.hybris.platform.cockpit.helpers.LocaleHelper;
import java.util.Locale;

public class DefaultLocaleHelper implements LocaleHelper
{
    public Locale getLocale(String isoCode)
    {
        Locale result;
        String[] splitted_code = isoCode.split("_");
        if(splitted_code.length == 1)
        {
            result = new Locale(splitted_code[0]);
        }
        else
        {
            result = new Locale(splitted_code[0], splitted_code[1]);
        }
        return result;
    }
}
