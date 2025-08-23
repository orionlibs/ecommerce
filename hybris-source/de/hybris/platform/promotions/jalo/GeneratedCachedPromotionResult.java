package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCachedPromotionResult extends PromotionResult
{
    public static final String CACHEDACTIONS = "cachedActions";
    public static final String CACHEDCONSUMEDENTRIES = "cachedConsumedEntries";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(PromotionResult.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<AbstractPromotionAction> getCachedActions()
    {
        return getCachedActions(getSession().getSessionContext());
    }


    public void setCachedActions(Collection<AbstractPromotionAction> value)
    {
        setCachedActions(getSession().getSessionContext(), value);
    }


    public Collection<CachedPromotionOrderEntryConsumed> getCachedConsumedEntries()
    {
        return getCachedConsumedEntries(getSession().getSessionContext());
    }


    public void setCachedConsumedEntries(Collection<CachedPromotionOrderEntryConsumed> value)
    {
        setCachedConsumedEntries(getSession().getSessionContext(), value);
    }


    public abstract Collection<AbstractPromotionAction> getCachedActions(SessionContext paramSessionContext);


    public abstract void setCachedActions(SessionContext paramSessionContext, Collection<AbstractPromotionAction> paramCollection);


    public abstract Collection<CachedPromotionOrderEntryConsumed> getCachedConsumedEntries(SessionContext paramSessionContext);


    public abstract void setCachedConsumedEntries(SessionContext paramSessionContext, Collection<CachedPromotionOrderEntryConsumed> paramCollection);
}
