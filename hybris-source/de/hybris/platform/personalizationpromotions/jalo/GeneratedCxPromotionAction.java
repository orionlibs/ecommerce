package de.hybris.platform.personalizationpromotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.personalizationservices.jalo.CxAbstractAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxPromotionAction extends CxAbstractAction
{
    public static final String PROMOTIONID = "promotionId";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CxAbstractAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("promotionId", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getPromotionId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "promotionId");
    }


    public String getPromotionId()
    {
        return getPromotionId(getSession().getSessionContext());
    }


    public void setPromotionId(SessionContext ctx, String value)
    {
        setProperty(ctx, "promotionId", value);
    }


    public void setPromotionId(String value)
    {
        setPromotionId(getSession().getSessionContext(), value);
    }
}
