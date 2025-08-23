package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.ordersplitting.daos.WarehouseDao;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.util.builder.WarehouseModelBuilder;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Required;

public class Warehouses extends AbstractItems<WarehouseModel>
{
    public static final String CODE_MONTREAL = "montreal";
    public static final String CODE_BOSTON = "boston";
    public static final String CODE_TORONTO = "toronto";
    public static final String CODE_GRIFFINTOWN = "griffintown";
    public static final String CODE_PARIS = "paris";
    public static final String CODE_MONTREAL_EXTERNAL = "montrealExternal";
    private WarehouseDao warehouseDao;
    private BaseStores baseStores;
    private Vendors vendors;
    private DeliveryModes deliveryModes;


    public WarehouseModel Montreal()
    {
        return (WarehouseModel)getFromCollectionOrSaveAndReturn(() -> getWarehouseDao().getWarehouseForCode("montreal"),
                        () -> WarehouseModelBuilder.fromModel(Default()).withCode("montreal").withName("montreal", Locale.ENGLISH).withDeliveryModes(new DeliveryModeModel[] {getDeliveryModes().Pickup(), getDeliveryModes().Regular()}).build());
    }


    public WarehouseModel Toronto()
    {
        return (WarehouseModel)getFromCollectionOrSaveAndReturn(() -> getWarehouseDao().getWarehouseForCode("toronto"),
                        () -> WarehouseModelBuilder.fromModel(Default()).withCode("toronto").withName("toronto", Locale.ENGLISH).withDeliveryModes(new DeliveryModeModel[] {getDeliveryModes().Pickup(), getDeliveryModes().Regular()}).build());
    }


    public WarehouseModel Boston()
    {
        return (WarehouseModel)getFromCollectionOrSaveAndReturn(() -> getWarehouseDao().getWarehouseForCode("boston"),
                        () -> WarehouseModelBuilder.fromModel(Default()).withCode("boston").withName("boston", Locale.ENGLISH).withDeliveryModes(new DeliveryModeModel[] {getDeliveryModes().Pickup(), getDeliveryModes().Regular()}).build());
    }


    public WarehouseModel Paris()
    {
        return (WarehouseModel)getFromCollectionOrSaveAndReturn(() -> getWarehouseDao().getWarehouseForCode("paris"),
                        () -> WarehouseModelBuilder.fromModel(Default()).withCode("paris").withName("paris", Locale.ENGLISH).withDeliveryModes(new DeliveryModeModel[] {getDeliveryModes().Pickup(), getDeliveryModes().Regular()}).withExternal(Boolean.valueOf(true)).build());
    }


    public WarehouseModel Griffintown()
    {
        return (WarehouseModel)getFromCollectionOrSaveAndReturn(() -> getWarehouseDao().getWarehouseForCode("griffintown"),
                        () -> WarehouseModelBuilder.fromModel(Default()).withCode("griffintown").withName("griffintown", Locale.ENGLISH).withDeliveryModes(new DeliveryModeModel[] {getDeliveryModes().Pickup()}).build());
    }


    public WarehouseModel Montreal_External()
    {
        return (WarehouseModel)getFromCollectionOrSaveAndReturn(() -> getWarehouseDao().getWarehouseForCode("montrealExternal"),
                        () -> WarehouseModelBuilder.fromModel(Default()).withCode("montrealExternal").withName("montrealExternal", Locale.ENGLISH).withDeliveryModes(new DeliveryModeModel[] {getDeliveryModes().Pickup(), getDeliveryModes().Regular()}).withExternal(Boolean.valueOf(true)).build());
    }


    public WarehouseModel Random()
    {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return (WarehouseModel)getFromCollectionOrSaveAndReturn(() -> getWarehouseDao().getWarehouseForCode(uuid),
                        () -> WarehouseModelBuilder.fromModel(Default()).withCode(uuid).withName(uuid, Locale.ENGLISH).withDeliveryModes(new DeliveryModeModel[] {getDeliveryModes().Pickup(), getDeliveryModes().Regular()}).build());
    }


    protected WarehouseModel Default()
    {
        return WarehouseModelBuilder.aModel()
                        .withBaseStores(new BaseStoreModel[] {getBaseStores().NorthAmerica()}).withDefault(Boolean.TRUE)
                        .withIsAllowRestock(Boolean.valueOf(false))
                        .withVendor(getVendors().Hybris())
                        .build();
    }


    public WarehouseDao getWarehouseDao()
    {
        return this.warehouseDao;
    }


    @Required
    public void setWarehouseDao(WarehouseDao warehouseDao)
    {
        this.warehouseDao = warehouseDao;
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


    public Vendors getVendors()
    {
        return this.vendors;
    }


    @Required
    public void setVendors(Vendors vendors)
    {
        this.vendors = vendors;
    }


    public DeliveryModes getDeliveryModes()
    {
        return this.deliveryModes;
    }


    @Required
    public void setDeliveryModes(DeliveryModes deliveryModes)
    {
        this.deliveryModes = deliveryModes;
    }
}
