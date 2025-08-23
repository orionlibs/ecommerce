package de.hybris.platform.promotionengineservices.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.promotions.jalo.PromotionGroup;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RuleBasedPromotion extends GeneratedRuleBasedPromotion
{
    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        checkJaloCall("evaluate()");
        return Collections.emptyList();
    }


    public String getResultDescription(SessionContext ctx, PromotionResult promotionResult, Locale locale)
    {
        checkJaloCall("getResultDescription()");
        return "not supported";
    }


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        setRestrictions(ctx, Collections.emptyList());
        super.remove(ctx);
    }


    public void setPromotionGroup(SessionContext ctx, PromotionGroup promotionGroup)
    {
        if(promotionGroup == null)
        {
            throw new JaloInvalidParameterException("Cannot set promotionGroup to NULL", 999);
        }
        super.setPromotionGroup(ctx, promotionGroup);
    }


    protected void checkJaloCall(String methodName)
    {
        if(Config.getBoolean("promotionengineservices.prohibit.abstractpromotionaction.jalo.calls", false))
        {
            throw new IllegalStateException("calling RuleBasedPromotionAction." + methodName + " is not allowed as it is incompatible with promotionengineservices extension. You can disable this behavior by setting promotionengineservices.prohibit.abstractpromotionaction.jalo.calls=false.");
        }
    }
}
