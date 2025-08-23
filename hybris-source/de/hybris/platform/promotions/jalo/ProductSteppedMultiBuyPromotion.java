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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Logger;

public class ProductSteppedMultiBuyPromotion extends GeneratedProductSteppedMultiBuyPromotion
{
    private static final Logger LOG = Logger.getLogger(ProductSteppedMultiBuyPromotion.class.getName());


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        Collection<PromotionQuantityAndPricesRow> rows = getQualifyingCountsAndBundlePrices(ctx);
        if(rows != null && !rows.isEmpty())
        {
            for(PromotionQuantityAndPricesRow row : rows)
            {
                row.remove(ctx);
            }
        }
        super.remove(ctx);
    }


    public List<PromotionResult> evaluate(SessionContext ctx, PromotionEvaluationContext promoContext)
    {
        List<PromotionResult> promotionResults = new ArrayList<>();
        PromotionsManager.RestrictionSetResult restrictRes = findEligibleProductsInBasket(ctx, promoContext);
        List<Product> products = restrictRes.getAllowedProducts();
        if(restrictRes.isAllowedToContinue() && !restrictRes.getAllowedProducts().isEmpty())
        {
            SortedSet<QuantityPrice> steps = getSteps(ctx, promoContext.getOrder(),
                            getQualifyingCountsAndBundlePrices(ctx));
            if(steps != null && !steps.isEmpty())
            {
                PromotionOrderView pov = promoContext.createView(ctx, (AbstractPromotion)this, products);
                QuantityPrice lastTriggeredStep = null;
                while(true)
                {
                    QuantityPrice triggeredStep = findStep(steps, pov.getTotalQuantity(ctx));
                    if(triggeredStep == null)
                    {
                        break;
                    }
                    lastTriggeredStep = triggeredStep;
                    promoContext.startLoggingConsumed((AbstractPromotion)this);
                    pov.consumeFromHead(ctx, PromotionEvaluationContext.createPriceComparator(ctx), triggeredStep.getQuantity());
                    double thisPromoTotal = 0.0D;
                    List<PromotionOrderEntryConsumed> consumedEntries = promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, true);
                    if(consumedEntries != null && !consumedEntries.isEmpty())
                    {
                        for(PromotionOrderEntryConsumed poec : consumedEntries)
                        {
                            thisPromoTotal += poec.getEntryPrice(ctx);
                        }
                    }
                    double adjustment = triggeredStep.getPrice() - thisPromoTotal;
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("(" + getPK() + ") evaluate: triggeredStep quantity=[" + triggeredStep.getQuantity() + "] totalValueOfConsumedEntries=[" + thisPromoTotal + "] promotionPriceValue=[" + triggeredStep
                                        .getPrice() + "] adjustment=[" + adjustment + "]");
                    }
                    Helper.adjustUnitPrices(ctx, promoContext, consumedEntries, triggeredStep.getPrice(), thisPromoTotal);
                    PromotionResult currResult = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), 1.0F);
                    currResult.setConsumedEntries(ctx, consumedEntries);
                    currResult.addAction(ctx, (AbstractPromotionAction)PromotionsManager.getInstance().createPromotionOrderAdjustTotalAction(ctx, adjustment));
                    promotionResults.add(currResult);
                }
                long remaining = pov.getTotalQuantity(ctx);
                if(remaining > 0L)
                {
                    float certainty;
                    QuantityPrice nextStep = findNextStep(steps, lastTriggeredStep);
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("(" + getPK() + ") evaluate: nextStep for protential promotion quantity=[" + nextStep.getQuantity() + "]");
                    }
                    if(remaining >= nextStep.getQuantity())
                    {
                        LOG.error("(" + getPK() + ") evaluate: nextStep for protential promotion, remaining=[" + remaining + "] is greater than or equal to nextStep.quantity=[" + nextStep
                                        .getQuantity() + "]");
                    }
                    promoContext.startLoggingConsumed((AbstractPromotion)this);
                    pov.consume(ctx, remaining);
                    if(lastTriggeredStep != null && lastTriggeredStep != steps.first())
                    {
                        certainty = (float)(remaining + lastTriggeredStep.getQuantity()) / (float)(nextStep.getQuantity() + lastTriggeredStep.getQuantity());
                    }
                    else
                    {
                        certainty = (float)remaining / (float)nextStep.getQuantity();
                    }
                    PromotionResult currResult = PromotionsManager.getInstance().createPromotionResult(ctx, (AbstractPromotion)this, promoContext
                                    .getOrder(), certainty);
                    currResult.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed((AbstractPromotion)this, false));
                    currResult.setCustom(ctx, String.valueOf(nextStep.getQuantity()));
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
            SortedSet<QuantityPrice> steps = getSteps(ctx, promotionResult.getOrder(),
                            getQualifyingCountsAndBundlePrices(ctx));
            if(steps != null && !steps.isEmpty())
            {
                if(promotionResult.getFired(ctx))
                {
                    double promotionPriceValue = 0.0D;
                    long consumedCount = promotionResult.getConsumedCount(ctx, false);
                    for(QuantityPrice step : steps)
                    {
                        if(step.getQuantity() == consumedCount)
                        {
                            promotionPriceValue = step.getPrice();
                        }
                    }
                    double totalDiscount = promotionResult.getTotalDiscount(ctx);
                    ArrayList<Long> args = new ArrayList();
                    args.add(Long.valueOf(consumedCount));
                    args.add(Double.valueOf(promotionPriceValue));
                    args.add(Helper.formatCurrencyAmount(ctx, locale, orderCurrency, promotionPriceValue));
                    args.add(Double.valueOf(totalDiscount));
                    args.add(Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount));
                    ArrayList<QuantityPrice> ascendingSteps = new ArrayList<>(steps);
                    Collections.reverse(ascendingSteps);
                    for(QuantityPrice step : ascendingSteps)
                    {
                        args.add(Long.valueOf(step.getQuantity()));
                        args.add(Double.valueOf(step.getPrice()));
                        args.add(Helper.formatCurrencyAmount(ctx, locale, orderCurrency, step.getPrice()));
                    }
                    return formatMessage(getMessageFired(ctx), args.toArray(), locale);
                }
                if(promotionResult.getCouldFire(ctx))
                {
                    String customData = promotionResult.getCustom(ctx);
                    if(customData != null && customData.length() > 0)
                    {
                        long nextStepQunatity = Long.parseLong(customData);
                        double nextStepPrice = 0.0D;
                        QuantityPrice nextStep = null;
                        for(QuantityPrice step : steps)
                        {
                            if(step.getQuantity() == nextStepQunatity)
                            {
                                nextStep = step;
                                nextStepPrice = step.getPrice();
                            }
                        }
                        QuantityPrice currentStep = null;
                        if(nextStep != null)
                        {
                            SortedSet<QuantityPrice> lowerSteps = steps.tailSet(nextStep);
                            if(lowerSteps.size() > 1)
                            {
                                Iterator<QuantityPrice> iter = lowerSteps.iterator();
                                iter.next();
                                currentStep = iter.next();
                            }
                        }
                        long currentStepQuantity = 0L;
                        if(currentStep != null)
                        {
                            currentStepQuantity = currentStep.getQuantity();
                        }
                        ArrayList<Long> args = new ArrayList();
                        args.add(Long.valueOf(nextStepQunatity));
                        args.add(Double.valueOf(nextStepPrice));
                        args.add(Helper.formatCurrencyAmount(ctx, locale, orderCurrency, nextStepPrice));
                        args.add(Long.valueOf(nextStepQunatity - currentStepQuantity - promotionResult.getConsumedCount(ctx, true)));
                        ArrayList<QuantityPrice> ascendingSteps = new ArrayList<>(steps);
                        Collections.reverse(ascendingSteps);
                        for(QuantityPrice step : ascendingSteps)
                        {
                            args.add(Long.valueOf(step.getQuantity()));
                            args.add(Double.valueOf(step.getPrice()));
                            args.add(Helper.formatCurrencyAmount(ctx, locale, orderCurrency, step.getPrice()));
                        }
                        return formatMessage(getMessageCouldHaveFired(ctx), args.toArray(), locale);
                    }
                }
            }
        }
        return "";
    }


    protected void buildDataUniqueKey(SessionContext ctx, StringBuilder builder)
    {
        super.buildDataUniqueKey(ctx, builder);
        Collection<PromotionQuantityAndPricesRow> rows = getQualifyingCountsAndBundlePrices(ctx);
        if(rows != null && !rows.isEmpty())
        {
            builder.append(rows.size()).append('|');
            for(PromotionQuantityAndPricesRow row : rows)
            {
                builder.append(row.getQuantity(ctx)).append('|');
                buildDataUniqueKeyForPriceRows(ctx, builder, row.getPrices(ctx));
            }
        }
        else
        {
            builder.append('0').append('|');
        }
    }


    protected void deepCloneAttributes(SessionContext ctx, Map<String, Collection<PromotionQuantityAndPricesRow>> values)
    {
        super.deepCloneAttributes(ctx, values);
        values.remove("qualifyingCountsAndBundlePrices");
        values.put("qualifyingCountsAndBundlePrices",
                        deepCloneQuantityAndPricesRows(ctx, getQualifyingCountsAndBundlePrices(ctx)));
    }


    protected static Collection<PromotionQuantityAndPricesRow> deepCloneQuantityAndPricesRows(SessionContext ctx, Collection<PromotionQuantityAndPricesRow> rows)
    {
        Collection<PromotionQuantityAndPricesRow> dupRows = new ArrayList<>();
        if(rows != null && !rows.isEmpty())
        {
            for(PromotionQuantityAndPricesRow row : rows)
            {
                dupRows.add(PromotionsManager.getInstance().createPromotionQuantityAndPricesRow(ctx, row.getQuantity(ctx).longValue(),
                                deepClonePriceRows(ctx, row.getPrices(ctx))));
            }
        }
        return dupRows;
    }


    protected SortedSet<QuantityPrice> getSteps(SessionContext ctx, AbstractOrder order, Collection<PromotionQuantityAndPricesRow> rows)
    {
        SortedSet<QuantityPrice> qualifyingCountAndPrices = new TreeSet<>((a, b) -> Long.valueOf(b.getQuantity()).compareTo(Long.valueOf(a.getQuantity())));
        if(rows != null && !rows.isEmpty())
        {
            for(PromotionQuantityAndPricesRow row : rows)
            {
                long quantity = row.getQuantity(ctx).longValue();
                if(quantity > 0L)
                {
                    Double promotionPriceValue = getPriceForOrder(ctx, row.getPrices(ctx), order, "qualifyingCountsAndBundlePrices");
                    if(promotionPriceValue != null)
                    {
                        qualifyingCountAndPrices.add(new QuantityPrice(quantity, promotionPriceValue.doubleValue()));
                    }
                }
            }
        }
        return qualifyingCountAndPrices;
    }


    protected static QuantityPrice findStep(SortedSet<QuantityPrice> steps, long count)
    {
        for(QuantityPrice step : steps)
        {
            if(step.getQuantity() <= count)
            {
                return step;
            }
        }
        return null;
    }


    protected static QuantityPrice findNextStep(SortedSet<QuantityPrice> steps, QuantityPrice lastTriggeredStep)
    {
        QuantityPrice nextStep;
        if(lastTriggeredStep == null)
        {
            nextStep = steps.last();
        }
        else
        {
            SortedSet<QuantityPrice> higherQuantitySteps = steps.headSet(lastTriggeredStep);
            if(higherQuantitySteps != null && !higherQuantitySteps.isEmpty())
            {
                nextStep = higherQuantitySteps.last();
            }
            else
            {
                nextStep = steps.last();
            }
        }
        return nextStep;
    }
}
