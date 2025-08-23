package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderEntry;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;

public class OrderThresholdPerfectPartnerPromotion extends GeneratedOrderThresholdPerfectPartnerPromotion
{
    private static final Logger LOG = Logger.getLogger(OrderThresholdPerfectPartnerPromotion.class.getName());


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        deletePromotionPriceRows(ctx, getThresholdTotals(ctx));
        deletePromotionPriceRows(ctx, getProductPrices(ctx));
        super.remove(ctx);
    }


    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> promotionResults = new ArrayList<>();
        if(checkRestrictions(ctx, promoContext))
        {
            AbstractOrder order = promoContext.getOrder();
            double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("(" + getPK() + ") evaluate: orderSubtotalAfterDiscounts=[" + orderSubtotalAfterDiscounts + "] orderSubtotal=[" + order
                                .getSubtotal(ctx) + "] orderTotal=[" + order.getTotal(ctx) + "] totalDiscounts=[" + order
                                .getTotalDiscounts(ctx) + "] totalTax=[" + order.getTotalTax(ctx) + "]");
            }
            Double thresholdPriceValue = getPriceForOrder(ctx, getThresholdTotals(ctx), promoContext.getOrder(), "thresholdTotals");
            if(thresholdPriceValue != null)
            {
                List<Product> allowedProducts = new ArrayList<>(1);
                allowedProducts.add(getDiscountProduct(ctx));
                PromotionOrderView pov = promoContext.createView(ctx, (AbstractPromotion)this, allowedProducts);
                if(pov.getTotalQuantity(ctx) > 0L)
                {
                    Double discountPriceValue = getPriceForOrder(ctx, getProductPrices(ctx), promoContext.getOrder(), "productPrices");
                    if(discountPriceValue != null)
                    {
                        Comparator<PromotionOrderEntry> priceComparator = PromotionEvaluationContext.createPriceComparator(ctx);
                        double productPrice = pov.peekFromHead(ctx, priceComparator).getBasePrice(ctx).doubleValue();
                        double adjustedThreshold = thresholdPriceValue.doubleValue() + productPrice;
                        if(isIncludeDiscountedPriceInThreshold(ctx).booleanValue())
                        {
                            adjustedThreshold -= discountPriceValue.doubleValue();
                        }
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("(" + getPK() + ") evaluate: Order contains product. orderSubtotalAfterDiscounts=[" + orderSubtotalAfterDiscounts + "] thresholdPrice=[" + thresholdPriceValue + "] productPrice=[" + productPrice + "] discountPrice=[" + discountPriceValue
                                            + "] adjustedThreshold=[" + adjustedThreshold + "]");
                        }
                        if(orderSubtotalAfterDiscounts >= adjustedThreshold)
                        {
                            if(LOG.isDebugEnabled())
                            {
                                LOG.debug("(" + getPK() + ") evaluate: Order contains product and has reached threshold");
                            }
                            promoContext.startLoggingConsumed((AbstractPromotion)this);
                            PromotionOrderEntryConsumed poec = pov.consumeFromHead(ctx, priceComparator, 1L).get(0);
                            poec.setAdjustedUnitPrice(ctx, discountPriceValue);
                            double adjustment = discountPriceValue.doubleValue() - productPrice;
                            if(LOG.isDebugEnabled())
                            {
                                LOG.debug("(" + getPK() + ") evaluate: discountPrice=[" + discountPriceValue + "] productPrice=[" + productPrice + "] adjustment=[" + adjustment + "]");
                            }
                            PromotionResult currResult = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                            .getOrder(), 1.0F);
                            currResult.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, true));
                            currResult.addAction(ctx,
                                            (AbstractPromotionAction)PromotionsManager.getInstance().createPromotionOrderAdjustTotalAction(ctx, adjustment));
                            promotionResults.add(currResult);
                        }
                        else
                        {
                            if(LOG.isDebugEnabled())
                            {
                                LOG.debug("(" + getPK() + ") evaluate: Order contains product. But has not reached threshold");
                            }
                            promoContext.startLoggingConsumed((AbstractPromotion)this);
                            pov.consumeFromHead(ctx, priceComparator, 1L);
                            float certainty = 0.5F;
                            if(adjustedThreshold > 0.0D && orderSubtotalAfterDiscounts > 0.0D)
                            {
                                certainty = (float)(orderSubtotalAfterDiscounts / adjustedThreshold * 0.5D) + 0.5F;
                            }
                            PromotionResult currResult = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                            .getOrder(), certainty);
                            currResult.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, false));
                            promotionResults.add(currResult);
                        }
                    }
                }
                else if(orderSubtotalAfterDiscounts >= thresholdPriceValue.doubleValue())
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("(" + getPK() + ") evaluate: Order does not contain product, but has reached threshold value. orderSubtotalAfterDiscounts=[" + orderSubtotalAfterDiscounts + "] thresholdPrice=[" + thresholdPriceValue + "]");
                    }
                    PromotionResult currResult = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), 0.6F);
                    promotionResults.add(currResult);
                }
                else
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("(" + getPK() + ") evaluate: Order does not contain product, not reached threshold value. orderSubtotalAfterDiscounts=[" + orderSubtotalAfterDiscounts + "] thresholdPrice=[" + thresholdPriceValue + "]");
                    }
                    float certainty = 0.0F;
                    if(thresholdPriceValue.doubleValue() > 0.0D && orderSubtotalAfterDiscounts > 0.0D)
                    {
                        certainty = (float)(orderSubtotalAfterDiscounts / thresholdPriceValue.doubleValue() * 0.5D);
                    }
                    PromotionResult currResult = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), certainty);
                    promotionResults.add(currResult);
                }
            }
        }
        return promotionResults;
    }


    public String getResultDescription(SessionContext ctx, PromotionResult promotionResult, Locale locale)
    {
        AbstractOrder order = promotionResult.getOrder(ctx);
        if(order != null)
        {
            Currency orderCurrency = order.getCurrency(ctx);
            Double thresholdPriceValue = getPriceForOrder(ctx, getThresholdTotals(ctx), order, "thresholdTotals");
            if(thresholdPriceValue != null)
            {
                Double discountPriceValue = getPriceForOrder(ctx, getProductPrices(ctx), order, "productPrices");
                if(discountPriceValue != null)
                {
                    if(promotionResult.getFired(ctx))
                    {
                        double totalSaving = promotionResult.getTotalDiscount(ctx);
                        Object[] arrayOfObject = {thresholdPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, thresholdPriceValue.doubleValue()), discountPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue()),
                                        Double.valueOf(totalSaving), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalSaving)};
                        return formatMessage(getMessageFired(ctx), arrayOfObject, locale);
                    }
                    double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);
                    Collection<PromotionOrderEntryConsumed> consumedEntries = promotionResult.getConsumedEntries(ctx);
                    if(consumedEntries != null && !consumedEntries.isEmpty())
                    {
                        double productPrice = 0.0D;
                        for(PromotionOrderEntryConsumed entry : consumedEntries)
                        {
                            productPrice += entry.getEntryPrice(ctx);
                        }
                        double adjustedThreshold = thresholdPriceValue.doubleValue() + productPrice;
                        if(isIncludeDiscountedPriceInThreshold(ctx).booleanValue())
                        {
                            adjustedThreshold -= discountPriceValue.doubleValue();
                        }
                        double amountMoreRequiredToQualify = adjustedThreshold - orderSubtotalAfterDiscounts;
                        Object[] arrayOfObject = {thresholdPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, thresholdPriceValue.doubleValue()), discountPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue()),
                                        Double.valueOf(amountMoreRequiredToQualify), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, amountMoreRequiredToQualify)};
                        return formatMessage(getMessageProductNoThreshold(ctx), arrayOfObject, locale);
                    }
                    float certainty = promotionResult.getCertainty(ctx).floatValue();
                    if(certainty <= 0.5F)
                    {
                        double amountMoreRequiredToQualify = thresholdPriceValue.doubleValue() - orderSubtotalAfterDiscounts;
                        Object[] arrayOfObject = {thresholdPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, thresholdPriceValue.doubleValue()), discountPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue()),
                                        Double.valueOf(amountMoreRequiredToQualify), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, amountMoreRequiredToQualify)};
                        return formatMessage(getMessageCouldHaveFired(ctx), arrayOfObject, locale);
                    }
                    Object[] args = {thresholdPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, thresholdPriceValue.doubleValue()), discountPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue())};
                    return formatMessage(getMessageThresholdNoProduct(ctx), args, locale);
                }
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        buildDataUniqueKeyForPriceRows(ctx, builder, getThresholdTotals(ctx));
        builder.append(getDiscountProduct(ctx).getCode(ctx)).append('|');
        buildDataUniqueKeyForPriceRows(ctx, builder, getProductPrices(ctx));
    }


    protected void deepCloneAttributes(SessionContext ctx, Map<String, Collection> values)
    {
        super.deepCloneAttributes(ctx, values);
        values.remove("thresholdTotals");
        values.remove("productPrices");
        values.put("thresholdTotals", deepClonePriceRows(ctx, getThresholdTotals(ctx)));
        values.put("productPrices", deepClonePriceRows(ctx, getProductPrices(ctx)));
    }
}
