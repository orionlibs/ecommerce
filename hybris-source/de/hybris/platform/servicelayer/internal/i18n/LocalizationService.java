package de.hybris.platform.servicelayer.internal.i18n;

import de.hybris.platform.core.PK;
import java.util.Locale;
import java.util.Set;

@Deprecated(since = "ages", forRemoval = true)
public interface LocalizationService
{
    public static final String BEAN = "localizationService";


    @Deprecated(since = "ages", forRemoval = true)
    Locale getLocaleByString(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    Locale[] getAllLocales(Locale paramLocale);


    @Deprecated(since = "ages", forRemoval = true)
    Locale[] getFallbackLocales(Locale paramLocale);


    @Deprecated(since = "ages", forRemoval = true)
    Locale getDataLocale(Locale paramLocale);


    @Deprecated(since = "ages", forRemoval = true)
    Locale getCurrentLocale();


    @Deprecated(since = "ages", forRemoval = true)
    void setCurrentLocale(Locale paramLocale);


    @Deprecated(since = "ages", forRemoval = true)
    Locale getCurrentDataLocale();


    @Deprecated(since = "ages", forRemoval = true)
    String getDataLanguageIsoCode(Locale paramLocale);


    @Deprecated(since = "ages", forRemoval = true)
    boolean isLocalizationFallbackEnabled();


    @Deprecated(since = "ages", forRemoval = true)
    void setLocalizationFallbackEnabled(boolean paramBoolean);


    @Deprecated(since = "ages", forRemoval = true)
    Set<Locale> getSupportedDataLocales();


    PK getMatchingPkForDataLocale(Locale paramLocale);
}
