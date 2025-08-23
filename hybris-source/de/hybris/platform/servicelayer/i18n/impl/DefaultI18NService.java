package de.hybris.platform.servicelayer.i18n.impl;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.exceptions.ConfigurationException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NDao;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.internal.i18n.I18NConstants;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Currency;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultI18NService extends AbstractBusinessService implements I18NService
{
    private static final Logger LOG = Logger.getLogger(DefaultI18NService.class);
    private LocalizationService localeService;
    private I18NDao i18nDao;
    private CurrencyDao currencyDao;
    private TypeService _typeService;


    public TypeService getTypeService()
    {
        if(this._typeService == null)
        {
            this._typeService = lookupTypeService();
        }
        return this._typeService;
    }


    public TypeService lookupTypeService()
    {
        throw new UnsupportedOperationException("please override DefaultI18NService.lookupTypeService() or use <lookup-method>");
    }


    public Currency getCurrentJavaCurrency()
    {
        Currency result;
        CurrencyModel currentCurrencyModel = (CurrencyModel)getSessionService().getAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY);
        try
        {
            result = Currency.getInstance(currentCurrencyModel.getIsocode());
        }
        catch(IllegalArgumentException iae)
        {
            throw new ConfigurationException("Error during getting current java currency. The model currency with iso code: " + currentCurrencyModel
                            .getIsocode() + "  doesn't exist within ISO 4217 Currency Code List.", iae);
        }
        return result;
    }


    public void setCurrentJavaCurrency(Currency currency)
    {
        try
        {
            List<CurrencyModel> currencies = this.currencyDao.findCurrenciesByCode(currency.getCurrencyCode());
            ServicesUtil.validateIfSingleResult(currencies, "Can not find currency with code " + currency
                            .getCurrencyCode() + " in system", "More then one currencies with code " + currency
                            .getCurrencyCode() + " found");
            getSessionService().setAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY, currencies.get(0));
        }
        catch(UnknownIdentifierException exception)
        {
            throw new ConfigurationException("Error during insertion of current java currency: " + currency.getCurrencyCode() + " to the system. Given currency does not exist in the type system.", exception);
        }
    }


    public final Locale getCurrentLocale()
    {
        return this.localeService.getCurrentLocale();
    }


    public final void setCurrentLocale(Locale locale)
    {
        this.localeService.setCurrentLocale(locale);
    }


    public void setCurrentTimeZone(TimeZone zone)
    {
        getSessionService().setAttribute(I18NConstants.TIMEZONE_SESSION_ATTR_KEY, zone);
    }


    public TimeZone getCurrentTimeZone()
    {
        return (TimeZone)getSessionService().getAttribute(I18NConstants.TIMEZONE_SESSION_ATTR_KEY);
    }


    public boolean isLocalizationFallbackEnabled()
    {
        return (Boolean.TRUE.equals(getSessionService().getAttribute("enable.language.fallback.serviceLayer")) && Boolean.TRUE
                        .equals(getSessionService().getAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED)));
    }


    public void setLocalizationFallbackEnabled(boolean enabled)
    {
        getSessionService().setAttribute("enable.language.fallback.serviceLayer", Boolean.valueOf(enabled));
        getSessionService().setAttribute(I18NConstants.LANGUAGE_FALLBACK_ENABLED, Boolean.valueOf(enabled));
    }


    public Currency getBestMatchingJavaCurrency(String isocode)
    {
        return Currency.getInstance(isocode);
    }


    public Locale[] getAllLocales(Locale locale)
    {
        return this.localeService.getAllLocales(locale);
    }


    public Locale getBestMatchingLocale(Locale locale)
    {
        return this.localeService.getDataLocale(locale);
    }


    public Locale[] getFallbackLocales(Locale locale)
    {
        return this.localeService.getFallbackLocales(locale);
    }


    public Set<Currency> getSupportedJavaCurrencies()
    {
        Set<Currency> result = new LinkedHashSet<>();
        List<CurrencyModel> allCurrencies = this.currencyDao.findCurrencies();
        for(CurrencyModel currencyModel : allCurrencies)
        {
            String currencyIsocode = currencyModel.getIsocode();
            try
            {
                Currency currency = Currency.getInstance(currencyIsocode);
                result.add(currency);
            }
            catch(Exception e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Asked for unsupported currency " + currencyIsocode);
                }
            }
        }
        return result;
    }


    public Set<Locale> getSupportedLocales()
    {
        return this.localeService.getSupportedDataLocales();
    }


    public void setCurrencyDao(CurrencyDao currencyDao)
    {
        this.currencyDao = currencyDao;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public LocalizationService getLocaleService()
    {
        return this.localeService;
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Required
    public void setLocaleService(LocalizationService localeService)
    {
        this.localeService = localeService;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public I18NDao getI18nDao()
    {
        return this.i18nDao;
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Required
    public void setI18nDao(I18NDao dao)
    {
        this.i18nDao = dao;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public CurrencyModel getCurrentCurrency()
    {
        return (CurrencyModel)getSessionService().getAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setCurrentCurrency(CurrencyModel currency)
    {
        getSessionService().setAttribute(I18NConstants.CURRENCY_SESSION_ATTR_KEY, currency);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public LanguageModel getLanguage(String isocode)
    {
        LanguageModel model = this.i18nDao.findLanguage(isocode);
        if(model == null)
        {
            throw new UnknownIdentifierException("Cannot find language with isocode '" + isocode + "'");
        }
        return model;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Set<LanguageModel> getAllActiveLanguages()
    {
        return getI18nDao().findAllActiveLanguages();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Set<LanguageModel> getAllLanguages()
    {
        return getI18nDao().findAllLanguages();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public CountryModel getCountry(String isocode)
    {
        return getI18nDao().findCountry(isocode);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Set<CountryModel> getAllCountries()
    {
        return getI18nDao().findAllCountries();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Set<CurrencyModel> getAllCurrencies()
    {
        return getI18nDao().findAllCurrencies();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public CurrencyModel getBaseCurrency()
    {
        return getI18nDao().findBaseCurrency();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public CurrencyModel getCurrency(String isoCode)
    {
        return getI18nDao().findCurrency(isoCode);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Set<Locale> getSupportedDataLocales()
    {
        return getLocaleService().getSupportedDataLocales();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public ResourceBundle getBundle(String baseName)
    {
        return getBundle(baseName, getLocaleService().getAllLocales(getLocaleService().getCurrentLocale()));
    }


    @Deprecated(since = "ages", forRemoval = true)
    public ResourceBundle getBundle(String baseName, Locale[] locales)
    {
        return getBundle(baseName, locales, null);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public ResourceBundle getBundle(String baseName, Locale[] locales, ClassLoader loader)
    {
        if(locales == null || locales.length == 0)
        {
            throw new IllegalArgumentException("At least one Locale must be given (was null or empty)");
        }
        ResourceBundle result = null;
        if(locales.length == 1)
        {
            result = (loader == null) ? ResourceBundle.getBundle(baseName, locales[0]) : ResourceBundle.getBundle(baseName, locales[0], loader);
        }
        else
        {
            result = CompositeResourceBundle.getBundle(baseName, locales, loader);
        }
        return result;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public String getEnumLocalizedName(HybrisEnumValue enumValue)
    {
        EnumerationValueModel evm = getTypeService().getEnumerationValue(enumValue);
        if(evm == null)
        {
            return null;
        }
        getModelService().refresh(evm);
        return evm.getName();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setEnumLocalizedName(HybrisEnumValue enumValue, String name)
    {
        EnumerationValueModel evm = getTypeService().getEnumerationValue(enumValue);
        if(evm != null)
        {
            evm.setName(name);
            getModelService().save(evm);
        }
        else
        {
            logDebugMessage("The EnumerationValueModel couldn't be found. the name=" + name + " will not be set for given Enumeration Object");
        }
    }


    public PK getLangPKFromLocale(Locale locale)
    {
        return this.localeService.getMatchingPkForDataLocale(locale);
    }


    private void logDebugMessage(String message)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(message);
        }
    }
}
