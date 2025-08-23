package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;

public class ProductPerfectPartnerPromotion extends GeneratedProductPerfectPartnerPromotion
{
    private static final Logger LOGGER = Logger.getLogger(ProductPerfectPartnerPromotion.class.getName());
    private static final float TRIGGER_BUT_NO_PARTNER = 0.75F;


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        deletePromotionPriceRows(ctx, getPartnerPrices(ctx));
        super.remove(ctx);
    }


    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> promotionResults = new ArrayList<>();
        Double partnerProductPrice = getPriceForOrder(ctx, getPartnerPrices(ctx), promoContext.getOrder(), "partnerPrices");
        if(partnerProductPrice != null)
        {
            PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(ctx, promoContext);
            if(rsr.isAllowedToContinue() && !rsr.getAllowedProducts().isEmpty())
            {
                PromotionOrderView triggerItemView = promoContext.createView(ctx, (AbstractPromotion)this, rsr.getAllowedProducts());
                PromotionOrderView partnerItemView = promoContext.createView(ctx, (AbstractPromotion)this, (List)
                                getPartnerProducts(ctx));
                while(triggerItemView.getTotalQuantity(ctx) > 0L)
                {
                    promoContext.startLoggingConsumed((AbstractPromotion)this);
                    triggerItemView.consume(ctx, 1L);
                    if(partnerItemView.getTotalQuantity(ctx) > 0L)
                    {
                        PromotionOrderEntryConsumed poec = partnerItemView.consume(ctx, 1L).get(0);
                        double partnerProductRetailPrice = poec.getUnitPrice(ctx);
                        double adjustment = partnerProductPrice.doubleValue() - partnerProductRetailPrice;
                        poec.setAdjustedUnitPrice(partnerProductPrice);
                        PromotionOrderEntryAdjustAction promotionOrderEntryAdjustAction = PromotionsManager.getInstance().createPromotionOrderEntryAdjustAction(ctx, poec.getOrderEntry(ctx), adjustment);
                        PromotionResult promotionResult = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                        .getOrder(), 1.0F);
                        promotionResult.setConsumedEntries(promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, true));
                        promotionResult.addAction(ctx, (AbstractPromotionAction)promotionOrderEntryAdjustAction);
                        promotionResults.add(promotionResult);
                        continue;
                    }
                    PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), 0.75F);
                    result.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, false));
                    promotionResults.add(result);
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
            Double offerPrice = getPriceForOrder(ctx, getPartnerPrices(ctx), promotionResult.getOrder(ctx), "partnerPrices");
            if(offerPrice != null)
            {
                if(promotionResult.getFired(ctx))
                {
                    double totalDiscount = promotionResult.getTotalDiscount(ctx);
                    Object[] args = {offerPrice, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, offerPrice.doubleValue()), Double.valueOf(totalDiscount), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount)};
                    return formatMessage(getMessageFired(ctx), args, locale);
                }
                if(promotionResult.getCouldFire(ctx))
                {
                    Object[] args = {offerPrice, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, offerPrice.doubleValue())};
                    return formatMessage(getMessageCouldHaveFired(ctx), args, locale);
                }
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        buildDataUniqueKeyForProducts(ctx, builder, getPartnerProducts(ctx));
        buildDataUniqueKeyForPriceRows(ctx, builder, getPartnerPrices(ctx));
    }


    protected void deepCloneAttributes(SessionContext ctx, Map<String, Collection> values)
    {
        super.deepCloneAttributes(ctx, values);
        values.remove("partnerPrices");
        values.put("partnerPrices", deepClonePriceRows(ctx, getPartnerPrices(ctx)));
    }
}
