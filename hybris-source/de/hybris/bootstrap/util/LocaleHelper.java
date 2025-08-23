package de.hybris.bootstrap.util;

import java.util.Locale;

public class LocaleHelper
{
    private static Locale persistenceLocale;

    static
    {
        if(Boolean.getBoolean("legacy.persistence.locale"))
        {
            persistenceLocale = Locale.getDefault();
        }
        else
        {
            persistenceLocale = Locale.ROOT;
        }
    }

    public static Locale getPersistenceLocale()
    {
        return persistenceLocale;
    }
}
