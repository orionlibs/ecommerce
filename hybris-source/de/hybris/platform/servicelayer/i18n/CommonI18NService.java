package de.hybris.platform.servicelayer.i18n;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import java.util.List;
import java.util.Locale;

public interface CommonI18NService
{
    Locale getLocaleForLanguage(LanguageModel paramLanguageModel);


    Locale getLocaleForIsoCode(String paramString);


    LanguageModel getLanguage(String paramString);


    List<LanguageModel> getAllLanguages();


    LanguageModel getCurrentLanguage();


    void setCurrentLanguage(LanguageModel paramLanguageModel);


    CountryModel getCountry(String paramString);


    List<CountryModel> getAllCountries();


    RegionModel getRegion(CountryModel paramCountryModel, String paramString);


    List<RegionModel> getAllRegions();


    CurrencyModel getCurrency(String paramString);


    void setCurrentCurrency(CurrencyModel paramCurrencyModel);


    CurrencyModel getCurrentCurrency();


    CurrencyModel getBaseCurrency();


    List<CurrencyModel> getAllCurrencies();


    double convertAndRoundCurrency(double paramDouble1, double paramDouble2, int paramInt, double paramDouble3);


    double convertCurrency(double paramDouble1, double paramDouble2, double paramDouble3);


    double roundCurrency(double paramDouble, int paramInt);
}
