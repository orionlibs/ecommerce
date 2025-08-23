package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import org.apache.log4j.Logger;

public class PromotionOrderAddFreeGiftAction extends GeneratedPromotionOrderAddFreeGiftAction
{
    private static final Logger LOG = Logger.getLogger(PromotionOrderAddFreeGiftAction.class.getName());
    private static final long QUANTITY = 1L;


    public boolean apply(SessionContext ctx)
    {
        AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
        Product product = getFreeProduct(ctx);
        Unit unit = product.getUnit(ctx);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("(" + getPK() + ") apply: Adding 1 free gift to Cart with " + order.getAllEntries().size() + " order entries.");
        }
        AbstractOrderEntry orderEntry = order.addNewEntry(product, 1L, unit, false);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("(" + getPK() + ") apply: Adding 1 free gift.  There are now " + order.getAllEntries().size() + " order entries.");
        }
        orderEntry.setGiveAway(ctx, true);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("(" + getPK() + ") apply: Created a free gift order entry with " + orderEntry.getDiscountValues(ctx).size() + " discount values");
        }
        PromotionResult pr = getPromotionResult(ctx);
        PromotionOrderEntryConsumed consumed = PromotionsManager.getInstance().createPromotionOrderEntryConsumed(ctx,
                        getGuid(ctx), orderEntry, 1L);
        consumed.setAdjustedUnitPrice(ctx, 0.0D);
        pr.addConsumedEntry(ctx, consumed);
        setMarkedApplied(ctx, true);
        return true;
    }


    public boolean undo(SessionContext ctx)
    {
        AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("(" + getPK() + ") undo: Undoing add free gift from order with " + order.getAllEntries().size() + " order entries");
        }
        for(AbstractOrderEntry aoe : order.getAllEntries())
        {
            if(aoe.isGiveAway(ctx).booleanValue() && aoe.getProduct(ctx).equals(getFreeProduct(ctx)) && aoe
                            .getQuantity(ctx).longValue() >= 1L)
            {
                long remainingQuantityAfterUndo = aoe.getQuantity(ctx).longValue() - 1L;
                if(remainingQuantityAfterUndo < 1L)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("(" + getPK() + ") undo: Line item has the same or less quantity than the offer.  Removing whole order entry.");
                    }
                    order.removeEntry(aoe);
                }
                else
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("(" + getPK() + ") undo: Line item has a greater quantity than the offer.  Removing the offer quantity and resetting giveaway flag.");
                    }
                    aoe.setQuantity(ctx, remainingQuantityAfterUndo);
                    aoe.setGiveAway(ctx, false);
                    try
                    {
                        aoe.recalculate();
                    }
                    catch(JaloPriceFactoryException jpe)
                    {
                        LOG.error("unable to calculate the entry: " + jpe.getMessage());
                    }
                }
                PromotionResult pr = getPromotionResult(ctx);
                Collection<PromotionOrderEntryConsumed> consumedEntries = pr.getConsumedEntries(ctx);
                Collection<PromotionOrderEntryConsumed> toRemoveConsumedEntries = new HashSet<>();
                for(PromotionOrderEntryConsumed poec : consumedEntries)
                {
                    if(poec.getCode(ctx).equals(getGuid(ctx)))
                    {
                        toRemoveConsumedEntries.add(poec);
                    }
                }
                for(PromotionOrderEntryConsumed poec : toRemoveConsumedEntries)
                {
                    pr.removeConsumedEntry(ctx, poec);
                }
                break;
            }
        }
        setMarkedApplied(ctx, false);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("(" + getPK() + ") undo: Free gift removed from order which now has " + order.getAllEntries().size() + " order entries");
        }
        return true;
    }


    public boolean isAppliedToOrder(SessionContext ctx)
    {
        AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
        for(AbstractOrderEntry aoe : order.getAllEntries())
        {
            if(aoe.isGiveAway(ctx).booleanValue() && aoe.getProduct(ctx).equals(getFreeProduct(ctx)) && aoe
                            .getQuantity(ctx).longValue() >= 1L)
            {
                return true;
            }
        }
        return false;
    }


    public double getValue(SessionContext ctx)
    {
        return 0.0D;
    }


    protected void deepCloneAttributes(SessionContext ctx, Map values)
    {
        super.deepCloneAttributes(ctx, values);
    }
}
