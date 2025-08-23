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

public class OrderThresholdFreeVoucherPromotion extends GeneratedOrderThresholdFreeVoucherPromotion
{
    private static final Logger LOG = Logger.getLogger(OrderThresholdFreeVoucherPromotion.class.getName());


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        deletePromotionPriceRows(ctx, getThresholdTotals(ctx));
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
                AbstractOrder order = promoContext.getOrder();
                double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);
                if(orderSubtotalAfterDiscounts >= threshold.doubleValue())
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("(" + getPK() + ") evaluate: Subtotal " + orderSubtotalAfterDiscounts + ">" + threshold.doubleValue() + ".  Creating a null action.");
                    }
                    PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), 1.0F);
                    result.addAction(ctx, (AbstractPromotionAction)PromotionsManager.getInstance().createPromotionNullAction(ctx));
                    promotionResults.add(result);
                }
                else
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("(" + getPK() + ") evaluate: Subtotal " + orderSubtotalAfterDiscounts + "<" + threshold + ".  Skipping null action.");
                    }
                    float certainty = (float)(orderSubtotalAfterDiscounts / threshold.doubleValue());
                    PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), certainty);
                    promotionResults.add(result);
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
                if(result.getFired(ctx))
                {
                    Object[] args = {threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue())};
                    return formatMessage(getMessageFired(ctx), args, locale);
                }
                if(result.getCouldFire(ctx))
                {
                    double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);
                    double amountRequired = threshold.doubleValue() - orderSubtotalAfterDiscounts;
                    Object[] args = {threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()), Double.valueOf(amountRequired), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, amountRequired)};
                    return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
                }
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        builder.append(getFreeVoucher(ctx).getName(ctx)).append('|');
        buildDataUniqueKeyForPriceRows(ctx, builder, getThresholdTotals(ctx));
    }


    protected void deepCloneAttributes(SessionContext ctx, Map<String, Collection> values)
    {
        super.deepCloneAttributes(ctx, values);
        values.remove("thresholdTotals");
        values.put("thresholdTotals", deepClonePriceRows(ctx, getThresholdTotals(ctx)));
    }


    protected void buildPromotionResultDataUnigueKey(SessionContext ctx, PromotionResult promotionResult, StringBuilder builder)
    {
        builder.append(getFreeVoucher(ctx).getName(ctx)).append('|');
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
