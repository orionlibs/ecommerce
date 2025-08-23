package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Lists;
import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

public class PointOfServiceModelBuilder
{
    private final PointOfServiceModel model = new PointOfServiceModel();


    private PointOfServiceModel getModel()
    {
        return this.model;
    }


    public static PointOfServiceModelBuilder aModel()
    {
        return new PointOfServiceModelBuilder();
    }


    public PointOfServiceModel build()
    {
        return getModel();
    }


    public PointOfServiceModelBuilder withName(String name)
    {
        getModel().setName(name);
        return this;
    }


    public PointOfServiceModelBuilder withType(PointOfServiceTypeEnum type)
    {
        getModel().setType(type);
        return this;
    }


    public PointOfServiceModelBuilder withBaseStore(BaseStoreModel baseStore)
    {
        getModel().setBaseStore(baseStore);
        return this;
    }


    public PointOfServiceModelBuilder withWarehouses(WarehouseModel... warehouses)
    {
        getModel().setWarehouses(Lists.newArrayList((Object[])warehouses));
        return this;
    }


    public PointOfServiceModelBuilder withAddress(AddressModel address)
    {
        getModel().setAddress(address);
        return this;
    }


    public PointOfServiceModelBuilder withLatitude(Double latitude)
    {
        getModel().setLatitude(latitude);
        return this;
    }


    public PointOfServiceModelBuilder withLongitude(Double longitude)
    {
        getModel().setLongitude(longitude);
        return this;
    }
}
