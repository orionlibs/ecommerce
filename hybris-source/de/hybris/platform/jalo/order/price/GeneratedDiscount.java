package de.hybris.platform.jalo.order.price;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedDiscount extends GenericItem
{
    public static final String ABSOLUTE = "absolute";
    public static final String CODE = "code";
    public static final String CURRENCY = "currency";
    public static final String GLOBAL = "global";
    public static final String NAME = "name";
    public static final String PRIORITY = "priority";
    public static final String VALUE = "value";
    public static final String DISCOUNTSTRING = "discountString";
    public static final String ORDERS = "orders";
    protected static String ORDERDISCOUNTRELATION_SRC_ORDERED = "relation.OrderDiscountRelation.source.ordered";
    protected static String ORDERDISCOUNTRELATION_TGT_ORDERED = "relation.OrderDiscountRelation.target.ordered";
    protected static String ORDERDISCOUNTRELATION_MARKMODIFIED = "relation.OrderDiscountRelation.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("currency", Item.AttributeMode.INITIAL);
        tmp.put("global", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAbsolute()
    {
        return isAbsolute(getSession().getSessionContext());
    }


    public boolean isAbsoluteAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAbsolute(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAbsoluteAsPrimitive()
    {
        return isAbsoluteAsPrimitive(getSession().getSessionContext());
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value) throws ConsistencyCheckException
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value) throws ConsistencyCheckException
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


    public String getDiscountString()
    {
        return getDiscountString(getSession().getSessionContext());
    }


    public Boolean isGlobal(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "global");
    }


    public Boolean isGlobal()
    {
        return isGlobal(getSession().getSessionContext());
    }


    public boolean isGlobalAsPrimitive(SessionContext ctx)
    {
        Boolean value = isGlobal(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isGlobalAsPrimitive()
    {
        return isGlobalAsPrimitive(getSession().getSessionContext());
    }


    public void setGlobal(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "global", value);
    }


    public void setGlobal(Boolean value)
    {
        setGlobal(getSession().getSessionContext(), value);
    }


    public void setGlobal(SessionContext ctx, boolean value)
    {
        setGlobal(ctx, Boolean.valueOf(value));
    }


    public void setGlobal(boolean value)
    {
        setGlobal(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("AbstractOrder");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(ORDERDISCOUNTRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedDiscount.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedDiscount.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public Collection<AbstractOrder> getOrders(SessionContext ctx)
    {
        List<AbstractOrder> items = getLinkedItems(ctx, false, GeneratedCoreConstants.Relations.ORDERDISCOUNTRELATION, "AbstractOrder", null,
                        Utilities.getRelationOrderingOverride(ORDERDISCOUNTRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<AbstractOrder> getOrders()
    {
        return getOrders(getSession().getSessionContext());
    }


    public long getOrdersCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCoreConstants.Relations.ORDERDISCOUNTRELATION, "AbstractOrder", null);
    }


    public long getOrdersCount()
    {
        return getOrdersCount(getSession().getSessionContext());
    }


    public void setOrders(SessionContext ctx, Collection<AbstractOrder> value)
    {
        setLinkedItems(ctx, false, GeneratedCoreConstants.Relations.ORDERDISCOUNTRELATION, null, value,
                        Utilities.getRelationOrderingOverride(ORDERDISCOUNTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ORDERDISCOUNTRELATION_MARKMODIFIED));
    }


    public void setOrders(Collection<AbstractOrder> value)
    {
        setOrders(getSession().getSessionContext(), value);
    }


    public void addToOrders(SessionContext ctx, AbstractOrder value)
    {
        addLinkedItems(ctx, false, GeneratedCoreConstants.Relations.ORDERDISCOUNTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ORDERDISCOUNTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ORDERDISCOUNTRELATION_MARKMODIFIED));
    }


    public void addToOrders(AbstractOrder value)
    {
        addToOrders(getSession().getSessionContext(), value);
    }


    public void removeFromOrders(SessionContext ctx, AbstractOrder value)
    {
        removeLinkedItems(ctx, false, GeneratedCoreConstants.Relations.ORDERDISCOUNTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(ORDERDISCOUNTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(ORDERDISCOUNTRELATION_MARKMODIFIED));
    }


    public void removeFromOrders(AbstractOrder value)
    {
        removeFromOrders(getSession().getSessionContext(), value);
    }


    public Integer getPriority(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "priority");
    }


    public Integer getPriority()
    {
        return getPriority(getSession().getSessionContext());
    }


    public int getPriorityAsPrimitive(SessionContext ctx)
    {
        Integer value = getPriority(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPriorityAsPrimitive()
    {
        return getPriorityAsPrimitive(getSession().getSessionContext());
    }


    public void setPriority(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "priority", value);
    }


    public void setPriority(Integer value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public void setPriority(SessionContext ctx, int value)
    {
        setPriority(ctx, Integer.valueOf(value));
    }


    public void setPriority(int value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public Double getValue(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "value");
    }


    public Double getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public double getValueAsPrimitive(SessionContext ctx)
    {
        Double value = getValue(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getValueAsPrimitive()
    {
        return getValueAsPrimitive(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, Double value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(Double value)
    {
        setValue(getSession().getSessionContext(), value);
    }


    public void setValue(SessionContext ctx, double value)
    {
        setValue(ctx, Double.valueOf(value));
    }


    public void setValue(double value)
    {
        setValue(getSession().getSessionContext(), value);
    }


    public abstract Boolean isAbsolute(SessionContext paramSessionContext);


    public abstract String getDiscountString(SessionContext paramSessionContext);
}
