package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.util.DiscountValue;
import org.apache.log4j.Logger;

public class PromotionOrderAdjustTotalAction extends GeneratedPromotionOrderAdjustTotalAction
{
    private static Logger log = Logger.getLogger(PromotionOrderAdjustTotalAction.class.getName());


    public boolean apply(SessionContext ctx)
    {
        AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
        String code = getGuid(ctx);
        if(log.isDebugEnabled())
        {
            log.debug("(" + getPK() + ") apply: Order total is currently: " + order.getTotal(ctx));
        }
        DiscountValue dv = new DiscountValue(code, getAmount(ctx).doubleValue() * -1.0D, true, order.getCurrency(ctx).getIsoCode(ctx));
        insertFirstGlobalDiscountValue(ctx, order, dv);
        if(log.isDebugEnabled())
        {
            log.debug("(" + getPK() + ") apply: Generated discount with name '" + code + "' for " + getAmount(ctx));
        }
        setMarkedApplied(ctx, true);
        return true;
    }


    public boolean undo(SessionContext ctx)
    {
        AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
        boolean calculateTotals = false;
        DiscountValue myDiscount = Helper.findGlobalDiscountValue(ctx, order, getGuid(ctx));
        if(myDiscount != null)
        {
            order.removeGlobalDiscountValue(ctx, myDiscount);
            calculateTotals = true;
        }
        setMarkedApplied(ctx, false);
        return calculateTotals;
    }


    public boolean isAppliedToOrder(SessionContext ctx)
    {
        AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
        return (Helper.findGlobalDiscountValue(ctx, order, getGuid(ctx)) != null);
    }


    public double getValue(SessionContext ctx)
    {
        return -1.0D * getAmount(ctx).doubleValue();
    }
}
