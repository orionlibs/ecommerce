package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedBaseStoreTimeRestriction extends TimeRestriction
{
    public static final String PASSIFSTOREDOESNTMATCH = "passIfStoreDoesntMatch";
    public static final String BASESTORES = "baseStores";
    protected static String STORETIMERESTRICTION2BASESTORE_SRC_ORDERED = "relation.StoreTimeRestriction2BaseStore.source.ordered";
    protected static String STORETIMERESTRICTION2BASESTORE_TGT_ORDERED = "relation.StoreTimeRestriction2BaseStore.target.ordered";
    protected static String STORETIMERESTRICTION2BASESTORE_MARKMODIFIED = "relation.StoreTimeRestriction2BaseStore.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(TimeRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("passIfStoreDoesntMatch", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<BaseStore> getBaseStores(SessionContext ctx)
    {
        List<BaseStore> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.STORETIMERESTRICTION2BASESTORE, "BaseStore", null, false, false);
        return items;
    }


    public Collection<BaseStore> getBaseStores()
    {
        return getBaseStores(getSession().getSessionContext());
    }


    public long getBaseStoresCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.STORETIMERESTRICTION2BASESTORE, "BaseStore", null);
    }


    public long getBaseStoresCount()
    {
        return getBaseStoresCount(getSession().getSessionContext());
    }


    public void setBaseStores(SessionContext ctx, Collection<BaseStore> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.STORETIMERESTRICTION2BASESTORE, null, value, false, false,
                        Utilities.getMarkModifiedOverride(STORETIMERESTRICTION2BASESTORE_MARKMODIFIED));
    }


    public void setBaseStores(Collection<BaseStore> value)
    {
        setBaseStores(getSession().getSessionContext(), value);
    }


    public void addToBaseStores(SessionContext ctx, BaseStore value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.STORETIMERESTRICTION2BASESTORE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STORETIMERESTRICTION2BASESTORE_MARKMODIFIED));
    }


    public void addToBaseStores(BaseStore value)
    {
        addToBaseStores(getSession().getSessionContext(), value);
    }


    public void removeFromBaseStores(SessionContext ctx, BaseStore value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.STORETIMERESTRICTION2BASESTORE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(STORETIMERESTRICTION2BASESTORE_MARKMODIFIED));
    }


    public void removeFromBaseStores(BaseStore value)
    {
        removeFromBaseStores(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("BaseStore");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(STORETIMERESTRICTION2BASESTORE_MARKMODIFIED);
        }
        return true;
    }


    public Boolean isPassIfStoreDoesntMatch(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "passIfStoreDoesntMatch");
    }


    public Boolean isPassIfStoreDoesntMatch()
    {
        return isPassIfStoreDoesntMatch(getSession().getSessionContext());
    }


    public boolean isPassIfStoreDoesntMatchAsPrimitive(SessionContext ctx)
    {
        Boolean value = isPassIfStoreDoesntMatch(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPassIfStoreDoesntMatchAsPrimitive()
    {
        return isPassIfStoreDoesntMatchAsPrimitive(getSession().getSessionContext());
    }


    public void setPassIfStoreDoesntMatch(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "passIfStoreDoesntMatch", value);
    }


    public void setPassIfStoreDoesntMatch(Boolean value)
    {
        setPassIfStoreDoesntMatch(getSession().getSessionContext(), value);
    }


    public void setPassIfStoreDoesntMatch(SessionContext ctx, boolean value)
    {
        setPassIfStoreDoesntMatch(ctx, Boolean.valueOf(value));
    }


    public void setPassIfStoreDoesntMatch(boolean value)
    {
        setPassIfStoreDoesntMatch(getSession().getSessionContext(), value);
    }
}
