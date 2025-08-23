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
import org.apache.log4j.Logger;

public class OrderThresholdDiscountPromotion extends GeneratedOrderThresholdDiscountPromotion
{
    private static final Logger LOG = Logger.getLogger(OrderThresholdDiscountPromotion.class.getName());


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        deletePromotionPriceRows(ctx, getThresholdTotals(ctx));
        deletePromotionPriceRows(ctx, getDiscountPrices(ctx));
        super.remove(ctx);
    }


    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> promotionResults = new ArrayList<>();
        if(checkRestrictions(ctx, promoContext))
        {
            Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), promoContext.getOrder(), "thresholdTotals");
            if(threshold != null)
            {
                Double discountPriceValue = getPriceForOrder(ctx, getDiscountPrices(ctx), promoContext.getOrder(), "discountPrices");
                if(discountPriceValue != null)
                {
                    AbstractOrder order = promoContext.getOrder();
                    double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);
                    if(orderSubtotalAfterDiscounts >= threshold.doubleValue())
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("(" + getPK() + ") evaluate: Subtotal " + orderSubtotalAfterDiscounts + ">" + threshold + ".  Creating a discount action for value:" + discountPriceValue + ".");
                        }
                        PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                        .getOrder(), 1.0F);
                        double realDiscountPriceValue = discountPriceValue.doubleValue();
                        if(realDiscountPriceValue > orderSubtotalAfterDiscounts)
                        {
                            realDiscountPriceValue = orderSubtotalAfterDiscounts;
                        }
                        result.addAction(ctx,
                                        (AbstractPromotionAction)PromotionsManager.getInstance().createPromotionOrderAdjustTotalAction(ctx, -realDiscountPriceValue));
                        promotionResults.add(result);
                    }
                    else
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("(" + getPK() + ") evaluate: Subtotal " + orderSubtotalAfterDiscounts + "<" + threshold + ".  Skipping discount action.");
                        }
                        float certainty = (float)(orderSubtotalAfterDiscounts / threshold.doubleValue());
                        PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                        .getOrder(), certainty);
                        promotionResults.add(result);
                    }
                }
            }
        }
        return promotionResults;
    }


    public String getResultDescription(SessionContext ctx, PromotionResult result, Locale locale)
    {
        AbstractOrder order = result.getOrder(ctx);
        if(order != null)
        {
            Currency orderCurrency = order.getCurrency(ctx);
            Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), order, "thresholdTotals");
            if(threshold != null)
            {
                Double discountPriceValue = getPriceForOrder(ctx, getDiscountPrices(ctx), order, "discountPrices");
                if(discountPriceValue != null)
                {
                    if(result.getFired(ctx))
                    {
                        Object[] args = {threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()), discountPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue())};
                        return formatMessage(getMessageFired(ctx), args, locale);
                    }
                    if(result.getCouldFire(ctx))
                    {
                        double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);
                        double amountRequired = threshold.doubleValue() - orderSubtotalAfterDiscounts;
                        Object[] args = {threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()), discountPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue()), Double.valueOf(amountRequired),
                                        Helper.formatCurrencyAmount(ctx, locale, orderCurrency, amountRequired)};
                        return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
                    }
                }
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        buildDataUniqueKeyForPriceRows(ctx, builder, getThresholdTotals(ctx));
        buildDataUniqueKeyForPriceRows(ctx, builder, getDiscountPrices(ctx));
    }


    protected void deepCloneAttributes(SessionContext ctx, Map<String, Collection> values)
    {
        super.deepCloneAttributes(ctx, values);
        values.remove("thresholdTotals");
        values.remove("discountPrices");
        values.put("thresholdTotals", deepClonePriceRows(ctx, getThresholdTotals(ctx)));
        values.put("discountPrices", deepClonePriceRows(ctx, getDiscountPrices(ctx)));
    }


    protected void buildPromotionResultDataUnigueKey(SessionContext ctx, PromotionResult promotionResult, StringBuilder builder)
    {
        builder.append(promotionResult.getCertainty(ctx)).append('|');
        builder.append(promotionResult.getCustom(ctx)).append('|');
        Collection<AbstractPromotionAction> actions = promotionResult.getActions(ctx);
        if(actions != null && !actions.isEmpty())
        {
            for(AbstractPromotionAction action : actions)
            {
                builder.append(action.getClass().getSimpleName()).append('|');
            }
        }
    }
}
