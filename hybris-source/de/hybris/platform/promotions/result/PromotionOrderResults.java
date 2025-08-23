package de.hybris.platform.promotions.result;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PromotionOrderResults
{
    private final List<PromotionResult> promotionResults;
    private final AbstractOrder order;
    private final SessionContext ctx;
    private final double changeFromLastResults;
    private volatile List<PromotionResult> firedProductPromotions;
    private volatile List<PromotionResult> appliedProductPromotions;
    private volatile List<PromotionResult> potentialProductPromotions;
    private volatile List<PromotionResult> firedOrderPromotions;
    private volatile List<PromotionResult> appliedOrderPromotions;
    private volatile List<PromotionResult> potentialOrderPromotions;
    private volatile List<WrappedOrderEntry> entriesNotInFiredPromotions;
    private volatile List<WrappedOrderEntry> entriesNotInPromotions;
    private volatile List<WrappedOrderEntry> entriesWithPotentialPromotions;


    public PromotionOrderResults(SessionContext ctx, AbstractOrder order, List<PromotionResult> promotionResults, double changeFromLastResults)
    {
        this.ctx = ctx;
        this.order = order;
        this.promotionResults = promotionResults;
        this.changeFromLastResults = changeFromLastResults;
    }


    public void invalidateCache()
    {
        synchronized(this)
        {
            this.firedProductPromotions = null;
            this.appliedProductPromotions = null;
            this.potentialProductPromotions = null;
            this.firedOrderPromotions = null;
            this.appliedOrderPromotions = null;
            this.potentialOrderPromotions = null;
            this.entriesNotInFiredPromotions = null;
            this.entriesNotInPromotions = null;
            this.entriesWithPotentialPromotions = null;
        }
    }


    public double getTotalChangeFromLastResults()
    {
        return this.changeFromLastResults;
    }


    public List<PromotionResult> getAllResults()
    {
        return Collections.unmodifiableList(this.promotionResults);
    }


    public List<PromotionResult> getAllProductPromotions()
    {
        return getPromotionResults(PromotionResultStatus.Any, PromotionResultProducts.RequireConsumedProducts);
    }


    public List<PromotionResult> getFiredProductPromotions()
    {
        if(this.firedProductPromotions == null)
        {
            synchronized(this)
            {
                if(this.firedProductPromotions == null)
                {
                    this.firedProductPromotions = getPromotionResults(PromotionResultStatus.FiredOrApplied, PromotionResultProducts.RequireConsumedProducts);
                }
            }
        }
        return this.firedProductPromotions;
    }


    public List<PromotionResult> getAppliedProductPromotions()
    {
        if(this.appliedProductPromotions == null)
        {
            synchronized(this)
            {
                if(this.appliedProductPromotions == null)
                {
                    this.appliedProductPromotions = getPromotionResults(PromotionResultStatus.AppliedOnly, PromotionResultProducts.RequireConsumedProducts);
                }
            }
        }
        return this.appliedProductPromotions;
    }


    public List<PromotionResult> getPotentialProductPromotions()
    {
        if(this.potentialProductPromotions == null)
        {
            synchronized(this)
            {
                if(this.potentialProductPromotions == null)
                {
                    this.potentialProductPromotions = getPromotionResults(PromotionResultStatus.CouldFireOnly, PromotionResultProducts.RequireConsumedProducts);
                }
            }
        }
        return this.potentialProductPromotions;
    }


    public List<PromotionResult> getAllOrderPromotions()
    {
        return getPromotionResults(PromotionResultStatus.Any, PromotionResultProducts.NoConsumedProducts);
    }


    public List<PromotionResult> getFiredOrderPromotions()
    {
        if(this.firedOrderPromotions == null)
        {
            synchronized(this)
            {
                if(this.firedOrderPromotions == null)
                {
                    this.firedOrderPromotions = getPromotionResults(PromotionResultStatus.FiredOrApplied, PromotionResultProducts.NoConsumedProducts);
                }
            }
        }
        return this.firedOrderPromotions;
    }


    public List<PromotionResult> getAppliedOrderPromotions()
    {
        if(this.appliedOrderPromotions == null)
        {
            synchronized(this)
            {
                if(this.appliedOrderPromotions == null)
                {
                    this.appliedOrderPromotions = getPromotionResults(PromotionResultStatus.AppliedOnly, PromotionResultProducts.NoConsumedProducts);
                }
            }
        }
        return this.appliedOrderPromotions;
    }


    public List<PromotionResult> getPotentialOrderPromotions()
    {
        if(this.potentialOrderPromotions == null)
        {
            synchronized(this)
            {
                if(this.potentialOrderPromotions == null)
                {
                    this.potentialOrderPromotions = getPromotionResults(PromotionResultStatus.CouldFireOnly, PromotionResultProducts.NoConsumedProducts);
                }
            }
        }
        return this.potentialOrderPromotions;
    }


    protected List<PromotionResult> getPromotionResults(PromotionResultStatus statusFlag, PromotionResultProducts productsFlag)
    {
        List<PromotionResult> tmpResults = new LinkedList<>();
        for(PromotionResult promotionResult : this.promotionResults)
        {
            boolean statusOk = false;
            if(statusFlag == PromotionResultStatus.Any || (statusFlag == PromotionResultStatus.CouldFireOnly && promotionResult
                            .getCouldFire(this.ctx)))
            {
                statusOk = true;
            }
            else if((statusFlag == PromotionResultStatus.FiredOnly || statusFlag == PromotionResultStatus.AppliedOnly || statusFlag == PromotionResultStatus.FiredOrApplied) && promotionResult
                            .getFired(this.ctx))
            {
                if(statusFlag == PromotionResultStatus.FiredOrApplied)
                {
                    statusOk = true;
                }
                else if(promotionResult.isApplied(this.ctx))
                {
                    statusOk = (statusFlag == PromotionResultStatus.AppliedOnly);
                }
                else
                {
                    statusOk = (statusFlag == PromotionResultStatus.FiredOnly);
                }
            }
            if(statusOk)
            {
                boolean productsOk;
                if(productsFlag == PromotionResultProducts.Any)
                {
                    productsOk = true;
                }
                else
                {
                    Collection consumed = promotionResult.getConsumedEntries(this.ctx);
                    boolean hasConsumedProducts = (consumed != null && !consumed.isEmpty());
                    productsOk = ((productsFlag == PromotionResultProducts.RequireConsumedProducts && hasConsumedProducts) || (productsFlag == PromotionResultProducts.NoConsumedProducts && !hasConsumedProducts));
                }
                if(productsOk)
                {
                    tmpResults.add(promotionResult);
                }
            }
        }
        return Collections.unmodifiableList(tmpResults);
    }


    public List<WrappedOrderEntry> getEntriesNotInPromotions()
    {
        if(this.entriesNotInPromotions == null)
        {
            synchronized(this)
            {
                if(this.entriesNotInPromotions == null)
                {
                    List<WrappedOrderEntry> wrappedEntries = new ArrayList<>();
                    for(WrappedOrderEntry entry : getEntriesNotInFiredPromotions())
                    {
                        List<PromotionResult> entryPromotionResults = entry.getPromotionResults();
                        if(entryPromotionResults == null || entryPromotionResults.isEmpty())
                        {
                            wrappedEntries.add(entry);
                            continue;
                        }
                        long maxConsumedForOrderEntry = getMaxConsumedQuantityForEntry(this.ctx, entryPromotionResults, entry
                                        .getBaseOrderEntry());
                        long entryQuantity = entry.getQuantity(this.ctx);
                        if(entryQuantity > maxConsumedForOrderEntry)
                        {
                            wrappedEntries.add(new WrappedOrderEntry(this.ctx, entry
                                            .getBaseOrderEntry(), entryQuantity - maxConsumedForOrderEntry));
                        }
                    }
                    this.entriesNotInPromotions = Collections.unmodifiableList(wrappedEntries);
                }
            }
        }
        return this.entriesNotInPromotions;
    }


    public List<WrappedOrderEntry> getEntriesWithPotentialPromotions()
    {
        if(this.entriesWithPotentialPromotions == null)
        {
            synchronized(this)
            {
                if(this.entriesWithPotentialPromotions == null)
                {
                    List<WrappedOrderEntry> wrappedEntries = new ArrayList<>();
                    for(WrappedOrderEntry entry : getEntriesNotInFiredPromotions())
                    {
                        List<PromotionResult> entryPromotionResults = entry.getPromotionResults();
                        if(entryPromotionResults != null && !entryPromotionResults.isEmpty())
                        {
                            long maxConsumedForOrderEntry = getMaxConsumedQuantityForEntry(this.ctx, entryPromotionResults, entry
                                            .getBaseOrderEntry());
                            long entryQuantity = entry.getQuantity(this.ctx);
                            if(maxConsumedForOrderEntry <= 0L)
                            {
                                continue;
                            }
                            if(entryQuantity == maxConsumedForOrderEntry)
                            {
                                wrappedEntries.add(entry);
                                continue;
                            }
                            if(maxConsumedForOrderEntry < entryQuantity)
                            {
                                wrappedEntries.add(new WrappedOrderEntry(this.ctx, entry.getBaseOrderEntry(), maxConsumedForOrderEntry, entry
                                                .getPromotionResults()));
                            }
                        }
                    }
                    this.entriesWithPotentialPromotions = Collections.unmodifiableList(wrappedEntries);
                }
            }
        }
        return this.entriesWithPotentialPromotions;
    }


    public List<WrappedOrderEntry> getEntriesNotInFiredPromotions()
    {
        if(this.entriesNotInFiredPromotions == null)
        {
            synchronized(this)
            {
                if(this.entriesNotInFiredPromotions == null)
                {
                    List<WrappedOrderEntry> wrappedEntries = wrapEntries(this.ctx, this.order);
                    for(PromotionResult promotionResult : getFiredProductPromotions())
                    {
                        removeConsumedEntries(this.ctx, wrappedEntries, promotionResult.getConsumedEntries(this.ctx));
                    }
                    wrappedEntries = cleanWrappedEntryList(this.ctx, wrappedEntries);
                    for(PromotionResult promotionResult : getPotentialProductPromotions())
                    {
                        Collection<WrappedOrderEntry> matchingEntries = findAllMatchingEntries(this.ctx, wrappedEntries, promotionResult.getConsumedEntries(this.ctx));
                        for(WrappedOrderEntry wrappedEntry : matchingEntries)
                        {
                            wrappedEntry.addPromotionResult(this.ctx, promotionResult);
                        }
                    }
                    this.entriesNotInFiredPromotions = Collections.unmodifiableList(wrappedEntries);
                }
            }
        }
        return this.entriesNotInFiredPromotions;
    }


    protected static long getMaxConsumedQuantityForEntry(SessionContext ctx, List<PromotionResult> promotionResults, AbstractOrderEntry orderEntry)
    {
        long maxConsumedForProduct = 0L;
        for(PromotionResult result : promotionResults)
        {
            long counsumedCountForProductInThisResult = getConsumedQuantityForEntry(ctx, result, orderEntry);
            if(counsumedCountForProductInThisResult > maxConsumedForProduct)
            {
                maxConsumedForProduct = counsumedCountForProductInThisResult;
            }
        }
        return maxConsumedForProduct;
    }


    protected static long getConsumedQuantityForEntry(SessionContext ctx, PromotionResult result, AbstractOrderEntry orderEntry)
    {
        long quantityTotal = 0L;
        Collection<PromotionOrderEntryConsumed> consumedEntries = result.getConsumedEntries(ctx);
        if(consumedEntries != null && !consumedEntries.isEmpty())
        {
            for(PromotionOrderEntryConsumed poec : consumedEntries)
            {
                if(poec.getOrderEntry(ctx) == orderEntry)
                {
                    quantityTotal += poec.getQuantityAsPrimitive(ctx);
                }
            }
        }
        return quantityTotal;
    }


    protected static Collection<WrappedOrderEntry> findAllMatchingEntries(SessionContext ctx, List<WrappedOrderEntry> wrappedEntries, Collection<PromotionOrderEntryConsumed> consumedEntries)
    {
        Set<WrappedOrderEntry> matchingEntries = new HashSet<>();
        for(PromotionOrderEntryConsumed consumedEntry : consumedEntries)
        {
            AbstractOrderEntry orderEntry = consumedEntry.getOrderEntry(ctx);
            for(WrappedOrderEntry wrappedEntry : wrappedEntries)
            {
                if(wrappedEntry.getBaseOrderEntry().equals(orderEntry))
                {
                    matchingEntries.add(wrappedEntry);
                }
            }
        }
        return matchingEntries;
    }


    protected static List<WrappedOrderEntry> wrapEntries(SessionContext ctx, AbstractOrder order)
    {
        List<WrappedOrderEntry> wrappedEntries = new ArrayList<>(10);
        if(order != null)
        {
            List<AbstractOrderEntry> entries = order.getAllEntries();
            for(AbstractOrderEntry entry : entries)
            {
                wrappedEntries.add(new WrappedOrderEntry(ctx, entry));
            }
        }
        return wrappedEntries;
    }


    protected static void removeConsumedEntries(SessionContext ctx, List<WrappedOrderEntry> wrappedEntries, Collection<PromotionOrderEntryConsumed> consumedEntries)
    {
        for(PromotionOrderEntryConsumed consumedEntry : consumedEntries)
        {
            for(WrappedOrderEntry wrappedEntry : wrappedEntries)
            {
                if(wrappedEntry.consumePromotionOrderEntryConsumed(ctx, consumedEntry))
                {
                    break;
                }
            }
        }
    }


    protected static List<WrappedOrderEntry> cleanWrappedEntryList(SessionContext ctx, List<WrappedOrderEntry> wrappedEntries)
    {
        List<WrappedOrderEntry> result = new ArrayList<>(wrappedEntries.size());
        for(WrappedOrderEntry entry : wrappedEntries)
        {
            if(entry.getQuantity(ctx) > 0L)
            {
                result.add(entry);
            }
        }
        return result;
    }
}
