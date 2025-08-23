package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.model.PickUpDeliveryModeModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.util.TaxValue;
import de.hybris.platform.warehousing.util.builder.OrderEntryModelBuilder;
import de.hybris.platform.warehousing.util.builder.OrderModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class Orders extends AbstractItems<OrderModel>
{
    public static final String CODE_CAMERA_SHIPPED = "camera-shipped";
    public static final String CODE_MEMORYCARD_SHIPPED = "memorycard-shipped";
    public static final String CODE_CAMERA_PICKUP_MONTREAL = "camera-pickup-montreal";
    public static final String CODE_CAMERA_AND_MEMORY_CARD_PICKUP_MONTREAL = "camera-and-memory-card-pickup-montreal";
    public static final String CODE_CAMERA_AND_MEMORY_CARD_SHIPPED = "camera-and-memory-card-shipped";
    public static final String CODE_PICKUP = "pickupTest";
    public static final String CODE_CAMERA_AND_MEMORY_CARD_AND_LENS_SHIPPED = "camera-and-memory-card-and-lens-shipped";
    public static final String CODE_TAX = "camera-tax";
    public static final Double TAX_VALUE = Double.valueOf(10.0D);
    private static final String PAYMENT_CODE = "CODE_PAYMENT";
    private WarehousingDao<OrderModel> warehousingOrderDao;
    private DeliveryModes deliveryModes;
    private Currencies currencies;
    private BaseStores baseStores;
    private Products products;
    private Units units;
    private Users users;
    private Customers customers;
    private PointsOfService pointsOfService;
    private Addresses addresses;
    private PaymentInfos paymentInfos;
    private BaseSites baseSites;
    private CmsSites cmsSites;
    private PaymentTransactions paymentTransactions;
    private PriceRows priceRows;


    public OrderModel Camera_Shipped(Long quantity)
    {
        return (OrderModel)getOrSaveAndReturn(() -> (OrderModel)getWarehousingOrderDao().getByCode("camera-shipped"), () -> {
            OrderEntryModel orderEntryModel = Camera(quantity);
            OrderModel order = OrderModelBuilder.aModel().withCode("camera-shipped").withCurrency(getCurrencies().AmericanDollar()).withStore(getBaseStores().NorthAmerica()).withDate(new Date()).withUser(getCustomers().polo())
                            .withEntries(new AbstractOrderEntryModel[] {(AbstractOrderEntryModel)orderEntryModel}).withDeliveryAddress(getAddresses().MontrealNancyHome()).withBaseSite((BaseSiteModel)getCmsSites().Canada()).withPaymentInfo(getPaymentInfos().PaymentInfoForNancy("CODE_PAYMENT"))
                            .withPaymentTransactions(Collections.singletonList(getPaymentTransactions().CreditCardTransaction())).build();
            orderEntryModel.setOrder((AbstractOrderModel)order);
            return order;
        });
    }


    public OrderModel MemoryCard_Shipped(Long quantity)
    {
        return (OrderModel)getOrSaveAndReturn(() -> (OrderModel)getWarehousingOrderDao().getByCode("memorycard-shipped"), () -> {
            OrderEntryModel orderEntryModel = MemoryCard(quantity);
            OrderModel order = OrderModelBuilder.aModel().withCode("memorycard-shipped").withCurrency(getCurrencies().AmericanDollar()).withStore(getBaseStores().NorthAmerica()).withDate(new Date()).withUser(getCustomers().polo())
                            .withEntries(new AbstractOrderEntryModel[] {(AbstractOrderEntryModel)orderEntryModel}).withDeliveryAddress(getAddresses().MontrealNancyHome()).withBaseSite((BaseSiteModel)getCmsSites().Canada()).withPaymentInfo(getPaymentInfos().PaymentInfoForNancy("CODE_PAYMENT"))
                            .build();
            orderEntryModel.setOrder((AbstractOrderModel)order);
            return order;
        });
    }


    public OrderModel Camera_PickupInMontreal(Long quantity)
    {
        return (OrderModel)getOrSaveAndReturn(() -> (OrderModel)getWarehousingOrderDao().getByCode("camera-pickup-montreal"), () -> {
            PickUpDeliveryModeModel pickUpDeliveryMode = new PickUpDeliveryModeModel();
            pickUpDeliveryMode.setCode("pickupTest");
            OrderEntryModel orderEntryModel = Camera_PickupMontreal(quantity);
            OrderModel order = OrderModelBuilder.aModel().withCode("camera-pickup-montreal").withCurrency(getCurrencies().AmericanDollar()).withStore(getBaseStores().NorthAmerica()).withDate(new Date()).withUser(getCustomers().polo())
                            .withEntries(new AbstractOrderEntryModel[] {(AbstractOrderEntryModel)orderEntryModel}).withDeliveryAddress(getAddresses().MontrealDeMaisonneuvePos()).withBaseSite((BaseSiteModel)getCmsSites().Canada()).withPaymentInfo(getPaymentInfos().PaymentInfoForNancy("CODE_PAYMENT"))
                            .build();
            order.setDeliveryMode((DeliveryModeModel)pickUpDeliveryMode);
            orderEntryModel.setOrder((AbstractOrderModel)order);
            return order;
        });
    }


    protected OrderEntryModel MemoryCard_PickupMontreal(Long quantity)
    {
        return OrderEntryModelBuilder.fromModel(MemoryCard(quantity))
                        .withDeliveryPointOfService(getPointsOfService().Montreal_Downtown())
                        .build();
    }


    public OrderModel CameraAndMemoryCard_PickupInMontreal(Long cameraQty, Long memoryCardQty)
    {
        return (OrderModel)getOrSaveAndReturn(() -> (OrderModel)getWarehousingOrderDao().getByCode("camera-and-memory-card-pickup-montreal"), () -> {
            PickUpDeliveryModeModel pickUpDeliveryMode = new PickUpDeliveryModeModel();
            pickUpDeliveryMode.setCode("pickupTest");
            OrderEntryModel orderEntryModel1 = Camera_PickupMontreal(cameraQty);
            OrderEntryModel orderEntryModel2 = MemoryCard_PickupMontreal(memoryCardQty);
            OrderModel order = OrderModelBuilder.aModel().withCode("camera-and-memory-card-pickup-montreal").withCurrency(getCurrencies().AmericanDollar()).withStore(getBaseStores().NorthAmerica()).withDate(new Date()).withUser(getCustomers().polo())
                            .withBaseSite((BaseSiteModel)getCmsSites().Canada()).withEntries(new AbstractOrderEntryModel[] {(AbstractOrderEntryModel)orderEntryModel1, (AbstractOrderEntryModel)orderEntryModel2}).build();
            order.setDeliveryMode((DeliveryModeModel)pickUpDeliveryMode);
            orderEntryModel1.setOrder((AbstractOrderModel)order);
            orderEntryModel2.setOrder((AbstractOrderModel)order);
            return order;
        });
    }


    public OrderModel CameraAndMemoryCard_Shipped(Long cameraQty, Long memoryCardQty)
    {
        return (OrderModel)getOrSaveAndReturn(() -> (OrderModel)getWarehousingOrderDao().getByCode("camera-and-memory-card-shipped"), () -> {
            OrderEntryModel orderEntryModel1 = Camera(cameraQty);
            OrderEntryModel orderEntryModel2 = MemoryCard(memoryCardQty);
            OrderModel order = OrderModelBuilder.aModel().withCode("camera-and-memory-card-shipped").withCurrency(getCurrencies().AmericanDollar()).withStore(getBaseStores().NorthAmerica()).withDate(new Date()).withUser(getCustomers().polo())
                            .withEntries(new AbstractOrderEntryModel[] {(AbstractOrderEntryModel)orderEntryModel1, (AbstractOrderEntryModel)orderEntryModel2}).withDeliveryAddress(getAddresses().MontrealNancyHome()).withBaseSite((BaseSiteModel)getCmsSites().Canada())
                            .withPaymentInfo(getPaymentInfos().PaymentInfoForNancy("CODE_PAYMENT")).build();
            orderEntryModel1.setOrder((AbstractOrderModel)order);
            orderEntryModel2.setOrder((AbstractOrderModel)order);
            return order;
        });
    }


    public OrderModel CameraAndMemoryCardAndLens_Shipped(Long cameraQty, Long memoryCardQty, Long lensQty)
    {
        return (OrderModel)getOrSaveAndReturn(() -> (OrderModel)getWarehousingOrderDao().getByCode("camera-and-memory-card-and-lens-shipped"), () -> {
            OrderEntryModel orderEntryModel1 = Camera(cameraQty);
            OrderEntryModel orderEntryModel2 = MemoryCard(memoryCardQty);
            OrderEntryModel orderEntryModel3 = Lens(lensQty);
            OrderModel order = OrderModelBuilder.aModel().withCode("camera-and-memory-card-and-lens-shipped").withCurrency(getCurrencies().AmericanDollar()).withStore(getBaseStores().NorthAmerica()).withDate(new Date()).withUser(getCustomers().polo())
                            .withEntries(new AbstractOrderEntryModel[] {(AbstractOrderEntryModel)orderEntryModel1, (AbstractOrderEntryModel)orderEntryModel2, (AbstractOrderEntryModel)orderEntryModel3}).withDeliveryAddress(getAddresses().MontrealNancyHome())
                            .withBaseSite((BaseSiteModel)getCmsSites().Canada()).withPaymentInfo(getPaymentInfos().PaymentInfoForNancy("CODE_PAYMENT")).build();
            orderEntryModel1.setOrder((AbstractOrderModel)order);
            orderEntryModel2.setOrder((AbstractOrderModel)order);
            orderEntryModel3.setOrder((AbstractOrderModel)order);
            return order;
        });
    }


    protected OrderEntryModel Default(Long quantity)
    {
        return OrderEntryModelBuilder.fromModel((OrderEntryModel)getModelService().create(OrderEntryModel.class)).withQuantity(quantity)
                        .withUnit(this.units.Unit()).withGiveAway(Boolean.FALSE).withRejected(Boolean.FALSE).build();
    }


    protected OrderEntryModel Camera(Long quantity)
    {
        List<TaxValue> taxes = new ArrayList<>();
        Double basePrice = getPriceRows().CameraPrice(getProducts().Camera().getCode()).getPrice();
        taxes.add(new TaxValue("camera-tax", TAX_VALUE.doubleValue(), Boolean.TRUE.booleanValue(), getCurrencies().AmericanDollar().getIsocode()));
        return OrderEntryModelBuilder.fromModel(Default(quantity)).withBasePrice(basePrice).withTotalPrice(Double.valueOf(quantity.longValue() * basePrice.doubleValue()))
                        .withProduct(getProducts().Camera()).withTaxes(taxes).withCalculated(Boolean.TRUE).build();
    }


    protected OrderEntryModel MemoryCard(Long quantity)
    {
        List<TaxValue> taxes = new ArrayList<>();
        Double basePrice = getPriceRows().MemoryCardPrice(getProducts().MemoryCard().getCode()).getPrice();
        taxes.add(new TaxValue("camera-tax", TAX_VALUE.doubleValue(), Boolean.TRUE.booleanValue(), getCurrencies().AmericanDollar().getIsocode()));
        return OrderEntryModelBuilder.fromModel(Default(quantity)).withBasePrice(basePrice).withTotalPrice(Double.valueOf(quantity.longValue() * basePrice.doubleValue()))
                        .withProduct(getProducts().MemoryCard()).withTaxes(taxes).withCalculated(Boolean.TRUE).build();
    }


    protected OrderEntryModel Lens(Long quantity)
    {
        List<TaxValue> taxes = new ArrayList<>();
        Double basePrice = getPriceRows().LensPrice(getProducts().Lens().getCode()).getPrice();
        taxes.add(new TaxValue("camera-tax", TAX_VALUE.doubleValue(), Boolean.TRUE.booleanValue(), getCurrencies().AmericanDollar().getIsocode()));
        return OrderEntryModelBuilder.fromModel(Default(quantity)).withBasePrice(basePrice).withTotalPrice(Double.valueOf(quantity.longValue() * basePrice.doubleValue()))
                        .withProduct(getProducts().Lens()).withTaxes(taxes).withCalculated(Boolean.TRUE).build();
    }


    protected OrderEntryModel Camera_PickupMontreal(Long quantity)
    {
        return OrderEntryModelBuilder.fromModel(Camera(quantity))
                        .withDeliveryPointOfService(getPointsOfService().Montreal_Downtown()).build();
    }


    public WarehousingDao<OrderModel> getWarehousingOrderDao()
    {
        return this.warehousingOrderDao;
    }


    @Required
    public void setWarehousingOrderDao(WarehousingDao<OrderModel> warehousingOrderDao)
    {
        this.warehousingOrderDao = warehousingOrderDao;
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


    public Currencies getCurrencies()
    {
        return this.currencies;
    }


    @Required
    public void setCurrencies(Currencies currencies)
    {
        this.currencies = currencies;
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


    public Products getProducts()
    {
        return this.products;
    }


    @Required
    public void setProducts(Products products)
    {
        this.products = products;
    }


    public Units getUnits()
    {
        return this.units;
    }


    @Required
    public void setUnits(Units units)
    {
        this.units = units;
    }


    public Users getUsers()
    {
        return this.users;
    }


    @Required
    public void setUsers(Users users)
    {
        this.users = users;
    }


    public PointsOfService getPointsOfService()
    {
        return this.pointsOfService;
    }


    @Required
    public void setPointsOfService(PointsOfService pointsOfService)
    {
        this.pointsOfService = pointsOfService;
    }


    public Customers getCustomers()
    {
        return this.customers;
    }


    @Required
    public void setCustomers(Customers customers)
    {
        this.customers = customers;
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


    public PaymentInfos getPaymentInfos()
    {
        return this.paymentInfos;
    }


    @Required
    public void setPaymentInfos(PaymentInfos paymentInfos)
    {
        this.paymentInfos = paymentInfos;
    }


    public BaseSites getBaseSites()
    {
        return this.baseSites;
    }


    @Required
    public void setBaseSites(BaseSites baseSites)
    {
        this.baseSites = baseSites;
    }


    public CmsSites getCmsSites()
    {
        return this.cmsSites;
    }


    @Required
    public void setCmsSites(CmsSites cmsSites)
    {
        this.cmsSites = cmsSites;
    }


    protected PaymentTransactions getPaymentTransactions()
    {
        return this.paymentTransactions;
    }


    @Required
    public void setPaymentTransactions(PaymentTransactions paymentTransactions)
    {
        this.paymentTransactions = paymentTransactions;
    }


    protected PriceRows getPriceRows()
    {
        return this.priceRows;
    }


    @Required
    public void setPriceRows(PriceRows priceRows)
    {
        this.priceRows = priceRows;
    }
}
