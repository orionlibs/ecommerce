package de.hybris.platform.jalo.order;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.payment.PaymentInfo;
import de.hybris.platform.jalo.order.payment.PaymentMode;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAbstractOrder extends GenericItem
{
    public static final String CALCULATED = "calculated";
    public static final String CODE = "code";
    public static final String CURRENCY = "currency";
    public static final String DATE = "date";
    public static final String DELIVERYADDRESS = "deliveryAddress";
    public static final String DELIVERYCOST = "deliveryCost";
    public static final String DELIVERYMODE = "deliveryMode";
    public static final String DELIVERYSTATUS = "deliveryStatus";
    public static final String DESCRIPTION = "description";
    public static final String EXPIRATIONTIME = "expirationTime";
    public static final String GLOBALDISCOUNTVALUESINTERNAL = "globalDiscountValuesInternal";
    public static final String NAME = "name";
    public static final String NET = "net";
    public static final String PAYMENTADDRESS = "paymentAddress";
    public static final String PAYMENTCOST = "paymentCost";
    public static final String PAYMENTINFO = "paymentInfo";
    public static final String PAYMENTMODE = "paymentMode";
    public static final String PAYMENTSTATUS = "paymentStatus";
    public static final String STATUS = "status";
    public static final String EXPORTSTATUS = "exportStatus";
    public static final String STATUSINFO = "statusInfo";
    public static final String TOTALPRICE = "totalPrice";
    public static final String TOTALDISCOUNTS = "totalDiscounts";
    public static final String TOTALTAX = "totalTax";
    public static final String TOTALTAXVALUESINTERNAL = "totalTaxValuesInternal";
    public static final String USER = "user";
    public static final String SUBTOTAL = "subtotal";
    public static final String DISCOUNTSINCLUDEDELIVERYCOST = "discountsIncludeDeliveryCost";
    public static final String DISCOUNTSINCLUDEPAYMENTCOST = "discountsIncludePaymentCost";
    public static final String ENTRYGROUPS = "entryGroups";
    public static final String ENTRIES = "entries";
    public static final String DISCOUNTS = "discounts";
    protected static String ORDERDISCOUNTRELATION_SRC_ORDERED = "relation.OrderDiscountRelation.source.ordered";
    protected static String ORDERDISCOUNTRELATION_TGT_ORDERED = "relation.OrderDiscountRelation.target.ordered";
    protected static String ORDERDISCOUNTRELATION_MARKMODIFIED = "relation.OrderDiscountRelation.markmodified";
    protected static final OneToManyHandler<AbstractOrderEntry> ENTRIESHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.ABSTRACTORDERENTRY, true, "order", "entryNumber", false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("calculated", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("date", Item.AttributeMode.INITIAL);
        tmp.put("deliveryAddress", Item.AttributeMode.INITIAL);
        tmp.put("deliveryCost", Item.AttributeMode.INITIAL);
        tmp.put("deliveryMode", Item.AttributeMode.INITIAL);
        tmp.put("deliveryStatus", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("expirationTime", Item.AttributeMode.INITIAL);
        tmp.put("globalDiscountValuesInternal", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("net", Item.AttributeMode.INITIAL);
        tmp.put("paymentAddress", Item.AttributeMode.INITIAL);
        tmp.put("paymentCost", Item.AttributeMode.INITIAL);
        tmp.put("paymentInfo", Item.AttributeMode.INITIAL);
        tmp.put("paymentMode", Item.AttributeMode.INITIAL);
        tmp.put("paymentStatus", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("exportStatus", Item.AttributeMode.INITIAL);
        tmp.put("statusInfo", Item.AttributeMode.INITIAL);
        tmp.put("totalPrice", Item.AttributeMode.INITIAL);
        tmp.put("totalDiscounts", Item.AttributeMode.INITIAL);
        tmp.put("totalTax", Item.AttributeMode.INITIAL);
        tmp.put("totalTaxValuesInternal", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        tmp.put("subtotal", Item.AttributeMode.INITIAL);
        tmp.put("discountsIncludeDeliveryCost", Item.AttributeMode.INITIAL);
        tmp.put("discountsIncludePaymentCost", Item.AttributeMode.INITIAL);
        tmp.put("entryGroups", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isCalculated(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "calculated");
    }


    public Boolean isCalculated()
    {
        return isCalculated(getSession().getSessionContext());
    }


    public boolean isCalculatedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCalculated(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCalculatedAsPrimitive()
    {
        return isCalculatedAsPrimitive(getSession().getSessionContext());
    }


    public void setCalculated(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "calculated", value);
    }


    public void setCalculated(Boolean value)
    {
        setCalculated(getSession().getSessionContext(), value);
    }


    public void setCalculated(SessionContext ctx, boolean value)
    {
        setCalculated(ctx, Boolean.valueOf(value));
    }


    public void setCalculated(boolean value)
    {
        setCalculated(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public Currency getCurrency(SessionContext ctx)
    {
        return (Currency)getProperty(ctx, "currency");
    }


    public Currency getCurrency()
    {
        return getCurrency(getSession().getSessionContext());
    }


    public void setCurrency(SessionContext ctx, Currency value)
    {
        setProperty(ctx, "currency", value);
    }


    public void setCurrency(Currency value)
    {
        setCurrency(getSession().getSessionContext(), value);
    }


    public Date getDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "date");
    }


    public Date getDate()
    {
        return getDate(getSession().getSessionContext());
    }


    public void setDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "date", value);
    }


    public void setDate(Date value)
    {
        setDate(getSession().getSessionContext(), value);
    }


    public Address getDeliveryAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "deliveryAddress");
    }


    public Address getDeliveryAddress()
    {
        return getDeliveryAddress(getSession().getSessionContext());
    }


    public void setDeliveryAddress(SessionContext ctx, Address value)
    {
        setProperty(ctx, "deliveryAddress", value);
    }


    public void setDeliveryAddress(Address value)
    {
        setDeliveryAddress(getSession().getSessionContext(), value);
    }


    public Double getDeliveryCost(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "deliveryCost");
    }


    public Double getDeliveryCost()
    {
        return getDeliveryCost(getSession().getSessionContext());
    }


    public double getDeliveryCostAsPrimitive(SessionContext ctx)
    {
        Double value = getDeliveryCost(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getDeliveryCostAsPrimitive()
    {
        return getDeliveryCostAsPrimitive(getSession().getSessionContext());
    }


    public void setDeliveryCost(SessionContext ctx, Double value)
    {
        setProperty(ctx, "deliveryCost", value);
    }


    public void setDeliveryCost(Double value)
    {
        setDeliveryCost(getSession().getSessionContext(), value);
    }


    public void setDeliveryCost(SessionContext ctx, double value)
    {
        setDeliveryCost(ctx, Double.valueOf(value));
    }


    public void setDeliveryCost(double value)
    {
        setDeliveryCost(getSession().getSessionContext(), value);
    }


    public DeliveryMode getDeliveryMode(SessionContext ctx)
    {
        return (DeliveryMode)getProperty(ctx, "deliveryMode");
    }


    public DeliveryMode getDeliveryMode()
    {
        return getDeliveryMode(getSession().getSessionContext());
    }


    public void setDeliveryMode(SessionContext ctx, DeliveryMode value)
    {
        setProperty(ctx, "deliveryMode", value);
    }


    public void setDeliveryMode(DeliveryMode value)
    {
        setDeliveryMode(getSession().getSessionContext(), value);
    }


    public EnumerationValue getDeliveryStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "deliveryStatus");
    }


    public EnumerationValue getDeliveryStatus()
    {
        return getDeliveryStatus(getSession().getSessionContext());
    }


    public void setDeliveryStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "deliveryStatus", value);
    }


    public void setDeliveryStatus(EnumerationValue value)
    {
        setDeliveryStatus(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public List<Discount> getDiscounts(SessionContext ctx)
    {
        List<Discount> items = getLinkedItems(ctx, true, GeneratedCoreConstants.Relations.ORDERDISCOUNTRELATION, "Discount", null,
                        Utilities.getRelationOrderingOverride(ORDERDISCOUNTRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public List<Discount> getDiscounts()
    {
        return getDiscounts(getSession().getSessionContext());
    }


    public long getDiscountsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCoreConstants.Relations.ORDERDISCOUNTRELATION, "Discount", null);
    }


    public long getDiscountsCount()
    {
        return getDiscountsCount(getSession().getSessionContext());
    }


    public void setDiscounts(SessionContext ctx, List<Discount> value)
    {
        setLinkedItems(ctx, true, GeneratedCoreConstants.Relations.ORDERDISCOUNTRELATION, null, value,
                        Utilities.getRelationOrderingOverride(ORDERDISCOUNTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ORDERDISCOUNTRELATION_MARKMODIFIED));
    }


    public void setDiscounts(List<Discount> value)
    {
        setDiscounts(getSession().getSessionContext(), value);
    }


    public void addToDiscounts(SessionContext ctx, Discount value)
    {
        addLinkedItems(ctx, true, GeneratedCoreConstants.Relations.ORDERDISCOUNTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ORDERDISCOUNTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ORDERDISCOUNTRELATION_MARKMODIFIED));
    }


    public void addToDiscounts(Discount value)
    {
        addToDiscounts(getSession().getSessionContext(), value);
    }


    public void removeFromDiscounts(SessionContext ctx, Discount value)
    {
        removeLinkedItems(ctx, true, GeneratedCoreConstants.Relations.ORDERDISCOUNTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ORDERDISCOUNTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ORDERDISCOUNTRELATION_MARKMODIFIED));
    }


    public void removeFromDiscounts(Discount value)
    {
        removeFromDiscounts(getSession().getSessionContext(), value);
    }


    public Boolean isDiscountsIncludeDeliveryCost(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "discountsIncludeDeliveryCost");
    }


    public Boolean isDiscountsIncludeDeliveryCost()
    {
        return isDiscountsIncludeDeliveryCost(getSession().getSessionContext());
    }


    public boolean isDiscountsIncludeDeliveryCostAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDiscountsIncludeDeliveryCost(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDiscountsIncludeDeliveryCostAsPrimitive()
    {
        return isDiscountsIncludeDeliveryCostAsPrimitive(getSession().getSessionContext());
    }


    public void setDiscountsIncludeDeliveryCost(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "discountsIncludeDeliveryCost", value);
    }


    public void setDiscountsIncludeDeliveryCost(Boolean value)
    {
        setDiscountsIncludeDeliveryCost(getSession().getSessionContext(), value);
    }


    public void setDiscountsIncludeDeliveryCost(SessionContext ctx, boolean value)
    {
        setDiscountsIncludeDeliveryCost(ctx, Boolean.valueOf(value));
    }


    public void setDiscountsIncludeDeliveryCost(boolean value)
    {
        setDiscountsIncludeDeliveryCost(getSession().getSessionContext(), value);
    }


    public Boolean isDiscountsIncludePaymentCost(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "discountsIncludePaymentCost");
    }


    public Boolean isDiscountsIncludePaymentCost()
    {
        return isDiscountsIncludePaymentCost(getSession().getSessionContext());
    }


    public boolean isDiscountsIncludePaymentCostAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDiscountsIncludePaymentCost(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDiscountsIncludePaymentCostAsPrimitive()
    {
        return isDiscountsIncludePaymentCostAsPrimitive(getSession().getSessionContext());
    }


    public void setDiscountsIncludePaymentCost(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "discountsIncludePaymentCost", value);
    }


    public void setDiscountsIncludePaymentCost(Boolean value)
    {
        setDiscountsIncludePaymentCost(getSession().getSessionContext(), value);
    }


    public void setDiscountsIncludePaymentCost(SessionContext ctx, boolean value)
    {
        setDiscountsIncludePaymentCost(ctx, Boolean.valueOf(value));
    }


    public void setDiscountsIncludePaymentCost(boolean value)
    {
        setDiscountsIncludePaymentCost(getSession().getSessionContext(), value);
    }


    public List<AbstractOrderEntry> getEntries(SessionContext ctx)
    {
        return (List<AbstractOrderEntry>)ENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<AbstractOrderEntry> getEntries()
    {
        return getEntries(getSession().getSessionContext());
    }


    public void setEntries(SessionContext ctx, List<AbstractOrderEntry> value)
    {
        ENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setEntries(List<AbstractOrderEntry> value)
    {
        setEntries(getSession().getSessionContext(), value);
    }


    public void addToEntries(SessionContext ctx, AbstractOrderEntry value)
    {
        ENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToEntries(AbstractOrderEntry value)
    {
        addToEntries(getSession().getSessionContext(), value);
    }


    public void removeFromEntries(SessionContext ctx, AbstractOrderEntry value)
    {
        ENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromEntries(AbstractOrderEntry value)
    {
        removeFromEntries(getSession().getSessionContext(), value);
    }


    public List<EntryGroup> getEntryGroups(SessionContext ctx)
    {
        List<EntryGroup> coll = (List<EntryGroup>)getProperty(ctx, "entryGroups");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public List<EntryGroup> getEntryGroups()
    {
        return getEntryGroups(getSession().getSessionContext());
    }


    public void setEntryGroups(SessionContext ctx, List<EntryGroup> value)
    {
        setProperty(ctx, "entryGroups", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setEntryGroups(List<EntryGroup> value)
    {
        setEntryGroups(getSession().getSessionContext(), value);
    }


    public Date getExpirationTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "expirationTime");
    }


    public Date getExpirationTime()
    {
        return getExpirationTime(getSession().getSessionContext());
    }


    public void setExpirationTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "expirationTime", value);
    }


    public void setExpirationTime(Date value)
    {
        setExpirationTime(getSession().getSessionContext(), value);
    }


    public EnumerationValue getExportStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "exportStatus");
    }


    public EnumerationValue getExportStatus()
    {
        return getExportStatus(getSession().getSessionContext());
    }


    public void setExportStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "exportStatus", value);
    }


    public void setExportStatus(EnumerationValue value)
    {
        setExportStatus(getSession().getSessionContext(), value);
    }


    public String getGlobalDiscountValuesInternal(SessionContext ctx)
    {
        return (String)getProperty(ctx, "globalDiscountValuesInternal");
    }


    public String getGlobalDiscountValuesInternal()
    {
        return getGlobalDiscountValuesInternal(getSession().getSessionContext());
    }


    public void setGlobalDiscountValuesInternal(SessionContext ctx, String value)
    {
        setProperty(ctx, "globalDiscountValuesInternal", value);
    }


    public void setGlobalDiscountValuesInternal(String value)
    {
        setGlobalDiscountValuesInternal(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Discount");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(ORDERDISCOUNTRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public Boolean isNet(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "net");
    }


    public Boolean isNet()
    {
        return isNet(getSession().getSessionContext());
    }


    public boolean isNetAsPrimitive(SessionContext ctx)
    {
        Boolean value = isNet(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isNetAsPrimitive()
    {
        return isNetAsPrimitive(getSession().getSessionContext());
    }


    public void setNet(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "net", value);
    }


    public void setNet(Boolean value)
    {
        setNet(getSession().getSessionContext(), value);
    }


    public void setNet(SessionContext ctx, boolean value)
    {
        setNet(ctx, Boolean.valueOf(value));
    }


    public void setNet(boolean value)
    {
        setNet(getSession().getSessionContext(), value);
    }


    public Address getPaymentAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "paymentAddress");
    }


    public Address getPaymentAddress()
    {
        return getPaymentAddress(getSession().getSessionContext());
    }


    public void setPaymentAddress(SessionContext ctx, Address value)
    {
        setProperty(ctx, "paymentAddress", value);
    }


    public void setPaymentAddress(Address value)
    {
        setPaymentAddress(getSession().getSessionContext(), value);
    }


    public Double getPaymentCost(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "paymentCost");
    }


    public Double getPaymentCost()
    {
        return getPaymentCost(getSession().getSessionContext());
    }


    public double getPaymentCostAsPrimitive(SessionContext ctx)
    {
        Double value = getPaymentCost(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getPaymentCostAsPrimitive()
    {
        return getPaymentCostAsPrimitive(getSession().getSessionContext());
    }


    public void setPaymentCost(SessionContext ctx, Double value)
    {
        setProperty(ctx, "paymentCost", value);
    }


    public void setPaymentCost(Double value)
    {
        setPaymentCost(getSession().getSessionContext(), value);
    }


    public void setPaymentCost(SessionContext ctx, double value)
    {
        setPaymentCost(ctx, Double.valueOf(value));
    }


    public void setPaymentCost(double value)
    {
        setPaymentCost(getSession().getSessionContext(), value);
    }


    public PaymentInfo getPaymentInfo(SessionContext ctx)
    {
        return (PaymentInfo)getProperty(ctx, "paymentInfo");
    }


    public PaymentInfo getPaymentInfo()
    {
        return getPaymentInfo(getSession().getSessionContext());
    }


    public void setPaymentInfo(SessionContext ctx, PaymentInfo value)
    {
        setProperty(ctx, "paymentInfo", value);
    }


    public void setPaymentInfo(PaymentInfo value)
    {
        setPaymentInfo(getSession().getSessionContext(), value);
    }


    public PaymentMode getPaymentMode(SessionContext ctx)
    {
        return (PaymentMode)getProperty(ctx, "paymentMode");
    }


    public PaymentMode getPaymentMode()
    {
        return getPaymentMode(getSession().getSessionContext());
    }


    public void setPaymentMode(SessionContext ctx, PaymentMode value)
    {
        setProperty(ctx, "paymentMode", value);
    }


    public void setPaymentMode(PaymentMode value)
    {
        setPaymentMode(getSession().getSessionContext(), value);
    }


    public EnumerationValue getPaymentStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "paymentStatus");
    }


    public EnumerationValue getPaymentStatus()
    {
        return getPaymentStatus(getSession().getSessionContext());
    }


    public void setPaymentStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "paymentStatus", value);
    }


    public void setPaymentStatus(EnumerationValue value)
    {
        setPaymentStatus(getSession().getSessionContext(), value);
    }


    public EnumerationValue getStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "status");
    }


    public EnumerationValue getStatus()
    {
        return getStatus(getSession().getSessionContext());
    }


    public void setStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "status", value);
    }


    public void setStatus(EnumerationValue value)
    {
        setStatus(getSession().getSessionContext(), value);
    }


    public String getStatusInfo(SessionContext ctx)
    {
        return (String)getProperty(ctx, "statusInfo");
    }


    public String getStatusInfo()
    {
        return getStatusInfo(getSession().getSessionContext());
    }


    public void setStatusInfo(SessionContext ctx, String value)
    {
        setProperty(ctx, "statusInfo", value);
    }


    public void setStatusInfo(String value)
    {
        setStatusInfo(getSession().getSessionContext(), value);
    }


    public Double getSubtotal(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "subtotal");
    }


    public Double getSubtotal()
    {
        return getSubtotal(getSession().getSessionContext());
    }


    public double getSubtotalAsPrimitive(SessionContext ctx)
    {
        Double value = getSubtotal(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getSubtotalAsPrimitive()
    {
        return getSubtotalAsPrimitive(getSession().getSessionContext());
    }


    public void setSubtotal(SessionContext ctx, Double value)
    {
        setProperty(ctx, "subtotal", value);
    }


    public void setSubtotal(Double value)
    {
        setSubtotal(getSession().getSessionContext(), value);
    }


    public void setSubtotal(SessionContext ctx, double value)
    {
        setSubtotal(ctx, Double.valueOf(value));
    }


    public void setSubtotal(double value)
    {
        setSubtotal(getSession().getSessionContext(), value);
    }


    public Double getTotalDiscounts(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "totalDiscounts");
    }


    public Double getTotalDiscounts()
    {
        return getTotalDiscounts(getSession().getSessionContext());
    }


    public double getTotalDiscountsAsPrimitive(SessionContext ctx)
    {
        Double value = getTotalDiscounts(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getTotalDiscountsAsPrimitive()
    {
        return getTotalDiscountsAsPrimitive(getSession().getSessionContext());
    }


    public void setTotalDiscounts(SessionContext ctx, Double value)
    {
        setProperty(ctx, "totalDiscounts", value);
    }


    public void setTotalDiscounts(Double value)
    {
        setTotalDiscounts(getSession().getSessionContext(), value);
    }


    public void setTotalDiscounts(SessionContext ctx, double value)
    {
        setTotalDiscounts(ctx, Double.valueOf(value));
    }


    public void setTotalDiscounts(double value)
    {
        setTotalDiscounts(getSession().getSessionContext(), value);
    }


    public Double getTotalPrice(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "totalPrice");
    }


    public Double getTotalPrice()
    {
        return getTotalPrice(getSession().getSessionContext());
    }


    public double getTotalPriceAsPrimitive(SessionContext ctx)
    {
        Double value = getTotalPrice(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getTotalPriceAsPrimitive()
    {
        return getTotalPriceAsPrimitive(getSession().getSessionContext());
    }


    public void setTotalPrice(SessionContext ctx, Double value)
    {
        setProperty(ctx, "totalPrice", value);
    }


    public void setTotalPrice(Double value)
    {
        setTotalPrice(getSession().getSessionContext(), value);
    }


    public void setTotalPrice(SessionContext ctx, double value)
    {
        setTotalPrice(ctx, Double.valueOf(value));
    }


    public void setTotalPrice(double value)
    {
        setTotalPrice(getSession().getSessionContext(), value);
    }


    public Double getTotalTax(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "totalTax");
    }


    public Double getTotalTax()
    {
        return getTotalTax(getSession().getSessionContext());
    }


    public double getTotalTaxAsPrimitive(SessionContext ctx)
    {
        Double value = getTotalTax(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getTotalTaxAsPrimitive()
    {
        return getTotalTaxAsPrimitive(getSession().getSessionContext());
    }


    public void setTotalTax(SessionContext ctx, Double value)
    {
        setProperty(ctx, "totalTax", value);
    }


    public void setTotalTax(Double value)
    {
        setTotalTax(getSession().getSessionContext(), value);
    }


    public void setTotalTax(SessionContext ctx, double value)
    {
        setTotalTax(ctx, Double.valueOf(value));
    }


    public void setTotalTax(double value)
    {
        setTotalTax(getSession().getSessionContext(), value);
    }


    public String getTotalTaxValuesInternal(SessionContext ctx)
    {
        return (String)getProperty(ctx, "totalTaxValuesInternal");
    }


    public String getTotalTaxValuesInternal()
    {
        return getTotalTaxValuesInternal(getSession().getSessionContext());
    }


    public void setTotalTaxValuesInternal(SessionContext ctx, String value)
    {
        setProperty(ctx, "totalTaxValuesInternal", value);
    }


    public void setTotalTaxValuesInternal(String value)
    {
        setTotalTaxValuesInternal(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        setProperty(ctx, "user", value);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }
}
