package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.PointOfServiceDao;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.util.builder.PointOfServiceModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class PointsOfService extends AbstractItems<PointOfServiceModel>
{
    public static final String NAME_MONTREAL_DOWNTOWN = "montreal-downtown";
    public static final String NAME_MONTREAL_DOWNTOWN_EXTERNAL = "montreal-downtown-ext";
    public static final String NAME_BOSTON = "boston";
    private PointOfServiceDao pointOfServiceDao;
    private BaseStores baseStores;
    private Warehouses warehouses;
    private Countries countries;
    private Addresses addresses;


    public PointOfServiceModel Montreal_Downtown()
    {
        return (PointOfServiceModel)getOrSaveAndReturn(() -> getPointOfServiceDao().getPosByName("montreal-downtown"),
                        () -> PointOfServiceModelBuilder.aModel().withBaseStore(getBaseStores().NorthAmerica()).withName("montreal-downtown").withType(PointOfServiceTypeEnum.WAREHOUSE).withWarehouses(new WarehouseModel[] {getWarehouses().Montreal(), getWarehouses().Griffintown()})
                                        .withAddress(getAddresses().MontrealDeMaisonneuvePos()).withLatitude(Addresses.LATITUDE_MONTREAL).withLongitude(Addresses.LONGITUDE_MONTREAL).build());
    }


    public PointOfServiceModel Boston()
    {
        return (PointOfServiceModel)getOrSaveAndReturn(() -> getPointOfServiceDao().getPosByName("boston"),
                        () -> PointOfServiceModelBuilder.aModel().withBaseStore(getBaseStores().NorthAmerica()).withName("boston").withType(PointOfServiceTypeEnum.STORE).withWarehouses(new WarehouseModel[] {getWarehouses().Boston()}).withAddress(getAddresses().Boston())
                                        .withLatitude(Addresses.LATITUDE_BOSTON).withLongitude(Addresses.LONGITUDE_BOSTON).build());
    }


    public PointOfServiceModel Montreal_External()
    {
        return (PointOfServiceModel)getOrSaveAndReturn(() -> getPointOfServiceDao().getPosByName("montreal-downtown-ext"),
                        () -> PointOfServiceModelBuilder.aModel().withBaseStore(getBaseStores().NorthAmerica()).withName("montreal-downtown-ext").withType(PointOfServiceTypeEnum.STORE).withWarehouses(new WarehouseModel[] {getWarehouses().Montreal_External()})
                                        .withAddress(getAddresses().MontrealDeMaisonneuvePos()).withLatitude(Addresses.LATITUDE_MONTREAL).withLongitude(Addresses.LONGITUDE_MONTREAL).build());
    }


    public PointOfServiceDao getPointOfServiceDao()
    {
        return this.pointOfServiceDao;
    }


    @Required
    public void setPointOfServiceDao(PointOfServiceDao pointOfServiceDao)
    {
        this.pointOfServiceDao = pointOfServiceDao;
    }


    public BaseStores getBaseStores()
    {
        return this.baseStores;
    }


    @Required
    public void setBaseStores(BaseStores baseStores)
    {
        this.baseStores = baseStores;
    }


    public Warehouses getWarehouses()
    {
        return this.warehouses;
    }


    @Required
    public void setWarehouses(Warehouses warehouses)
    {
        this.warehouses = warehouses;
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


    public Addresses getAddresses()
    {
        return this.addresses;
    }


    @Required
    public void setAddresses(Addresses addresses)
    {
        this.addresses = addresses;
    }
}
