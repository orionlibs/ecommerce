package de.hybris.platform.servicelayer.i18n.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.ConversionStrategy;
import de.hybris.platform.servicelayer.i18n.daos.CountryDao;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.i18n.daos.LanguageDao;
import de.hybris.platform.servicelayer.i18n.daos.RegionDao;
import de.hybris.platform.servicelayer.internal.i18n.I18NConstants;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Utilities;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCommonI18NService extends AbstractBusinessService implements CommonI18NService
{
    private final ConcurrentMap<String, Locale> localeCache = new ConcurrentHashMap<>();
    private LanguageDao languageDao;
    private CurrencyDao currencyDao;
    private CountryDao countryDao;
    private RegionDao regionDao;
    private ConversionStrategy conversionStrategy;


    @Required
    public void setConversionStrategy(ConversionStrategy conversionStrategy)
    {
        this.conversionStrategy = conversionStrategy;
    }


    public Locale getLocaleForLanguage(LanguageModel languageModel)
    {
        String isoCode = languageModel.getIsocode();
        return getLocaleForIsoCode(isoCode);
    }


    public Locale getLocaleForIsoCode(String isoCode)
    {
        Locale ret = this.localeCache.get(isoCode);
        if(ret == null)
        {
            String[] loc = Utilities.parseLocaleCodes(isoCode);
            ret = new Locale(loc[0], loc[1], loc[2]);
            Locale previous = this.localeCache.putIfAbsent(isoCode, ret);
            if(previous != null)
            {
                ret = previous;
            }
        }
        return ret;
    }


    public List<LanguageModel> getAllLanguages()
    {
        return this.languageDao.findLanguages();
    }


    public LanguageModel getLanguage(String isocode)
    {
        List<LanguageModel> result = this.languageDao.findLanguagesByCode(isocode);
        ServicesUtil.validateIfSingleResult(result, LanguageModel.class, "isocode", isocode);
        return result.get(0);
    }


    public void setCurrentLanguage(LanguageModel language)
    {
        getSessionService().setAttribute(I18NConstants.LANGUAGE_SESSION_ATTR_KEY, language);
    }


    public LanguageModel getCurrentLanguage()
    {
        return (LanguageModel)getSessionService().getAttribute(I18NConstants.LANGUAGE_SESSION_ATTR_KEY);
    }


    public List<CountryModel> getAllCountries()
    {
        return this.countryDao.findCountries();
    }


    public CountryModel getCountry(String isocode)
    {
        List<CountryModel> result = this.countryDao.findCountriesByCode(isocode);
        ServicesUtil.validateIfSingleResult(result, CountryModel.class, "isocode", isocode);
        return result.get(0);
    }


    public void setCurrentCurrency(CurrencyModel currency)
    {
        getSessionService().setAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY, currency);
    }


    public CurrencyModel getCurrentCurrency()
    {
        return (CurrencyModel)getSessionService().getAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY);
    }


    public List<CurrencyModel> getAllCurrencies()
    {
        return this.currencyDao.findCurrencies();
    }


    public CurrencyModel getBaseCurrency()
    {
        List<CurrencyModel> result = this.currencyDao.findBaseCurrencies();
        validateBaseCurrency(result);
        return result.get(0);
    }


    public CurrencyModel getCurrency(String isoCode)
    {
        List<CurrencyModel> result = this.currencyDao.findCurrenciesByCode(isoCode);
        ServicesUtil.validateIfSingleResult(result, CurrencyModel.class, "isocode", isoCode);
        return result.get(0);
    }


    public List<RegionModel> getAllRegions()
    {
        return this.regionDao.findRegions();
    }


    public RegionModel getRegion(CountryModel country, String code)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("country", country);
        List<RegionModel> result = this.regionDao.findRegionsByCountryAndCode(country, code);
        ServicesUtil.validateIfSingleResult(result, RegionModel.class, "country, isocode", country
                        .getIsocode() + ", " + country.getIsocode());
        return result.get(0);
    }


    private void validateBaseCurrency(List<CurrencyModel> currenciesList)
    {
        if(currenciesList.isEmpty())
        {
            throw new SystemException("There must be at least one base currency!");
        }
    }


    public double convertAndRoundCurrency(double sourceFactor, double targetFactor, int digits, double value)
    {
        return this.conversionStrategy.round(this.conversionStrategy.convert(value, sourceFactor, targetFactor), digits);
    }


    public double convertCurrency(double sourceFactor, double targetFactor, double value)
    {
        return this.conversionStrategy.convert(value, sourceFactor, targetFactor);
    }


    public double roundCurrency(double value, int digits)
    {
        return this.conversionStrategy.round(value, digits);
    }


    @Required
    public void setLanguageDao(LanguageDao dao)
    {
        this.languageDao = dao;
    }


    @Required
    public void setCountryDao(CountryDao countryDao)
    {
        this.countryDao = countryDao;
    }


    @Required
    public void setCurrencyDao(CurrencyDao currencyDao)
    {
        this.currencyDao = currencyDao;
    }


    @Required
    public void setRegionDao(RegionDao regionDao)
    {
        this.regionDao = regionDao;
    }
}
