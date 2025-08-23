package de.hybris.platform.warehousing.util;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderBuilder
{
    public static final Double DEFAULT_DELIVER_LATITUDE = Double.valueOf(35.6673D);
    public static final Double DEFAULT_DELIVER_LONGITDUE = Double.valueOf(139.75429D);
    public static final String DEFAULT_COUNTRY_CODE = "US";
    private Double deliveryLatitude = DEFAULT_DELIVER_LATITUDE;
    private Double deliveryLongitude = DEFAULT_DELIVER_LONGITDUE;
    private final String countryCode = "US";
    private DeliveryModeModel deliveryModeModel;
    private PointOfServiceModel pointOfServiceModel;
    private BaseStoreModel baseStore;


    public static OrderBuilder aSourcingOrder()
    {
        return new OrderBuilder();
    }


    public OrderModel build(AddressModel address, CurrencyModel currency, ProductService productService, Map<String, Long> productInfo)
    {
        address.setLatitude(DEFAULT_DELIVER_LATITUDE);
        address.setLongitude(DEFAULT_DELIVER_LONGITDUE);
        OrderModel order = new OrderModel();
        order.setDeliveryMode(this.deliveryModeModel);
        order.setStore(this.baseStore);
        order.setDeliveryAddress(address);
        order.setCurrency(currency);
        order.setDate(new Date());
        order.setUser(setDefaultUser());
        List<AbstractOrderEntryModel> entries = new ArrayList<>();
        productInfo.entrySet().stream().forEach(product -> {
            OrderEntryModel entry = new OrderEntryModel();
            entry.setQuantity((Long)product.getValue());
            entry.setProduct(productService.getProductForCode((String)product.getKey()));
            entries.add(entry);
            entry.setOrder((AbstractOrderModel)order);
            UnitModel unit = new UnitModel();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            unit.setUnitType("piece");
            unit.setCode(uuid);
            entry.setUnit(unit);
        });
        order.setEntries(entries);
        return order;
    }


    public OrderModel build(AddressModel address, CurrencyModel currency, ProductService productService, Map<String, Long> productInfo, PointOfServiceModel pointOfServiceModel, DeliveryModeModel deliveryModeModel)
    {
        OrderModel order = new OrderModel();
        address.setLatitude(DEFAULT_DELIVER_LATITUDE);
        address.setLongitude(DEFAULT_DELIVER_LONGITDUE);
        order.setDeliveryAddress(address);
        order.setDeliveryMode(deliveryModeModel);
        order.setCurrency(currency);
        order.setDate(new Date());
        order.setUser(setDefaultUser());
        List<AbstractOrderEntryModel> entries = new ArrayList<>();
        productInfo.entrySet().stream().forEach(product -> {
            UnitModel unit = new UnitModel();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            unit.setUnitType("piece");
            unit.setCode(uuid);
            OrderEntryModel entry = new OrderEntryModel();
            entry.setQuantity((Long)product.getValue());
            entry.setProduct(productService.getProductForCode((String)product.getKey()));
            entry.setOrder((AbstractOrderModel)order);
            entry.setDeliveryPointOfService(pointOfServiceModel);
            entry.setUnit(unit);
            entries.add(entry);
        });
        order.setEntries(entries);
        return order;
    }


    public OrderBuilder withDeliveryGeoCode(double deliveryLatitude, double deliveryLongitude)
    {
        this.deliveryLatitude = Double.valueOf(deliveryLatitude);
        this.deliveryLongitude = Double.valueOf(deliveryLongitude);
        return this;
    }


    public UserModel setDefaultUser()
    {
        UserModel user = new UserModel();
        user.setUid("2002928137");
        return user;
    }


    public OrderBuilder withBaseStore(BaseStoreModel baseStore)
    {
        this.baseStore = baseStore;
        return this;
    }
}
