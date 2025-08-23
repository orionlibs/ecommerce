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

public abstract class GeneratedSourcingConfig extends GenericItem
{
    public static final String CODE = "code";
    public static final String DISTANCEWEIGHTFACTOR = "distanceWeightFactor";
    public static final String ALLOCATIONWEIGHTFACTOR = "allocationWeightFactor";
    public static final String PRIORITYWEIGHTFACTOR = "priorityWeightFactor";
    public static final String SCOREWEIGHTFACTOR = "scoreWeightFactor";
    public static final String BASESTORES = "baseStores";
    protected static final OneToManyHandler<BaseStore> BASESTORESHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.BASESTORE, false, "sourcingConfig", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("distanceWeightFactor", Item.AttributeMode.INITIAL);
        tmp.put("allocationWeightFactor", Item.AttributeMode.INITIAL);
        tmp.put("priorityWeightFactor", Item.AttributeMode.INITIAL);
        tmp.put("scoreWeightFactor", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getAllocationWeightFactor(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "allocationWeightFactor");
    }


    public Integer getAllocationWeightFactor()
    {
        return getAllocationWeightFactor(getSession().getSessionContext());
    }


    public int getAllocationWeightFactorAsPrimitive(SessionContext ctx)
    {
        Integer value = getAllocationWeightFactor(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getAllocationWeightFactorAsPrimitive()
    {
        return getAllocationWeightFactorAsPrimitive(getSession().getSessionContext());
    }


    public void setAllocationWeightFactor(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "allocationWeightFactor", value);
    }


    public void setAllocationWeightFactor(Integer value)
    {
        setAllocationWeightFactor(getSession().getSessionContext(), value);
    }


    public void setAllocationWeightFactor(SessionContext ctx, int value)
    {
        setAllocationWeightFactor(ctx, Integer.valueOf(value));
    }


    public void setAllocationWeightFactor(int value)
    {
        setAllocationWeightFactor(getSession().getSessionContext(), value);
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


    public Integer getDistanceWeightFactor(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "distanceWeightFactor");
    }


    public Integer getDistanceWeightFactor()
    {
        return getDistanceWeightFactor(getSession().getSessionContext());
    }


    public int getDistanceWeightFactorAsPrimitive(SessionContext ctx)
    {
        Integer value = getDistanceWeightFactor(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getDistanceWeightFactorAsPrimitive()
    {
        return getDistanceWeightFactorAsPrimitive(getSession().getSessionContext());
    }


    public void setDistanceWeightFactor(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "distanceWeightFactor", value);
    }


    public void setDistanceWeightFactor(Integer value)
    {
        setDistanceWeightFactor(getSession().getSessionContext(), value);
    }


    public void setDistanceWeightFactor(SessionContext ctx, int value)
    {
        setDistanceWeightFactor(ctx, Integer.valueOf(value));
    }


    public void setDistanceWeightFactor(int value)
    {
        setDistanceWeightFactor(getSession().getSessionContext(), value);
    }


    public Integer getPriorityWeightFactor(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "priorityWeightFactor");
    }


    public Integer getPriorityWeightFactor()
    {
        return getPriorityWeightFactor(getSession().getSessionContext());
    }


    public int getPriorityWeightFactorAsPrimitive(SessionContext ctx)
    {
        Integer value = getPriorityWeightFactor(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPriorityWeightFactorAsPrimitive()
    {
        return getPriorityWeightFactorAsPrimitive(getSession().getSessionContext());
    }


    public void setPriorityWeightFactor(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "priorityWeightFactor", value);
    }


    public void setPriorityWeightFactor(Integer value)
    {
        setPriorityWeightFactor(getSession().getSessionContext(), value);
    }


    public void setPriorityWeightFactor(SessionContext ctx, int value)
    {
        setPriorityWeightFactor(ctx, Integer.valueOf(value));
    }


    public void setPriorityWeightFactor(int value)
    {
        setPriorityWeightFactor(getSession().getSessionContext(), value);
    }


    public Integer getScoreWeightFactor(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "scoreWeightFactor");
    }


    public Integer getScoreWeightFactor()
    {
        return getScoreWeightFactor(getSession().getSessionContext());
    }


    public int getScoreWeightFactorAsPrimitive(SessionContext ctx)
    {
        Integer value = getScoreWeightFactor(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getScoreWeightFactorAsPrimitive()
    {
        return getScoreWeightFactorAsPrimitive(getSession().getSessionContext());
    }


    public void setScoreWeightFactor(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "scoreWeightFactor", value);
    }


    public void setScoreWeightFactor(Integer value)
    {
        setScoreWeightFactor(getSession().getSessionContext(), value);
    }


    public void setScoreWeightFactor(SessionContext ctx, int value)
    {
        setScoreWeightFactor(ctx, Integer.valueOf(value));
    }


    public void setScoreWeightFactor(int value)
    {
        setScoreWeightFactor(getSession().getSessionContext(), value);
    }
}
