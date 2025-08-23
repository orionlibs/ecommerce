package de.hybris.platform.servicelayer.i18n;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class I18NDaoMock implements I18NDao
{
    private final Map<String, LanguageModel> languages = new ConcurrentHashMap<>();


    public I18NDaoMock()
    {
        LanguageModel model = new LanguageModel();
        model.setIsocode("en");
        model.setActive(Boolean.TRUE);
        this.languages.put(model.getIsocode(), model);
    }


    public LanguageModel findLanguage(String isocode)
    {
        LanguageModel model = this.languages.get(isocode);
        return model;
    }


    public Set<LanguageModel> findAllActiveLanguages()
    {
        Set<LanguageModel> models = new HashSet<>();
        for(LanguageModel model : new HashSet(this.languages.values()))
        {
            if(Boolean.TRUE.equals(model.getActive()))
            {
                models.add(model);
            }
        }
        return Collections.unmodifiableSet(models);
    }


    public Set<LanguageModel> findAllLanguages()
    {
        return Collections.unmodifiableSet(new HashSet<>(this.languages.values()));
    }


    public Set<CountryModel> findAllCountries()
    {
        throw new UnsupportedOperationException();
    }


    public Set<CurrencyModel> findAllCurrencies()
    {
        throw new UnsupportedOperationException();
    }


    public Set<RegionModel> findAllRegions()
    {
        throw new UnsupportedOperationException();
    }


    public CurrencyModel findBaseCurrency()
    {
        throw new UnsupportedOperationException();
    }


    public CountryModel findCountry(String isocode)
    {
        throw new UnsupportedOperationException();
    }


    public CurrencyModel findCurrency(String isocode)
    {
        throw new UnsupportedOperationException();
    }


    public RegionModel findRegion(String code)
    {
        throw new UnsupportedOperationException();
    }
}
