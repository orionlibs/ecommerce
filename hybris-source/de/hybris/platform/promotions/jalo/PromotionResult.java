package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections.map.Flat3Map;
import org.apache.log4j.Logger;

public class PromotionResult extends GeneratedPromotionResult
{
    private static final Logger LOG = Logger.getLogger(PromotionResult.class);


    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        setConsumedEntries(ctx, Collections.emptyList());
        setActions(ctx, Collections.emptyList());
        super.remove(ctx);
    }


    public final boolean isApplied()
    {
        return isApplied(getSession().getSessionContext());
    }


    public boolean isApplied(SessionContext ctx)
    {
        if(getFired(ctx))
        {
            Collection<AbstractPromotionAction> actions = getActions(ctx);
            if(actions != null && !actions.isEmpty())
            {
                for(AbstractPromotionAction action : actions)
                {
                    if(!action.isMarkedApplied(ctx).booleanValue())
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    public boolean isAppliedToOrder(SessionContext ctx)
    {
        if(getFired(ctx))
        {
            Collection<AbstractPromotionAction> actions = getActions(ctx);
            if(actions != null && !actions.isEmpty())
            {
                for(AbstractPromotionAction action : actions)
                {
                    if(!action.isAppliedToOrder(ctx))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    public final boolean getFired()
    {
        return getFired(getSession().getSessionContext());
    }


    public boolean getFired(SessionContext ctx)
    {
        return (getCertainty(ctx).floatValue() >= 1.0F);
    }


    public final boolean getCouldFire()
    {
        return getCouldFire(getSession().getSessionContext());
    }


    public boolean getCouldFire(SessionContext ctx)
    {
        float certainty = getCertainty(ctx).floatValue();
        return (certainty < 1.0F);
    }


    public final String getDescription()
    {
        return getDescription(getSession().getSessionContext(), null);
    }


    public final String getDescription(Locale locale)
    {
        return getDescription(getSession().getSessionContext(), locale);
    }


    public String getDescription(SessionContext ctx, Locale locale)
    {
        if(locale == null)
        {
            locale = Locale.getDefault();
        }
        AbstractPromotion promotion = getPromotion(ctx);
        if(promotion != null)
        {
            return promotion.getResultDescription(ctx, this, locale);
        }
        return "";
    }


    public final boolean apply()
    {
        return apply(getSession().getSessionContext());
    }


    public boolean apply(SessionContext ctx)
    {
        boolean needsCalculate = false;
        if(getFired(ctx))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("(" + getPK() + ") apply: Applying actions for promotion result (" + getPK().getLongValueAsString() + ")");
            }
            Collection<AbstractPromotionAction> actions = getActions(ctx);
            if(actions != null)
            {
                for(AbstractPromotionAction action : actions)
                {
                    if(!action.isMarkedApplied(ctx).booleanValue())
                    {
                        needsCalculate |= action.apply(ctx);
                    }
                }
            }
        }
        return needsCalculate;
    }


    public final boolean undo()
    {
        return undo(getSession().getSessionContext());
    }


    public boolean undo(SessionContext ctx)
    {
        boolean needsCalculate = false;
        if(getFired(ctx))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("(" + getPK() + ") undo: Undoing actions for promotion result (" + getPK().getLongValueAsString() + ")");
            }
            Collection<AbstractPromotionAction> actions = getActions(ctx);
            if(actions != null)
            {
                for(AbstractPromotionAction action : actions)
                {
                    if(action.isMarkedApplied(ctx).booleanValue())
                    {
                        needsCalculate |= action.undo(ctx);
                    }
                }
            }
        }
        return needsCalculate;
    }


    public final long getConsumedCount(boolean includeCouldFirePromotions)
    {
        return getConsumedCount(getSession().getSessionContext(), includeCouldFirePromotions);
    }


    public long getConsumedCount(SessionContext ctx, boolean includeCouldFirePromotions)
    {
        long count = 0L;
        for(PromotionOrderEntryConsumed poec : getConsumedEntries(ctx))
        {
            if(includeCouldFirePromotions || poec.isRemovedFromOrder())
            {
                count += poec.getQuantity(ctx).longValue();
            }
        }
        return count;
    }


    public final double getTotalDiscount()
    {
        return getTotalDiscount(getSession().getSessionContext());
    }


    public double getTotalDiscount(SessionContext ctx)
    {
        double totalDiscount = 0.0D;
        Collection<AbstractPromotionAction> actions = getActions(ctx);
        if(actions != null)
        {
            for(AbstractPromotionAction action : actions)
            {
                totalDiscount += action.getValue(ctx);
            }
        }
        return totalDiscount;
    }


    protected boolean isValid(SessionContext ctx)
    {
        try
        {
            AbstractPromotion promotion = getPromotion(ctx);
            if(promotion == null)
            {
                return false;
            }
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }


    public void addAction(SessionContext ctx, AbstractPromotionAction action)
    {
        if(ctx != null && action != null)
        {
            action.setPromotionResult(ctx, this);
        }
    }


    public void addConsumedEntry(SessionContext ctx, PromotionOrderEntryConsumed poec)
    {
        if(ctx != null && poec != null)
        {
            poec.setPromotionResult(ctx, this);
        }
    }


    public void removeConsumedEntry(SessionContext ctx, PromotionOrderEntryConsumed poec)
    {
        if(ctx != null && poec != null)
        {
            try
            {
                poec.remove(ctx);
            }
            catch(ConsistencyCheckException ex)
            {
                LOG.warn("(" + getPK() + ") removeConsumedEntry failed to remove [" + poec + "] from db..");
            }
        }
    }


    public PromotionResult transferToOrder(SessionContext ctx, Order target)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("(" + getPK() + ") transferToOrder [" + this + "] order=[" + target + "]");
        }
        if(ctx != null && target != null)
        {
            AbstractPromotion promotion = getPromotion(ctx);
            if(promotion != null)
            {
                AbstractPromotion dupPromotion = promotion.findOrCreateImmutableClone(ctx);
                if(dupPromotion != null)
                {
                    Collection<AbstractPromotionAction> dupActions = deepCloneAllActions(ctx);
                    Collection<PromotionOrderEntryConsumed> dupConsumedEntries = deepCloneConsumedEntriesAndAttachToOrder(ctx, target);
                    PromotionResult dupPromotionResult = null;
                    Map<Object, Object> promotionResultValues = new HashMap<>();
                    promotionResultValues.put("certainty", getCertainty(ctx));
                    promotionResultValues.put("promotion", dupPromotion);
                    promotionResultValues.put("order", target);
                    promotionResultValues.put("actions", dupActions);
                    promotionResultValues.put("consumedEntries", dupConsumedEntries);
                    ComposedType type = TypeManager.getInstance().getComposedType(PromotionResult.class);
                    try
                    {
                        dupPromotionResult = (PromotionResult)type.newInstance(ctx, promotionResultValues);
                    }
                    catch(JaloGenericCreationException | de.hybris.platform.jalo.type.JaloAbstractTypeException ex)
                    {
                        LOG.warn("(" + getPK() + ") transferToOrder: failed to create instance of PromotionResult", (Throwable)ex);
                    }
                    if(dupPromotionResult != null)
                    {
                        for(PromotionOrderEntryConsumed dupConsumedEntry : dupConsumedEntries)
                        {
                            dupConsumedEntry.setPromotionResult(ctx, dupPromotionResult);
                        }
                    }
                    return dupPromotionResult;
                }
            }
        }
        return null;
    }


    protected Collection<AbstractPromotionAction> deepCloneAllActions(SessionContext ctx)
    {
        Collection<AbstractPromotionAction> dupActions = new ArrayList<>();
        Collection<AbstractPromotionAction> actions = getActions(ctx);
        if(actions != null && !actions.isEmpty())
        {
            for(AbstractPromotionAction a : actions)
            {
                dupActions.add(a.deepClone(ctx));
            }
        }
        return dupActions;
    }


    protected Collection<PromotionOrderEntryConsumed> deepCloneConsumedEntriesAndAttachToOrder(SessionContext ctx, Order target)
    {
        Collection<PromotionOrderEntryConsumed> dupConsumedEntries = new ArrayList<>();
        List<AbstractOrderEntry> allTargetEntries = target.getAllEntries();
        Collection<PromotionOrderEntryConsumed> consumedEntries = getConsumedEntries(ctx);
        if(consumedEntries != null && !consumedEntries.isEmpty())
        {
            for(PromotionOrderEntryConsumed poe : consumedEntries)
            {
                PromotionOrderEntryConsumed consumedEntry = deepCloneConsumedEntryAndAttachToOrder(ctx, poe, allTargetEntries);
                if(consumedEntry != null)
                {
                    dupConsumedEntries.add(consumedEntry);
                }
            }
        }
        return dupConsumedEntries;
    }


    protected static PromotionOrderEntryConsumed deepCloneConsumedEntryAndAttachToOrder(SessionContext ctx, PromotionOrderEntryConsumed source, List<AbstractOrderEntry> allTargetEntries)
    {
        AbstractOrderEntry sourceOrderEntry = source.getOrderEntry(ctx);
        if(sourceOrderEntry != null)
        {
            int entryNumber = sourceOrderEntry.getEntryNumber().intValue();
            AbstractOrderEntry targetOrderEntry = findOrderEntryWithEntryNumber(allTargetEntries, entryNumber);
            if(targetOrderEntry == null)
            {
                LOG.warn("cloneConsumedEntryToOrder source=[" + source + "] cannot find matching order entry with entryNumber=[" + entryNumber + "]");
            }
            else if(!sourceOrderEntry.getProduct(ctx).equals(targetOrderEntry.getProduct(ctx)))
            {
                LOG.warn("transferToOrder source=[" + source + "] order entry with entryNumber=[" + entryNumber + "] has different product. expected=[" + sourceOrderEntry
                                .getProduct(ctx) + "] actual=[" + targetOrderEntry
                                .getProduct(ctx) + "]");
            }
            else if(!sourceOrderEntry.getQuantity(ctx).equals(targetOrderEntry.getQuantity(ctx)))
            {
                LOG.warn("transferToOrder source=[" + source + "] order entry with entryNumber=[" + entryNumber + "] has different quantity. expected=[" + sourceOrderEntry
                                .getQuantity(ctx) + "] actual=[" + targetOrderEntry
                                .getQuantity(ctx) + "]");
            }
            else
            {
                return PromotionsManager.getInstance().createPromotionOrderEntryConsumed(ctx, source.getCode(ctx), targetOrderEntry, source
                                .getQuantity(ctx).longValue(), source.getAdjustedUnitPrice(ctx).doubleValue());
            }
        }
        return null;
    }


    protected static AbstractOrderEntry findOrderEntryWithEntryNumber(List<AbstractOrderEntry> allTargetEntries, int entryNumber)
    {
        for(AbstractOrderEntry entry : allTargetEntries)
        {
            if(entryNumber == entry.getEntryNumber().intValue())
            {
                return entry;
            }
        }
        return null;
    }


    public Collection getConsumedEntries(SessionContext ctx)
    {
        String query = "GET {" + TypeManager.getInstance().getComposedType(PromotionOrderEntryConsumed.class).getCode() + "} WHERE  {promotionResult} = ?promotionResult";
        Flat3Map args = new Flat3Map();
        args.put("promotionResult", this);
        Collection<?> results = getSession().getFlexibleSearch().search(ctx, query, (Map)args, PromotionOrderEntryConsumed.class).getResult();
        return Collections.unmodifiableCollection(results);
    }


    public void setConsumedEntries(SessionContext ctx, Collection promotionOrderEntryConsumeds)
    {
        ArrayList<PromotionOrderEntryConsumed> newItems = new ArrayList<>();
        if(promotionOrderEntryConsumeds != null && !promotionOrderEntryConsumeds.isEmpty())
        {
            for(Object obj : promotionOrderEntryConsumeds)
            {
                if(obj instanceof PromotionOrderEntryConsumed)
                {
                    newItems.add((PromotionOrderEntryConsumed)obj);
                }
            }
        }
        Collection<PromotionOrderEntryConsumed> oldItems = getConsumedEntries(ctx);
        if(oldItems != null && !oldItems.isEmpty())
        {
            for(PromotionOrderEntryConsumed oldItem : oldItems)
            {
                boolean keepItem = newItems.remove(oldItem);
                if(!keepItem)
                {
                    try
                    {
                        oldItem.remove(ctx);
                    }
                    catch(ConsistencyCheckException ex)
                    {
                        LOG.error("setConsumedEntries failed to remove [" + oldItem + "] from database", (Throwable)ex);
                    }
                }
            }
        }
        if(!newItems.isEmpty())
        {
            for(PromotionOrderEntryConsumed newItem : newItems)
            {
                newItem.setPromotionResult(ctx, this);
            }
        }
    }


    public Collection getActions(SessionContext ctx)
    {
        String query = "GET {AbstractPromotionAction} WHERE  {promotionResult} = ?promotionResult";
        Flat3Map args = new Flat3Map();
        args.put("promotionResult", this);
        Collection<?> results = getSession().getFlexibleSearch().search(ctx, "GET {AbstractPromotionAction} WHERE  {promotionResult} = ?promotionResult", (Map)args, AbstractPromotionAction.class).getResult();
        return Collections.unmodifiableCollection(results);
    }


    public void setActions(SessionContext ctx, Collection actions)
    {
        ArrayList<AbstractPromotionAction> newActions = new ArrayList<>();
        if(actions != null && !actions.isEmpty())
        {
            for(Object obj : actions)
            {
                if(obj instanceof AbstractPromotionAction)
                {
                    newActions.add((AbstractPromotionAction)obj);
                }
            }
        }
        Collection<AbstractPromotionAction> oldActions = getActions(ctx);
        if(oldActions != null && !oldActions.isEmpty())
        {
            for(AbstractPromotionAction oldAction : oldActions)
            {
                boolean keepItem = newActions.remove(oldAction);
                if(!keepItem)
                {
                    try
                    {
                        oldAction.remove(ctx);
                    }
                    catch(ConsistencyCheckException ex)
                    {
                        LOG.error("setActions failed to remove [" + oldAction + "] from database", (Throwable)ex);
                    }
                }
            }
        }
        if(!newActions.isEmpty())
        {
            for(AbstractPromotionAction newAction : newActions)
            {
                newAction.setPromotionResult(ctx, this);
            }
        }
    }


    protected String getDataUnigueKey(SessionContext ctx)
    {
        AbstractPromotion promotion = getPromotion(ctx);
        if(promotion != null)
        {
            return promotion.getPromotionResultDataUnigueKey(ctx, this);
        }
        return null;
    }
}
