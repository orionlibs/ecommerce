package de.hybris.platform.servicelayer.internal.model.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public interface LocaleProvider extends Serializable
{
    Locale getCurrentDataLocale();


    Locale toDataLocale(Locale paramLocale);


    boolean isFallbackEnabled();


    List<Locale> getFallbacks(Locale paramLocale);
}
