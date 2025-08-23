package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderEntry;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.Logger;

public class ProductBOGOFPromotion extends GeneratedProductBOGOFPromotion
{
    private static final Logger LOG = Logger.getLogger(ProductBOGOFPromotion.class);


    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> results = new ArrayList<>();
        PromotionsManager.RestrictionSetResult restrictResult = findEligibleProductsInBasket(ctx, promoContext);
        if(restrictResult.isAllowedToContinue() && !restrictResult.getAllowedProducts().isEmpty())
        {
            int qualifyingCount = getQualifyingCount(ctx).intValue();
            int freeCount = getFreeCount(ctx).intValue();
            PromotionsManager promotionsManager = PromotionsManager.getInstance();
            PromotionOrderView orderView = promoContext.createView(ctx, (AbstractPromotion)this, restrictResult.getAllowedProducts());
            while(orderView.getTotalQuantity(ctx) >= qualifyingCount)
            {
                promoContext.startLoggingConsumed((AbstractPromotion)this);
                Comparator<PromotionOrderEntry> comparator = PromotionEvaluationContext.createPriceComparator(ctx);
                orderView.consumeFromTail(ctx, comparator, (qualifyingCount - freeCount));
                List<PromotionOrderEntryConsumed> freeItems = orderView.consumeFromHead(ctx, comparator, freeCount);
                List<AbstractPromotionAction> actions = new ArrayList<>();
                for(PromotionOrderEntryConsumed poec : freeItems)
                {
                    poec.setAdjustedUnitPrice(ctx, 0.0D);
                    double adjustment = poec.getEntryPrice(ctx) * -1.0D;
                    actions.add(promotionsManager.createPromotionOrderEntryAdjustAction(ctx, poec.getOrderEntry(ctx), adjustment));
                }
                PromotionResult result = promotionsManager.createPromotionResult(ctx, (AbstractPromotion)this, promoContext.getOrder(), 1.0F);
                List<PromotionOrderEntryConsumed> consumed = promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, true);
                result.setConsumedEntries(ctx, consumed);
                result.setActions(ctx, actions);
                results.add(result);
            }
            long remainingCount = orderView.getTotalQuantity(ctx);
            if(orderView.getTotalQuantity(ctx) > 0L)
            {
                promoContext.startLoggingConsumed((AbstractPromotion)this);
                orderView.consume(ctx, remainingCount);
                float certainty = (float)remainingCount / qualifyingCount;
                PromotionResult result = promotionsManager.createPromotionResult(ctx, (AbstractPromotion)this, promoContext.getOrder(), certainty);
                result.setConsumedEntries(promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, false));
                results.add(result);
            }
        }
        return results;
    }


    public String getResultDescription(SessionContext ctx, PromotionResult promotionResult, Locale locale)
    {
        AbstractOrder order = promotionResult.getOrder(ctx);
        if(order != null)
        {
            Currency orderCurrency = order.getCurrency(ctx);
            Integer qualifyingCount = getQualifyingCount(ctx);
            Integer freeCount = getFreeCount(ctx);
            if(promotionResult.getFired(ctx))
            {
                double totalDiscount = promotionResult.getTotalDiscount(ctx);
                Object[] args = {qualifyingCount, freeCount, Double.valueOf(totalDiscount), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount)};
                return formatMessage(getMessageFired(ctx), args, locale);
            }
            if(promotionResult.getCouldFire(ctx))
            {
                Object[] args = {Long.valueOf(getQualifyingCount(ctx).longValue() - promotionResult.getConsumedCount(ctx, true)), qualifyingCount, freeCount};
                return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        builder.append(getQualifyingCount(ctx)).append('|');
        builder.append(getFreeCount(ctx)).append('|');
    }
}
