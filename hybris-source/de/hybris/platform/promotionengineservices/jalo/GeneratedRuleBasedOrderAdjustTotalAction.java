package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRuleBasedOrderAdjustTotalAction extends AbstractRuleBasedPromotionAction
{
    public static final String AMOUNT = "amount";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRuleBasedPromotionAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("amount", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public BigDecimal getAmount(SessionContext ctx)
    {
        return (BigDecimal)getProperty(ctx, "amount");
    }


    public BigDecimal getAmount()
    {
        return getAmount(getSession().getSessionContext());
    }


    public void setAmount(SessionContext ctx, BigDecimal value)
    {
        setProperty(ctx, "amount", value);
    }


    public void setAmount(BigDecimal value)
    {
        setAmount(getSession().getSessionContext(), value);
    }
}
