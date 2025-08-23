package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class OrderPromotion extends GeneratedOrderPromotion
{
    private static Logger log = Logger.getLogger(OrderPromotion.class.getName());


    protected static final double getOrderSubtotalAfterDiscounts(SessionContext ctx, AbstractOrder order)
    {
        double orderSubtotalWithoutDiscounts = 0.0D;
        if(ctx != null && order != null)
        {
            try
            {
                order.calculateTotals(false);
            }
            catch(JaloPriceFactoryException ex)
            {
                log.error("orderSubtotalAfterDiscounts - failed to calculateTotals on order [" + order + "]", (Throwable)ex);
            }
            orderSubtotalWithoutDiscounts = order.getSubtotal(ctx).doubleValue() - order.getTotalDiscounts(ctx).doubleValue();
        }
        return orderSubtotalWithoutDiscounts;
    }


    protected boolean checkRestrictions(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        if(!promoContext.getObserveRestrictions())
        {
            return true;
        }
        AbstractOrder order = promoContext.getOrder();
        List<Product> products = new ArrayList<>();
        for(AbstractOrderEntry entry : order.getEntries())
        {
            products.add(entry.getProduct());
        }
        PromotionsManager.RestrictionSetResult evaluateRestrictions = PromotionsManager.getInstance().evaluateRestrictions(ctx, products, order, (AbstractPromotion)this, promoContext
                        .getDate());
        return evaluateRestrictions.isAllowedToContinue();
    }
}
