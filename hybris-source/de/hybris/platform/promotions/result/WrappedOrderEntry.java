package de.hybris.platform.promotions.result;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class WrappedOrderEntry
{
    private final List<PromotionResult> promotionResults = new LinkedList<>();
    private final AbstractOrderEntry baseEntry;
    private long quantity;


    protected WrappedOrderEntry(SessionContext ctx, AbstractOrderEntry orderEntry)
    {
        this.baseEntry = orderEntry;
        this.quantity = this.baseEntry.getQuantity(ctx).longValue();
    }


    protected WrappedOrderEntry(SessionContext ctx, AbstractOrderEntry orderEntry, long quantity)
    {
        this.baseEntry = orderEntry;
        this.quantity = quantity;
    }


    protected WrappedOrderEntry(SessionContext ctx, AbstractOrderEntry orderEntry, long quantity, Collection<PromotionResult> promotionResults)
    {
        this.baseEntry = orderEntry;
        this.quantity = quantity;
        addPromotionResultsInternal(ctx, promotionResults);
    }


    public long getQuantity()
    {
        return this.quantity;
    }


    public long getQuantity(SessionContext ctx)
    {
        return this.quantity;
    }


    public Product getProduct()
    {
        return getProduct(JaloSession.getCurrentSession().getSessionContext());
    }


    public Product getProduct(SessionContext ctx)
    {
        return this.baseEntry.getProduct(ctx);
    }


    public AbstractOrderEntry getBaseOrderEntry()
    {
        return this.baseEntry;
    }


    public List<PromotionResult> getPromotionResults()
    {
        return this.promotionResults;
    }


    protected void addPromotionResult(SessionContext ctx, PromotionResult promotionResult)
    {
        int insertIndex = Collections.binarySearch(this.promotionResults, promotionResult, (Comparator<? super PromotionResult>)new PromotionResultComparator(ctx));
        insertIndex = (insertIndex < 0) ? (-insertIndex - 1) : insertIndex;
        this.promotionResults.add(insertIndex, promotionResult);
    }


    protected void addPromotionResults(SessionContext ctx, Collection<PromotionResult> promotionResults)
    {
        addPromotionResultsInternal(ctx, promotionResults);
    }


    private void addPromotionResultsInternal(SessionContext ctx, Collection<PromotionResult> promotionResults)
    {
        for(PromotionResult result : promotionResults)
        {
            addPromotionResult(ctx, result);
        }
    }


    protected void consume(long quantity)
    {
        if(quantity > this.quantity)
        {
            throw new IllegalArgumentException("Cannot consume more than quantity");
        }
        this.quantity -= quantity;
    }


    public double getEntryPrice()
    {
        return getEntryPrice(JaloSession.getCurrentSession().getSessionContext());
    }


    public double getEntryPrice(SessionContext ctx)
    {
        return this.quantity * getUnitPrice(ctx);
    }


    public double getUnitPrice()
    {
        return getUnitPrice(JaloSession.getCurrentSession().getSessionContext());
    }


    public double getUnitPrice(SessionContext ctx)
    {
        return this.baseEntry.getTotalPrice(ctx).doubleValue() / this.baseEntry.getQuantity(ctx).longValue();
    }


    public Unit getUnit()
    {
        return getUnit(JaloSession.getCurrentSession().getSessionContext());
    }


    public Unit getUnit(SessionContext ctx)
    {
        return this.baseEntry.getUnit(ctx);
    }


    protected boolean consumePromotionOrderEntryConsumed(SessionContext ctx, PromotionOrderEntryConsumed poec)
    {
        if(this.baseEntry.equals(poec.getOrderEntry(ctx)) &&
                        isLegacyPromotion(poec))
        {
            consume(poec.getQuantity(ctx).longValue());
            return true;
        }
        return false;
    }


    protected boolean isLegacyPromotion(PromotionOrderEntryConsumed poec)
    {
        if(poec == null || poec.getPromotionResult() == null || poec.getPromotionResult().getPromotion() == null)
        {
            return true;
        }
        Class<?> clazz = poec.getPromotionResult().getPromotion().getClass();
        while(clazz != null)
        {
            if(clazz.getName().contains("promotionengineservices"))
            {
                return false;
            }
            clazz = clazz.getSuperclass();
        }
        return true;
    }
}
