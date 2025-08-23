package de.hybris.platform.servicelayer.i18n.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NDao;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import java.util.HashSet;
import java.util.Set;

@Deprecated(since = "ages", forRemoval = true)
public class DefaultI18NDao extends AbstractItemDao implements I18NDao
{
    protected C2LManager getC2lManager()
    {
        return C2LManager.getInstance();
    }


    public Set<LanguageModel> findAllActiveLanguages()
    {
        Set<LanguageModel> all = new HashSet<>();
        return (Set<LanguageModel>)loadAll(getC2lManager().getActiveLanguages(), all);
    }


    public Set<LanguageModel> findAllLanguages()
    {
        Set<LanguageModel> all = new HashSet<>();
        return (Set<LanguageModel>)loadAll(getC2lManager().getAllLanguages(), all);
    }


    public LanguageModel findLanguage(String isocode)
    {
        LanguageModel result;
        try
        {
            result = (LanguageModel)load(getC2lManager().getLanguageByIsoCode(isocode));
        }
        catch(JaloItemNotFoundException e)
        {
            return null;
        }
        return result;
    }


    public CountryModel findCountry(String iso)
    {
        Country country;
        try
        {
            country = getC2lManager().getCountryByIsoCode(iso);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new UnknownIdentifierException("No country for isocode " + iso + " found", e);
        }
        return (CountryModel)load(country);
    }


    public CurrencyModel findCurrency(String isocode)
    {
        try
        {
            return (CurrencyModel)load(getC2lManager().getCurrencyByIsoCode(isocode));
        }
        catch(JaloItemNotFoundException e)
        {
            throw new UnknownIdentifierException("No currency found for isocode " + isocode, e);
        }
    }


    public Set<CurrencyModel> findAllCurrencies()
    {
        return (Set<CurrencyModel>)loadAll(getC2lManager().getAllCurrencies(), new HashSet());
    }


    public Set<CountryModel> findAllCountries()
    {
        return (Set<CountryModel>)loadAll(getC2lManager().getAllCountries(), new HashSet());
    }


    public Set<RegionModel> findAllRegions()
    {
        return (Set<RegionModel>)loadAll(getC2lManager().getAllRegions(), new HashSet());
    }


    public CurrencyModel findBaseCurrency()
    {
        return (CurrencyModel)load(getC2lManager().getBaseCurrency());
    }


    public RegionModel findRegion(String code)
    {
        try
        {
            return (RegionModel)load(getC2lManager().getRegionByCode(code));
        }
        catch(JaloItemNotFoundException e)
        {
            throw new UnknownIdentifierException("No region found for isocode " + code, e);
        }
    }
}
