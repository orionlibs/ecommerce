package de.hybris.platform.servicelayer.i18n.daos.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.i18n.daos.RegionDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultRegionDao extends DefaultGenericDao<RegionModel> implements RegionDao
{
    public DefaultRegionDao()
    {
        this("Region");
    }


    private DefaultRegionDao(String typecode)
    {
        super(typecode);
    }


    public List<RegionModel> findRegions()
    {
        return find();
    }


    public List<RegionModel> findRegionsByCountry(CountryModel country)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("country", country);
        return find(params);
    }


    public List<RegionModel> findRegionsByCountryAndCode(CountryModel country, String regionCode)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("country", country);
        params.put("isocode", regionCode);
        return find(params);
    }
}
