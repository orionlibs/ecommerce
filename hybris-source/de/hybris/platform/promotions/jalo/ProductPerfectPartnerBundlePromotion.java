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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;

public class ProductPerfectPartnerBundlePromotion extends GeneratedProductPerfectPartnerBundlePromotion
{
    private static final Logger LOGGER = Logger.getLogger(ProductPerfectPartnerBundlePromotion.class.getName());


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        deletePromotionPriceRows(ctx, getBundlePrices(ctx));
        super.remove(ctx);
    }


    public Product getBaseProduct(SessionContext ctx)
    {
        Collection<Product> products = getProducts(ctx);
        if(products != null && !products.isEmpty())
        {
            return products.iterator().next();
        }
        return null;
    }


    public void setBaseProduct(SessionContext ctx, Product value)
    {
        Collection<Product> products = getProducts(ctx);
        if(products != null && !products.isEmpty())
        {
            for(Product p : products)
            {
                removeFromProducts(ctx, p);
            }
        }
        addToProducts(ctx, value);
    }


    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> promotionResults = new ArrayList<>();
        Integer qualifyingCount = getQualifyingCount(ctx);
        if(qualifyingCount != null && qualifyingCount.intValue() > 0)
        {
            Double bundlePrice = getPriceForOrder(ctx, getBundlePrices(ctx), promoContext.getOrder(), "bundlePrices");
            if(bundlePrice != null)
            {
                PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(ctx, promoContext);
                if(rsr.isAllowedToContinue() && !rsr.getAllowedProducts().isEmpty())
                {
                    PromotionOrderView triggerItemView = promoContext.createView(ctx, (AbstractPromotion)this, rsr.getAllowedProducts());
                    List<Product> allPartnerProducts = (List<Product>)getPartnerProducts(ctx);
                    PromotionOrderView partnerItemView = promoContext.createView(ctx, (AbstractPromotion)this, allPartnerProducts);
                    while(triggerItemView.getTotalQuantity(ctx) > 0L)
                    {
                        promoContext.startLoggingConsumed((AbstractPromotion)this);
                        triggerItemView.consume(ctx, 1L);
                        if(partnerItemView.getTotalQuantity(ctx) > 0L)
                        {
                            List<PromotionOrderEntry> entriesSortedByPrice = partnerItemView.getAllEntriesByPrice(ctx);
                            ArrayList<Product> uniqueSortedPartnerProducts = new ArrayList<>(entriesSortedByPrice.size());
                            for(PromotionOrderEntry entry : entriesSortedByPrice)
                            {
                                Product product = entry.getProduct(ctx);
                                if(!uniqueSortedPartnerProducts.contains(product))
                                {
                                    uniqueSortedPartnerProducts.add(product);
                                }
                            }
                            long foundCount = 0L;
                            for(Product product : uniqueSortedPartnerProducts)
                            {
                                long availableQuantity = partnerItemView.getQuantity(ctx, product);
                                if(availableQuantity > 0L)
                                {
                                    partnerItemView.consume(ctx, product, 1L);
                                    foundCount++;
                                    if(foundCount >= qualifyingCount.longValue())
                                    {
                                        break;
                                    }
                                }
                            }
                            if(foundCount == qualifyingCount.longValue())
                            {
                                List<PromotionOrderEntryConsumed> consumedEntries = promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, true);
                                double bundleRetailValue = 0.0D;
                                for(PromotionOrderEntryConsumed poec : consumedEntries)
                                {
                                    bundleRetailValue += poec.getUnitPrice(ctx) * poec.getQuantityAsPrimitive(ctx);
                                }
                                Helper.adjustUnitPrices(ctx, promoContext, consumedEntries, bundlePrice.doubleValue(), bundleRetailValue);
                                PromotionOrderAdjustTotalAction poata = PromotionsManager.getInstance().createPromotionOrderAdjustTotalAction(ctx, bundlePrice.doubleValue() - bundleRetailValue);
                                List<AbstractPromotionAction> actions = new ArrayList<>();
                                actions.add(poata);
                                PromotionResult promotionResult1 = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                                .getOrder(), 1.0F);
                                promotionResult1.setConsumedEntries(ctx, consumedEntries);
                                promotionResult1.setActions(actions);
                                promotionResults.add(promotionResult1);
                                continue;
                            }
                            float f = (float)foundCount / qualifyingCount.floatValue();
                            PromotionResult promotionResult = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                            .getOrder(), f);
                            promotionResult.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, false));
                            promotionResults.add(promotionResult);
                            break;
                        }
                        float certainty = 0.5F;
                        PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                        .getOrder(), 0.5F);
                        result.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, false));
                        promotionResults.add(result);
                    }
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
            Double bundlePrices = getPriceForOrder(ctx, getBundlePrices(ctx), promotionResult.getOrder(ctx), "bundlePrices");
            if(bundlePrices != null)
            {
                if(promotionResult.getFired(ctx))
                {
                    double totalDiscount = promotionResult.getTotalDiscount(ctx);
                    Object[] args = {bundlePrices, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, bundlePrices.doubleValue()), Double.valueOf(totalDiscount), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount)};
                    return formatMessage(getMessageFired(ctx), args, locale);
                }
                if(promotionResult.getCouldFire(ctx))
                {
                    Integer qualifyingCount = getQualifyingCount(ctx);
                    if(qualifyingCount != null && qualifyingCount.intValue() > 0)
                    {
                        long consumedCount = promotionResult.getConsumedCount(ctx, true) - 1L;
                        long neededCount = qualifyingCount.longValue() - consumedCount;
                        if(LOGGER.isDebugEnabled())
                        {
                            LOGGER.debug("(" + getPK() + ") getResultDescription: consumedCount=[" + consumedCount + "] certainty=[" + promotionResult
                                            .getCertainty(ctx) + "] neededCount=[" + neededCount + "]");
                        }
                        Object[] args = {bundlePrices, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, bundlePrices.doubleValue()), Long.valueOf(neededCount)};
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
        buildDataUniqueKeyForProducts(ctx, builder, getPartnerProducts(ctx));
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
