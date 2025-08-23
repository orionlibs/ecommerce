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

public class ProductOneToOnePerfectPartnerPromotion extends GeneratedProductOneToOnePerfectPartnerPromotion
{
    private static final Logger LOG = Logger.getLogger(ProductOneToOnePerfectPartnerPromotion.class);
    private static final float TRIGGER_BUT_NO_PARTNER = 0.75F;


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        deletePromotionPriceRows(ctx, getBundlePrices(ctx));
        super.remove(ctx);
    }


    public Product getBaseProduct(SessionContext ctx)
    {
        Collection<Product> products = getProducts(ctx);
        if(!products.isEmpty())
        {
            return products.iterator().next();
        }
        return null;
    }


    public void setBaseProduct(SessionContext ctx, Product value)
    {
        Collection<Product> products = getProducts(ctx);
        if(!products.isEmpty())
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
        PromotionsManager.RestrictionSetResult restrictRes = findEligibleProductsInBasket(ctx, promoContext);
        if(restrictRes.isAllowedToContinue() && !restrictRes.getAllowedProducts().isEmpty())
        {
            PromotionOrderView triggerItemView = promoContext.createView(ctx, (AbstractPromotion)this, restrictRes.getAllowedProducts());
            List<Product> partnerProducts = new ArrayList<>();
            partnerProducts.add(getPartnerProduct(ctx));
            PromotionOrderView partnerItemView = promoContext.createView(ctx, (AbstractPromotion)this, partnerProducts);
            PromotionsManager promotionsManager = PromotionsManager.getInstance();
            while(triggerItemView.getTotalQuantity(ctx) > 0L)
            {
                promoContext.startLoggingConsumed((AbstractPromotion)this);
                PromotionOrderEntryConsumed poecBase = triggerItemView.consume(ctx, 1L).get(0);
                if(partnerItemView.getTotalQuantity(ctx) > 0L)
                {
                    double baseProductRetailPrice = poecBase.getUnitPrice(ctx);
                    PromotionOrderEntryConsumed poec = partnerItemView.consume(ctx, 1L).get(0);
                    double partnerProductRetailPrice = poec.getUnitPrice(ctx);
                    Double bundlePrice = getPriceForOrder(ctx, getBundlePrices(ctx), promoContext.getOrder(), "bundlePrices");
                    double bundleRetailValue = baseProductRetailPrice + partnerProductRetailPrice;
                    List<PromotionOrderEntryConsumed> consumedEntries = promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, true);
                    Helper.adjustUnitPrices(ctx, promoContext, consumedEntries, bundlePrice.doubleValue(), bundleRetailValue);
                    PromotionOrderAdjustTotalAction promotionOrderAdjustTotalAction = promotionsManager.createPromotionOrderAdjustTotalAction(ctx, bundlePrice
                                    .doubleValue() - bundleRetailValue);
                    PromotionResult promotionResult = promotionsManager.createPromotionResult(ctx, (AbstractPromotion)this, promoContext.getOrder(), 1.0F);
                    promotionResult.setConsumedEntries(consumedEntries);
                    promotionResult.addAction(ctx, (AbstractPromotionAction)promotionOrderAdjustTotalAction);
                    promotionResults.add(promotionResult);
                    continue;
                }
                PromotionResult result = promotionsManager.createPromotionResult(ctx, (AbstractPromotion)this, promoContext.getOrder(), 0.75F);
                result.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, false));
                promotionResults.add(result);
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
            Double bundlePrice = getPriceForOrder(ctx, getBundlePrices(ctx), promotionResult.getOrder(ctx), "bundlePrices");
            if(bundlePrice != null)
            {
                if(promotionResult.getFired(ctx))
                {
                    double totalDiscount = promotionResult.getTotalDiscount(ctx);
                    Object[] args = {bundlePrice, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, bundlePrice.doubleValue()), Double.valueOf(totalDiscount), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount)};
                    return formatMessage(getMessageFired(ctx), args, locale);
                }
                if(promotionResult.getCouldFire(ctx))
                {
                    Object[] args = {bundlePrice, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, bundlePrice.doubleValue())};
                    return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
                }
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        builder.append(getPartnerProduct(ctx).getCode(ctx)).append('|');
        buildDataUniqueKeyForPriceRows(ctx, builder, getBundlePrices(ctx));
    }


    protected void deepCloneAttributes(SessionContext ctx, Map<String, Collection> values)
    {
        super.deepCloneAttributes(ctx, values);
        values.remove("bundlePrices");
        values.put("bundlePrices", deepClonePriceRows(ctx, getBundlePrices(ctx)));
    }
}
