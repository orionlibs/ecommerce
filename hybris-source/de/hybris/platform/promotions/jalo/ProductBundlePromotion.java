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

public class ProductBundlePromotion extends GeneratedProductBundlePromotion
{
    private static final Logger LOG = Logger.getLogger(ProductBundlePromotion.class);


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        deletePromotionPriceRows(ctx, getBundlePrices(ctx));
        super.remove(ctx);
    }


    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> promotionResults = new ArrayList<>();
        PromotionsManager.RestrictionSetResult rsr = findAllProducts(ctx, promoContext);
        if(rsr.isAllowedToContinue() && !rsr.getAllowedProducts().isEmpty())
        {
            long foundCount;
            List<Product> allBundleProducts = rsr.getAllowedProducts();
            PromotionOrderView view = promoContext.createView(ctx, (AbstractPromotion)this, allBundleProducts);
            long neededToFireCount = allBundleProducts.size();
            while(true)
            {
                promoContext.startLoggingConsumed((AbstractPromotion)this);
                foundCount = 0L;
                for(Product product : allBundleProducts)
                {
                    long availableQuantity = view.getQuantity(ctx, product);
                    if(availableQuantity > 0L)
                    {
                        view.consume(ctx, product, 1L);
                        foundCount++;
                    }
                }
                if(foundCount == neededToFireCount)
                {
                    List<PromotionOrderEntryConsumed> consumedEntries = promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, true);
                    double bundleRetailValue = 0.0D;
                    for(PromotionOrderEntryConsumed poec : consumedEntries)
                    {
                        bundleRetailValue += poec.getUnitPrice(ctx);
                    }
                    Double offerValue = getPriceForOrder(ctx, getBundlePrices(ctx), promoContext.getOrder(), "bundlePrices");
                    if(offerValue != null)
                    {
                        Helper.adjustUnitPrices(ctx, promoContext, consumedEntries, offerValue.doubleValue(), bundleRetailValue);
                        PromotionOrderAdjustTotalAction poata = PromotionsManager.getInstance().createPromotionOrderAdjustTotalAction(ctx, offerValue.doubleValue() - bundleRetailValue);
                        List<AbstractPromotionAction> actions = new ArrayList<>();
                        actions.add(poata);
                        PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                        .getOrder(), 1.0F);
                        result.setConsumedEntries(ctx, consumedEntries);
                        result.setActions(actions);
                        promotionResults.add(result);
                        continue;
                    }
                    promoContext.abandonLogging((AbstractPromotion)this);
                    continue;
                }
                break;
            }
            if(foundCount > 0L)
            {
                float certainty = (float)foundCount / (float)neededToFireCount;
                PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                .getOrder(), certainty);
                result.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, false));
                promotionResults.add(result);
            }
            else
            {
                promoContext.abandonLogging((AbstractPromotion)this);
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
            Double offerValue = getPriceForOrder(ctx, getBundlePrices(ctx), promotionResult.getOrder(ctx), "bundlePrices");
            if(offerValue != null)
            {
                if(promotionResult.getFired(ctx))
                {
                    double totalDiscount = promotionResult.getTotalDiscount(ctx);
                    Object[] args = {offerValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, offerValue.doubleValue()), Double.valueOf(totalDiscount), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount)};
                    return formatMessage(getMessageFired(ctx), args, locale);
                }
                if(promotionResult.getCouldFire(ctx))
                {
                    long consumedCount = promotionResult.getConsumedCount(ctx, true);
                    long neededCount = Math.round((float)consumedCount / promotionResult.getCertainty(ctx).floatValue());
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("(" + getPK() + ") getResultDescription: consumedCount=[" + consumedCount + "] certainty=[" + promotionResult
                                        .getCertainty(ctx) + "] neededCount=[" + neededCount + "]");
                    }
                    Object[] args = {Long.valueOf(neededCount - consumedCount), offerValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, offerValue.doubleValue())};
                    return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
                }
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        buildDataUniqueKeyForPriceRows(ctx, builder, getBundlePrices(ctx));
    }


    protected void deepCloneAttributes(SessionContext ctx, Map<String, Collection> values)
    {
        super.deepCloneAttributes(ctx, values);
        values.remove("bundlePrices");
        values.put("bundlePrices", deepClonePriceRows(ctx, getBundlePrices(ctx)));
    }
}
