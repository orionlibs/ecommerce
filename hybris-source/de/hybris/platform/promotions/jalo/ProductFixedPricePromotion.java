package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
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

public class ProductFixedPricePromotion extends GeneratedProductFixedPricePromotion
{
    private static final Logger LOG = Logger.getLogger(ProductFixedPricePromotion.class);


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        deletePromotionPriceRows(ctx, getProductFixedUnitPrice(ctx));
        super.remove(ctx);
    }


    private boolean hasPromotionPriceRowForCurrency(AbstractOrder order, Collection<PromotionPriceRow> promotionPriceRows)
    {
        String name = getComposedType().getName() + " (" + getComposedType().getName() + ": " + getCode() + ")";
        if(promotionPriceRows.isEmpty())
        {
            LOG.warn(name + " has no PromotionPriceRow. Skipping evaluation");
            return false;
        }
        Currency currency = order.getCurrency();
        for(PromotionPriceRow ppr : promotionPriceRows)
        {
            if(currency.equals(ppr.getCurrency()))
            {
                return true;
            }
        }
        LOG.warn(name + " has no PromotionPriceRow for currency " + name + ". Skipping evaluation");
        return false;
    }


    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> results = new ArrayList<>();
        PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(ctx, promoContext);
        Collection<PromotionPriceRow> promotionPriceRows = getProductFixedUnitPrice(ctx);
        AbstractOrder order = promoContext.getOrder();
        boolean hasValidPromotionPriceRow = hasPromotionPriceRowForCurrency(order, promotionPriceRows);
        if(hasValidPromotionPriceRow && rsr.isAllowedToContinue() && !rsr.getAllowedProducts().isEmpty())
        {
            PromotionOrderView view = promoContext.createView(ctx, (AbstractPromotion)this, rsr.getAllowedProducts());
            while(view.getTotalQuantity(ctx) > 0L)
            {
                promoContext.startLoggingConsumed((AbstractPromotion)this);
                PromotionOrderEntry entry = view.peek(ctx);
                long quantityToDiscount = entry.getQuantity(ctx);
                long quantityOfOrderEntry = entry.getBaseOrderEntry().getQuantity(ctx).longValue();
                Double fixedUnitPrice = getPriceForOrder(ctx, promotionPriceRows, order, "productFixedUnitPrice");
                if(fixedUnitPrice != null)
                {
                    for(PromotionOrderEntryConsumed poec : view.consume(ctx, quantityToDiscount))
                    {
                        poec.setAdjustedUnitPrice(ctx, fixedUnitPrice);
                    }
                    double adjustment = quantityToDiscount * (fixedUnitPrice.doubleValue() - entry.getBasePrice(ctx).doubleValue());
                    PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), 1.0F);
                    result.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, true));
                    PromotionOrderEntryAdjustAction poeac = PromotionsManager.getInstance().createPromotionOrderEntryAdjustAction(ctx, entry.getBaseOrderEntry(), quantityOfOrderEntry, adjustment);
                    result.addAction(ctx, (AbstractPromotionAction)poeac);
                    results.add(result);
                    continue;
                }
                promoContext.abandonLogging((AbstractPromotion)this);
            }
            return results;
        }
        return results;
    }


    public String getResultDescription(SessionContext ctx, PromotionResult promotionResult, Locale locale)
    {
        AbstractOrder order = promotionResult.getOrder(ctx);
        if(order != null && promotionResult.getFired(ctx))
        {
            Double fixedUnitPrice = getPriceForOrder(ctx, getProductFixedUnitPrice(ctx), order, "productFixedUnitPrice");
            if(fixedUnitPrice != null)
            {
                double totalDiscount = promotionResult.getTotalDiscount(ctx);
                Currency orderCurrency = order.getCurrency(ctx);
                Object[] args = {fixedUnitPrice, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, fixedUnitPrice.doubleValue()), Double.valueOf(totalDiscount), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount)};
                return formatMessage(getMessageFired(ctx), args, locale);
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        buildDataUniqueKeyForPriceRows(ctx, builder, getProductFixedUnitPrice(ctx));
    }


    protected void deepCloneAttributes(SessionContext ctx, Map<String, Collection> values)
    {
        super.deepCloneAttributes(ctx, values);
        values.remove("productFixedUnitPrice");
        values.put("productFixedUnitPrice", deepClonePriceRows(ctx, getProductFixedUnitPrice(ctx)));
    }
}
