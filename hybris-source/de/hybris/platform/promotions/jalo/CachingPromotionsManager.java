package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.result.PromotionException;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.promotions.util.Helper;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

public class CachingPromotionsManager extends PromotionsManager
{
    private static final Logger LOG = Logger.getLogger(CachingPromotionsManager.class);
    protected static final String CACHING_ALLOWED = "de.hybris.platform.promotions.jalo.cachingAllowed";
    private CachingStrategy cache;


    public PromotionOrderResults updatePromotions(SessionContext ctx, Collection<PromotionGroup> promotionGroups, AbstractOrder order)
    {
        return updatePromotions(ctx, promotionGroups, order, true, PromotionsManager.AutoApplyMode.APPLY_ALL, PromotionsManager.AutoApplyMode.KEEP_APPLIED,
                        Helper.getDateNowRoundedToMinute());
    }


    public PromotionOrderResults updatePromotions(SessionContext ctx, Collection<PromotionGroup> promotionGroups, AbstractOrder order, boolean evaluateRestrictions, PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, Date date)
    {
        return applyWithCachingEnabled(ctx, order, (c, o) -> doUpdatePromotions(c, promotionGroups, o, evaluateRestrictions, productPromotionMode, orderPromotionMode, date));
    }


    public PromotionOrderResults getPromotionResults(SessionContext ctx, AbstractOrder order)
    {
        return applyWithCachingEnabled(ctx, order, (c, o) -> super.getPromotionResults(c, o));
    }


    public void cleanupCart(SessionContext ctx, Cart cart)
    {
        acceptWithCachingEnabled(ctx, (AbstractOrder)cart, (c, o) -> super.cleanupCart(c, (Cart)o));
    }


