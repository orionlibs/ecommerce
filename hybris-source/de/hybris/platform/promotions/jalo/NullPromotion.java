package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NullPromotion extends OrderPromotion
{
    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> promotionResults = new ArrayList<>();
        if(checkRestrictions(ctx, promoContext))
        {
            PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext.getOrder(), 1.0F);
            result.addAction(ctx, (AbstractPromotionAction)PromotionsManager.getInstance().createPromotionOrderAdjustTotalAction(ctx, 0.0D));
            promotionResults.add(result);
        }
        return promotionResults;
    }


    public String getResultDescription(SessionContext ctx, PromotionResult result, Locale locale)
    {
        AbstractOrder order = result.getOrder(ctx);
        if(order != null)
        {
            if(result.getFired(ctx))
            {
                Object[] args = new Object[0];
                return formatMessage("Fired", args, locale);
            }
            if(result.getCouldFire(ctx))
            {
                Object[] args = new Object[0];
                return formatMessage("Not Fired", args, locale);
            }
        }
        return "";
    }
}
