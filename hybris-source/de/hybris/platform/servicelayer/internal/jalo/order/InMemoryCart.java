package de.hybris.platform.servicelayer.internal.jalo.order;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloOnlyItem;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.order.payment.PaymentMode;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InMemoryCart extends GeneratedInMemoryCart implements JaloOnlyItem
{
    private JaloOnlyItemHelper data;
    private final List<InMemoryCartEntry> entries = new ArrayList<>();


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("user", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("currency", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + " to create a cart ", 0);
        }
        Class<InMemoryCart> cl = type.getJaloClass();
        try
        {
            InMemoryCart newOne = cl.newInstance();
            newOne.setTenant(type.getTenant());
            newOne
                            .data = new JaloOnlyItemHelper((PK)allAttributes.get(PK), (Item)newOne, type, new Date(), null);
            return (Item)newOne;
        }
        catch(ClassCastException e)
        {
            throw new JaloGenericCreationException("could not instantiate wizard class " + cl + " of type " + type
                            .getCode() + " : " + e, 0);
        }
        catch(InstantiationException e)
        {
            throw new JaloGenericCreationException("could not instantiate wizard class " + cl + " of type " + type
                            .getCode() + " : " + e, 0);
        }
        catch(IllegalAccessException e)
        {
            throw new JaloGenericCreationException("could not instantiate wizard class " + cl + " of type " + type
                            .getCode() + " : " + e, 0);
        }
    }


    @ForceJALO(reason = "something else")
    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap ret = new Item.ItemAttributeMap((Map)allAttributes);
        ret.remove(Item.PK);
        ret.remove(Item.TYPE);
        ret.remove("itemtype");
        return ret;
    }


    public final ComposedType provideComposedType()
    {
        return this.data.provideComposedType();
    }


    public final Date provideCreationTime()
    {
        return this.data.provideCreationTime();
    }


    public final Date provideModificationTime()
    {
        return this.data.provideModificationTime();
    }


    public final PK providePK()
    {
        return this.data.providePK();
    }


    public void removeJaloOnly() throws ConsistencyCheckException
    {
        this.data.removeJaloOnly();
    }


    public Object doGetAttribute(SessionContext ctx, String attrQualifier) throws JaloInvalidParameterException, JaloSecurityException
    {
        return this.data.doGetAttribute(ctx, attrQualifier);
    }


    public void doSetAttribute(SessionContext ctx, String attrQualifier, Object value) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        this.data.doSetAttribute(ctx, attrQualifier, value);
    }


    @ForceJALO(reason = "something else")
    public Collection getEntries(int startIdx, int endIdx)
    {
        List<InMemoryCartEntry> ret = new ArrayList(this.entries.size());
        for(InMemoryCartEntry e : this.entries)
        {
            if(e.getEntryNumber().intValue() >= startIdx && e.getEntryNumber().intValue() <= endIdx)
            {
                ret.add(e);
            }
        }
        return ret;
    }


    @ForceJALO(reason = "something else")
    public AbstractOrderEntry getEntry(int index) throws JaloItemNotFoundException
    {
        for(InMemoryCartEntry e : this.entries)
        {
            if(e.getEntryNumber().intValue() == index)
            {
                return (AbstractOrderEntry)e;
            }
        }
        throw new JaloItemNotFoundException("no entry for position " + index, 0);
    }


    @ForceJALO(reason = "something else")
    public List getEntriesByProduct(Product product)
    {
        List<InMemoryCartEntry> ret = new ArrayList(this.entries.size());
        for(InMemoryCartEntry e : this.entries)
        {
            if(product.equals(e.getProduct()))
            {
                ret.add(e);
            }
        }
        return ret;
    }


    public void removeAllEntries()
    {
        removeEntries(getSession().getSessionContext(), new HashSet<>(getAllEntries()));
    }


    protected void removeEntries(SessionContext ctx, Set<AbstractOrderEntry> entries)
    {
        if(entries != null)
        {
            this.entries.removeAll(entries);
        }
        super.removeEntries(ctx, entries);
    }


    public void removeEntry(AbstractOrderEntry entry)
    {
        this.entries.remove(entry);
        super.removeEntry(entry);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "something else")
    public List getAllEntries()
    {
        return Collections.unmodifiableList(this.entries);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "something else")
    protected void setAllEntries(SessionContext ctx, List<? extends InMemoryCartEntry> entries)
    {
        super.setAllEntries(ctx, entries);
        this.entries.clear();
        if(entries != null)
        {
            this.entries.addAll(entries);
        }
    }


    @ForceJALO(reason = "something else")
    public List getEntries(SessionContext ctx)
    {
        return Collections.unmodifiableList(this.entries);
    }


    @ForceJALO(reason = "something else")
    public void setEntries(SessionContext ctx, List<AbstractOrderEntry> value)
    {
        setAllEntries(ctx, value);
    }


    public AbstractOrderEntry addNewEntry(Product prod, long qtd, Unit unit, int position, boolean addToPresent)
    {
        InMemoryCartEntry newOne = (InMemoryCartEntry)super.addNewEntry(prod, qtd, unit, position, addToPresent);
        if(!this.entries.contains(newOne))
        {
            if(!this.entries.contains(newOne))
            {
                this.entries.add(newOne);
                Collections.sort(this.entries, ENTRY_COMP);
            }
        }
        return (AbstractOrderEntry)newOne;
    }


    private static final Comparator<InMemoryCartEntry> ENTRY_COMP = (Comparator<InMemoryCartEntry>)new Object();


    @ForceJALO(reason = "something else")
    public String getCode(SessionContext ctx)
    {
        return (String)this.data.getProperty(ctx, "code");
    }


    @ForceJALO(reason = "something else")
    public void setCode(SessionContext ctx, String code)
    {
        this.data.setProperty(ctx, "code", code);
    }


    @ForceJALO(reason = "something else")
    public void setDate(SessionContext ctx, Date date)
    {
        setCreationTime(date);
        setChanged(true);
    }


    @SLDSafe
    protected void setCreationTime(Date creationTime)
    {
        this.data.setCreationTime(creationTime);
    }


    @ForceJALO(reason = "something else")
    public User getUser(SessionContext ctx)
    {
        return (User)this.data.getProperty(ctx, "user");
    }


    @ForceJALO(reason = "something else")
    public void setUser(SessionContext ctx, User user)
    {
        this.data.setProperty(ctx, "user", user);
        setChanged(true);
    }


    @ForceJALO(reason = "something else")
    public Currency getCurrency(SessionContext ctx)
    {
        return (Currency)this.data.getProperty(ctx, "currency");
    }


    @ForceJALO(reason = "something else")
    public void setCurrency(SessionContext ctx, Currency curr)
    {
        this.data.setProperty(ctx, "currency", curr);
        setChanged(true);
    }


    @ForceJALO(reason = "something else")
    public EnumerationValue getPaymentStatus(SessionContext ctx)
    {
        return (EnumerationValue)this.data.getProperty(ctx, "paymentStatus");
    }


    @ForceJALO(reason = "something else")
    public void setPaymentStatus(SessionContext ctx, EnumerationValue ps)
    {
        this.data.setProperty(ctx, "paymentStatus", ps);
    }


    @ForceJALO(reason = "something else")
    public EnumerationValue getDeliveryStatus(SessionContext ctx)
    {
        return (EnumerationValue)this.data.getProperty(ctx, "deliveryStatus");
    }


    @ForceJALO(reason = "something else")
    public void setDeliveryStatus(SessionContext ctx, EnumerationValue ds)
    {
        this.data.setProperty(ctx, "deliveryStatus", ds);
    }


    @ForceJALO(reason = "something else")
    public Boolean isNet(SessionContext ctx)
    {
        return Boolean.valueOf(this.data.getPropertyBoolean(ctx, "net", false));
    }


    @ForceJALO(reason = "something else")
    public void setNet(SessionContext ctx, boolean net)
    {
        this.data.setProperty(ctx, "net", Boolean.valueOf(net));
        setChanged(true);
    }


    @ForceJALO(reason = "something else")
    public DeliveryMode getDeliveryMode(SessionContext ctx)
    {
        return (DeliveryMode)this.data.getProperty(ctx, "deliveryMode");
    }


    @ForceJALO(reason = "something else")
    public void setDeliveryMode(SessionContext ctx, DeliveryMode mode)
    {
        this.data.setProperty(ctx, "deliveryMode", mode);
        setChanged(false);
    }


    @ForceJALO(reason = "something else")
    public Address getDeliveryAddress(SessionContext ctx)
    {
        return (Address)this.data.getProperty(ctx, "deliveryAddress");
    }


    @ForceJALO(reason = "something else")
    public void setDeliveryAddress(SessionContext ctx, Address address)
    {
        this.data.setProperty(ctx, "deliveryAddress", address);
        setChanged(false);
    }


    @ForceJALO(reason = "something else")
    public PaymentMode getPaymentMode(SessionContext ctx)
    {
        return (PaymentMode)this.data.getProperty(ctx, "paymentMode");
    }


    @ForceJALO(reason = "something else")
    public void setPaymentMode(SessionContext ctx, PaymentMode mode)
    {
        this.data.setProperty(ctx, "paymentMode", mode);
        setChanged(false);
    }


    @ForceJALO(reason = "something else")
    public Address getPaymentAddress(SessionContext ctx)
    {
        return (Address)this.data.getProperty(ctx, "paymentAddress");
    }


    @ForceJALO(reason = "something else")
    public void setPaymentAddress(SessionContext ctx, Address adr)
    {
        this.data.setProperty(ctx, "paymentAddress", adr);
        setChanged(false);
    }


    @ForceJALO(reason = "something else")
    public PaymentInfo getPaymentInfo(SessionContext ctx)
    {
        return (PaymentInfo)this.data.getProperty(ctx, "paymentInfo");
    }


    @ForceJALO(reason = "something else")
    public void setPaymentInfo(SessionContext ctx, PaymentInfo info)
    {
        this.data.setProperty(ctx, "paymentInfo", info);
    }


    @ForceJALO(reason = "something else")
    public void setPaymentCosts(SessionContext ctx, double paymentCost)
    {
        this.data.setProperty(ctx, "paymentCost", Double.valueOf(paymentCost));
        setChanged(false);
    }


    @ForceJALO(reason = "something else")
    public double getPaymentCosts(SessionContext ctx)
    {
        return this.data.getPropertyDouble(ctx, "paymentCost", 0.0D);
    }


    @ForceJALO(reason = "something else")
    public void setStatusInfo(SessionContext ctx, String s)
    {
        this.data.setProperty(ctx, "statusInfo", s);
    }


    @ForceJALO(reason = "something else")
    public String getStatusInfo(SessionContext ctx)
    {
        return (String)this.data.getProperty(ctx, "statusInfo");
    }


    @ForceJALO(reason = "something else")
    public void setStatus(SessionContext ctx, EnumerationValue s)
    {
        this.data.setProperty(ctx, "status", s);
    }


    @ForceJALO(reason = "something else")
    public EnumerationValue getStatus(SessionContext ctx)
    {
        return (EnumerationValue)this.data.getProperty(ctx, "status");
    }


    @ForceJALO(reason = "something else")
    public Boolean isCalculated(SessionContext ctx)
    {
        return Boolean.valueOf(this.data.getPropertyBoolean(ctx, "calculated", false));
    }


    @ForceJALO(reason = "something else")
    public void setCalculated(SessionContext ctx, boolean calculated)
    {
        this.data.setProperty(ctx, "calculated", Boolean.valueOf(calculated));
    }


    @ForceJALO(reason = "something else")
    public String getTotalTaxValuesInternal(SessionContext ctx)
    {
        return (String)this.data.getProperty(ctx, "totalTaxValuesInternal");
    }


    @ForceJALO(reason = "something else")
    public void setTotalTaxValuesInternal(SessionContext ctx, String value)
    {
        this.data.setProperty(ctx, "totalTaxValuesInternal", value);
    }


    @ForceJALO(reason = "something else")
    public String getGlobalDiscountValuesInternal(SessionContext ctx)
    {
        return (String)this.data.getProperty(ctx, "globalDiscountValuesInternal");
    }


    @ForceJALO(reason = "something else")
    public void setGlobalDiscountValuesInternal(SessionContext ctx, String value)
    {
        this.data.setProperty(ctx, "globalDiscountValuesInternal", value);
    }


    @ForceJALO(reason = "something else")
    public void setDeliveryCosts(SessionContext ctx, double deliveryCost)
    {
        this.data.setProperty(ctx, "deliveryCost", Double.valueOf(deliveryCost));
        setChanged(false);
    }


    @ForceJALO(reason = "something else")
    public double getDeliveryCosts(SessionContext ctx)
    {
        return this.data.getPropertyDouble(ctx, "deliveryCost", 0.0D);
    }


    @ForceJALO(reason = "something else")
    public void setTotal(SessionContext ctx, double price)
    {
        this.data.setProperty(ctx, "totalPrice", Double.valueOf(price));
    }


    @ForceJALO(reason = "something else")
    public double getTotal(SessionContext ctx)
    {
        return this.data.getPropertyDouble(ctx, "totalPrice", 0.0D);
    }


    @ForceJALO(reason = "something else")
    public void setSubtotal(SessionContext ctx, double price)
    {
        this.data.setProperty(ctx, "subtotal", Double.valueOf(price));
    }


    @ForceJALO(reason = "something else")
    public Double getSubtotal(SessionContext ctx)
    {
        return Double.valueOf(this.data.getPropertyDouble(ctx, "subtotal", 0.0D));
    }


    @ForceJALO(reason = "something else")
    public void setTotalDiscounts(SessionContext ctx, double totalDiscounts)
    {
        this.data.setProperty(ctx, "totalDiscounts", Double.valueOf(totalDiscounts));
    }


    @ForceJALO(reason = "something else")
    public Double getTotalDiscounts(SessionContext ctx)
    {
        return Double.valueOf(this.data.getPropertyDouble(ctx, "totalDiscounts", 0.0D));
    }


    @ForceJALO(reason = "something else")
    public void setTotalTax(SessionContext ctx, double taxes)
    {
        this.data.setProperty(ctx, "totalTax", Double.valueOf(taxes));
    }


    @ForceJALO(reason = "something else")
    public Double getTotalTax(SessionContext ctx)
    {
        return Double.valueOf(this.data.getPropertyDouble(ctx, "totalTax", 0.0D));
    }


    @ForceJALO(reason = "something else")
    public void setDiscountsIncludeDeliveryCost(SessionContext ctx, Boolean value)
    {
        this.data.setProperty(getSession().getSessionContext(), "discountsIncludeDeliveryCost", value);
        setChanged(false);
    }


    @ForceJALO(reason = "something else")
    public Boolean isDiscountsIncludeDeliveryCost(SessionContext ctx)
    {
        return Boolean.valueOf(this.data.getPropertyBoolean(getSession().getSessionContext(), "discountsIncludeDeliveryCost", false));
    }


    @ForceJALO(reason = "something else")
    public void setDiscountsIncludePaymentCost(SessionContext ctx, Boolean value)
    {
        this.data.setProperty(getSession().getSessionContext(), "discountsIncludePaymentCost", value);
        setChanged(false);
    }


    @ForceJALO(reason = "something else")
    public Boolean isDiscountsIncludePaymentCost(SessionContext ctx)
    {
        return Boolean.valueOf(this.data.getPropertyBoolean(getSession().getSessionContext(), "discountsIncludePaymentCost", false));
    }


    public void setModificationTime(Date d)
    {
        this.data.markModified(d);
    }


    public InMemoryCartEntry createNewEntry(SessionContext ctx, ComposedType entryType, Product p, long amount, Unit u, int pos)
    {
        ComposedType t = (entryType != null) ? entryType : TypeManager.getInstance().getComposedType(GeneratedCoreConstants.TC.INMEMORYCARTENTRY);
        Map<String, Object> values = new HashMap<>();
        values.put("order", this);
        values.put("product", p);
        values.put("unit", u);
        values.put("quantity", Long.valueOf(amount));
        values.put("entryNumber", Integer.valueOf(pos));
        try
        {
            return (InMemoryCartEntry)t.newInstance(ctx, values);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @ForceJALO(reason = "something else")
    protected String getAbstractOrderEntryTypeCode()
    {
        return GeneratedCoreConstants.TC.INMEMORYCARTENTRY;
    }
}
