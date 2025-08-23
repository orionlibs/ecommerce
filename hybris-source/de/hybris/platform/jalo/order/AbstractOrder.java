package de.hybris.platform.jalo.order;

import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.payment.JaloPaymentModeException;
import de.hybris.platform.jalo.order.payment.PaymentMode;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.JaloTools;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

public abstract class AbstractOrder extends GeneratedAbstractOrder
{
    public static final String CFG_TAX_FREE_ENTRIES_SUPPORT = "abstractorder.taxFreeEntrySupport";
    public static final int APPEND_AS_LAST = -1;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DELIVERY_ADDRESS = "deliveryAddress";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PAYMENT_ADDRESS = "paymentAddress";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String EXPORT_STATUS = "exportStatus";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PAYMENT_MODE = "paymentMode";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DELIVERY_MODE = "deliveryMode";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PAYMENT_INFO = "paymentInfo";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PAYMENT_STATUS = "paymentStatus";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DELIVERY_STATUS = "deliveryStatus";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String GLOBAL_DISCOUNT_VALUES = "globalDiscountValues";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String TOTAL_TAX_VALUES = "totalTaxValues";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String TOTAL_TAX = "totalTax";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String TOTAL = "totalPrice";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DELIVERY_COST = "deliveryCost";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PAYMENT_COST = "paymentCost";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String STATUS_INFO = "statusInfo";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String TOTAL_DISCOUNTS = "totalDiscounts";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String ORDER_DISCOUNT_RELATION_NAME = "OrderDiscountRelation";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String ORDER_STATUS_TYPE = "OrderStatus";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String PAYMENT_STATUS_TYPE = "PaymentStatus";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String DELIVERY_STATUS_TYPE = "DeliveryStatus";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String EXPORT_STATUS_TYPE = "ExportStatus";


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("currency", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("user", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing parameters to create a AbstractOrder ( missing " + missing + ")", 0);
        }
        if(allAttributes.get("code") == null)
        {
            allAttributes.put("code", OrderManager.getInstance().adjustOrderCode(null));
        }
        Date date = (Date)allAttributes.get("date");
        if(date == null)
        {
            date = new Date();
        }
        allAttributes.remove("date");
        allAttributes.put(CREATION_TIME, date);
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getAllEntries()
    {
        return getSession()
                        .getFlexibleSearch()
                        .search("GET {" +
                                                        getAbstractOrderEntryTypeCode() + "} WHERE {order} = ?order ORDER BY {entryNumber} ASC",
                                        Collections.singletonMap("order", this), Collections.singletonList(AbstractOrderEntry.class), true, true, 0, -1)
                        .getResult();
    }


    @ForceJALO(reason = "something else")
    public List<AbstractOrderEntry> getEntries()
    {
        return getEntries(getSession().getSessionContext());
    }


    @ForceJALO(reason = "something else")
    public List<AbstractOrderEntry> getEntries(SessionContext ctx)
    {
        return getAllEntries();
    }


    protected void setAllEntries(SessionContext ctx, List<?> value)
    {
        Set<AbstractOrderEntry> toRemove = new HashSet<>(getEntries());
        if(value != null)
        {
            toRemove.removeAll(value);
            removeEntries(ctx, toRemove);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public AbstractOrderEntry getEntry(int index) throws JaloItemNotFoundException
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("o", this);
        values.put("nr", Integer.valueOf(index));
        SearchResult res = getSession().getFlexibleSearch().search("GET {" +
                                        getAbstractOrderEntryTypeCode() + "} WHERE {order} = ?o AND {entryNumber} = ?nr", values,
                        Collections.singletonList(AbstractOrderEntry.class), false, false, 0, -1);
        Collection<AbstractOrderEntry> coll = res.getResult();
        if(coll.isEmpty())
        {
            throw new JaloItemNotFoundException("cant find entry number " + index + " within order " + this, 0);
        }
        if(coll.size() > 1)
        {
            throw new JaloSystemException(null, "there are more than one entries [" + coll + "]with the same number " + index + " within one order", 0);
        }
        return coll.iterator().next();
    }


    public Collection getEntries(int startIdx, int endIdx)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("order", this);
        values.put("startIdx", Integer.valueOf(startIdx));
        values.put("endIdx", Integer.valueOf(endIdx));
        return getSession()
                        .getFlexibleSearch()
                        .search("GET {" +
                                        getAbstractOrderEntryTypeCode() + "} WHERE {order} = ?order AND {entryNumber} >= ?startIdx AND {entryNumber} <= ?endIdx ORDER BY {entryNumber} ASC", values, AbstractOrderEntry.class)
                        .getResult();
    }


    public List getEntriesByProduct(Product product)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("order", this);
        values.put("product", product);
        return getSession()
                        .getFlexibleSearch()
                        .search("GET {" +
                                        getAbstractOrderEntryTypeCode() + "} WHERE {order} = ?order AND {product} = ?product ORDER BY {entryNumber} ASC", values, AbstractOrderEntry.class)
                        .getResult();
    }


    protected int createNewEntryNumber(int requested)
    {
        int ret = requested;
        List<AbstractOrderEntry> all = getAllEntries();
        if(requested < 0)
        {
            ret = getNewEntryNumberForAppending(all);
        }
        else
        {
            shuffleEntriesOnInsertNew(all, requested);
        }
        return ret;
    }


    protected void shuffleEntriesOnInsertNew(List<AbstractOrderEntry> all, int newEntryNumber)
    {
        int numberToShift = newEntryNumber;
        for(AbstractOrderEntry entry : all)
        {
            int entryNumber = entry.getEntryNumber().intValue();
            if(entryNumber > numberToShift)
            {
                break;
            }
            if(entryNumber == numberToShift)
            {
                numberToShift = entryNumber + 1;
                entry.setEntryNumberDirect(numberToShift);
            }
        }
    }


    protected int getNewEntryNumberForAppending(List<AbstractOrderEntry> all)
    {
        if(all.isEmpty())
        {
            return 0;
        }
        int max = 0;
        for(AbstractOrderEntry e : all)
        {
            max = Math.max(max, e.getEntryNumber().intValue());
        }
        return max + 1;
    }


