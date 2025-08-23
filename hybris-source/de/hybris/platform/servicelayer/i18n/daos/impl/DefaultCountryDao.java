package de.hybris.platform.servicelayer.i18n.daos.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.i18n.daos.CountryDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCountryDao extends DefaultGenericDao<CountryModel> implements CountryDao
{
    public DefaultCountryDao()
    {
        this("Country");
    }


    private DefaultCountryDao(String typecode)
    {
        super(typecode);
    }


    public List<CountryModel> findCountries()
    {
        return find();
    }


    public List<CountryModel> findCountriesByCode(String countryCode)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("isocode", countryCode);
        return find(params);
    }
}
