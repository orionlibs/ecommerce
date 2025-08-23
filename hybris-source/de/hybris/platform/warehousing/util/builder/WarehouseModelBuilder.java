package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Sets;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Arrays;
import java.util.Locale;

public class WarehouseModelBuilder
{
    private final WarehouseModel model;


    private WarehouseModelBuilder()
    {
        this.model = new WarehouseModel();
    }


    private WarehouseModelBuilder(WarehouseModel model)
    {
        this.model = model;
    }


    private WarehouseModel getModel()
    {
        return this.model;
    }


    public static WarehouseModelBuilder aModel()
    {
        return new WarehouseModelBuilder();
    }


    public static WarehouseModelBuilder fromModel(WarehouseModel model)
    {
        return new WarehouseModelBuilder(model);
    }


    public WarehouseModel build()
    {
        return getModel();
    }


    public WarehouseModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public WarehouseModelBuilder withIsAllowRestock(Boolean isAllowRestock)
    {
        getModel().setIsAllowRestock(isAllowRestock);
        return this;
    }


    public WarehouseModelBuilder withName(String name, Locale locale)
    {
        getModel().setName(name, locale);
        return this;
    }


    public WarehouseModelBuilder withVendor(VendorModel vendor)
    {
        getModel().setVendor(vendor);
        return this;
    }


    public WarehouseModelBuilder withDefault(Boolean _default)
    {
        getModel().setDefault(_default);
        return this;
    }


    public WarehouseModelBuilder withBaseStores(BaseStoreModel... baseStores)
    {
        getModel().setBaseStores(Arrays.asList(baseStores));
        return this;
    }


    public WarehouseModelBuilder withDeliveryModes(DeliveryModeModel... deliveryModes)
    {
        getModel().setDeliveryModes(Sets.newHashSet((Object[])deliveryModes));
        return this;
    }


    public WarehouseModelBuilder withExternal(Boolean isExternal)
    {
        getModel().setExternal(isExternal.booleanValue());
        return this;
    }
}