    protected int getNextEntryNumber(AbstractOrderEntry forMe)
    {
        List<AbstractOrderEntry> all = getAllEntries();
        if(all.isEmpty())
        {
            return 0;
        }
        int size = all.size();
        AbstractOrderEntry lastOne = all.get(size - 1);
        if(lastOne.equals(forMe))
        {
            if(size == 1)
            {
                return 0;
            }
            lastOne = all.get(size - 2);
        }
        return lastOne.getEntryNumber().intValue() + 1;
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public AbstractOrderEntry addNewEntry(Product prod, long qtd, Unit unit)
    {
        return addNewEntry(prod, qtd, unit, false);
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public AbstractOrderEntry addNewEntry(Product prod, long qtd, Unit unit, boolean addToPresent)
    {
        return addNewEntry(prod, qtd, unit, -1, addToPresent);
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public AbstractOrderEntry addNewEntry(Product prod, long qtd, Unit unit, int position, boolean addToPresent)
    {
        return addNewEntry(getCustomEntryType(), prod, qtd, unit, position, addToPresent);
    }


    @Deprecated(since = "4.3", forRemoval = false)
    public AbstractOrderEntry addNewEntry(ComposedType type, Product prod, long qtd, Unit unit, int position, boolean addToPresent)
    {
        synchronized(getSyncObject())
        {
            if(prod == null)
            {
                throw new IllegalArgumentException("Product must not be 'null' ");
            }
            if(unit == null)
            {
                throw new IllegalArgumentException("Unit must not be 'null' ");
            }
            AbstractOrderEntry ret = null;
            if(addToPresent)
            {
                for(AbstractOrderEntry e : getEntriesByProduct(prod))
                {
                    if(!e.isGiveAway().booleanValue() && unit.equals(e.getUnit()))
                    {
                        e.setQuantity(e.getQuantity().longValue() + qtd);
                        ret = e;
                        break;
                    }
                }
            }
            setChanged(false);
            if(ret == null)
            {
                return setInfo(
                                createNewEntry(
                                                getSession().getSessionContext(),
                                                (type == null) ? getCustomEntryType() : type, prod, qtd, unit,
                                                createNewEntryNumber(position)));
            }
            return ret;
        }
    }


    private final AbstractOrderEntry setInfo(AbstractOrderEntry entry)
    {
        String info = createEntryInformation(entry);
        if(info != null)
        {
            entry.setInfo(info);
        }
        return entry;
    }


    protected ComposedType getCustomEntryType()
    {
        return null;
    }


    public Item setComposedType(ComposedType type) throws JaloInvalidParameterException
    {
        try
        {
            return (Item)Transaction.current().execute((TransactionBody)new Object(this, type));
        }
        catch(JaloInvalidParameterException e)
        {
            throw e;
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "4.3", forRemoval = false)
    protected String createEntryInformation(AbstractOrderEntry newEntry)
    {
        ComposedType type = newEntry.getProduct().getComposedType();
        String infoField = Config.getString("orderentry.infofield." + type.getCode().toLowerCase(), null);
        boolean hasInfoFieldAValue = true;
        while(infoField == null)
        {
            if(type.getSuperType() == null)
            {
                hasInfoFieldAValue = false;
                break;
            }
            type = type.getSuperType();
            infoField = Config.getString("orderentry.infofield." + type.getCode().toLowerCase(), null);
        }
        Product product = newEntry.getProduct();
        String defaultstring = (product == null) ? "n/a" : product.getCode();
        StringBuilder returnString = new StringBuilder(defaultstring);
        if(hasInfoFieldAValue)
        {
            returnString.setLength(0);
            int openTag = infoField.indexOf("${");
            int closeTag = infoField.indexOf('}');
            int start = 0;
            Product product1 = product;
            while(openTag != -1 && closeTag != -1)
            {
                Item item;
                returnString.append(infoField.substring(start, openTag));
                String code = infoField.substring(openTag + 2, closeTag);
                int attr_start = 0;
                int attr_dot = code.indexOf('.');
                while(attr_dot != -1)
                {
                    try
                    {
                        item = goToConcreteItem((Item)product1, code.substring(attr_start, attr_dot));
                    }
                    catch(JaloInvalidParameterException e1)
                    {
                        throw new JaloInvalidParameterException("pattern '" + infoField + "' contains a missing attribute '" + code + "' for type " + item
                                        .getComposedType().getCode(), 0);
                    }
                    catch(JaloSecurityException e1)
                    {
                        throw new JaloSystemException(e1);
                    }
                    attr_start = attr_dot + 1;
                    attr_dot = code.indexOf('.', attr_start);
                }
                if(attr_start > 0 && attr_dot == -1)
                {
                    code = code.substring(attr_start, code
                                    .length());
                }
                try
                {
                    Object value = getAttributeValue(item, code);
                    returnString.append((value != null) ? value.toString() : "n/a");
                }
                catch(JaloItemNotFoundException e)
                {
                    throw new JaloInvalidParameterException("pattern '" + infoField + "' contains a missing attribute '" + code + "' for type " + item
                                    .getComposedType().getCode(), 0);
                }
                catch(JaloSecurityException e)
                {
                    throw new JaloSystemException(e);
                }
                start = closeTag + 1;
                openTag = infoField.indexOf("${", start);
                closeTag = infoField.indexOf('}', start);
            }
            if(start < infoField.length())
            {
                returnString.append(infoField.substring(start));
            }
        }
        return returnString.toString();
    }


    protected Object getAttributeValue(Item item, String qualifer) throws JaloSecurityException
    {
        AttributeDescriptor ad = item.getComposedType().getAttributeDescriptorIncludingPrivate(qualifer);
        if(ad.isLocalized())
        {
            JaloSession js = getSession();
            SessionContext ctx = js.getSessionContext();
            if(ctx == null || ctx.getLanguage() == null)
            {
                Map<Language, Object> locMap = (Map<Language, Object>)item.getAttribute(ctx, qualifer);
                if(MapUtils.isNotEmpty(locMap))
                {
                    Language fallbackLang = tryToFindFallbackLanguage(js);
                    if(fallbackLang != null)
                    {
                        Object ret = locMap.get(fallbackLang);
                        if(ret != null)
                        {
                            return ret;
                        }
                    }
                    Optional<Map.Entry<Language, Object>> entry = locMap.entrySet().stream().filter(e -> (e.getValue() != null)).findFirst();
                    return entry.isPresent() ? ((Map.Entry)entry.get()).getValue() : null;
                }
                return null;
            }
            return item.getAttribute(ctx, qualifer);
        }
        return item.getAttribute(qualifer);
    }


    protected Language tryToFindFallbackLanguage(JaloSession js)
    {
        Language fallbackLang = getUser().getSessionLanguage();
        if(fallbackLang != null)
        {
            return fallbackLang;
        }
        try
        {
            fallbackLang = C2LManager.getInstance().getLanguageByIsoCode(getTenant().getTenantSpecificLocale().toString());
            return fallbackLang;
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
            try
            {
                fallbackLang = C2LManager.getInstance().getLanguageByIsoCode(Locale.getDefault().toString());
                return fallbackLang;
            }
            catch(JaloItemNotFoundException jaloItemNotFoundException1)
            {
                return null;
            }
        }
    }


    private Item goToConcreteItem(Item item, String qualifier) throws JaloInvalidParameterException, JaloSecurityException
    {
        return (Item)item.getAttribute(qualifier);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeEntry(AbstractOrderEntry entry)
    {
        synchronized(getSyncObject())
        {
            try
            {
                entry.removeWithoutOrderNotification(getSession().getSessionContext());
                setChanged(false);
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e, e.getMessage(), e.getErrorCode());
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void removeEntries(SessionContext ctx, Set<AbstractOrderEntry> entries)
    {
        synchronized(getSyncObject())
        {
            int count = 0;
            try
            {
                for(AbstractOrderEntry e : entries)
                {
                    e.removeWithoutOrderNotification(ctx);
                    count++;
                }
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e, e.getMessage(), e.getErrorCode());
            }
            finally
            {
                if(count > 0)
                {
                    setChanged(false);
                }
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeAllEntries()
    {
        synchronized(getSyncObject())
        {
            ConsistencyCheckException ex = null;
            for(Iterator<AbstractOrderEntry> iter = getAllEntries().iterator(); iter.hasNext(); )
            {
                try
                {
                    ((AbstractOrderEntry)iter.next()).remove();
                }
                catch(ConsistencyCheckException e)
                {
                    ex = e;
                }
            }
            if(ex != null)
            {
                throw new JaloSystemException(ex);
            }
            setChanged(false);
        }
    }


    @ForceJALO(reason = "consistency check")
    public void setCode(String code)
    {
        setCode(getSession().getSessionContext(), code);
    }


    @ForceJALO(reason = "consistency check")
    public void setCode(SessionContext ctx, String code)
    {
        if(code == null)
        {
            throw new IllegalArgumentException("order code cannot be null");
        }
        super.setCode(ctx, code);
    }


    @ForceJALO(reason = "consistency check")
    public void setDate(Date date)
    {
        setDate(getSession().getSessionContext(), date);
    }


    @ForceJALO(reason = "something else")
    public Date getDate(SessionContext ctx)
    {
        return getCreationTime();
    }


    @ForceJALO(reason = "consistency check")
    public void setDate(SessionContext ctx, Date date)
    {
        if(date == null)
        {
            throw new IllegalArgumentException("Date cannot be null");
        }
        synchronized(getSyncObject())
        {
            setCreationTime(date);
            setChanged(true);
        }
    }


    @ForceJALO(reason = "consistency check")
    public void setUser(User user)
    {
        setUser(getSession().getSessionContext(), user);
    }


    @ForceJALO(reason = "consistency check")
    public void setUser(SessionContext ctx, User user)
    {
        if(user == null)
        {
            throw new IllegalArgumentException("User cannot be null");
        }
        synchronized(getSyncObject())
        {
            super.setUser(ctx, user);
            setChanged(true);
        }
    }


    @ForceJALO(reason = "consistency check")
    public void setCurrency(Currency curr)
    {
        setCurrency(getSession().getSessionContext(), curr);
    }


    @ForceJALO(reason = "consistency check")
    public void setCurrency(SessionContext ctx, Currency curr)
    {
        if(curr == null)
        {
            throw new IllegalArgumentException("order currency cannot be null");
        }
        synchronized(this)
        {
            setChanged(true);
            super.setCurrency(ctx, curr);
        }
    }


    @ForceJALO(reason = "something else")
    public void setStatus(EnumerationValue status)
    {
        setStatus(getSession().getSessionContext(), status);
    }


    @ForceJALO(reason = "something else")
    public void setStatus(SessionContext ctx, EnumerationValue status)
    {
        synchronized(getSyncObject())
        {
            super.setStatus(ctx, status);
        }
    }


    @ForceJALO(reason = "something else")
    public void setPaymentStatus(EnumerationValue paymentStatus)
    {
        setPaymentStatus(getSession().getSessionContext(), paymentStatus);
    }


    @ForceJALO(reason = "something else")
    public void setPaymentStatus(SessionContext ctx, EnumerationValue paymentStatus)
    {
        synchronized(getSyncObject())
        {
            super.setPaymentStatus(ctx, paymentStatus);
        }
    }


    @ForceJALO(reason = "something else")
    public void setDeliveryStatus(EnumerationValue deliveryStatus)
    {
        setDeliveryStatus(getSession().getSessionContext(), deliveryStatus);
    }


    @ForceJALO(reason = "something else")
    public void setDeliveryStatus(SessionContext ctx, EnumerationValue deliveryStatus)
    {
        synchronized(getSyncObject())
        {
            super.setDeliveryStatus(ctx, deliveryStatus);
        }
    }


    @ForceJALO(reason = "something else")
    public Boolean isNet()
    {
        return isNet(getSession().getSessionContext());
    }


    @ForceJALO(reason = "something else")
    public void setNet(boolean net)
    {
        setNet(getSession().getSessionContext(), net);
    }


    @ForceJALO(reason = "something else")
    public void setNet(SessionContext ctx, boolean net)
    {
        synchronized(getSyncObject())
        {
            super.setNet(ctx, net);
            setChanged(true);
        }
    }


    @ForceJALO(reason = "something else")
    public void setDeliveryMode(DeliveryMode mode)
    {
        setDeliveryMode(getSession().getSessionContext(), mode);
    }


    @ForceJALO(reason = "something else")
    public void setDeliveryMode(SessionContext ctx, DeliveryMode mode)
    {
        synchronized(getSyncObject())
        {
            super.setDeliveryMode(ctx, mode);
            setChanged(false);
        }
    }


    @ForceJALO(reason = "something else")
    public void setDeliveryAddress(Address address)
    {
        setDeliveryAddress(getSession().getSessionContext(), address);
    }


    public Address createNewDeliveryAddress()
    {
        return createNewDeliveryAddress(getSession().getSessionContext());
    }


    public Address createNewDeliveryAddress(SessionContext ctx)
    {
        setChanged(false);
        try
        {
            Address result = UserManager.getInstance().createAddress(null, (getUser() == null) ? null : (Item)getUser(), true);
            super.setDeliveryAddress(ctx, result);
            return result;
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, "cannot create new delivery address", 0);
        }
    }


    @ForceJALO(reason = "something else")
    public void setDeliveryAddress(SessionContext ctx, Address address)
    {
        (new Object(this, "deliveryAddress", address))
                        .set(ctx);
    }


    protected void doSetDeliveryAddress(SessionContext ctx, Address newOne)
    {
        super.setDeliveryAddress(ctx, newOne);
        setChanged(false);
    }


    public double getDeliveryCosts()
    {
        return getDeliveryCosts(getSession().getSessionContext());
    }


    public void setDeliveryCosts(double deliveryCost)
    {
        setDeliveryCosts(getSession().getSessionContext(), deliveryCost);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public double getDeliveryCosts(SessionContext ctx)
    {
        return getDeliveryCostAsPrimitive(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDeliveryCosts(SessionContext ctx, double deliveryCost)
    {
        setDeliveryCost(ctx, deliveryCost);
    }


    @ForceJALO(reason = "something else")
    public void setDeliveryCost(SessionContext ctx, Double value)
    {
        synchronized(getSyncObject())
        {
            super.setDeliveryCost(ctx, value);
            setChanged(false);
        }
    }


    @ForceJALO(reason = "something else")
    public void setPaymentMode(PaymentMode mode)
    {
        setPaymentMode(getSession().getSessionContext(), mode);
    }


    @ForceJALO(reason = "something else")
    public void setPaymentMode(SessionContext ctx, PaymentMode mode)
    {
        synchronized(getSyncObject())
        {
            super.setPaymentMode(ctx, mode);
            setChanged(false);
        }
    }


    @ForceJALO(reason = "something else")
    public void setPaymentAddress(Address adr)
    {
        setPaymentAddress(getSession().getSessionContext(), adr);
    }


    public Address createNewPaymentAddress()
    {
        return createNewPaymentAddress(getSession().getSessionContext());
    }


    public Address createNewPaymentAddress(SessionContext ctx)
    {
        synchronized(getSyncObject())
        {
            setChanged(true);
            try
            {
                Address result = UserManager.getInstance().createAddress(null, (getUser() == null) ? null : (Item)getUser(), true);
                super.setPaymentAddress(ctx, result);
                return result;
            }
            catch(Exception e)
            {
                throw new JaloSystemException(e, "cannot create new payment address", 0);
            }
        }
    }


    @ForceJALO(reason = "something else")
    public void setPaymentAddress(SessionContext ctx, Address adr)
    {
        doSetPaymentAddress(ctx, adr);
    }


    protected void doSetPaymentAddress(SessionContext ctx, Address newOne)
    {
        super.setPaymentAddress(ctx, newOne);
        setChanged(false);
    }


    public double getPaymentCosts()
    {
        return getPaymentCosts(getSession().getSessionContext());
    }


    public void setPaymentCosts(double paymentCost)
    {
        setPaymentCosts(getSession().getSessionContext(), paymentCost);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public double getPaymentCosts(SessionContext ctx)
    {
        return getPaymentCostAsPrimitive(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setPaymentCosts(SessionContext ctx, double paymentCost)
    {
        setPaymentCost(ctx, paymentCost);
    }


    @ForceJALO(reason = "something else")
    public void setPaymentCost(SessionContext ctx, Double value)
    {
        super.setPaymentCost(ctx, value);
        setChanged(false);
    }


    @ForceJALO(reason = "something else")
    public Double getTotalTax()
    {
        return getTotalTax(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public double getTotal()
    {
        return getTotal(getSession().getSessionContext());
    }


    public void setTotal(double total)
    {
        setTotal(getSession().getSessionContext(), total);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public double getTotal(SessionContext ctx)
    {
        return getTotalPriceAsPrimitive(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setTotal(SessionContext ctx, double price)
    {
        setTotalPrice(ctx, price);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addTotalTaxValue(TaxValue taxValue)
    {
        addTotalTaxValue(getSession().getSessionContext(), taxValue);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addAllTotalTaxValues(Collection values)
    {
        addAllTotalTaxValues(getSession().getSessionContext(), values);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeTotalTaxValue(TaxValue taxValue)
    {
        removeTotalTaxValue(getSession().getSessionContext(), taxValue);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeAllTotalTaxValues()
    {
        removeAllTotalTaxValues(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getTotalTaxValues()
    {
        return getTotalTaxValues(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getTotalTaxValues(SessionContext ctx)
    {
        Collection result = convertAndGetTotalTaxValues(ctx);
        return (result == null) ? Collections.EMPTY_LIST : result;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addTotalTaxValue(SessionContext ctx, TaxValue taxValue)
    {
        Collection<TaxValue> ttv = convertAndGetTotalTaxValues(ctx);
        if(ttv == null)
        {
            ttv = new LinkedList();
        }
        ttv.add(taxValue);
        convertAndSetTotalTaxValues(ctx, ttv);
        setChanged(false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addAllTotalTaxValues(SessionContext ctx, Collection values)
    {
        Collection ttv = convertAndGetTotalTaxValues(ctx);
        if(ttv == null)
        {
            ttv = new LinkedList();
        }
        ttv.addAll(values);
        convertAndSetTotalTaxValues(ctx, ttv);
        setChanged(false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeTotalTaxValue(SessionContext ctx, TaxValue taxValue)
    {
        Collection ttv = convertAndGetTotalTaxValues(ctx);
        if(ttv != null)
        {
            ttv.remove(taxValue);
        }
        convertAndSetTotalTaxValues(ctx, ttv);
        setChanged(false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeAllTotalTaxValues(SessionContext ctx)
    {
        convertAndSetTotalTaxValues(ctx, new ArrayList());
        setChanged(false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setTotalTaxValues(Collection totalTaxValues)
    {
        setTotalTaxValues(getSession().getSessionContext(), totalTaxValues);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setTotalTaxValues(SessionContext ctx, Collection totalTaxValues)
    {
        removeAllTotalTaxValues(ctx);
        addAllTotalTaxValues(ctx, totalTaxValues);
    }


    private void convertAndSetTotalTaxValues(SessionContext ctx, Collection values)
    {
        String convertedValues = TaxValue.toString(values);
        setTotalTaxValuesInternal(ctx, convertedValues);
    }


    private Collection convertAndGetTotalTaxValues(SessionContext ctx)
    {
        String values = getTotalTaxValuesInternal(ctx);
        return TaxValue.parseTaxValueCollection(values);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addGlobalDiscountValue(DiscountValue discountValue)
    {
        addGlobalDiscountValue(getSession().getSessionContext(), discountValue);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addAllGlobalDiscountValues(Collection values)
    {
        addAllGlobalDiscountValues(getSession().getSessionContext(), values);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeGlobalDiscountValue(DiscountValue discountValue)
    {
        removeGlobalDiscountValue(getSession().getSessionContext(), discountValue);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeAllGlobalDiscountValues()
    {
        removeAllGlobalDiscountValues(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getGlobalDiscountValues()
    {
        return getGlobalDiscountValues(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getGlobalDiscountValues(SessionContext ctx)
    {
        List l = convertAndGetGlobalDiscountValues(ctx);
        return (l != null) ? l : Collections.EMPTY_LIST;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addGlobalDiscountValue(SessionContext ctx, DiscountValue discountValue)
    {
        List<DiscountValue> gdv = convertAndGetGlobalDiscountValues(ctx);
        if(gdv == null)
        {
            gdv = new LinkedList();
        }
        gdv.add(discountValue);
        convertAndSetGlobalDiscountValues(ctx, gdv);
        setChanged(false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addAllGlobalDiscountValues(SessionContext ctx, Collection values)
    {
        List gdv = convertAndGetGlobalDiscountValues(ctx);
        if(gdv == null)
        {
            gdv = new LinkedList();
        }
        gdv.addAll(values);
        convertAndSetGlobalDiscountValues(ctx, gdv);
        setChanged(false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeGlobalDiscountValue(SessionContext ctx, DiscountValue discountValue)
    {
        List<DiscountValue> gdv = convertAndGetGlobalDiscountValues(ctx);
        if(gdv != null)
        {
            if(Config.getBoolean("discount.value.use.old.equals", true))
            {
                gdv.remove(discountValue);
            }
            else
            {
                for(Iterator<DiscountValue> iterator = gdv.iterator(); iterator.hasNext(); )
                {
                    DiscountValue discount = iterator.next();
                    if(discount.equalsIgnoreAppliedValue(discountValue))
                    {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        convertAndSetGlobalDiscountValues(ctx, gdv);
        setChanged(false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeAllGlobalDiscountValues(SessionContext ctx)
    {
        convertAndSetGlobalDiscountValues(ctx, new LinkedList());
        setChanged(false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setGlobalDiscountValues(List globalDiscounts)
    {
        setGlobalDiscountValues(getSession().getSessionContext(), globalDiscounts);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setGlobalDiscountValues(SessionContext ctx, List globalDiscounts)
    {
        removeAllGlobalDiscountValues(ctx);
        addAllGlobalDiscountValues(ctx, globalDiscounts);
    }


    private void convertAndSetGlobalDiscountValues(SessionContext ctx, List values)
    {
        String newOne = DiscountValue.toString(values);
        setGlobalDiscountValuesInternal(ctx, newOne);
    }


    private List convertAndGetGlobalDiscountValues(SessionContext ctx)
    {
        String values = getGlobalDiscountValuesInternal(ctx);
        return (List)DiscountValue.parseDiscountValueCollection(values);
    }


    @ForceJALO(reason = "something else")
    public Boolean isCalculated()
    {
        return isCalculated(getSession().getSessionContext());
    }


    @ForceJALO(reason = "something else")
    public Boolean isCalculated(SessionContext ctx)
    {
        Boolean result = super.isCalculated(ctx);
        return (result == null) ? Boolean.FALSE : result;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean discountsIncludePaymentCosts()
    {
        return isDiscountsIncludePaymentCostAsPrimitive();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDiscountsIncludePaymentCosts(boolean includes)
    {
        setDiscountsIncludePaymentCosts(includes);
    }


    @ForceJALO(reason = "something else")
    public void setDiscountsIncludePaymentCost(SessionContext ctx, Boolean value)
    {
        super.setDiscountsIncludePaymentCost(ctx, value);
        setChanged(false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean discountsIncludeDeliveryCosts()
    {
        return isDiscountsIncludePaymentCostAsPrimitive();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDiscountsIncludeDeliveryCosts(boolean includes)
    {
        setDiscountsIncludeDeliveryCost(includes);
    }


    @ForceJALO(reason = "something else")
    public void setDiscountsIncludeDeliveryCost(SessionContext ctx, Boolean value)
    {
        super.setDiscountsIncludeDeliveryCost(ctx, value);
        setChanged(false);
    }


    protected void calculateEntries() throws JaloPriceFactoryException
    {
        double subtotal = 0.0D;
        for(AbstractOrderEntry e : getAllEntries())
        {
            e.calculate();
            subtotal += e.getTotalPrice().doubleValue();
        }
        setTotal(subtotal);
    }


    protected void recalculateEntries() throws JaloPriceFactoryException
    {
        synchronized(this)
        {
            double subtotal = 0.0D;
            for(AbstractOrderEntry e : getAllEntries())
            {
                e.recalculate();
                subtotal += e.getTotalPrice().doubleValue();
            }
            setTotal(subtotal);
        }
    }


    protected PriceValue findPaymentCosts() throws JaloPriceFactoryException
    {
        PaymentMode pamentMode = getPaymentMode();
        if(pamentMode != null)
        {
            try
            {
                return pamentMode.getCost(this);
            }
            catch(JaloPaymentModeException e)
            {
                throw new JaloPriceFactoryException(e, e.getErrorCode());
            }
        }
        return null;
    }


    protected PriceValue findDeliveryCosts() throws JaloPriceFactoryException
    {
        return OrderManager.getInstance().getDeliveryCostsStrategy().findDeliveryCosts(getSession().getSessionContext(), this);
    }


    protected List findGlobalDiscounts() throws JaloPriceFactoryException
    {
        List<?> priceFactoryDiscountValues = getSession().getOrderManager().getPriceFactory().getDiscountValues(this);
        List<DiscountValue> allValues = (priceFactoryDiscountValues == null) ? new ArrayList() : new ArrayList(priceFactoryDiscountValues);
        for(Iterator<Discount> it = getDiscounts().iterator(); it.hasNext(); )
        {
            Discount discount = it.next();
            DiscountValue discountValue = discount.getDiscountValue(this);
            if(discountValue != null)
            {
                allValues.add(discountValue);
            }
        }
        return allValues;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void calculateTotals(boolean recalculate) throws JaloPriceFactoryException
    {
        synchronized(getSyncObject())
        {
            calculateTotals(recalculate, calculateSubtotal(recalculate));
        }
    }


    protected void calculateTotals(boolean recalculate, Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap) throws JaloPriceFactoryException
    {
        if(recalculate || !isCalculated().booleanValue())
        {
            Currency curr = getCurrency();
            int digits = curr.getDigits().intValue();
            double subtotal = getSubtotal().doubleValue();
            double totalDiscounts = curr.round(calculateDiscountValues(recalculate));
            setTotalDiscounts(totalDiscounts);
            double total = curr.round(subtotal + getPaymentCosts() + getDeliveryCosts() - totalDiscounts);
            setTotal(total);
            double totalTaxes = curr.round(
                            calculateTotalTaxValues(recalculate, digits,
                                            getTaxCorrectionFactor(taxValueMap, subtotal, total), taxValueMap));
            setTotalTax(totalTaxes);
            setCalculated(true);
        }
    }


    private double getTaxCorrectionFactor(Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap, double subtotal, double total) throws JaloPriceFactoryException
    {
        double factor = (subtotal != 0.0D) ? (total / subtotal) : 1.0D;
        if(mustHandleTaxFreeEntries(taxValueMap, subtotal))
        {
            double taxFreeSubTotal = getTaxFreeSubTotal();
            double taxedTotal = total - taxFreeSubTotal;
            double taxedSubTotal = subtotal - taxFreeSubTotal;
            if(taxedSubTotal <= 0.0D)
            {
                throw new JaloPriceFactoryException("illegal taxed subtotal " + taxedSubTotal + ", must be > 0", -1);
            }
            if(taxedTotal <= 0.0D)
            {
                throw new JaloPriceFactoryException("illegal taxed total " + taxedTotal + ", must be > 0", -1);
            }
            factor = (taxedSubTotal != 0.0D) ? (taxedTotal / taxedSubTotal) : 1.0D;
        }
        return factor;
    }


    private boolean mustHandleTaxFreeEntries(Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap, double subtotal)
    {
        return (MapUtils.isNotEmpty(taxValueMap) &&
                        Config.getBoolean("abstractorder.taxFreeEntrySupport", false) &&
                        !isAllEntriesTaxed(taxValueMap, subtotal));
    }


    private double getTaxFreeSubTotal()
    {
        double sum = 0.0D;
        for(AbstractOrderEntry e : getEntries())
        {
            if(CollectionUtils.isEmpty(e.getTaxValues()))
            {
                sum += e.getTotalPrice().doubleValue();
            }
        }
        return sum;
    }


    private boolean isAllEntriesTaxed(Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap, double subtotal)
    {
        double sum = 0.0D;
        Set<Set<TaxValue>> consumedTaxGroups = new HashSet<>();
        for(Map.Entry<TaxValue, Map<Set<TaxValue>, Double>> taxEntry : taxValueMap.entrySet())
        {
            for(Map.Entry<Set<TaxValue>, Double> taxGroupEntry : (Iterable<Map.Entry<Set<TaxValue>, Double>>)((Map)taxEntry.getValue()).entrySet())
            {
                if(consumedTaxGroups.add(taxGroupEntry.getKey()))
                {
                    sum += ((Double)taxGroupEntry.getValue()).doubleValue();
                }
            }
        }
        return (Math.abs(subtotal - sum) <= getAllowedDoubleDelta());
    }


    private double getAllowedDoubleDelta()
    {
        return Math.pow(10.0D, (-1 * (getCurrency().getDigits().intValue() + 1)));
    }


    @ForceJALO(reason = "something else")
    public Double getTotalDiscounts()
    {
        return getTotalDiscounts(getSession().getSessionContext());
    }


    protected Map<TaxValue, Map<Set<TaxValue>, Double>> calculateSubtotal(boolean recalculate)
    {
        if(recalculate || !isCalculated().booleanValue())
        {
            double subtotal = 0.0D;
            List<AbstractOrderEntry> entries = getAllEntries();
            Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap = new LinkedHashMap<>(entries.size() * 2);
            for(AbstractOrderEntry entry : entries)
            {
                entry.calculateTotals(recalculate);
                double entryTotal = entry.getTotalPriceAsPrimitive();
                subtotal += entryTotal;
                Collection<TaxValue> allTaxValues = entry.getTaxValues();
                Set<TaxValue> relativeTaxGroupKey = getUnappliedRelativeTaxValues(allTaxValues);
                for(TaxValue taxValue : allTaxValues)
                {
                    if(taxValue.isAbsolute())
                    {
                        addAbsoluteEntryTaxValue(entry.getQuantityAsPrimitive(), taxValue.unapply(), taxValueMap);
                        continue;
                    }
                    addRelativeEntryTaxValue(entryTotal, taxValue.unapply(), relativeTaxGroupKey, taxValueMap);
                }
            }
            setSubtotal(getCurrency().round(subtotal));
            return taxValueMap;
        }
        return Collections.EMPTY_MAP;
    }


    protected void addRelativeEntryTaxValue(double entryTotal, TaxValue taxValue, Set<TaxValue> relativeEntryTaxValues, Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap)
    {
        Double relativeTaxTotalSum = null;
        Map<Set<TaxValue>, Double> taxTotalsMap = taxValueMap.get(taxValue);
        if(taxTotalsMap != null)
        {
            relativeTaxTotalSum = taxTotalsMap.get(relativeEntryTaxValues);
        }
        else
        {
            taxTotalsMap = new LinkedHashMap<>();
            taxValueMap.put(taxValue, taxTotalsMap);
        }
        taxTotalsMap.put(relativeEntryTaxValues,
                        Double.valueOf(((relativeTaxTotalSum != null) ? relativeTaxTotalSum.doubleValue() : 0.0D) + entryTotal));
    }


    protected void addAbsoluteEntryTaxValue(long entryQuantity, TaxValue taxValue, Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap)
    {
        Map<Set<TaxValue>, Double> taxGroupMap = taxValueMap.get(taxValue);
        Double quantitySum = null;
        Set<TaxValue> absoluteTaxGroupKey = Collections.singleton(taxValue);
        if(taxGroupMap == null)
        {
            taxGroupMap = new LinkedHashMap<>(4);
            taxValueMap.put(taxValue, taxGroupMap);
        }
        else
        {
            quantitySum = taxGroupMap.get(absoluteTaxGroupKey);
        }
        taxGroupMap.put(absoluteTaxGroupKey,
                        Double.valueOf(((quantitySum != null) ? quantitySum.doubleValue() : 0.0D) + entryQuantity));
    }


    private Set<TaxValue> getUnappliedRelativeTaxValues(Collection<TaxValue> allTaxValues)
    {
        if(CollectionUtils.isNotEmpty(allTaxValues))
        {
            Set<TaxValue> ret = new LinkedHashSet<>(allTaxValues.size());
            for(TaxValue appliedTv : allTaxValues)
            {
                if(!appliedTv.isAbsolute())
                {
                    ret.add(appliedTv.unapply());
                }
            }
            return ret;
        }
        return Collections.EMPTY_SET;
    }


    protected double calculateDiscountValues(boolean recalculate)
    {
        if(recalculate || !isCalculated().booleanValue())
        {
            List discountValues = getGlobalDiscountValues();
            if(discountValues != null && !discountValues.isEmpty())
            {
                removeAllGlobalDiscountValues();
                String iso = getCurrency().getIsoCode();
                Currency curr = getCurrency();
                int digits = curr.getDigits().intValue();
                double discountablePrice = getSubtotal().doubleValue() + (discountsIncludeDeliveryCosts() ? getDeliveryCosts() : 0.0D) + (discountsIncludePaymentCosts() ? getPaymentCosts() : 0.0D);
                List appliedDiscounts = DiscountValue.apply(1.0D, discountablePrice, digits,
                                convertDiscountValues(discountValues), iso);
                addAllGlobalDiscountValues(appliedDiscounts);
                return DiscountValue.sumAppliedValues(appliedDiscounts);
            }
            return 0.0D;
        }
        return DiscountValue.sumAppliedValues(getGlobalDiscountValues());
    }


    protected List convertDiscountValues(List<?> dvs)
    {
        if(dvs == null)
        {
            return null;
        }
        if(dvs.isEmpty())
        {
            return dvs;
        }
        String iso = getCurrency().getIsoCode();
        Currency curr = getCurrency();
        List<DiscountValue> tmp = new ArrayList(dvs);
        Map<Object, Object> currencyMap = new HashMap<>();
        for(int i = 0; i < tmp.size(); i++)
        {
            DiscountValue discountValue = tmp.get(i);
            if(discountValue.isAbsolute() && !iso.equals(discountValue.getCurrencyIsoCode()))
            {
                Currency dCurr = (Currency)currencyMap.get(discountValue.getCurrencyIsoCode());
                if(dCurr == null)
                {
                    currencyMap.put(discountValue.getCurrencyIsoCode(),
                                    dCurr = getSession().getC2LManager().getCurrencyByIsoCode(discountValue.getCurrencyIsoCode()));
                }
                tmp.set(i, new DiscountValue(discountValue.getCode(), dCurr.convertAndRound(curr, discountValue.getValue()), true, iso));
            }
        }
        return tmp;
    }


    protected double calculateTotalTaxValues(boolean recalculate, int digits, double taxAdjustmentFactor, Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap)
    {
        if(recalculate || !isCalculated().booleanValue())
        {
            Currency curr = getCurrency();
            String iso = curr.getIsoCode();
            removeAllTotalTaxValues();
            boolean net = isNet().booleanValue();
            double totalTaxes = 0.0D;
            if(MapUtils.isNotEmpty(taxValueMap))
            {
                for(Map.Entry<TaxValue, Map<Set<TaxValue>, Double>> taxValueEntry : taxValueMap.entrySet())
                {
                    TaxValue appliedTaxValue, unappliedTaxValue = taxValueEntry.getKey();
                    Map<Set<TaxValue>, Double> taxGroups = taxValueEntry.getValue();
                    if(unappliedTaxValue.isAbsolute())
                    {
                        double quantitySum = ((Double)((Map.Entry)taxGroups.entrySet().iterator().next()).getValue()).doubleValue();
                        appliedTaxValue = calculateAbsoluteTotalTaxValue(curr, iso, digits, net, unappliedTaxValue, quantitySum);
                    }
                    else if(net)
                    {
                        appliedTaxValue = applyNetMixedRate(unappliedTaxValue, taxGroups, digits, taxAdjustmentFactor);
                    }
                    else
                    {
                        appliedTaxValue = applyGrossMixedRate(unappliedTaxValue, taxGroups, digits, taxAdjustmentFactor);
                    }
                    totalTaxes += appliedTaxValue.getAppliedValue();
                    addTotalTaxValue(appliedTaxValue);
                }
            }
            return totalTaxes;
        }
        return getTotalTax().doubleValue();
    }


    protected TaxValue calculateAbsoluteTotalTaxValue(Currency curr, String currencyIso, int digits, boolean net, TaxValue taxValue, double cumulatedEntryQuantities)
    {
        String taxValueIsoCode = taxValue.getCurrencyIsoCode();
        if(taxValueIsoCode != null && !currencyIso.equalsIgnoreCase(taxValueIsoCode))
        {
            Currency taxCurrency = getSession().getC2LManager().getCurrencyByIsoCode(taxValueIsoCode);
            taxValue = new TaxValue(taxValue.getCode(), taxCurrency.convertAndRound(curr, taxValue.getValue()), true, 0.0D, currencyIso);
        }
        return taxValue.apply(cumulatedEntryQuantities, 0.0D, digits, net, currencyIso);
    }


    private TaxValue applyGrossMixedRate(TaxValue unappliedTaxValue, Map<Set<TaxValue>, Double> taxGroups, int digits, double taxAdjustmentFactor)
    {
        if(unappliedTaxValue.isAbsolute())
        {
            throw new IllegalStateException("AbstractOrder.applyGrossMixedRate(..) cannot be called for absolute tax value!");
        }
        double singleTaxRate = unappliedTaxValue.getValue();
        double appliedTaxValueNotRounded = 0.0D;
        for(Map.Entry<Set<TaxValue>, Double> taxGroupEntry : taxGroups.entrySet())
        {
            double groupTaxesRate = TaxValue.sumRelativeTaxValues(taxGroupEntry.getKey());
            double taxGroupPrice = ((Double)taxGroupEntry.getValue()).doubleValue();
            appliedTaxValueNotRounded += taxGroupPrice * singleTaxRate / (100.0D + groupTaxesRate);
        }
        appliedTaxValueNotRounded *= taxAdjustmentFactor;
        return new TaxValue(unappliedTaxValue
                        .getCode(), unappliedTaxValue
                        .getValue(), false,
                        CoreAlgorithms.round(appliedTaxValueNotRounded, Math.max(digits, 0)), null);
    }


    private TaxValue applyNetMixedRate(TaxValue unappliedTaxValue, Map<Set<TaxValue>, Double> taxGroups, int digits, double taxAdjustmentFactor)
    {
        if(unappliedTaxValue.isAbsolute())
        {
            throw new IllegalStateException("cannot applyGrossMixedRate(..) cannot be called on absolute tax value!");
        }
        double entriesTotalPrice = 0.0D;
        for(Map.Entry<Set<TaxValue>, Double> taxGroupEntry : taxGroups.entrySet())
        {
            entriesTotalPrice += ((Double)taxGroupEntry.getValue()).doubleValue();
        }
        return unappliedTaxValue.apply(1.0D, entriesTotalPrice * taxAdjustmentFactor, digits, true, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void calculate() throws JaloPriceFactoryException
    {
        if(!isCalculated().booleanValue())
        {
            synchronized(getSyncObject())
            {
                calculateEntries();
                Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap = resetAllValues();
                calculateTotals(false, taxValueMap);
                notifyDiscountsAboutCalculation();
            }
        }
    }


    protected void notifyDiscountsAboutCalculation()
    {
        for(Iterator<Discount> it = getDiscounts().iterator(); it.hasNext(); )
        {
            Discount discount = it.next();
            discount.notifyOrderCalculated(this);
        }
    }


    protected void notifyDiscountsAboutRemoval()
    {
        for(Iterator<Discount> it = getDiscounts().iterator(); it.hasNext(); )
        {
            Discount discount = it.next();
            discount.notifyOrderRemoval(this);
        }
    }


    protected Map resetAllValues() throws JaloPriceFactoryException
    {
        synchronized(getSyncObject())
        {
            Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap = calculateSubtotal(false);
            Collection<TaxValue> relativeTaxValues = new LinkedList<>();
            for(Map.Entry<TaxValue, ?> e : taxValueMap.entrySet())
            {
                TaxValue taxValue = e.getKey();
                if(!taxValue.isAbsolute())
                {
                    relativeTaxValues.add(taxValue);
                }
            }
            boolean setAdditionalCostsBeforeDiscounts = Config.getBoolean("ordercalculation.reset.additionalcosts.before.discounts", true);
            if(setAdditionalCostsBeforeDiscounts)
            {
                resetAdditionalCosts(relativeTaxValues);
            }
            removeAllGlobalDiscountValues();
            addAllGlobalDiscountValues(findGlobalDiscounts());
            if(!setAdditionalCostsBeforeDiscounts)
            {
                resetAdditionalCosts(relativeTaxValues);
            }
            return taxValueMap;
        }
    }


    protected void resetAdditionalCosts(Collection<TaxValue> relativeTaxValues) throws JaloPriceFactoryException
    {
        PriceValue deliCost = findDeliveryCosts();
        setDeliveryCosts((deliCost == null) ? 0.0D : JaloTools.convertPriceIfNecessary(
                        deliCost, isNet().booleanValue(), getCurrency(), relativeTaxValues).getValue());
        PriceValue payCost = findPaymentCosts();
        setPaymentCosts((payCost == null) ? 0.0D : JaloTools.convertPriceIfNecessary(
                        payCost, isNet().booleanValue(), getCurrency(), relativeTaxValues).getValue());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void calculate(Date date) throws JaloPriceFactoryException
    {
        synchronized(getSyncObject())
        {
            Date old = getDate();
            setDate(date);
            try
            {
                calculate();
            }
            finally
            {
                setDate(old);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void recalculate() throws JaloPriceFactoryException
    {
        synchronized(getSyncObject())
        {
            recalculateEntries();
            Map<TaxValue, Map<Set<TaxValue>, Double>> taxValueMap = resetAllValues();
            calculateTotals(true, taxValueMap);
            notifyDiscountsAboutCalculation();
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void recalculate(Date date) throws JaloPriceFactoryException
    {
        synchronized(getSyncObject())
        {
            Date old = getDate();
            setDate(date);
            try
            {
                recalculate();
            }
            finally
            {
                setDate(old);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void addDiscount(Discount discount)
    {
        addToDiscounts(discount);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void removeDiscount(Discount discount)
    {
        removeFromDiscounts(discount);
    }


    protected void setChanged(boolean entriesToo)
    {
        if(Boolean.TRUE.equals(getSession().getAttribute("save.from.service.layer")))
        {
            return;
        }
        setCalculated(false);
        if(entriesToo)
        {
            for(AbstractOrderEntry aoe : getAllEntries())
            {
                aoe.setCalculated(false);
            }
        }
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        notifyDiscountsAboutRemoval();
        List<AbstractOrderEntry> entryList = new ArrayList<>(getEntries());
        for(ListIterator<AbstractOrderEntry> i = entryList.listIterator(entryList.size()); i.hasPrevious(); )
        {
            ((AbstractOrderEntry)i.previous()).remove();
        }
        super.remove(ctx);
    }


    @ForceJALO(reason = "something else")
    public void setEntries(List<AbstractOrderEntry> value)
    {
        setEntries(getSession().getSessionContext(), value);
    }


    @ForceJALO(reason = "something else")
    public void setEntries(SessionContext ctx, List<AbstractOrderEntry> value)
    {
        setAllEntries(ctx, value);
    }


    protected abstract String getAbstractOrderEntryTypeCode();


    protected abstract AbstractOrderEntry createNewEntry(SessionContext paramSessionContext, ComposedType paramComposedType, Product paramProduct, long paramLong, Unit paramUnit, int paramInt);
}
