package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.promotions.constants.GeneratedPromotionsConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionGroup extends GenericItem
{
    public static final String IDENTIFIER = "Identifier";
    public static final String PROMOTIONS = "Promotions";
    protected static final OneToManyHandler<AbstractPromotion> PROMOTIONSHANDLER = new OneToManyHandler(GeneratedPromotionsConstants.TC.ABSTRACTPROMOTION, true, "PromotionGroup", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("Identifier", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getIdentifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "Identifier");
    }


    public String getIdentifier()
    {
        return getIdentifier(getSession().getSessionContext());
    }


    public void setIdentifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "Identifier", value);
    }


    public void setIdentifier(String value)
    {
        setIdentifier(getSession().getSessionContext(), value);
    }


    public Collection<AbstractPromotion> getPromotions(SessionContext ctx)
    {
        return PROMOTIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<AbstractPromotion> getPromotions()
    {
        return getPromotions(getSession().getSessionContext());
    }


    public void setPromotions(SessionContext ctx, Collection<AbstractPromotion> value)
    {
        PROMOTIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setPromotions(Collection<AbstractPromotion> value)
    {
        setPromotions(getSession().getSessionContext(), value);
    }


    public void addToPromotions(SessionContext ctx, AbstractPromotion value)
    {
        PROMOTIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToPromotions(AbstractPromotion value)
    {
        addToPromotions(getSession().getSessionContext(), value);
    }


    public void removeFromPromotions(SessionContext ctx, AbstractPromotion value)
    {
        PROMOTIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromPromotions(AbstractPromotion value)
    {
        removeFromPromotions(getSession().getSessionContext(), value);
    }
}
