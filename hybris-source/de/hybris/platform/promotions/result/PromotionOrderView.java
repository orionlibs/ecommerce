package de.hybris.platform.promotions.result;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.util.Comparators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PromotionOrderView
{
    private final List<PromotionOrderEntry> orderEntries;
    private final AbstractPromotion promotion;


    protected PromotionOrderView(AbstractPromotion promotion, List<PromotionOrderEntry> orderEntries)
    {
        this.orderEntries = orderEntries;
        this.promotion = promotion;
    }


    public long getTotalQuantity(SessionContext ctx)
    {
        long retval = 0L;
        for(PromotionOrderEntry oe : this.orderEntries)
        {
            retval += oe.getQuantity(ctx);
        }
        return retval;
    }


    public long getQuantity(SessionContext ctx, Product product)
    {
        long retval = 0L;
        if(product != null)
        {
            for(PromotionOrderEntry poe : this.orderEntries)
            {
                List<Product> baseProducts = poe.getBaseProducts(ctx);
                if(poe.getProduct(ctx).equals(product) || baseProducts.contains(product))
                {
                    retval += poe.getQuantity(ctx);
                }
            }
        }
        return retval;
    }


    public List<PromotionOrderEntryConsumed> consume(SessionContext ctx, long quantity)
    {
        return doConsume(ctx, this.orderEntries, this.promotion, quantity);
    }


    public List<PromotionOrderEntryConsumed> consumeFromHead(SessionContext ctx, Comparator<PromotionOrderEntry> comparator, long quantity)
    {
        List<PromotionOrderEntry> orderedEntries = new ArrayList<>(this.orderEntries);
        if(comparator != null)
        {
            Collections.sort(orderedEntries, comparator);
        }
        return doConsume(ctx, orderedEntries, this.promotion, quantity);
    }


    public List<PromotionOrderEntryConsumed> consumeFromTail(SessionContext ctx, Comparator<PromotionOrderEntry> comparator, long quantity)
    {
        List<PromotionOrderEntry> orderedEntries = new ArrayList<>(this.orderEntries);
        if(comparator != null)
        {
            Collections.sort(orderedEntries, Collections.reverseOrder(comparator));
        }
        return doConsume(ctx, orderedEntries, this.promotion, quantity);
    }


    protected static List<PromotionOrderEntryConsumed> doConsume(SessionContext ctx, List<PromotionOrderEntry> workingEntries, AbstractPromotion promotion, long quantity)
    {
        List<PromotionOrderEntryConsumed> consumed = new ArrayList<>();
        long remaining = quantity;
        for(PromotionOrderEntry entry : workingEntries)
        {
            if(remaining <= 0L)
            {
                break;
            }
            long available = entry.getQuantity(ctx);
            if(available > 0L)
            {
                long consumeCount = (available < remaining) ? available : remaining;
                consumed.add(entry.consume(ctx, promotion, consumeCount));
                remaining -= consumeCount;
            }
        }
        if(remaining > 0L)
        {
            throw new PromotionException("Attempt to consume more items than exist in this view of the order");
        }
        return consumed;
    }


    public List<PromotionOrderEntryConsumed> consume(SessionContext ctx, Product product, long quantity)
    {
        List<PromotionOrderEntryConsumed> consumed = new ArrayList<>();
        long remaining = quantity;
        for(PromotionOrderEntry entry : this.orderEntries)
        {
            if(remaining <= 0L)
            {
                break;
            }
            List<Product> baseProducts = entry.getBaseProducts(ctx);
            if(entry.getProduct(ctx).equals(product) || baseProducts.contains(product))
            {
                long available = entry.getQuantity(ctx);
                if(available > 0L)
                {
                    long consumeCount = (available < remaining) ? available : remaining;
                    consumed.add(entry.consume(ctx, this.promotion, consumeCount));
                    remaining -= consumeCount;
                }
            }
        }
        if(remaining > 0L)
        {
            throw new PromotionException("Attempt to consume more items than exist in this view of the order");
        }
        return consumed;
    }


    public List<PromotionOrderEntry> getAllEntries(SessionContext ctx)
    {
        return Collections.unmodifiableList(this.orderEntries);
    }


    public List<PromotionOrderEntry> getAllEntriesByPrice(SessionContext ctx)
    {
        ArrayList<PromotionOrderEntry> sortedEntries = new ArrayList<>(this.orderEntries);
        Collections.sort(sortedEntries, Comparators.promotionOrderEntryByPriceComparator);
        return sortedEntries;
    }


    public PromotionOrderEntry peekFromHead(SessionContext ctx, Comparator<PromotionOrderEntry> comparator)
    {
        List<PromotionOrderEntry> orderedEntries = new ArrayList<>(this.orderEntries);
        if(comparator != null)
        {
            Collections.sort(orderedEntries, comparator);
        }
        return doPeek(ctx, orderedEntries);
    }


    public PromotionOrderEntry peek(SessionContext ctx)
    {
        return doPeek(ctx, this.orderEntries);
    }


    public PromotionOrderEntry peekFromTail(SessionContext ctx, Comparator<PromotionOrderEntry> comparator)
    {
        List<PromotionOrderEntry> orderedEntries = new ArrayList<>(this.orderEntries);
        if(comparator != null)
        {
            Collections.sort(orderedEntries, Collections.reverseOrder(comparator));
        }
        return doPeek(ctx, orderedEntries);
    }


    protected static PromotionOrderEntry doPeek(SessionContext ctx, List<PromotionOrderEntry> workingEntries)
    {
        for(PromotionOrderEntry entry : workingEntries)
        {
            long available = entry.getQuantity(ctx);
            if(available > 0L)
            {
                return entry;
            }
        }
        return null;
    }


    protected List<PromotionOrderEntry> getOrderEntries()
    {
        return this.orderEntries;
    }


    protected AbstractPromotion getPromotion()
    {
        return this.promotion;
    }
}
