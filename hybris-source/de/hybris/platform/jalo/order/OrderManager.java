package de.hybris.platform.jalo.order;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SearchContext;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.extension.ExtensionNotFoundException;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.order.delivery.DefaultDeliveryCostsStrategy;
import de.hybris.platform.jalo.order.delivery.DeliveryCostsStrategy;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.order.payment.PaymentMode;
import de.hybris.platform.jalo.order.price.AbstractPriceFactory;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.DummyPriceFactory;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.order.price.Tax;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.ItemCloneCreator;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.SearchTools;
import de.hybris.platform.util.TaxValue;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class OrderManager extends Manager
{
    public static final String BEAN_NAME = "core.orderManager";
    private static final Logger LOG = Logger.getLogger(OrderManager.class.getName());
    private transient AbstractPriceFactory pricefactory;
    private transient String _orderEntryTypeCode = null;
    private transient String _cartEntryTypeCode = null;
    private CloneOrderStrategy<Order> cloneOrderStrategy;
    private CloneCartStrategy<Cart> cloneCartStrategy;
    private DeliveryCostsStrategy deliveryCostsStrategy;
    private KeyGenerator orderCodeGenerator;


    @Required
    public void setOrderCodeGenerator(KeyGenerator orderCodeGenerator)
    {
        this.orderCodeGenerator = orderCodeGenerator;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected CloneOrderStrategy<Order> getCloneOrderStrategy()
    {
        if(this.cloneOrderStrategy == null)
        {
            this.cloneOrderStrategy = (CloneOrderStrategy<Order>)new DefaultCloneOrderStrategy();
        }
        return this.cloneOrderStrategy;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setCloneOrderStrategy(CloneOrderStrategy<Order> cloneOrderStrategy)
    {
        this.cloneOrderStrategy = cloneOrderStrategy;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CloneCartStrategy<Cart> getCloneCartStrategy()
    {
        if(this.cloneCartStrategy == null)
        {
            this.cloneCartStrategy = (CloneCartStrategy<Cart>)new DefaultCloneCartStrategy();
        }
        return this.cloneCartStrategy;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setCloneCartStrategy(CloneCartStrategy<Cart> cloneCartStrategy)
    {
        this.cloneCartStrategy = cloneCartStrategy;
    }


    public DeliveryCostsStrategy getDeliveryCostsStrategy()
    {
        if(this.deliveryCostsStrategy == null)
        {
            this.deliveryCostsStrategy = (DeliveryCostsStrategy)new DefaultDeliveryCostsStrategy();
        }
        return this.deliveryCostsStrategy;
    }


    public void setDeliveryCostsStrategy(DeliveryCostsStrategy deliveryCostsStrategys)
    {
        this.deliveryCostsStrategy = deliveryCostsStrategys;
    }


    public static OrderManager getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getOrderManager();
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    private Collection<Discount> findOrdersByDiscountPK(PK currencyPK)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("pk", currencyPK);
        StringBuffer query = new StringBuffer(60);
        query.append("SELECT {").append("PK").append("} FROM {").append(GeneratedCoreConstants.TC.DISCOUNT).append("} WHERE {")
                        .append("currency").append("}=?pk");
        return FlexibleSearch.getInstance()
                        .search(query.toString(), params, Collections.singletonList(Discount.class), true, true, 0, 1)
                        .getResult();
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        try
        {
            if(item instanceof Currency)
            {
                Item.removeItemCollection(ctx, findOrdersByDiscountPK(item.getPK()));
            }
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    public AbstractPriceFactory getDefaultPriceFactory()
    {
        if(this.pricefactory == null)
        {
            String pfName = Config.getParameter("default.pricefactory");
            try
            {
                if(StringUtils.isBlank(pfName))
                {
                    return DummyPriceFactory.getInstance();
                }
                this.pricefactory = (AbstractPriceFactory)ExtensionManager.getInstance().getExtension(pfName);
            }
            catch(ExtensionNotFoundException e)
            {
                LOG.warn("pricefactory " + pfName + " not found (property default.pricefactory)! using DummyPriceFactory instead.");
                return DummyPriceFactory.getInstance();
            }
        }
        return this.pricefactory;
    }


    public PriceFactory getPriceFactory()
    {
        if(JaloSession.getCurrentSession().getPriceFactory() != null)
        {
            return JaloSession.getCurrentSession()
                            .getPriceFactory();
        }
        return (PriceFactory)getDefaultPriceFactory();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String getPaymentInfoTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(PaymentInfo.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "PaymentInfo type code not found", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getOrderEntryTypeCode()
    {
        if(this._orderEntryTypeCode == null)
        {
            this._orderEntryTypeCode = getSession().getTypeManager().getComposedType(OrderEntry.class).getCode();
        }
        return this._orderEntryTypeCode;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String getOrderTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(Order.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Order type code not found", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String getTaxTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(Tax.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Tax type code not found", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String getDiscountTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(Discount.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Discount type code not found", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String getDeliveryModeTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(DeliveryMode.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Delivery Mode type code not found", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String getPaymentModeTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(PaymentMode.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Payment Mode type code not found", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getCartEntryTypeCode()
    {
        if(this._cartEntryTypeCode == null)
        {
            this._cartEntryTypeCode = getSession().getTypeManager().getComposedType(CartEntry.class).getCode();
        }
        return this._cartEntryTypeCode;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String getCartTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(Cart.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Cart type code not found", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllPaymentInfos()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getPaymentInfoTypeCode() + "} ORDER BY {code} ASC", null,
                        Collections.singletonList(PaymentInfo.class), true, true, 0, -1);
        return Collections.unmodifiableCollection(res.getResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getPaymentInfos(int type)
    {
        return searchPaymentInfos(null, null, Integer.valueOf(type));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getPaymentInfosByUser(User user)
    {
        return searchPaymentInfos(user, null, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public PaymentInfo getPaymentInfoByCode(String code) throws JaloItemNotFoundException
    {
        Collection<PaymentInfo> res = searchPaymentInfos(null, code, null);
        if(res.isEmpty())
        {
            throw new JaloItemNotFoundException("cant find any payment info for code \"" + code + "\"", 0);
        }
        if(res.size() > 1)
        {
            LOG.error("warning: OrderManager.getPaymentInfoByCode(code) truncated resultset " + res + " to first element");
        }
        return res.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getPaymentInfosByCode(String code)
    {
        return searchPaymentInfos(null, code, null);
    }


    protected Collection searchPaymentInfos(User user, String code, Integer type)
    {
        Map<Object, Object> values = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append(Item.PK).append("} FROM {").append(getPaymentInfoTypeCode()).append("} WHERE ");
        query.append("{").append("original").append("} is NULL");
        if(user != null)
        {
            values.put("user", user);
            query.append(" AND {").append("user").append("} = ?").append("user");
        }
        if(code != null && !"".equals(code.trim()))
        {
            values.put("code", code);
            query.append(" AND {").append("code").append("} ");
            query.append(SearchTools.isLIKEPattern(code) ? "LIKE ?code" : "= ?code");
        }
        if(type != null)
        {
            values.put(PaymentInfo.TYPE, type);
            query.append(" AND {").append(PaymentInfo.TYPE).append("} = ?").append(PaymentInfo.TYPE);
        }
        SearchResult res = getSession().getFlexibleSearch().search(query.toString(), values,
                        Collections.singletonList(PaymentInfo.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getSupportedDeliveryModes(PaymentMode pm)
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getDeliveryModeTypeCode() + "} WHERE {supportedPaymentModesInternal} LIKE ?supportedPaymentModesInternal ORDER BY {code} ASC",
                        Collections.singletonMap("supportedPaymentModesInternal", "%" + pm.getPK().toString() + "%"),
                        Collections.singletonList(DeliveryMode.class), true, true, 0, -1);
        return res.getResult();
    }


    public Collection getAllOrders()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getOrderTypeCode() + "} ORDER BY {" + Item.CREATION_TIME + "} DESC", null,
                        Collections.singletonList(Order.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getOrders(User user)
    {
        return user.getOrders();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Order createOrder(User user, Currency currency, Date date, boolean net) throws JaloInvalidParameterException
    {
        return createOrder(null, user, currency, date, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Order createOrder(String code, User user, Currency currency, Date date, boolean net) throws JaloInvalidParameterException
    {
        return createOrder((PK)null, code, user, currency, date, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Order createOrder(ComposedType type, String code, User user, Currency currency, Date date, boolean net) throws JaloInvalidParameterException
    {
        return createOrder(type, null, code, user, currency, date, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Order createOrder(PK pk, String code, User user, Currency currency, Date date, boolean net) throws JaloInvalidParameterException
    {
        return createOrder(null, pk, code, user, currency, date, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Order createOrder(ComposedType type, PK pkBase, String code, User user, Currency currency, Date date, boolean net)
    {
        if(type != null && !Order.class.isAssignableFrom(type.getJaloClass()))
        {
            throw new JaloInvalidParameterException("type is not assignable from Order", 0);
        }
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put("code", adjustOrderCode(code));
            params.put("user", user);
            params.put("currency", currency);
            params.put("date", date);
            params.put("net", Boolean.valueOf(net));
            if(type == null)
            {
                type = TypeManager.getInstance().getComposedType(Order.class);
            }
            return (Order)type.newInstance(getSession().getSessionContext(), (Map)params);
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Cart createCart(User user, Currency currency, Date date, boolean net) throws ConsistencyCheckException, JaloInvalidParameterException
    {
        return createCart(null, user, currency, date, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Cart createCart(String code, User user, Currency currency, Date date, boolean net) throws ConsistencyCheckException, JaloInvalidParameterException
    {
        return createCart((PK)null, code, user, currency, date, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Cart createCart(ComposedType type, String code, User user, Currency currency, Date date, boolean net) throws ConsistencyCheckException, JaloInvalidParameterException
    {
        return createCart(type, null, code, user, currency, date, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Cart createCart(PK pk, String code, User user, Currency currency, Date date, boolean net) throws ConsistencyCheckException, JaloInvalidParameterException
    {
        return createCart(null, pk, code, user, currency, date, net);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Cart createCart(ComposedType type, PK pkBase, String code, User user, Currency currency, Date date, boolean net) throws ConsistencyCheckException, JaloInvalidParameterException
    {
        if(type != null && !Cart.class.isAssignableFrom(type.getJaloClass()))
        {
            throw new JaloInvalidParameterException("type " + type + " is not assignable from Cart", 0);
        }
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put(Item.PK, pkBase);
            params.put("code", code);
            params.put("user", user);
            params.put("currency", currency);
            params.put("date", date);
            params.put("net", Boolean.valueOf(net));
            if(type == null)
            {
                type = TypeManager.getInstance().getComposedType(Cart.class);
            }
            return (Cart)type.newInstance(getSession().getSessionContext(), (Map)params);
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            if(jaloGenericCreationException1 instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Order createOrder(AbstractOrder original)
    {
        return createOrder(null, null, original);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Order createOrder(ComposedType type, ComposedType entryType, AbstractOrder original)
    {
        if(type != null && !Order.class.isAssignableFrom(type.getJaloClass()))
        {
            throw new JaloInvalidParameterException("type " + type + " is not assignable from Order", 0);
        }
        if(entryType != null && !OrderEntry.class.isAssignableFrom(entryType.getJaloClass()))
        {
            throw new JaloInvalidParameterException("entry type " + entryType + " is not assignable from OrderEntry", 0);
        }
        return getCloneOrderStrategy().clone(type, entryType, original, this);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Cart createCart(AbstractOrder original, String newCode) throws ConsistencyCheckException
    {
        return createCart(null, null, original, newCode);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Cart createCart(ComposedType type, ComposedType entryType, AbstractOrder original, String newCode) throws ConsistencyCheckException
    {
        if(type != null && !Cart.class.isAssignableFrom(type.getJaloClass()))
        {
            throw new JaloInvalidParameterException("type " + type + " is not assignable from Cart", 0);
        }
        if(entryType != null && !CartEntry.class.isAssignableFrom(entryType.getJaloClass()))
        {
            throw new JaloInvalidParameterException("entry type " + entryType + " is not assignable from CartEntry", 0);
        }
        return getCloneCartStrategy().clone(type, entryType, original, newCode, this);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection searchOrders(SearchContext context)
    {
        return searchAbstractOrders(getOrderTypeCode(), context);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected Collection searchAbstractOrders(String orderTypeCode, SearchContext context)
    {
        Map<Object, Object> values = new HashMap<>();
        StringBuilder query = (new StringBuilder("GET {")).append(orderTypeCode).append("} WHERE ");
        boolean first = true;
        if(context.getProperty("code") != null &&
                        !"".equals(((String)context.getProperty("code")).trim()))
        {
            values.put("code", context.getProperty("code"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append("{").append("code").append("} = ?").append("code");
        }
        if(context.getProperty("user") != null)
        {
            values.put("user", context.getProperty("user"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append("{").append("user").append("} = ?").append("user");
        }
        if(context.getProperty("Order.startDate") != null && context.getProperty("Order.endDate") != null)
        {
            values.put("Order.startDate", context.getProperty("Order.startDate"));
            values.put("Order.endDate", context.getProperty("Order.endDate"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append(Item.CREATION_TIME).append("} >= ?").append("Order.startDate");
            query.append(" AND {").append(Item.CREATION_TIME).append("} <= ?").append("Order.endDate");
        }
        if(context.getProperty("currency") != null)
        {
            values.put("currency", context.getProperty("currency"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("currency").append("} = ?").append("currency");
        }
        if(context.getProperty("net") != null)
        {
            values.put("net", context.getProperty("net"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("net").append("} = ?").append("net");
        }
        if(context.getProperty("status") != null)
        {
            values.put("status", context.getProperty("status"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("status").append("} = ?").append("status");
        }
        if(context.getProperty("paymentMode") != null)
        {
            values.put("paymentMode", context.getProperty("paymentMode"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("paymentMode").append("} = ?").append("paymentMode");
        }
        if(context.getProperty("deliveryMode") != null)
        {
            values.put("deliveryMode", context.getProperty("deliveryMode"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("deliveryMode").append("} = ?").append("deliveryMode");
        }
        if(context.getProperty("paymentStatus") != null)
        {
            values.put("paymentStatus", context.getProperty("paymentStatus"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("paymentStatus").append("} = ?").append("paymentStatus");
        }
        if(context.getProperty("deliveryStatus") != null)
        {
            values.put("deliveryStatus", context.getProperty("deliveryStatus"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("deliveryStatus").append("} = ?").append("deliveryStatus");
        }
        SearchResult res = getSession().getFlexibleSearch().search(query.toString(), values,
                        Collections.singletonList(AbstractOrder.class), true, true, context
                                        .getRangeStart(), context.getRangeCount());
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection searchCarts(SearchContext context)
    {
        return searchAbstractOrders(getCartTypeCode(), context);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection searchTaxes(SearchContext context)
    {
        Map<Object, Object> values = new HashMap<>();
        StringBuilder query = (new StringBuilder("SELECT {")).append(Item.PK).append("} FROM {").append(getTaxTypeCode()).append("} WHERE ");
        boolean first = true;
        if(context.getProperty("code") != null && !"".equals(((String)context.getProperty("code")).trim()))
        {
            values.put("code", context.getProperty("code"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("code").append("} = ?").append("code");
        }
        if(context.getProperty("Tax.valueStart") != null && context.getProperty("Tax.valueEnd") != null)
        {
            values.put("Tax.valueStart", context.getProperty("Tax.valueStart"));
            values.put("Tax.valueEnd", context.getProperty("Tax.valueEnd"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("value").append("} >= ?").append("Tax.valueStart");
            query.append(" AND {").append("value").append("} <= ?").append("Tax.valueEnd");
        }
        SearchResult res = getSession().getFlexibleSearch().search(query.toString(), values,
                        Collections.singletonList(Tax.class), true, true, context
                                        .getRangeStart(), context.getRangeCount());
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection searchDiscounts(SearchContext context)
    {
        Map<Object, Object> values = new HashMap<>();
        StringBuilder query = (new StringBuilder("SELECT {")).append(Item.PK).append("} FROM {").append(getDiscountTypeCode()).append("} WHERE ");
        boolean first = true;
        if(context.getProperty("code") != null && !"".equals(((String)context.getProperty("code")).trim()))
        {
            values.put("code", context.getProperty("code"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("code").append("} = ?").append("code");
        }
        if(context.getProperty("global") != null)
        {
            values.put("global", context.getProperty("global"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("global").append("} = ?").append("global");
        }
        if(context.getProperty("absolute") != null)
        {
            boolean abs = Boolean.TRUE.equals(context.getProperty("absolute"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("currency").append("} ").append(abs ? " IS NOT NULL " : " IS NULL ");
        }
        if(context.getProperty("currency") != null)
        {
            values.put("currency", context.getProperty("currency"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("currency").append("} = ?").append("currency");
        }
        if(context.getProperty("Discount.valueStart") != null && context.getProperty("Discount.valueEnd") != null)
        {
            values.put("Discount.valueStart", context.getProperty("Discount.valueStart"));
            values.put("Discount.valueEnd", context.getProperty("Discount.valueEnd"));
            if(first)
            {
                first = false;
            }
            else
            {
                query.append(" AND ");
            }
            query.append(" {").append("value").append("} >= ?").append("Discount.valueStart").append(" AND {")
                            .append("value").append("} <= ?").append("Discount.valueEnd");
        }
        SearchResult res = getSession().getFlexibleSearch().search(query.toString(), values,
                        Collections.singletonList(Discount.class), true, true, context
                                        .getRangeStart(), context.getRangeCount());
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllTaxes()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getTaxTypeCode() + "} ORDER BY {code} ASC", null,
                        Collections.singletonList(Tax.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getTaxesByCode(String searchString)
    {
        StringBuilder query = (new StringBuilder("SELECT {")).append(Item.PK).append("} FROM {").append(getTaxTypeCode()).append("} WHERE {code}");
        query.append(SearchTools.isLIKEPattern(searchString) ? " LIKE " : " = ").append("?").append("code");
        SearchResult res = getSession().getFlexibleSearch().search(query.toString(),
                        Collections.singletonMap("code", searchString),
                        Collections.singletonList(Tax.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Tax getTaxByCode(String code)
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getTaxTypeCode() + "} WHERE {code}=?code ORDER BY {" + Item.CREATION_TIME + "} DESC",
                        Collections.singletonMap("code", code),
                        Collections.singletonList(Tax.class), true, true, 0, -1);
        Collection<Tax> coll = res.getResult();
        if(coll.size() < 1)
        {
            return null;
        }
        return coll.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Tax createTax(String code) throws ConsistencyCheckException
    {
        try
        {
            return (Tax)ComposedType.newInstance(getSession().getSessionContext(), Tax.class, new Object[] {"code", code});
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            if(jaloGenericCreationException1 instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public TaxValue createTaxValue(String code, double value, boolean absolute, Tax tax)
    {
        return new TaxValue(code, value, absolute, absolute ? getSession().getSessionContext().getCurrency().getIsoCode() : null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public DiscountValue createDiscountValue(String code, String description, double value, int priority, boolean absolute, Discount discount)
    {
        return new DiscountValue(code, value, absolute, absolute ? discount.getCurrency().getIsoCode() : null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllDiscounts()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getDiscountTypeCode() + "} ORDER BY {code} ASC", null,
                        Collections.singletonList(Discount.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllDiscounts(boolean global)
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getDiscountTypeCode() + "} WHERE {global}=?global ORDER BY {code} ASC",
                        Collections.singletonMap("global", global ? Boolean.TRUE : Boolean.FALSE),
                        Collections.singletonList(Discount.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Discount getDiscountByCode(String code)
    {
        List<Discount> result = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} FROM {" + getDiscountTypeCode() + "} WHERE {code} = ?code ORDER BY {" + Item.CREATION_TIME + "} DESC", Collections.singletonMap("code", code), Discount.class).getResult();
        if(result.isEmpty())
        {
            return null;
        }
        if(result.size() > 1)
        {
            LOG.error("found multiple discounts for code '" + code + "' (gout " + result + ") - choosing first one");
        }
        return result.get(0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getDiscountsByCode(String searchString)
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getDiscountTypeCode() + "} WHERE {code} " + (
                                        SearchTools.isLIKEPattern(searchString) ? " LIKE " : " = ") + " ?code ORDER BY {code} ASC, {" + Item.CREATION_TIME + "} DESC",
                        Collections.singletonMap("code", searchString), Collections.singletonList(Discount.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Discount createDiscount(String code) throws ConsistencyCheckException
    {
        try
        {
            return (Discount)ComposedType.newInstance(getSession().getSessionContext(), Discount.class, new Object[] {"code", code});
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            if(jaloGenericCreationException1 instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public DeliveryMode createDeliveryMode(ComposedType type, String code) throws ConsistencyCheckException
    {
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put("code", code);
            if(type == null)
            {
                type = TypeManager.getInstance().getComposedType(DeliveryMode.class);
            }
            return (DeliveryMode)type.newInstance(getSession().getSessionContext(), (Map)params);
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            if(jaloGenericCreationException1 instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllDeliveryModes()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getDeliveryModeTypeCode() + "} ORDER BY {code} ASC", null,
                        Collections.singletonList(DeliveryMode.class), true, true, 0, -1);
        return Collections.unmodifiableCollection(res.getResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public DeliveryMode getDeliveryModeByCode(String code) throws JaloItemNotFoundException
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getDeliveryModeTypeCode() + "} WHERE {code} = ?code ORDER BY {" + Item.CREATION_TIME + "} DESC",
                        Collections.singletonMap("code", code), Collections.singletonList(DeliveryMode.class), true, true, 0, -1);
        List<DeliveryMode> result = res.getResult();
        if(!result.isEmpty())
        {
            return result.get(0);
        }
        throw new JaloItemNotFoundException("DeliveryMode with code " + code + " wasn't found", 0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public PaymentMode createPaymentMode(ComposedType type, String code, ComposedType paymentInfoType) throws ConsistencyCheckException
    {
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put("code", code);
            params.put("paymentInfoType", paymentInfoType);
            if(type == null)
            {
                type = TypeManager.getInstance().getComposedType(PaymentMode.class);
            }
            return (PaymentMode)type.newInstance(getSession().getSessionContext(), (Map)params);
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            if(jaloGenericCreationException1 instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public PaymentMode getPaymentModeByCode(String code) throws JaloItemNotFoundException
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getPaymentModeTypeCode() + "} WHERE {code} = ?code ORDER BY {" + Item.CREATION_TIME + "} DESC",
                        Collections.singletonMap("code", code), Collections.singletonList(PaymentMode.class), true, true, 0, -1);
        List<PaymentMode> result = res.getResult();
        if(!result.isEmpty())
        {
            return result.get(0);
        }
        throw new JaloItemNotFoundException("PaymentCode with code " + code + " wasn't found", 0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllPaymentModes()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getPaymentModeTypeCode() + "} ORDER BY {code} ASC", null,
                        Collections.singletonList(PaymentMode.class), true, true, 0, -1);
        return Collections.unmodifiableCollection(res.getResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void appendToCart(Cart sourceCart, Cart targetCart)
    {
        AddToCartCopyContext addToCartCopyContext = new AddToCartCopyContext(targetCart.getCustomEntryType(), sourceCart, targetCart);
        int nr = targetCart.getNextEntryNumber(null);
        List<CartEntry> srcEntries = sourceCart.getAllEntries();
        for(CartEntry src : srcEntries)
        {
            addToCartCopyContext.addPreset((Item)src, "entryNumber", Integer.valueOf(nr++));
            addToCartCopyContext.addCopyUntypedPropsFor((Item)src);
        }
        try
        {
            (new ItemCloneCreator()).copyAll(srcEntries, (ItemCloneCreator.CopyContext)addToCartCopyContext);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationType getOrderStatusType()
    {
        try
        {
            return getSession().getEnumerationManager().getEnumerationType("OrderStatus");
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "OrderStatus enumeration type could not be found.", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationType getPaymentStatusType()
    {
        try
        {
            return getSession().getEnumerationManager().getEnumerationType("PaymentStatus");
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "PaymentStatus enumeration type could not be found.", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationType getDeliveryStatusType()
    {
        try
        {
            return getSession().getEnumerationManager().getEnumerationType("DeliveryStatus");
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "DeliveryStatus enumeration type could not be found.", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationType getExportStatusType()
    {
        try
        {
            return getSession().getEnumerationManager().getEnumerationType("ExportStatus");
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "ExportStatus enumeration type could not be found.", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected String adjustOrderCode(String presetCode)
    {
        if(presetCode == null)
        {
            return (String)this.orderCodeGenerator.generate();
        }
        return presetCode;
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new OrderManagerSerializableDTO(getTenant());
    }
}
