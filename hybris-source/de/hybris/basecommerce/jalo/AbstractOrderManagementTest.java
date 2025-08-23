package de.hybris.basecommerce.jalo;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.UUID;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.junit.Before;

public abstract class AbstractOrderManagementTest extends ServicelayerTest
{
    private OrderModel order;
    @Resource
    private ProductService productService;
    @Resource
    private CartService cartService;
    @Resource
    private UserService userService;
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
        createDefaultUsers();
        createDefaultCatalog();
        setOrder(placeTestOrder());
    }


    protected OrderModel placeTestOrder() throws InvalidCartException, CalculationException
    {
        try
        {
            CartModel cart = this.cartService.getSessionCart();
            UserModel user = this.userService.getCurrentUser();
            this.cartService.addNewEntry((AbstractOrderModel)cart, this.productService.getProductForCode("testProduct1"), 1L, null);
            this.cartService.addNewEntry((AbstractOrderModel)cart, this.productService.getProductForCode("testProduct2"), 2L, null);
            this.cartService.addNewEntry((AbstractOrderModel)cart, this.productService.getProductForCode("testProduct3"), 3L, null);
            AddressModel deliveryAddress = new AddressModel();
            deliveryAddress.setOwner((ItemModel)user);
            deliveryAddress.setFirstname("Der");
            deliveryAddress.setLastname("Buck");
            deliveryAddress.setTown("Muenchen");
            DebitPaymentInfoModel paymentInfo = new DebitPaymentInfoModel();
            paymentInfo.setCode(UUID.randomUUID().toString());
            paymentInfo.setOwner((ItemModel)cart);
            paymentInfo.setBank("MeineBank");
            paymentInfo.setUser(user);
            paymentInfo.setAccountNumber("34434");
            paymentInfo.setBankIDNumber("1111112");
            paymentInfo.setBaOwner("Ich");
            cart.setDeliveryAddress(deliveryAddress);
            cart.setPaymentInfo((PaymentInfoModel)paymentInfo);
            OrderModel order = this.orderService.createOrderFromCart(cart);
            this.modelService.save(order);
            this.calculationService.calculate((AbstractOrderModel)order);
            return order;
        }
        catch(InvalidCartException e)
        {
            getLogger().error("Error placing test order: " + e.getMessage(), (Throwable)e);
            throw e;
        }
    }


    public abstract Logger getLogger();


    public OrderModel getOrder()
    {
        return this.order;
    }


    public void setOrder(OrderModel order)
    {
        this.order = order;
    }
}
