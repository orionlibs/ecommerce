package de.hybris.platform.promotions.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloOnlyItem;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.servicelayer.internal.jalo.order.JaloOnlyItemHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public class CachedPromotionResult extends GeneratedCachedPromotionResult implements JaloOnlyItem
{
    private static final Logger LOG = Logger.getLogger(CachedPromotionResult.class.getName());
    private JaloOnlyItemHelper data;
    private final List<AbstractPromotionAction> cachedActions = new ArrayList<>();
    private final List<CachedPromotionOrderEntryConsumed> cachedConsumedEntries = new ArrayList<>();


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Class<CachedPromotionResult> cl = type.getJaloClass();
        try
        {
            CachedPromotionResult newOne = cl.newInstance();
            newOne.setTenant(type.getTenant());
            newOne
                            .data = new JaloOnlyItemHelper((PK)allAttributes.get(PK), (Item)newOne, type, new Date(), null);
            return (Item)newOne;
        }
        catch(ClassCastException | InstantiationException | IllegalAccessException e)
        {
            throw new JaloGenericCreationException("could not instantiate wizard class " + cl + " of type " + type
                            .getCode() + " : " + e, 0);
        }
    }


    public Float getCertainty(SessionContext ctx)
    {
        return (Float)this.data.getProperty(ctx, "certainty");
    }


    public void setCertainty(SessionContext ctx, Float certainty)
    {
        this.data.setProperty(ctx, "certainty", certainty);
    }


    public String getCustom(SessionContext ctx)
    {
        return (String)this.data.getProperty(ctx, "custom");
    }


    public void setCustom(SessionContext ctx, String custom)
    {
        this.data.setProperty(ctx, "custom", custom);
    }


    public AbstractOrder getOrder(SessionContext ctx)
    {
        return (AbstractOrder)this.data.getProperty(ctx, "order");
    }


    public void setOrder(SessionContext ctx, AbstractOrder order)
    {
        this.data.setProperty(ctx, "order", order);
    }


    public AbstractPromotion getPromotion(SessionContext ctx)
    {
        return (AbstractPromotion)this.data.getProperty(ctx, "promotion");
    }


    public void setPromotion(SessionContext ctx, AbstractPromotion promotion)
    {
        this.data.setProperty(ctx, "promotion", promotion);
    }


    public void addAction(SessionContext ctx, AbstractPromotionAction action)
    {
        addToCachedActions(ctx, action);
        action.setPromotionResult((PromotionResult)this);
    }


    public Collection<AbstractPromotionAction> getActions(SessionContext ctx)
    {
        return getCachedActions();
    }


    public void setActions(SessionContext ctx, Collection actions)
    {
        setCachedActions(actions);
        for(AbstractPromotionAction action : actions)
        {
            action.setPromotionResult((PromotionResult)this);
        }
    }


    public Set<AbstractPromotionAction> getAllPromotionActions(SessionContext ctx)
    {
        return Collections.unmodifiableSet(new HashSet<>(getCachedActions()));
    }


    public void setAllPromotionActions(SessionContext ctx, Set<AbstractPromotionAction> actions)
    {
        setCachedActions(ctx, actions);
        for(AbstractPromotionAction action : actions)
        {
            action.setPromotionResult((PromotionResult)this);
        }
    }


    public void addToAllPromotionActions(SessionContext ctx, AbstractPromotionAction action)
    {
        addToCachedActions(ctx, action);
        action.setPromotionResult((PromotionResult)this);
    }


    public void removeFromAllPromotionActions(SessionContext ctx, AbstractPromotionAction action)
    {
        removeFromCachedActions(ctx, action);
    }


    public Collection<AbstractPromotionAction> getCachedActions(SessionContext ctx)
    {
        return Collections.unmodifiableCollection(this.cachedActions);
    }


    public void setCachedActions(SessionContext ctx, Collection<AbstractPromotionAction> abstractPromotionActions)
    {
        this.cachedActions.clear();
        this.cachedActions.addAll(abstractPromotionActions);
    }


    public void addToCachedActions(SessionContext ctx, AbstractPromotionAction action)
    {
        this.cachedActions.add(action);
    }


    public void removeFromCachedActions(SessionContext ctx, AbstractPromotionAction action)
    {
        this.cachedActions.remove(action);
    }


    public Collection<CachedPromotionOrderEntryConsumed> getCachedConsumedEntries(SessionContext sessionContext)
    {
        return Collections.unmodifiableCollection(this.cachedConsumedEntries);
    }


    public void setCachedConsumedEntries(SessionContext ctx, Collection<CachedPromotionOrderEntryConsumed> cachedConsumedEntries)
    {
        this.cachedConsumedEntries.clear();
        this.cachedConsumedEntries.addAll(cachedConsumedEntries);
    }


    public void addToCachedConsumedEntries(SessionContext ctx, CachedPromotionOrderEntryConsumed poec)
    {
        this.cachedConsumedEntries.add(poec);
    }


    public void removeFromCachedConsumedEntries(SessionContext ctx, CachedPromotionOrderEntryConsumed poec)
    {
        this.cachedConsumedEntries.remove(poec);
    }


    public void addConsumedEntry(SessionContext ctx, PromotionOrderEntryConsumed poec)
    {
        if(!(poec instanceof CachedPromotionOrderEntryConsumed))
        {
            throw new UnsupportedOperationException("Can't store persistent POEC in cache");
        }
        addToCachedConsumedEntries(ctx, (CachedPromotionOrderEntryConsumed)poec);
        poec.setPromotionResult((PromotionResult)this);
    }


    public void removeConsumedEntry(SessionContext ctx, PromotionOrderEntryConsumed poec)
    {
        if(!(poec instanceof CachedPromotionOrderEntryConsumed))
        {
            throw new UnsupportedOperationException("Can't remove persistent POEC from cache");
        }
        removeFromCachedConsumedEntries(ctx, (CachedPromotionOrderEntryConsumed)poec);
    }


    public Collection<PromotionOrderEntryConsumed> getConsumedEntries(SessionContext ctx)
    {
        return getCachedConsumedEntries();
    }


    public void setConsumedEntries(SessionContext ctx, Collection<CachedPromotionOrderEntryConsumed> entries)
    {
        setCachedConsumedEntries(ctx, entries);
        for(PromotionOrderEntryConsumed poec : entries)
        {
            poec.setPromotionResult((PromotionResult)this);
        }
    }


    public final ComposedType provideComposedType()
    {
        return this.data.provideComposedType();
    }


    public final Date provideCreationTime()
    {
        return this.data.provideCreationTime();
    }


    public final Date provideModificationTime()
    {
        return this.data.provideModificationTime();
    }


    public final PK providePK()
    {
        return this.data.providePK();
    }


    public void removeJaloOnly() throws ConsistencyCheckException
    {
        this.data.removeJaloOnly();
    }


    public Object doGetAttribute(SessionContext ctx, String attrQualifier) throws JaloSecurityException
    {
        return this.data.doGetAttribute(ctx, attrQualifier);
    }


    public void doSetAttribute(SessionContext ctx, String attrQualifier, Object value) throws JaloBusinessException
    {
        this.data.doSetAttribute(ctx, attrQualifier, value);
    }
}
