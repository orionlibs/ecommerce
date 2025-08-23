package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;

public class ProductMultiBuyPromotion extends GeneratedProductMultiBuyPromotion
{
    private static final Logger LOG = Logger.getLogger(ProductMultiBuyPromotion.class);


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        deletePromotionPriceRows(ctx, getBundlePrices(ctx));
        super.remove(ctx);
    }


    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> promotionResults = new ArrayList<>();
        PromotionsManager.RestrictionSetResult restrictRes = findEligibleProductsInBasket(ctx, promoContext);
        List<Product> products = restrictRes.getAllowedProducts();
        if(restrictRes.isAllowedToContinue() && !restrictRes.getAllowedProducts().isEmpty())
        {
            Double promotionPriceValue = getPriceForOrder(ctx, getBundlePrices(ctx), promoContext.getOrder(), "bundlePrices");
            if(promotionPriceValue != null)
            {
                int triggerSize = getQualifyingCount(ctx).intValue();
                PromotionOrderView pov = promoContext.createView(ctx, (AbstractPromotion)this, products);
                while(pov.getTotalQuantity(ctx) >= triggerSize)
                {
                    promoContext.startLoggingConsumed((AbstractPromotion)this);
                    pov.consumeFromHead(ctx, PromotionEvaluationContext.createPriceComparator(ctx), triggerSize);
                    double thisPromoTotal = 0.0D;
                    List<PromotionOrderEntryConsumed> consumedEntries = promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, true);
                    if(consumedEntries != null && !consumedEntries.isEmpty())
                    {
                        for(PromotionOrderEntryConsumed poec : consumedEntries)
                        {
                            thisPromoTotal += poec.getEntryPrice(ctx);
                        }
                    }
                    double adjustment = promotionPriceValue.doubleValue() - thisPromoTotal;
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("(" + getPK() + ") evaluate: totalValueOfConsumedEntries=[" + thisPromoTotal + "] promotionPriceValue=[" + promotionPriceValue + "] adjustment=[" + adjustment + "]");
                    }
                    Helper.adjustUnitPrices(ctx, promoContext, consumedEntries, promotionPriceValue.doubleValue(), thisPromoTotal);
                    PromotionResult currResult = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), 1.0F);
                    currResult.setConsumedEntries(ctx, consumedEntries);
                    currResult.addAction(ctx, (AbstractPromotionAction)PromotionsManager.getInstance().createPromotionOrderAdjustTotalAction(ctx, adjustment));
                    promotionResults.add(currResult);
                }
                long remaining = pov.getTotalQuantity(ctx);
                if(remaining > 0L)
                {
                    promoContext.startLoggingConsumed((AbstractPromotion)this);
                    pov.consume(ctx, remaining);
                    float certainty = (float)remaining / triggerSize;
                    PromotionResult currResult = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), certainty);
                    currResult.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, false));
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
            int qualifyingCount = getQualifyingCount(ctx).intValue();
            Double promotionPriceValue = getPriceForOrder(ctx, getBundlePrices(ctx), promotionResult.getOrder(ctx), "bundlePrices");
            if(promotionPriceValue != null)
            {
                if(promotionResult.getFired(ctx))
                {
                    double totalDiscount = promotionResult.getTotalDiscount(ctx);
                    Object[] args = {Integer.valueOf(qualifyingCount), promotionPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, promotionPriceValue.doubleValue()), Double.valueOf(totalDiscount), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount)};
                    return formatMessage(getMessageFired(ctx), args, locale);
                }
                if(promotionResult.getCouldFire(ctx))
                {
                    Object[] args = {Integer.valueOf(qualifyingCount), promotionPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, promotionPriceValue.doubleValue()), Long.valueOf(qualifyingCount - promotionResult.getConsumedCount(ctx, true))};
                    return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
                }
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        builder.append(getQualifyingCount(ctx)).append('|');
        buildDataUniqueKeyForPriceRows(ctx, builder, getBundlePrices(ctx));
    }


    protected void deepCloneAttributes(SessionContext ctx, Map<String, Collection> values)
    {
        super.deepCloneAttributes(ctx, values);
        values.remove("bundlePrices");
        values.put("bundlePrices", deepClonePriceRows(ctx, getBundlePrices(ctx)));
    }
}
