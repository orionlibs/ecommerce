package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.util.Helper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderThresholdChangeDeliveryModePromotion extends GeneratedOrderThresholdChangeDeliveryModePromotion
{
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        deletePromotionPriceRows(ctx, getThresholdTotals(ctx));
        super.remove(ctx);
    }


    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> results = new ArrayList<>();
        if(checkRestrictions(ctx, promoContext))
        {
            AbstractOrder order = promoContext.getOrder();
            double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);
            Double thresholdPriceValue = getPriceForOrder(ctx, getThresholdTotals(ctx), promoContext.getOrder(), "thresholdTotals");
            if(thresholdPriceValue != null)
            {
                if(orderSubtotalAfterDiscounts >= thresholdPriceValue.doubleValue())
                {
                    PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), 1.0F);
                    result.addAction(ctx, (AbstractPromotionAction)PromotionsManager.getInstance().createPromotionOrderChangeDeliveryModeAction(ctx,
                                    getDeliveryMode(ctx)));
                    results.add(result);
                }
                else
                {
                    float certainty = (float)orderSubtotalAfterDiscounts / (float)thresholdPriceValue.doubleValue();
                    PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), certainty);
                    results.add(result);
                }
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
            Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), order, "thresholdTotals");
            if(threshold != null)
            {
                if(promotionResult.getFired(ctx))
                {
                    Object[] args = {threshold, Helper.formatCurrencyAmount(ctx, locale, order.getCurrency(ctx), threshold.doubleValue())};
                    return formatMessage(getMessageFired(ctx), args, locale);
                }
                if(promotionResult.getCouldFire(ctx))
                {
                    double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);
                    double difference = threshold.doubleValue() - orderSubtotalAfterDiscounts;
                    Object[] args = {threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()), Double.valueOf(difference), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, difference)};
                    return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
                }
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        builder.append(getDeliveryMode(ctx).getCode(ctx)).append('|');
        buildDataUniqueKeyForPriceRows(ctx, builder, getThresholdTotals(ctx));
    }


    protected void deepCloneAttributes(SessionContext ctx, Map<String, Collection> values)
    {
        super.deepCloneAttributes(ctx, values);
        values.remove("thresholdTotals");
        values.put("thresholdTotals", deepClonePriceRows(ctx, getThresholdTotals(ctx)));
    }
}
