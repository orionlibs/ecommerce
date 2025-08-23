package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedAtpFormula extends GenericItem
{
    public static final String CODE = "code";
    public static final String AVAILABILITY = "availability";
    public static final String ALLOCATION = "allocation";
    public static final String CANCELLATION = "cancellation";
    public static final String INCREASE = "increase";
    public static final String RESERVED = "reserved";
    public static final String SHRINKAGE = "shrinkage";
    public static final String WASTAGE = "wastage";
    public static final String RETURNED = "returned";
    public static final String EXTERNAL = "external";
    public static final String BASESTORES = "baseStores";
    protected static final OneToManyHandler<BaseStore> BASESTORESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.BASESTORE, false, "defaultAtpFormula", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("availability", Item.AttributeMode.INITIAL);
        tmp.put("allocation", Item.AttributeMode.INITIAL);
        tmp.put("cancellation", Item.AttributeMode.INITIAL);
        tmp.put("increase", Item.AttributeMode.INITIAL);
        tmp.put("reserved", Item.AttributeMode.INITIAL);
        tmp.put("shrinkage", Item.AttributeMode.INITIAL);
        tmp.put("wastage", Item.AttributeMode.INITIAL);
        tmp.put("returned", Item.AttributeMode.INITIAL);
        tmp.put("external", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAllocation(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "allocation");
    }


    public Boolean isAllocation()
    {
        return isAllocation(getSession().getSessionContext());
    }


    public boolean isAllocationAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAllocation(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAllocationAsPrimitive()
    {
        return isAllocationAsPrimitive(getSession().getSessionContext());
    }


    public void setAllocation(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "allocation", value);
    }


    public void setAllocation(Boolean value)
    {
        setAllocation(getSession().getSessionContext(), value);
    }


    public void setAllocation(SessionContext ctx, boolean value)
    {
        setAllocation(ctx, Boolean.valueOf(value));
    }


    public void setAllocation(boolean value)
    {
        setAllocation(getSession().getSessionContext(), value);
    }


    public Boolean isAvailability(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "availability");
    }


    public Boolean isAvailability()
    {
        return isAvailability(getSession().getSessionContext());
    }


    public boolean isAvailabilityAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAvailability(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAvailabilityAsPrimitive()
    {
        return isAvailabilityAsPrimitive(getSession().getSessionContext());
    }


    public void setAvailability(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "availability", value);
    }


    public void setAvailability(Boolean value)
    {
        setAvailability(getSession().getSessionContext(), value);
    }


    public void setAvailability(SessionContext ctx, boolean value)
    {
        setAvailability(ctx, Boolean.valueOf(value));
    }


    public void setAvailability(boolean value)
    {
        setAvailability(getSession().getSessionContext(), value);
    }


    public Set<BaseStore> getBaseStores(SessionContext ctx)
    {
        return (Set<BaseStore>)BASESTORESHANDLER.getValues(ctx, (Item)this);
    }


    public Set<BaseStore> getBaseStores()
    {
        return getBaseStores(getSession().getSessionContext());
    }


    public void setBaseStores(SessionContext ctx, Set<BaseStore> value)
    {
        BASESTORESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setBaseStores(Set<BaseStore> value)
    {
        setBaseStores(getSession().getSessionContext(), value);
    }


    public void addToBaseStores(SessionContext ctx, BaseStore value)
    {
        BASESTORESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToBaseStores(BaseStore value)
    {
        addToBaseStores(getSession().getSessionContext(), value);
    }


    public void removeFromBaseStores(SessionContext ctx, BaseStore value)
    {
        BASESTORESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromBaseStores(BaseStore value)
    {
        removeFromBaseStores(getSession().getSessionContext(), value);
    }


    public Boolean isCancellation(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "cancellation");
    }


    public Boolean isCancellation()
    {
        return isCancellation(getSession().getSessionContext());
    }


    public boolean isCancellationAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCancellation(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCancellationAsPrimitive()
    {
        return isCancellationAsPrimitive(getSession().getSessionContext());
    }


    public void setCancellation(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "cancellation", value);
    }


    public void setCancellation(Boolean value)
    {
        setCancellation(getSession().getSessionContext(), value);
    }


    public void setCancellation(SessionContext ctx, boolean value)
    {
        setCancellation(ctx, Boolean.valueOf(value));
    }


    public void setCancellation(boolean value)
    {
        setCancellation(getSession().getSessionContext(), value);
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


    public Boolean isExternal(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "external");
    }


    public Boolean isExternal()
    {
        return isExternal(getSession().getSessionContext());
    }


    public boolean isExternalAsPrimitive(SessionContext ctx)
    {
        Boolean value = isExternal(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isExternalAsPrimitive()
    {
        return isExternalAsPrimitive(getSession().getSessionContext());
    }


    public void setExternal(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "external", value);
    }


    public void setExternal(Boolean value)
    {
        setExternal(getSession().getSessionContext(), value);
    }


    public void setExternal(SessionContext ctx, boolean value)
    {
        setExternal(ctx, Boolean.valueOf(value));
    }


    public void setExternal(boolean value)
    {
        setExternal(getSession().getSessionContext(), value);
    }


    public Boolean isIncrease(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "increase");
    }


    public Boolean isIncrease()
    {
        return isIncrease(getSession().getSessionContext());
    }


    public boolean isIncreaseAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIncrease(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIncreaseAsPrimitive()
    {
        return isIncreaseAsPrimitive(getSession().getSessionContext());
    }


    public void setIncrease(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "increase", value);
    }


    public void setIncrease(Boolean value)
    {
        setIncrease(getSession().getSessionContext(), value);
    }


    public void setIncrease(SessionContext ctx, boolean value)
    {
        setIncrease(ctx, Boolean.valueOf(value));
    }


    public void setIncrease(boolean value)
    {
        setIncrease(getSession().getSessionContext(), value);
    }


    public Boolean isReserved(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "reserved");
    }


    public Boolean isReserved()
    {
        return isReserved(getSession().getSessionContext());
    }


    public boolean isReservedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isReserved(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isReservedAsPrimitive()
    {
        return isReservedAsPrimitive(getSession().getSessionContext());
    }


    public void setReserved(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "reserved", value);
    }


    public void setReserved(Boolean value)
    {
        setReserved(getSession().getSessionContext(), value);
    }


    public void setReserved(SessionContext ctx, boolean value)
    {
        setReserved(ctx, Boolean.valueOf(value));
    }


    public void setReserved(boolean value)
    {
        setReserved(getSession().getSessionContext(), value);
    }


    public Boolean isReturned(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "returned");
    }


    public Boolean isReturned()
    {
        return isReturned(getSession().getSessionContext());
    }


    public boolean isReturnedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isReturned(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isReturnedAsPrimitive()
    {
        return isReturnedAsPrimitive(getSession().getSessionContext());
    }


    public void setReturned(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "returned", value);
    }


    public void setReturned(Boolean value)
    {
        setReturned(getSession().getSessionContext(), value);
    }


    public void setReturned(SessionContext ctx, boolean value)
    {
        setReturned(ctx, Boolean.valueOf(value));
    }


    public void setReturned(boolean value)
    {
        setReturned(getSession().getSessionContext(), value);
    }


    public Boolean isShrinkage(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "shrinkage");
    }


    public Boolean isShrinkage()
    {
        return isShrinkage(getSession().getSessionContext());
    }


    public boolean isShrinkageAsPrimitive(SessionContext ctx)
    {
        Boolean value = isShrinkage(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isShrinkageAsPrimitive()
    {
        return isShrinkageAsPrimitive(getSession().getSessionContext());
    }


    public void setShrinkage(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "shrinkage", value);
    }


    public void setShrinkage(Boolean value)
    {
        setShrinkage(getSession().getSessionContext(), value);
    }


    public void setShrinkage(SessionContext ctx, boolean value)
    {
        setShrinkage(ctx, Boolean.valueOf(value));
    }


    public void setShrinkage(boolean value)
    {
        setShrinkage(getSession().getSessionContext(), value);
    }


    public Boolean isWastage(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "wastage");
    }


    public Boolean isWastage()
    {
        return isWastage(getSession().getSessionContext());
    }


    public boolean isWastageAsPrimitive(SessionContext ctx)
    {
        Boolean value = isWastage(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isWastageAsPrimitive()
    {
        return isWastageAsPrimitive(getSession().getSessionContext());
    }


    public void setWastage(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "wastage", value);
    }


    public void setWastage(Boolean value)
    {
        setWastage(getSession().getSessionContext(), value);
    }


    public void setWastage(SessionContext ctx, boolean value)
    {
        setWastage(ctx, Boolean.valueOf(value));
    }


    public void setWastage(boolean value)
    {
        setWastage(getSession().getSessionContext(), value);
    }
}
