package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.constants.GeneratedPromotionsConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedPromotionResult extends GenericItem
{
    public static final String ACTIONS = "actions";
    public static final String CONSUMEDENTRIES = "consumedEntries";
    public static final String PROMOTION = "promotion";
    public static final String CERTAINTY = "certainty";
    public static final String CUSTOM = "custom";
    public static final String ORDER = "order";
    public static final String ALLPROMOTIONACTIONS = "allPromotionActions";
    protected static final BidirectionalOneToManyHandler<GeneratedPromotionResult> ORDERHANDLER = new BidirectionalOneToManyHandler(GeneratedPromotionsConstants.TC.PROMOTIONRESULT, false, "order", null, false, true, 1);
    protected static final OneToManyHandler<AbstractPromotionAction> ALLPROMOTIONACTIONSHANDLER = new OneToManyHandler(GeneratedPromotionsConstants.TC.ABSTRACTPROMOTIONACTION, true, "promotionResult", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("promotion", Item.AttributeMode.INITIAL);
        tmp.put("certainty", Item.AttributeMode.INITIAL);
        tmp.put("custom", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<AbstractPromotionAction> getActions()
    {
        return getActions(getSession().getSessionContext());
    }


    public void setActions(Collection<AbstractPromotionAction> value)
    {
        setActions(getSession().getSessionContext(), value);
    }


    public Set<AbstractPromotionAction> getAllPromotionActions(SessionContext ctx)
    {
        return (Set<AbstractPromotionAction>)ALLPROMOTIONACTIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<AbstractPromotionAction> getAllPromotionActions()
    {
        return getAllPromotionActions(getSession().getSessionContext());
    }


    public void setAllPromotionActions(SessionContext ctx, Set<AbstractPromotionAction> value)
    {
        ALLPROMOTIONACTIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setAllPromotionActions(Set<AbstractPromotionAction> value)
    {
        setAllPromotionActions(getSession().getSessionContext(), value);
    }


    public void addToAllPromotionActions(SessionContext ctx, AbstractPromotionAction value)
    {
        ALLPROMOTIONACTIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToAllPromotionActions(AbstractPromotionAction value)
    {
        addToAllPromotionActions(getSession().getSessionContext(), value);
    }


    public void removeFromAllPromotionActions(SessionContext ctx, AbstractPromotionAction value)
    {
        ALLPROMOTIONACTIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromAllPromotionActions(AbstractPromotionAction value)
    {
        removeFromAllPromotionActions(getSession().getSessionContext(), value);
    }


    public Float getCertainty(SessionContext ctx)
    {
        return (Float)getProperty(ctx, "certainty");
    }


    public Float getCertainty()
    {
        return getCertainty(getSession().getSessionContext());
    }


    public float getCertaintyAsPrimitive(SessionContext ctx)
    {
        Float value = getCertainty(ctx);
        return (value != null) ? value.floatValue() : 0.0F;
    }


    public float getCertaintyAsPrimitive()
    {
        return getCertaintyAsPrimitive(getSession().getSessionContext());
    }


    public void setCertainty(SessionContext ctx, Float value)
    {
        setProperty(ctx, "certainty", value);
    }


    public void setCertainty(Float value)
    {
        setCertainty(getSession().getSessionContext(), value);
    }


    public void setCertainty(SessionContext ctx, float value)
    {
        setCertainty(ctx, Float.valueOf(value));
    }


    public void setCertainty(float value)
    {
        setCertainty(getSession().getSessionContext(), value);
    }


    public Collection<PromotionOrderEntryConsumed> getConsumedEntries()
    {
        return getConsumedEntries(getSession().getSessionContext());
    }


    public void setConsumedEntries(Collection<PromotionOrderEntryConsumed> value)
    {
        setConsumedEntries(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ORDERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getCustom(SessionContext ctx)
    {
        return (String)getProperty(ctx, "custom");
    }


    public String getCustom()
    {
        return getCustom(getSession().getSessionContext());
    }


    public void setCustom(SessionContext ctx, String value)
    {
        setProperty(ctx, "custom", value);
    }


    public void setCustom(String value)
    {
        setCustom(getSession().getSessionContext(), value);
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


    public AbstractPromotion getPromotion(SessionContext ctx)
    {
        return (AbstractPromotion)getProperty(ctx, "promotion");
    }


    public AbstractPromotion getPromotion()
    {
        return getPromotion(getSession().getSessionContext());
    }


    public void setPromotion(SessionContext ctx, AbstractPromotion value)
    {
        setProperty(ctx, "promotion", value);
    }


    public void setPromotion(AbstractPromotion value)
    {
        setPromotion(getSession().getSessionContext(), value);
    }


    public abstract Collection<AbstractPromotionAction> getActions(SessionContext paramSessionContext);


    public abstract void setActions(SessionContext paramSessionContext, Collection<AbstractPromotionAction> paramCollection);


    public abstract Collection<PromotionOrderEntryConsumed> getConsumedEntries(SessionContext paramSessionContext);


    public abstract void setConsumedEntries(SessionContext paramSessionContext, Collection<PromotionOrderEntryConsumed> paramCollection);
}
