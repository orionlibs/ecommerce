package de.hybris.platform.servicelayer.i18n.daos;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import java.util.List;

public interface RegionDao
{
    List<RegionModel> findRegions();


    List<RegionModel> findRegionsByCountry(CountryModel paramCountryModel);


    List<RegionModel> findRegionsByCountryAndCode(CountryModel paramCountryModel, String paramString);
}
