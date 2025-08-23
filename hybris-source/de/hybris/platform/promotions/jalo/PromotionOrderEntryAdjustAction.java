package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.util.DiscountValue;
import java.util.Collection;
import org.apache.log4j.Logger;

public class PromotionOrderEntryAdjustAction extends GeneratedPromotionOrderEntryAdjustAction
{
    private static Logger log = Logger.getLogger(PromotionOrderEntryAdjustAction.class.getName());


    public boolean apply(SessionContext ctx)
    {
        AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
        if(log.isDebugEnabled())
        {
            log.debug("(" + getPK() + ") apply: Applying OrderEntry adjustment action for order [" + order.getPK() + "]");
        }
        boolean needsCalc = false;
        Integer orderEntryNumber = getOrderEntryNumber(ctx);
        AbstractOrderEntry orderEntry = findOrderEntry(order, ctx, orderEntryNumber);
        if(orderEntry != null)
        {
            double orderEntryAdjustment = getAmount(ctx).doubleValue();
            double unitAdjustment = orderEntryAdjustment / orderEntry.getQuantity(ctx).longValue();
            String code = getGuid(ctx);
            DiscountValue dv = new DiscountValue(code, -1.0D * unitAdjustment, true, order.getCurrency(ctx).getIsoCode(ctx));
            insertFirstOrderEntryDiscountValue(ctx, orderEntry, dv);
            if(log.isDebugEnabled())
            {
                log.debug("(" + getPK() + ") apply: Creating an adjustment of " + getAmount(ctx) + " to order entry '" + orderEntry
                                .getPK() + "'.  Order entry now has " + orderEntry.getDiscountValues(ctx).size() + " adjustments");
            }
            needsCalc = true;
        }
        else
        {
            log.error("(" + getPK() + ") apply: Could not find an order entry to adjust with product '" +
                            getOrderEntryProduct(ctx) + "' and quantity '" + getOrderEntryQuantity(ctx) + "'");
        }
        setMarkedApplied(ctx, true);
        return needsCalc;
    }


    protected AbstractOrderEntry findOrderEntry(AbstractOrder order, SessionContext ctx, Integer orderEntryNumber)
    {
        AbstractOrderEntry result = null;
        if(orderEntryNumber == null)
        {
            for(AbstractOrderEntry oe : order.getAllEntries())
            {
                if(oe.getProduct(ctx).equals(getOrderEntryProduct(ctx)) && oe
                                .getQuantity(ctx).longValue() >= getOrderEntryQuantityAsPrimitive(ctx))
                {
                    result = oe;
                    break;
                }
            }
        }
        else
        {
            result = order.getEntry(orderEntryNumber.intValue());
        }
        return result;
    }


    public boolean undo(SessionContext ctx)
    {
        AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
        if(log.isDebugEnabled())
        {
            log.debug("(" + getPK() + ") undo: Undoing order entry adjustment for order [" + order.getPK() + "]");
        }
        boolean calculateTotals = false;
        OrderEntryAndDiscountValue orderEntryAndDiscountValue = findOrderEntryDiscountValue(ctx, order, getGuid(ctx));
        if(orderEntryAndDiscountValue != null)
        {
            ((AbstractOrderEntry)orderEntryAndDiscountValue.getKey()).removeDiscountValue(ctx, (DiscountValue)orderEntryAndDiscountValue.getValue());
            calculateTotals = true;
        }
        setMarkedApplied(ctx, false);
        return calculateTotals;
    }


    public boolean isAppliedToOrder(SessionContext ctx)
    {
        AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
        if(log.isDebugEnabled())
        {
            log.debug("(" + getPK() + ") isAppliedToOrder: Checking if this action is applied to order [" + order.getPK() + "]");
        }
        return (findOrderEntryDiscountValue(ctx, order, getGuid(ctx)) != null);
    }


    public double getValue(SessionContext ctx)
    {
        return -1.0D * getAmount(ctx).doubleValue();
    }


    protected static OrderEntryAndDiscountValue findOrderEntryDiscountValue(SessionContext ctx, AbstractOrder order, String discountValueCode)
    {
        Collection<AbstractOrderEntry> entries = order.getAllEntries();
        for(AbstractOrderEntry entry : entries)
        {
            DiscountValue discountValue = findOrderEntryDiscountValue(ctx, entry, discountValueCode);
            if(discountValue != null)
            {
                return new OrderEntryAndDiscountValue(entry, discountValue);
            }
        }
        return null;
    }


    protected static DiscountValue findOrderEntryDiscountValue(SessionContext ctx, AbstractOrderEntry orderEntry, String discountValueCode)
    {
        Collection<DiscountValue> discounts = orderEntry.getDiscountValues(ctx);
        for(DiscountValue dv : discounts)
        {
            if(discountValueCode.equals(dv.getCode()))
            {
                return dv;
            }
        }
        return null;
    }
}
