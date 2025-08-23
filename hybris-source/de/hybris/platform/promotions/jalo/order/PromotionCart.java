package de.hybris.platform.promotions.jalo.order;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import java.util.HashSet;
import java.util.Set;

public class PromotionCart extends Cart
{
    public Object getAttribute(SessionContext ctx, String qualifier) throws JaloSecurityException
    {
        Object<PromotionResult> retval = null;
        if("allPromotionResults".equals(qualifier))
        {
            Set<PromotionResult> results = new HashSet<>();
            retval = (Object<PromotionResult>)results;
            PromotionOrderResults por = PromotionsManager.getInstance().getPromotionResults(ctx, (AbstractOrder)this);
            if(por != null)
            {
                results.addAll(por.getAllResults());
            }
        }
        else
        {
            retval = (Object<PromotionResult>)super.getAttribute(ctx, qualifier);
        }
        return retval;
    }
}
