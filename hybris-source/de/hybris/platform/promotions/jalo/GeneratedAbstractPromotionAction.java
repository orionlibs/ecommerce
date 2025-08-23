package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.constants.GeneratedPromotionsConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractPromotionAction extends GenericItem
{
    public static final String MARKEDAPPLIED = "markedApplied";
    public static final String GUID = "guid";
    public static final String PROMOTIONRESULT = "promotionResult";
    protected static final BidirectionalOneToManyHandler<GeneratedAbstractPromotionAction> PROMOTIONRESULTHANDLER = new BidirectionalOneToManyHandler(GeneratedPromotionsConstants.TC.ABSTRACTPROMOTIONACTION, false, "promotionResult", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("markedApplied", Item.AttributeMode.INITIAL);
        tmp.put("guid", Item.AttributeMode.INITIAL);
        tmp.put("promotionResult", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PROMOTIONRESULTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getGuid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "guid");
    }


    public String getGuid()
    {
        return getGuid(getSession().getSessionContext());
    }


    public void setGuid(SessionContext ctx, String value)
    {
        setProperty(ctx, "guid", value);
    }


    public void setGuid(String value)
    {
        setGuid(getSession().getSessionContext(), value);
    }


    public Boolean isMarkedApplied(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "markedApplied");
    }


    public Boolean isMarkedApplied()
    {
        return isMarkedApplied(getSession().getSessionContext());
    }


    public boolean isMarkedAppliedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isMarkedApplied(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isMarkedAppliedAsPrimitive()
    {
        return isMarkedAppliedAsPrimitive(getSession().getSessionContext());
    }


    public void setMarkedApplied(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "markedApplied", value);
    }


    public void setMarkedApplied(Boolean value)
    {
        setMarkedApplied(getSession().getSessionContext(), value);
    }


    public void setMarkedApplied(SessionContext ctx, boolean value)
    {
        setMarkedApplied(ctx, Boolean.valueOf(value));
    }


    public void setMarkedApplied(boolean value)
    {
        setMarkedApplied(getSession().getSessionContext(), value);
    }


    public PromotionResult getPromotionResult(SessionContext ctx)
    {
        return (PromotionResult)getProperty(ctx, "promotionResult");
    }


    public PromotionResult getPromotionResult()
    {
        return getPromotionResult(getSession().getSessionContext());
    }


    public void setPromotionResult(SessionContext ctx, PromotionResult value)
    {
        PROMOTIONRESULTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setPromotionResult(PromotionResult value)
    {
        setPromotionResult(getSession().getSessionContext(), value);
    }
}