    public void transferPromotionsToOrder(SessionContext ctx, AbstractOrder source, Order target, boolean onlyTransferAppliedPromotions)
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("transferPromotionsToOrder from [" + source + "] to [" + target + "] onlyTransferAppliedPromotions=[" + onlyTransferAppliedPromotions + "]");
                LOG.debug("Dump Source Order\r\n" + Helper.dumpOrder(ctx, source));
                LOG.debug("Dump Target Order\r\n" + Helper.dumpOrder(ctx, (AbstractOrder)target));
            }
            List<PromotionResult> promotionResults = applyWithCachingEnabled(ctx, source, (c, o) -> getPromotionResultsInternal(c, o));
            if(promotionResults != null && !promotionResults.isEmpty())
            {
                promotionResults.stream().filter(r -> (!onlyTransferAppliedPromotions || r.isApplied(ctx)))
                                .forEach(r -> r.transferToOrder(ctx, target));
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("transferPromotionsToOrder completed");
                LOG.debug("Dump Target Order after transfer\r\n" + Helper.dumpOrder(ctx, (AbstractOrder)target));
            }
        }
        catch(Exception ex)
        {
            LOG.error("Failed to transferPromotionsToOrder", ex);
        }
    }


    protected List<PromotionResult> getPromotionResultsInternal(SessionContext ctx, AbstractOrder order)
    {
        List<PromotionResult> results;
        if(isCachingAllowed(ctx))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Using Promotion Results from Cache");
            }
            results = this.cache.get(order.getCode(ctx));
            if(Objects.isNull(results))
            {
                results = Collections.emptyList();
            }
        }
        else
        {
            results = getNonCachedPromotionResultsInternal(ctx, order);
        }
        return results;
    }


    protected void deleteStoredPromotionResults(SessionContext ctx, AbstractOrder order, boolean undoActions)
    {
        if(isCachingAllowed(ctx))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Using Promotion Results from Cache");
            }
            List<PromotionResult> results = this.cache.get(order.getCode(ctx));
            if(results != null)
            {
                boolean calculateTotals = false;
                if(undoActions)
                {
                    for(PromotionResult result : results)
                    {
                        calculateTotals |= result.undo(ctx);
                    }
                }
                if(calculateTotals)
                {
                    try
                    {
                        order.calculateTotals(true);
                    }
                    catch(JaloPriceFactoryException ex)
                    {
                        LOG.error("deleteStoredPromotionResult failed to calculateTotals on order [" + order + "]", (Throwable)ex);
                    }
                }
                this.cache.remove(order.getCode(ctx));
            }
        }
        else
        {
            super.deleteStoredPromotionResults(ctx, order, undoActions);
        }
    }


    public PromotionOrderEntryConsumed createPromotionOrderEntryConsumed(SessionContext ctx, String code, AbstractOrderEntry orderEntry, long quantity)
    {
        if(isCachingAllowed(ctx))
        {
            double unitPrice = orderEntry.getBasePrice(ctx).doubleValue();
            Map<Object, Object> parameters = new HashMap<>();
            parameters.put("code", code);
            parameters.put("orderEntry", orderEntry);
            parameters.put("quantity", Long.valueOf(quantity));
            parameters.put("adjustedUnitPrice", Double.valueOf(unitPrice));
            return (PromotionOrderEntryConsumed)createCachedPromotionOrderEntryConsumed(ctx, parameters);
        }
        return super.createPromotionOrderEntryConsumed(ctx, code, orderEntry, quantity);
    }


    public PromotionOrderEntryConsumed createPromotionOrderEntryConsumed(SessionContext ctx, String code, AbstractOrderEntry orderEntry, long quantity, double adjustedUnitPrice)
    {
        if(isCachingAllowed(ctx))
        {
            Map<Object, Object> parameters = new HashMap<>();
            parameters.put("code", code);
            parameters.put("orderEntry", orderEntry);
            parameters.put("quantity", Long.valueOf(quantity));
            parameters.put("adjustedUnitPrice", Double.valueOf(adjustedUnitPrice));
            return (PromotionOrderEntryConsumed)createCachedPromotionOrderEntryConsumed(ctx, parameters);
        }
        return super.createPromotionOrderEntryConsumed(ctx, code, orderEntry, quantity, adjustedUnitPrice);
    }


    public PromotionResult createPromotionResult(SessionContext ctx, AbstractPromotion promotion, AbstractOrder order, float certainty)
    {
        if(promotion == null || order == null || certainty < 0.0F || certainty > 1.0F)
        {
            throw new PromotionException("Invalid attempt to create a promotion result");
        }
        if(isCachingAllowed(ctx))
        {
            Map<Object, Object> parameters = new HashMap<>();
            parameters.put("promotion", promotion);
            parameters.put("order", order);
            parameters.put("certainty", Float.valueOf(certainty));
            return (PromotionResult)createCachedPromotionResult(ctx, parameters);
        }
        return super.createPromotionResult(ctx, promotion, order, certainty);
    }


    public PromotionOrderAdjustTotalAction createPromotionOrderAdjustTotalAction(SessionContext ctx, double totalAdjustment)
    {
        if(isCachingAllowed(ctx))
        {
            Map<Object, Object> parameters = new HashMap<>();
            parameters.put("guid", makeActionGUID());
            parameters.put("amount", Double.valueOf(totalAdjustment));
            return (PromotionOrderAdjustTotalAction)createCachedPromotionOrderAdjustTotalAction(ctx, parameters);
        }
        return super.createPromotionOrderAdjustTotalAction(ctx, totalAdjustment);
    }


    public PromotionOrderAddFreeGiftAction createPromotionOrderAddFreeGiftAction(SessionContext ctx, Product product, PromotionResult result)
    {
        if(isCachingAllowed(ctx))
        {
            Map<Object, Object> parameters = new HashMap<>();
            parameters.put("guid", makeActionGUID());
            parameters.put("freeProduct", product);
            parameters.put("promotionResult", result);
            return (PromotionOrderAddFreeGiftAction)createCachedPromotionOrderAddFreeGiftAction(ctx, parameters);
        }
        return super.createPromotionOrderAddFreeGiftAction(ctx, product, result);
    }


    public PromotionOrderChangeDeliveryModeAction createPromotionOrderChangeDeliveryModeAction(SessionContext ctx, DeliveryMode deliveryMode)
    {
        if(isCachingAllowed(ctx))
        {
            Map<Object, Object> parameters = new HashMap<>();
            parameters.put("guid", makeActionGUID());
            parameters.put("deliveryMode", deliveryMode);
            return (PromotionOrderChangeDeliveryModeAction)createCachedPromotionOrderChangeDeliveryModeAction(ctx, parameters);
        }
        return super.createPromotionOrderChangeDeliveryModeAction(ctx, deliveryMode);
    }


    public PromotionOrderEntryAdjustAction createPromotionOrderEntryAdjustAction(SessionContext ctx, AbstractOrderEntry entry, long quantity, double adjustment)
    {
        if(isCachingAllowed(ctx))
        {
            Map<Object, Object> parameters = new HashMap<>();
            parameters.put("guid", makeActionGUID());
            parameters.put("amount", Double.valueOf(adjustment));
            parameters.put("orderEntryProduct", entry.getProduct(ctx));
            parameters.put("orderEntryNumber", entry.getEntryNumber());
            parameters.put("orderEntryQuantity", Long.valueOf(quantity));
            return (PromotionOrderEntryAdjustAction)createCachedPromotionOrderEntryAdjustAction(ctx, parameters);
        }
        return super.createPromotionOrderEntryAdjustAction(ctx, entry, quantity, adjustment);
    }


    public PromotionOrderEntryAdjustAction createPromotionOrderEntryAdjustAction(SessionContext ctx, AbstractOrderEntry entry, double adjustment)
    {
        if(isCachingAllowed(ctx))
        {
            Map<Object, Object> parameters = new HashMap<>();
            parameters.put("guid", makeActionGUID());
            parameters.put("amount", Double.valueOf(adjustment));
            parameters.put("orderEntryProduct", entry.getProduct(ctx));
            parameters.put("orderEntryNumber", entry.getEntryNumber());
            parameters.put("orderEntryQuantity", entry.getQuantity(ctx));
            return (PromotionOrderEntryAdjustAction)createCachedPromotionOrderEntryAdjustAction(ctx, parameters);
        }
        return super.createPromotionOrderEntryAdjustAction(ctx, entry, adjustment);
    }


    public PromotionNullAction createPromotionNullAction(SessionContext ctx)
    {
        if(isCachingAllowed(ctx))
        {
            Map<Object, Object> parameters = new HashMap<>();
            parameters.put("guid", makeActionGUID());
            return (PromotionNullAction)createCachedPromotionNullAction(ctx, parameters);
        }
        return super.createPromotionNullAction(ctx);
    }


    public void removeFromAllPromotionResults(SessionContext ctx, AbstractOrder item, PromotionResult value)
    {
        acceptWithCachingEnabled(ctx, item, (c, o) -> {
            if(isCachingAllowed(c))
            {
                this.cache.remove(o.getCode(c));
            }
            else
            {
                super.removeFromAllPromotionResults(c, o, value);
            }
        });
    }


    protected PromotionOrderResults doUpdatePromotions(SessionContext ctx, Collection<PromotionGroup> promotionGroups, AbstractOrder order, boolean evaluateRestrictions, PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, Date date)
    {
        PromotionOrderResults orderResults = doUpdatePromotionsOutOfCache(ctx, promotionGroups, order, evaluateRestrictions, productPromotionMode, orderPromotionMode, date);
        if(isCachingAllowed(ctx))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Putting the Promotion Results in Cache (OrderCode, PromotionOrderResults.getAllResults() )");
            }
            this.cache.put(order.getCode(ctx), orderResults.getAllResults());
        }
        return orderResults;
    }


    protected PromotionOrderResults doUpdatePromotionsOutOfCache(SessionContext ctx, Collection<PromotionGroup> promotionGroups, AbstractOrder order, boolean evaluateRestrictions, PromotionsManager.AutoApplyMode productPromotionMode, PromotionsManager.AutoApplyMode orderPromotionMode, Date date)
    {
        return super.updatePromotions(ctx, promotionGroups, order, evaluateRestrictions, productPromotionMode, orderPromotionMode, date);
    }


    protected List<PromotionResult> getNonCachedPromotionResultsInternal(SessionContext ctx, AbstractOrder order)
    {
        return super.getPromotionResultsInternal(ctx, order);
    }


    protected <T> T applyWithCachingEnabled(SessionContext ctx, AbstractOrder order, BiFunction<SessionContext, AbstractOrder, T> function)
    {
        JaloSession js = getCurrentJaloSession();
        SessionContext lCtx = js.createLocalSessionContext(ctx);
        try
        {
            setCachingAllowed(lCtx, order);
            return function.apply(lCtx, order);
        }
        finally
        {
            js.removeLocalSessionContext();
        }
    }


    protected JaloSession getCurrentJaloSession()
    {
        return JaloSession.getCurrentSession();
    }


    protected void acceptWithCachingEnabled(SessionContext ctx, AbstractOrder order, BiConsumer<SessionContext, AbstractOrder> consumer)
    {
        JaloSession js = getCurrentJaloSession();
        SessionContext lCtx = js.createLocalSessionContext(ctx);
        try
        {
            setCachingAllowed(lCtx, order);
            consumer.accept(lCtx, order);
        }
        finally
        {
            js.removeLocalSessionContext();
        }
    }


    protected void setCachingAllowed(SessionContext ctx, AbstractOrder order)
    {
        ctx.setAttribute("de.hybris.platform.promotions.jalo.cachingAllowed", Boolean.valueOf(order instanceof Cart));
    }


    protected boolean isCachingAllowed(SessionContext ctx)
    {
        return BooleanUtils.isTrue((Boolean)ctx.getAttribute("de.hybris.platform.promotions.jalo.cachingAllowed"));
    }


    public CachingStrategy getCache()
    {
        return this.cache;
    }


    public void setCache(CachingStrategy cache)
    {
        this.cache = cache;
    }
}
