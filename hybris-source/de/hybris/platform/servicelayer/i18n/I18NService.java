package de.hybris.platform.servicelayer.i18n;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

public interface I18NService
{
    Locale getCurrentLocale();


    void setCurrentLocale(Locale paramLocale);


    Set<Locale> getSupportedLocales();


    Set<Currency> getSupportedJavaCurrencies();


    TimeZone getCurrentTimeZone();


    void setCurrentTimeZone(TimeZone paramTimeZone);


    Currency getCurrentJavaCurrency();


    void setCurrentJavaCurrency(Currency paramCurrency);


    boolean isLocalizationFallbackEnabled();


    void setLocalizationFallbackEnabled(boolean paramBoolean);


    Currency getBestMatchingJavaCurrency(String paramString);


    Locale getBestMatchingLocale(Locale paramLocale);


    Locale[] getAllLocales(Locale paramLocale);


    Locale[] getFallbackLocales(Locale paramLocale);


    @Deprecated(since = "ages", forRemoval = true)
    CurrencyModel getCurrentCurrency();


    @Deprecated(since = "ages", forRemoval = true)
    void setCurrentCurrency(CurrencyModel paramCurrencyModel);


    @Deprecated(since = "ages", forRemoval = true)
    Set<Locale> getSupportedDataLocales();


    @Deprecated(since = "ages", forRemoval = true)
    ResourceBundle getBundle(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    ResourceBundle getBundle(String paramString, Locale[] paramArrayOfLocale);


    @Deprecated(since = "ages", forRemoval = true)
    ResourceBundle getBundle(String paramString, Locale[] paramArrayOfLocale, ClassLoader paramClassLoader);


    @Deprecated(since = "ages", forRemoval = true)
    LanguageModel getLanguage(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    Set<LanguageModel> getAllLanguages();


    @Deprecated(since = "ages", forRemoval = true)
    Set<LanguageModel> getAllActiveLanguages();


    @Deprecated(since = "ages", forRemoval = true)
    CountryModel getCountry(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    Set<CountryModel> getAllCountries();


    @Deprecated(since = "ages", forRemoval = true)
    CurrencyModel getCurrency(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    Set<CurrencyModel> getAllCurrencies();


    @Deprecated(since = "ages", forRemoval = true)
    CurrencyModel getBaseCurrency();


    @Deprecated(since = "ages", forRemoval = true)
    String getEnumLocalizedName(HybrisEnumValue paramHybrisEnumValue);


    @Deprecated(since = "ages", forRemoval = true)
    void setEnumLocalizedName(HybrisEnumValue paramHybrisEnumValue, String paramString);


    PK getLangPKFromLocale(Locale paramLocale);
}
