package de.hybris.platform.ruleengineservices.util;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class OrderUtilsIT extends ServicelayerTransactionalTest
{
    @Resource
    private ProductService productService;
    @Resource
    private CartService cartService;
    @Resource
    private OrderUtils orderUtils;
    @Resource
    private OrderService orderService;
    @Resource
    private ModelService modelService;
    @Resource
    private CalculationService calculationService;


    @Before
    public void setUp() throws Exception
    {
        createCoreData();
        createDefaultCatalog();
    }


    @Test
    public void testChangeSomeQuantitiesInOrder() throws InvalidCartException, CalculationException
    {
        ProductModel product0 = this.productService.getProductForCode("testProduct0");
        ProductModel product1 = this.productService.getProductForCode("testProduct1");
        ProductModel product2 = this.productService.getProductForCode("testProduct2");
        ProductModel product3 = this.productService.getProductForCode("testProduct3");
        Assert.assertNotNull(product0);
        Assert.assertNotNull(product1);
        Assert.assertNotNull(product2);
        Assert.assertNotNull(product3);
        CartModel cart = this.cartService.getSessionCart();
        this.cartService.addNewEntry((AbstractOrderModel)cart, product0, 10L, null);
        this.cartService.addNewEntry((AbstractOrderModel)cart, product1, 15L, null);
        this.cartService.addNewEntry((AbstractOrderModel)cart, product2, 1L, null);
        this.cartService.addNewEntry((AbstractOrderModel)cart, product3, 18L, null);
        this.modelService.save(cart);
        OrderModel order = this.orderService.createOrderFromCart(cart);
        this.modelService.save(order);
        Assert.assertEquals("Number of entries", 4, order.getEntries().size());
        this.calculationService.calculate((AbstractOrderModel)order);
        Assert.assertEquals("Number of entries", 4, order.getEntries().size());
        Assert.assertEquals(Long.valueOf(10L), ((OrderEntryModel)this.orderService.getEntryForNumber((AbstractOrderModel)order, 0)).getQuantity());
        Assert.assertEquals(Long.valueOf(15L), ((OrderEntryModel)this.orderService.getEntryForNumber((AbstractOrderModel)order, 1)).getQuantity());
        Assert.assertEquals(Long.valueOf(1L), ((OrderEntryModel)this.orderService.getEntryForNumber((AbstractOrderModel)order, 2)).getQuantity());
        Assert.assertEquals(Long.valueOf(18L), ((OrderEntryModel)this.orderService.getEntryForNumber((AbstractOrderModel)order, 3)).getQuantity());
        Map<Integer, Long> newQuantities = new HashMap<>();
        newQuantities.put(Integer.valueOf(0), Long.valueOf(5L));
        newQuantities.put(Integer.valueOf(1), Long.valueOf(0L));
        newQuantities.put(Integer.valueOf(2), Long.valueOf(0L));
        newQuantities.put(Integer.valueOf(3), Long.valueOf(10L));
        this.orderUtils.updateOrderQuantities(order, newQuantities);
        Assert.assertEquals("Number of entries", 2, order.getEntries().size());
        this.calculationService.calculate((AbstractOrderModel)order);
        Assert.assertEquals("Number of entries", 2, order.getEntries().size());
        Assert.assertEquals(Long.valueOf(5L), ((OrderEntryModel)this.orderService.getEntryForNumber((AbstractOrderModel)order, 0)).getQuantity());
        Assert.assertEquals(Long.valueOf(10L), ((OrderEntryModel)this.orderService.getEntryForNumber((AbstractOrderModel)order, 3)).getQuantity());
        newQuantities.clear();
        newQuantities.put(Integer.valueOf(3), Long.valueOf(30L));
        this.orderUtils.updateOrderQuantities(order, newQuantities);
        Assert.assertEquals("Number of entries", 2, order.getEntries().size());
        this.calculationService.calculate((AbstractOrderModel)order);
        Assert.assertEquals("Number of entries", 2, order.getEntries().size());
        Assert.assertEquals(Long.valueOf(5L), ((OrderEntryModel)this.orderService.getEntryForNumber((AbstractOrderModel)order, 0)).getQuantity());
        Assert.assertEquals(Long.valueOf(30L), ((OrderEntryModel)this.orderService.getEntryForNumber((AbstractOrderModel)order, 3)).getQuantity());
        newQuantities.clear();
        newQuantities.put(Integer.valueOf(0), Long.valueOf(-5L));
        newQuantities.put(Integer.valueOf(3), Long.valueOf(5L));
        this.orderUtils.updateOrderQuantities(order, newQuantities);
        Assert.assertEquals("Number of entries", 1, order.getEntries().size());
        this.calculationService.calculate((AbstractOrderModel)order);
        Assert.assertEquals("Number of entries", 1, order.getEntries().size());
        Assert.assertEquals(Long.valueOf(5L), ((OrderEntryModel)this.orderService.getEntryForNumber((AbstractOrderModel)order, 3)).getQuantity());
        newQuantities.clear();
        newQuantities.put(Integer.valueOf(100), Long.valueOf(5L));
        try
        {
            this.orderUtils.updateOrderQuantities(order, newQuantities);
            Assert.assertTrue("IllegalArgumentException expected here", false);
        }
        catch(IllegalArgumentException ex)
        {
            Assert.assertTrue(true);
        }
    }
}
