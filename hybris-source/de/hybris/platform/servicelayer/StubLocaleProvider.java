package de.hybris.platform.servicelayer;

import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import java.util.List;
import java.util.Locale;

public class StubLocaleProvider implements LocaleProvider
{
    private final Locale locale;


    public StubLocaleProvider(Locale locale)
    {
        this.locale = locale;
    }


    public Locale getCurrentDataLocale()
    {
        return this.locale;
    }


    public List<Locale> getFallbacks(Locale requestedLocale)
    {
        throw new UnsupportedOperationException();
    }


    public boolean isFallbackEnabled()
    {
        return false;
    }


    public Locale toDataLocale(Locale external)
    {
        return this.locale;
    }
}
