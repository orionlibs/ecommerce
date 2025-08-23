package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.deliveryzone.model.ZoneModel;
import de.hybris.platform.warehousing.util.builder.ZoneModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import org.springframework.beans.factory.annotation.Required;

public class Zones extends AbstractItems<ZoneModel>
{
    public static final String CODE_UNITED_STATES = "usa";
    private Countries countries;
    private WarehousingDao<ZoneModel> zoneDao;


    public ZoneModel UnitedStates()
    {
        return (ZoneModel)getOrSaveAndReturn(() -> (ZoneModel)getZoneDao().getByCode("usa"), () -> ZoneModelBuilder.aModel().withCode("usa").withCountries(new CountryModel[] {getCountries().UnitedStates()}).build());
    }


    public Countries getCountries()
    {
        return this.countries;
    }


    @Required
    public void setCountries(Countries countries)
    {
        this.countries = countries;
    }


    public WarehousingDao<ZoneModel> getZoneDao()
    {
        return this.zoneDao;
    }


    @Required
    public void setZoneDao(WarehousingDao<ZoneModel> zoneDao)
    {
        this.zoneDao = zoneDao;
    }
}
