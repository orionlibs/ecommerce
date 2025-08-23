package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.i18n.daos.RegionDao;
import de.hybris.platform.warehousing.util.builder.RegionModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class Regions extends AbstractItems<RegionModel>
{
    public static final String QUEBEC_NAME = "Quebec";
    public static final String MASSACHUSETTS_NAME = "Massachusetts";
    public static final String QUEBEC_ISOCODE = "CA-QC";
    public static final String MASSACHUSETTS_ISOCODE = "US-MA";
    public static final String QUEBEC_ISOCODE_SHORT = "QC";
    public static final String MASSACHUSETTS_ISOCODE_SHORT = "MA";
    private RegionDao regionDao;
    private Countries countries;


    public RegionModel quebecRegion()
    {
        return (RegionModel)getFromCollectionOrSaveAndReturn(() -> getRegionDao().findRegionsByCountryAndCode(getCountries().Canada(), "CA-QC"), () -> RegionModelBuilder.aModel().withCountry(getCountries().Canada()).withIsocode("CA-QC").withIsocodeShort("QC").withName("Quebec").build());
    }


    public RegionModel massachusettsRegion()
    {
        return (RegionModel)getFromCollectionOrSaveAndReturn(() -> getRegionDao().findRegionsByCountryAndCode(getCountries().UnitedStates(), "US-MA"),
                        () -> RegionModelBuilder.aModel().withCountry(getCountries().UnitedStates()).withIsocode("US-MA").withIsocodeShort("MA").withName("Massachusetts").build());
    }


    protected RegionDao getRegionDao()
    {
        return this.regionDao;
    }


    @Required
    public void setRegionDao(RegionDao regionDao)
    {
        this.regionDao = regionDao;
    }


    protected Countries getCountries()
    {
        return this.countries;
    }


    @Required
    public void setCountries(Countries countries)
    {
        this.countries = countries;
    }
}
