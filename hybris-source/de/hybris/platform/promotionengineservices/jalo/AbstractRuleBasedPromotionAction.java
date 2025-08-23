package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.Config;

public abstract class AbstractRuleBasedPromotionAction extends GeneratedAbstractRuleBasedPromotionAction
{
    public boolean apply(SessionContext ctx)
    {
        checkJaloCall("apply()");
        return false;
    }


    public boolean undo(SessionContext ctx)
    {
        checkJaloCall("undo()");
        return false;
    }


    public boolean isAppliedToOrder(SessionContext ctx)
    {
        checkJaloCall("isAppliedToOrder()");
        return false;
    }


    public double getValue(SessionContext ctx)
    {
        checkJaloCall("getValue()");
        return 0.0D;
    }


    protected void checkJaloCall(String methodName)
    {
        if(Config.getBoolean("promotionengineservices.prohibit.abstractpromotionaction.jalo.calls", false))
        {
            throw new IllegalStateException("calling RuleBasedPromotionAction." + methodName + " is not allowed as it is incompatible with promotionengineservices extension. You can disable this behavior by setting promotionengineservices.prohibit.abstractpromotionaction.jalo.calls=false.");
        }
    }
}
