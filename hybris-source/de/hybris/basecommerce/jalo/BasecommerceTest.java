package de.hybris.basecommerce.jalo;

import de.hybris.platform.basecommerce.jalo.BasecommerceManager;
import de.hybris.platform.basecommerce.jalo.DefaultMultiAddressDeliveryCostsStrategy;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.deliveryzone.jalo.Zone;
import de.hybris.platform.deliveryzone.jalo.ZoneDeliveryMode;
import de.hybris.platform.deliveryzone.jalo.ZoneDeliveryModeManager;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.order.delivery.DefaultDeliveryCostsStrategy;
import de.hybris.platform.jalo.order.delivery.DeliveryCostsStrategy;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasecommerceTest extends ServicelayerTest
{
    private static final Logger LOG = Logger.getLogger(BasecommerceTest.class.getName());
    @Resource
    private ModelService modelService;
    @Resource
    private UserService userService;
    @Resource
    private CartService cartService;
    @Resource
    private OrderService orderService;
    @Resource
    private ProductService productService;
    @Resource
    private CatalogService catalogService;
    @Resource
    private CatalogVersionService catalogVersionService;
    @Resource
    private CalculationService calculationService;
    @Resource
    private UnitService unitService;
    @Resource
    private TypeService typeService;
    private OrderModel order;


    @Before
    public void setUp() throws Exception
    {
        createCoreData();
        createDefaultCatalog();
        createOrder();
    }


    @Test
    public void testMultiAddressCalculation() throws JaloDeliveryModeException, JaloPriceFactoryException
    {
        AbstractOrder abstractOrder = (AbstractOrder)this.modelService.toPersistenceLayer(this.order);
        DeliveryCostsStrategy strategy = OrderManager.getInstance().getDeliveryCostsStrategy();
        OrderManager.getInstance().setDeliveryCostsStrategy((DeliveryCostsStrategy)new DefaultMultiAddressDeliveryCostsStrategy());
        abstractOrder.recalculate();
        LOG.info("Scenario 1 -> " + abstractOrder.getDeliveryCosts());
        Assert.assertEquals("DeliveryCost calculation failed!", Double.valueOf(8.0D), Double.valueOf(abstractOrder.getDeliveryCosts()));
        OrderManager.getInstance().setDeliveryCostsStrategy((DeliveryCostsStrategy)new DefaultDeliveryCostsStrategy());
        abstractOrder.recalculate();
        LOG.info("Scenario 2 -> " + abstractOrder.getDeliveryCosts());
        Assert.assertEquals("DeliveryCost calculation failed!", Double.valueOf(0.0D), Double.valueOf(abstractOrder.getDeliveryCosts()));
        List<AbstractOrderEntryModel> entries = this.order.getEntries();
        for(AbstractOrderEntryModel entry : entries)
        {
            if(entry.getProduct().getCode().equals("testProduct2"))
            {
                BasecommerceManager.getInstance().setDeliveryMode((AbstractOrderEntry)this.modelService.toPersistenceLayer(entry), null);
            }
        }
        OrderManager.getInstance().setDeliveryCostsStrategy((DeliveryCostsStrategy)new DefaultMultiAddressDeliveryCostsStrategy());
        abstractOrder.recalculate();
        LOG.info("Scenario 3 -> " + abstractOrder.getDeliveryCosts());
        Assert.assertEquals("DeliveryCost calculation failed!", Double.valueOf(4.2D), Double.valueOf(abstractOrder.getDeliveryCosts()));
        for(AbstractOrderEntryModel entry : entries)
        {
            if(entry.getProduct().getCode().equals("testProduct0"))
            {
                BasecommerceManager.getInstance().setDeliveryAddress((AbstractOrderEntry)this.modelService.toPersistenceLayer(entry), null);
            }
        }
        OrderManager.getInstance().setDeliveryCostsStrategy((DeliveryCostsStrategy)new DefaultMultiAddressDeliveryCostsStrategy());
        abstractOrder.recalculate();
        LOG.info("Scenario 4 -> " + abstractOrder.getDeliveryCosts());
        Assert.assertEquals("DeliveryCost calculation failed!", Double.valueOf(8.4D), Double.valueOf(abstractOrder.getDeliveryCosts()));
        OrderManager.getInstance().setDeliveryCostsStrategy(strategy);
    }


    public static void createCoreData() throws Exception
    {
        LOG.info("Creating essential data for core ...");
        JaloSession.getCurrentSession().setUser((User)UserManager.getInstance().getAdminEmployee());
        long startTime = System.currentTimeMillis();
        (new CoreBasicDataCreator()).createEssentialData(Collections.EMPTY_MAP, null);
        importCsv("/servicelayer/test/testBasics.csv", "windows-1252");
        LOG.info("Finished creating essential data for core in " + System.currentTimeMillis() - startTime + "ms");
    }


    public static void createDefaultCatalog() throws Exception
    {
        LOG.info("Creating test catalog ...");
        JaloSession.getCurrentSession().setUser((User)UserManager.getInstance().getAdminEmployee());
        long startTime = System.currentTimeMillis();
        importCsv("/servicelayer/test/testCatalog.csv", "windows-1252");
        CatalogVersionService catalogVersionService = (CatalogVersionService)Registry.getApplicationContext().getBean("catalogVersionService");
        ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService");
        ProductService productService = (ProductService)Registry.getApplicationContext().getBean("productService");
        CatalogVersion version = (CatalogVersion)modelService.getSource(catalogVersionService.getCatalogVersion("testCatalog", "Online"));
        Assert.assertNotNull(version);
        JaloSession.getCurrentSession().getSessionContext().setAttribute("catalogversions",
                        Collections.singletonList(version));
        Category category = CategoryManager.getInstance().getCategoriesByCode("testCategory0").iterator().next();
        Assert.assertNotNull(category);
        Product product = (Product)modelService.getSource(productService.getProductForCode("testProduct0"));
        Assert.assertNotNull(product);
        Assert.assertEquals(category, CategoryManager.getInstance().getCategoriesByProduct(product).iterator().next());
        LOG.info("Finished creating test catalog in " + System.currentTimeMillis() - startTime + "ms");
    }


    public void createOrder() throws InvalidCartException, Exception
    {
        LOG.info("Creating order ...");
        CurrencyModel currency = (CurrencyModel)this.modelService.create(CurrencyModel.class);
        currency.setIsocode("EUR1");
        currency.setSymbol("EUR1");
        currency.setBase(Boolean.TRUE);
        currency.setActive(Boolean.TRUE);
        currency.setConversion(Double.valueOf(1.0D));
        this.modelService.save(currency);
        ProductModel product0 = this.productService.getProductForCode("testProduct0");
        PriceRowModel prmodel0 = (PriceRowModel)this.modelService.create(PriceRowModel.class);
        prmodel0.setCurrency(currency);
        prmodel0.setMinqtd(Long.valueOf(1L));
        prmodel0.setNet(Boolean.TRUE);
        prmodel0.setPrice(Double.valueOf(5.0D));
        prmodel0.setUnit(this.unitService.getUnitForCode("kg"));
        prmodel0.setProduct(product0);
        prmodel0.setCatalogVersion(this.catalogVersionService.getCatalogVersion("testCatalog", "Online"));
        this.modelService.saveAll(Arrays.asList(new ItemModel[] {(ItemModel)prmodel0, (ItemModel)product0}));
        ProductModel product1 = this.productService.getProductForCode("testProduct1");
        PriceRowModel prmodel1 = (PriceRowModel)this.modelService.create(PriceRowModel.class);
        prmodel1.setCurrency(currency);
        prmodel1.setMinqtd(Long.valueOf(1L));
        prmodel1.setNet(Boolean.TRUE);
        prmodel1.setPrice(Double.valueOf(7.0D));
        prmodel1.setUnit(this.unitService.getUnitForCode("kg"));
        prmodel1.setProduct(product1);
        prmodel1.setCatalogVersion(this.catalogVersionService.getCatalogVersion("testCatalog", "Online"));
        this.modelService.saveAll(Arrays.asList(new ItemModel[] {(ItemModel)prmodel1, (ItemModel)product1}));
        ProductModel product2 = this.productService.getProductForCode("testProduct2");
        PriceRowModel prmodel2 = (PriceRowModel)this.modelService.create(PriceRowModel.class);
        prmodel2.setCurrency(currency);
        prmodel2.setMinqtd(Long.valueOf(1L));
        prmodel2.setNet(Boolean.TRUE);
        prmodel2.setPrice(Double.valueOf(7.0D));
        prmodel2.setUnit(this.unitService.getUnitForCode("kg"));
        prmodel2.setProduct(product2);
        prmodel2.setCatalogVersion(this.catalogVersionService.getCatalogVersion("testCatalog", "Online"));
        this.modelService.saveAll(Arrays.asList(new ItemModel[] {(ItemModel)prmodel2, (ItemModel)product2}));
        CartModel cart = this.cartService.getSessionCart();
        UserModel user = this.userService.getCurrentUser();
        this.cartService.addNewEntry((AbstractOrderModel)cart, product0, 2L, null);
        this.cartService.addNewEntry((AbstractOrderModel)cart, product1, 2L, null);
        this.cartService.addNewEntry((AbstractOrderModel)cart, product2, 2L, null);
        CountryModel country = new CountryModel();
        country.setActive(Boolean.TRUE);
        country.setIsocode("xyz");
        this.modelService.save(country);
        Zone zone = ZoneDeliveryModeManager.getInstance().createZone("myZone");
        zone.addCountry((Country)this.modelService.toPersistenceLayer(country));
        AddressModel defaultDeliveryAddress = new AddressModel();
        defaultDeliveryAddress.setOwner((ItemModel)user);
        defaultDeliveryAddress.setFirstname("Albert");
        defaultDeliveryAddress.setLastname("Einstein");
        defaultDeliveryAddress.setTown("Munich");
        defaultDeliveryAddress.setCountry(country);
        DebitPaymentInfoModel paymentInfo = new DebitPaymentInfoModel();
        paymentInfo.setOwner((ItemModel)cart);
        paymentInfo.setBank("MyBank");
        paymentInfo.setUser(user);
        paymentInfo.setAccountNumber("34434");
        paymentInfo.setBankIDNumber("1111112");
        paymentInfo.setBaOwner("Ich");
        this.order = this.orderService.placeOrder(cart, defaultDeliveryAddress, null, (PaymentInfoModel)paymentInfo);
        AddressModel deliveryAddress1 = new AddressModel();
        deliveryAddress1.setOwner((ItemModel)user);
        deliveryAddress1.setFirstname("Albert1");
        deliveryAddress1.setLastname("Einstein1");
        deliveryAddress1.setTown("Munich1");
        deliveryAddress1.setCountry(country);
        AddressModel deliveryAddress2 = new AddressModel();
        deliveryAddress2.setOwner((ItemModel)user);
        deliveryAddress2.setFirstname("Albert2");
        deliveryAddress2.setLastname("Einstein2");
        deliveryAddress2.setTown("Munich2");
        deliveryAddress2.setCountry(country);
        AddressModel deliveryAddress3 = new AddressModel();
        deliveryAddress3.setOwner((ItemModel)user);
        deliveryAddress3.setFirstname("Albert3");
        deliveryAddress3.setLastname("Einstein3");
        deliveryAddress3.setTown("Munich3");
        deliveryAddress3.setCountry(country);
        this.modelService.saveAll(new Object[] {deliveryAddress1, deliveryAddress2, deliveryAddress3});
        List<AbstractOrderEntryModel> entries = this.order.getEntries();
        AbstractOrderEntryModel entry1 = entries.get(0);
        entry1.setDeliveryAddress(deliveryAddress1);
        ComposedType dmType = (ComposedType)this.modelService.getSource(this.typeService.getComposedTypeForClass(ZoneDeliveryModeModel.class));
        ZoneDeliveryMode dm1 = (ZoneDeliveryMode)OrderManager.getInstance().createDeliveryMode(dmType, "deliveryModeCode1");
        dm1.setCost((Currency)this.modelService.toPersistenceLayer(currency), 1.0D, 4.2D, zone);
        BasecommerceManager.getInstance().setDeliveryMode((AbstractOrderEntry)this.modelService.toPersistenceLayer(entry1), (DeliveryMode)dm1);
        AbstractOrderEntryModel entry2 = entries.get(1);
        entry2.setDeliveryAddress(deliveryAddress1);
        BasecommerceManager.getInstance().setDeliveryMode((AbstractOrderEntry)this.modelService.toPersistenceLayer(entry2), (DeliveryMode)dm1);
        AbstractOrderEntryModel entry3 = entries.get(2);
        entry3.setDeliveryAddress(deliveryAddress3);
        ZoneDeliveryMode dm3 = (ZoneDeliveryMode)OrderManager.getInstance().createDeliveryMode(dmType, "deliveryModeCode3");
        dm3.setCost((Currency)this.modelService.toPersistenceLayer(currency), 1.0D, 3.8D, zone);
        BasecommerceManager.getInstance().setDeliveryMode((AbstractOrderEntry)this.modelService.toPersistenceLayer(entry3), (DeliveryMode)dm3);
        this.modelService.saveAll(new Object[] {entry1, entry2, entry3});
    }
}
