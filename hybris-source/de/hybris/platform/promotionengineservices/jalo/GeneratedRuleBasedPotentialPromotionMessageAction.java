package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRuleBasedPotentialPromotionMessageAction extends AbstractRuleBasedPromotionAction
{
    public static final String PARAMETERS = "parameters";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRuleBasedPromotionAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("parameters", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<PromotionActionParameter> getParameters(SessionContext ctx)
    {
        Collection<PromotionActionParameter> coll = (Collection<PromotionActionParameter>)getProperty(ctx, "parameters");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<PromotionActionParameter> getParameters()
    {
        return getParameters(getSession().getSessionContext());
    }


    public void setParameters(SessionContext ctx, Collection<PromotionActionParameter> value)
    {
        (new Object(this))
                        .setValue(ctx, value);
    }


    public void setParameters(Collection<PromotionActionParameter> value)
    {
        setParameters(getSession().getSessionContext(), value);
    }
}
