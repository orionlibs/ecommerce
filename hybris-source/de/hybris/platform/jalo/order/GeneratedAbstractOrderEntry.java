package de.hybris.platform.jalo.order;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedAbstractOrderEntry extends GenericItem
{
    public static final String BASEPRICE = "basePrice";
    public static final String CALCULATED = "calculated";
    public static final String DISCOUNTVALUESINTERNAL = "discountValuesInternal";
    public static final String ENTRYNUMBER = "entryNumber";
    public static final String INFO = "info";
    public static final String PRODUCT = "product";
    public static final String QUANTITY = "quantity";
    public static final String TAXVALUESINTERNAL = "taxValuesInternal";
    public static final String TOTALPRICE = "totalPrice";
    public static final String UNIT = "unit";
    public static final String GIVEAWAY = "giveAway";
    public static final String REJECTED = "rejected";
    public static final String ENTRYGROUPNUMBERS = "entryGroupNumbers";
    public static final String ORDER = "order";
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractOrderEntry> ORDERHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.ABSTRACTORDERENTRY, false, "order", "entryNumber", false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("basePrice", Item.AttributeMode.INITIAL);
        tmp.put("calculated", Item.AttributeMode.INITIAL);
        tmp.put("discountValuesInternal", Item.AttributeMode.INITIAL);
        tmp.put("entryNumber", Item.AttributeMode.INITIAL);
        tmp.put("info", Item.AttributeMode.INITIAL);
        tmp.put("product", Item.AttributeMode.INITIAL);
        tmp.put("quantity", Item.AttributeMode.INITIAL);
        tmp.put("taxValuesInternal", Item.AttributeMode.INITIAL);
        tmp.put("totalPrice", Item.AttributeMode.INITIAL);
        tmp.put("unit", Item.AttributeMode.INITIAL);
        tmp.put("giveAway", Item.AttributeMode.INITIAL);
        tmp.put("rejected", Item.AttributeMode.INITIAL);
        tmp.put("entryGroupNumbers", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Double getBasePrice(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "basePrice");
    }


    public Double getBasePrice()
    {
        return getBasePrice(getSession().getSessionContext());
    }


    public double getBasePriceAsPrimitive(SessionContext ctx)
    {
        Double value = getBasePrice(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getBasePriceAsPrimitive()
    {
        return getBasePriceAsPrimitive(getSession().getSessionContext());
    }


    public void setBasePrice(SessionContext ctx, Double value)
    {
        setProperty(ctx, "basePrice", value);
    }


    public void setBasePrice(Double value)
    {
        setBasePrice(getSession().getSessionContext(), value);
    }


    public void setBasePrice(SessionContext ctx, double value)
    {
        setBasePrice(ctx, Double.valueOf(value));
    }


    public void setBasePrice(double value)
    {
        setBasePrice(getSession().getSessionContext(), value);
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


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ORDERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDiscountValuesInternal(SessionContext ctx)
    {
        return (String)getProperty(ctx, "discountValuesInternal");
    }


    public String getDiscountValuesInternal()
    {
        return getDiscountValuesInternal(getSession().getSessionContext());
    }


    public void setDiscountValuesInternal(SessionContext ctx, String value)
    {
        setProperty(ctx, "discountValuesInternal", value);
    }


    public void setDiscountValuesInternal(String value)
    {
        setDiscountValuesInternal(getSession().getSessionContext(), value);
    }


    public Set<Integer> getEntryGroupNumbers(SessionContext ctx)
    {
        Set<Integer> coll = (Set<Integer>)getProperty(ctx, "entryGroupNumbers");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<Integer> getEntryGroupNumbers()
    {
        return getEntryGroupNumbers(getSession().getSessionContext());
    }


    public void setEntryGroupNumbers(SessionContext ctx, Set<Integer> value)
    {
        setProperty(ctx, "entryGroupNumbers", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setEntryGroupNumbers(Set<Integer> value)
    {
        setEntryGroupNumbers(getSession().getSessionContext(), value);
    }


    public Integer getEntryNumber(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "entryNumber");
    }


    public Integer getEntryNumber()
    {
        return getEntryNumber(getSession().getSessionContext());
    }


    public int getEntryNumberAsPrimitive(SessionContext ctx)
    {
        Integer value = getEntryNumber(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getEntryNumberAsPrimitive()
    {
        return getEntryNumberAsPrimitive(getSession().getSessionContext());
    }


    public void setEntryNumber(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "entryNumber", value);
    }


    public void setEntryNumber(Integer value)
    {
        setEntryNumber(getSession().getSessionContext(), value);
    }


    public void setEntryNumber(SessionContext ctx, int value)
    {
        setEntryNumber(ctx, Integer.valueOf(value));
    }


    public void setEntryNumber(int value)
    {
        setEntryNumber(getSession().getSessionContext(), value);
    }


    public Boolean isGiveAway(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "giveAway");
    }


    public Boolean isGiveAway()
    {
        return isGiveAway(getSession().getSessionContext());
    }


    public boolean isGiveAwayAsPrimitive(SessionContext ctx)
    {
        Boolean value = isGiveAway(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isGiveAwayAsPrimitive()
    {
        return isGiveAwayAsPrimitive(getSession().getSessionContext());
    }


    public void setGiveAway(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "giveAway", value);
    }


    public void setGiveAway(Boolean value)
    {
        setGiveAway(getSession().getSessionContext(), value);
    }


    public void setGiveAway(SessionContext ctx, boolean value)
    {
        setGiveAway(ctx, Boolean.valueOf(value));
    }


    public void setGiveAway(boolean value)
    {
        setGiveAway(getSession().getSessionContext(), value);
    }


    public String getInfo(SessionContext ctx)
    {
        return (String)getProperty(ctx, "info");
    }


    public String getInfo()
    {
        return getInfo(getSession().getSessionContext());
    }


    public void setInfo(SessionContext ctx, String value)
    {
        setProperty(ctx, "info", value);
    }


    public void setInfo(String value)
    {
        setInfo(getSession().getSessionContext(), value);
    }


    public AbstractOrder getOrder(SessionContext ctx)
    {
        return (AbstractOrder)getProperty(ctx, "order");
    }


    public AbstractOrder getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    public void setOrder(SessionContext ctx, AbstractOrder value)
    {
        ORDERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setOrder(AbstractOrder value)
    {
        setOrder(getSession().getSessionContext(), value);
    }


    public Product getProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "product");
    }


    public Product getProduct()
    {
        return getProduct(getSession().getSessionContext());
    }


    public void setProduct(SessionContext ctx, Product value)
    {
        setProperty(ctx, "product", value);
    }


    public void setProduct(Product value)
    {
        setProduct(getSession().getSessionContext(), value);
    }


    public Long getQuantity(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "quantity");
    }


    public Long getQuantity()
    {
        return getQuantity(getSession().getSessionContext());
    }


    public long getQuantityAsPrimitive(SessionContext ctx)
    {
        Long value = getQuantity(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getQuantityAsPrimitive()
    {
        return getQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setQuantity(SessionContext ctx, Long value)
    {
        setProperty(ctx, "quantity", value);
    }


    public void setQuantity(Long value)
    {
        setQuantity(getSession().getSessionContext(), value);
    }


    public void setQuantity(SessionContext ctx, long value)
    {
        setQuantity(ctx, Long.valueOf(value));
    }


    public void setQuantity(long value)
    {
        setQuantity(getSession().getSessionContext(), value);
    }


    public Boolean isRejected(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "rejected");
    }


    public Boolean isRejected()
    {
        return isRejected(getSession().getSessionContext());
    }


    public boolean isRejectedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRejected(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRejectedAsPrimitive()
    {
        return isRejectedAsPrimitive(getSession().getSessionContext());
    }


    public void setRejected(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "rejected", value);
    }


    public void setRejected(Boolean value)
    {
        setRejected(getSession().getSessionContext(), value);
    }


    public void setRejected(SessionContext ctx, boolean value)
    {
        setRejected(ctx, Boolean.valueOf(value));
    }


    public void setRejected(boolean value)
    {
        setRejected(getSession().getSessionContext(), value);
    }


    public String getTaxValuesInternal(SessionContext ctx)
    {
        return (String)getProperty(ctx, "taxValuesInternal");
    }


    public String getTaxValuesInternal()
    {
        return getTaxValuesInternal(getSession().getSessionContext());
    }


    public void setTaxValuesInternal(SessionContext ctx, String value)
    {
        setProperty(ctx, "taxValuesInternal", value);
    }


    public void setTaxValuesInternal(String value)
    {
        setTaxValuesInternal(getSession().getSessionContext(), value);
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


    public Unit getUnit(SessionContext ctx)
    {
        return (Unit)getProperty(ctx, "unit");
    }


    public Unit getUnit()
    {
        return getUnit(getSession().getSessionContext());
    }


    public void setUnit(SessionContext ctx, Unit value)
    {
        setProperty(ctx, "unit", value);
    }


    public void setUnit(Unit value)
    {
        setUnit(getSession().getSessionContext(), value);
    }
}
