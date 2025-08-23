package de.hybris.platform.servicelayer.i18n.daos;

import de.hybris.platform.core.model.c2l.CountryModel;
import java.util.List;

public interface CountryDao
{
    List<CountryModel> findCountries();


    List<CountryModel> findCountriesByCode(String paramString);
}
