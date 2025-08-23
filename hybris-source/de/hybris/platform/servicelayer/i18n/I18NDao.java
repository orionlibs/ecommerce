package de.hybris.platform.servicelayer.i18n;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Set;

@Deprecated(since = "ages", forRemoval = true)
public interface I18NDao extends Dao
{
    @Deprecated(since = "ages", forRemoval = true)
    LanguageModel findLanguage(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    Set<LanguageModel> findAllLanguages();


    @Deprecated(since = "ages", forRemoval = true)
    Set<LanguageModel> findAllActiveLanguages();


    @Deprecated(since = "ages", forRemoval = true)
    CurrencyModel findCurrency(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    CurrencyModel findBaseCurrency();


    @Deprecated(since = "ages", forRemoval = true)
    Set<CurrencyModel> findAllCurrencies();


    @Deprecated(since = "ages", forRemoval = true)
    CountryModel findCountry(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    Set<CountryModel> findAllCountries();


    @Deprecated(since = "ages", forRemoval = true)
    RegionModel findRegion(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    Set<RegionModel> findAllRegions();
}
