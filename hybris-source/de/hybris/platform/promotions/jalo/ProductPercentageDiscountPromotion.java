package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderEntry;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.Logger;

public class ProductPercentageDiscountPromotion extends GeneratedProductPercentageDiscountPromotion
{
    private static final Logger LOG = Logger.getLogger(ProductPercentageDiscountPromotion.class);


    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> results = new ArrayList<>();
        PromotionsManager.RestrictionSetResult rsr = findEligibleProductsInBasket(ctx, promoContext);
        if(rsr.isAllowedToContinue() && !rsr.getAllowedProducts().isEmpty())
        {
            PromotionOrderView view = promoContext.createView(ctx, (AbstractPromotion)this, rsr.getAllowedProducts());
            PromotionsManager promotionsManager = PromotionsManager.getInstance();
            while(view.getTotalQuantity(ctx) > 0L)
            {
                promoContext.startLoggingConsumed((AbstractPromotion)this);
                PromotionOrderEntry entry = view.peek(ctx);
                BigDecimal quantityToDiscount = BigDecimal.valueOf(entry.getQuantity(ctx));
                BigDecimal quantityOfOrderEntry = BigDecimal.valueOf(entry.getBaseOrderEntry().getQuantity(ctx).longValue());
                BigDecimal percentageDiscount = (new BigDecimal(getPercentageDiscount(ctx).toString())).divide(new BigDecimal("100.0"));
                BigDecimal originalUnitPrice = new BigDecimal(entry.getBasePrice(ctx).toString());
                BigDecimal originalEntryPrice = originalUnitPrice.multiply(quantityToDiscount);
                Currency currency = promoContext.getOrder().getCurrency(ctx);
                BigDecimal adjustedEntryPrice = Helper.roundCurrencyValue(ctx, currency, originalEntryPrice
                                .subtract(originalEntryPrice.multiply(percentageDiscount)));
                BigDecimal adjustedUnitPrice = Helper.roundCurrencyValue(ctx, currency,
                                adjustedEntryPrice.equals(BigDecimal.ZERO) ? BigDecimal.ZERO :
                                                adjustedEntryPrice.divide(quantityToDiscount, RoundingMode.HALF_EVEN));
                BigDecimal fiddleAmount = adjustedEntryPrice.subtract(adjustedUnitPrice.multiply(quantityToDiscount));
                if(fiddleAmount.compareTo(BigDecimal.ZERO) == 0)
                {
                    for(PromotionOrderEntryConsumed poec : view.consume(ctx, quantityToDiscount.longValue()))
                    {
                        poec.setAdjustedUnitPrice(ctx, adjustedUnitPrice.doubleValue());
                    }
                }
                else
                {
                    for(PromotionOrderEntryConsumed poec : view.consume(ctx, quantityToDiscount.longValue() - 1L))
                    {
                        poec.setAdjustedUnitPrice(ctx, adjustedUnitPrice.doubleValue());
                    }
                    for(PromotionOrderEntryConsumed poec : view.consume(ctx, 1L))
                    {
                        poec.setAdjustedUnitPrice(ctx,
                                        Helper.roundCurrencyValue(ctx, currency, adjustedUnitPrice.add(fiddleAmount)).doubleValue());
                    }
                }
                PromotionResult result = promotionsManager.createPromotionResult(ctx, (AbstractPromotion)this, promoContext.getOrder(), 1.0F);
                result.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, true));
                BigDecimal adjustment = Helper.roundCurrencyValue(ctx, currency, adjustedEntryPrice
                                .subtract(originalEntryPrice));
                PromotionOrderEntryAdjustAction poeac = promotionsManager.createPromotionOrderEntryAdjustAction(ctx, entry
                                .getBaseOrderEntry(), quantityOfOrderEntry.longValue(), adjustment.doubleValue());
                result.addAction(ctx, (AbstractPromotionAction)poeac);
                results.add(result);
            }
            return results;
        }
        return results;
    }


    public String getResultDescription(SessionContext ctx, PromotionResult promotionResult, Locale locale)
    {
        AbstractOrder order = promotionResult.getOrder(ctx);
        if(order != null)
        {
            Currency orderCurrency = order.getCurrency(ctx);
            if(promotionResult.getFired(ctx))
            {
                Double totalDiscount = Double.valueOf(promotionResult.getTotalDiscount(ctx));
                Double percentageDiscount = getPercentageDiscount(ctx);
                Object[] args = {percentageDiscount, totalDiscount, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount.doubleValue())};
                return formatMessage(getMessageFired(ctx), args, locale);
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        builder.append(getPercentageDiscount(ctx)).append('|');
    }
}
