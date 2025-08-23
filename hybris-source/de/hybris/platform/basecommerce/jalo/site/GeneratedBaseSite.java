package de.hybris.platform.basecommerce.jalo.site;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedBaseSite extends GenericItem
{
    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String STORES = "stores";
    protected static String STORESFORCMSSITE_SRC_ORDERED = "relation.StoresForCMSSite.source.ordered";
    protected static String STORESFORCMSSITE_TGT_ORDERED = "relation.StoresForCMSSite.target.ordered";
    protected static String STORESFORCMSSITE_MARKMODIFIED = "relation.StoresForCMSSite.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("uid", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("BaseStore");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(STORESFORCMSSITE_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedBaseSite.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedBaseSite.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public List<BaseStore> getStores(SessionContext ctx)
    {
        List<BaseStore> items = getLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.STORESFORCMSSITE, "BaseStore", null,
                        Utilities.getRelationOrderingOverride(STORESFORCMSSITE_SRC_ORDERED, true), false);
        return items;
    }


    public List<BaseStore> getStores()
    {
        return getStores(getSession().getSessionContext());
    }


    public long getStoresCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedBasecommerceConstants.Relations.STORESFORCMSSITE, "BaseStore", null);
    }


    public long getStoresCount()
    {
        return getStoresCount(getSession().getSessionContext());
    }


    public void setStores(SessionContext ctx, List<BaseStore> value)
    {
        setLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.STORESFORCMSSITE, null, value,
                        Utilities.getRelationOrderingOverride(STORESFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(STORESFORCMSSITE_MARKMODIFIED));
    }


    public void setStores(List<BaseStore> value)
    {
        setStores(getSession().getSessionContext(), value);
    }


    public void addToStores(SessionContext ctx, BaseStore value)
    {
        addLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.STORESFORCMSSITE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(STORESFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(STORESFORCMSSITE_MARKMODIFIED));
    }


    public void addToStores(BaseStore value)
    {
        addToStores(getSession().getSessionContext(), value);
    }


    public void removeFromStores(SessionContext ctx, BaseStore value)
    {
        removeLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.STORESFORCMSSITE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(STORESFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(STORESFORCMSSITE_MARKMODIFIED));
    }


    public void removeFromStores(BaseStore value)
    {
        removeFromStores(getSession().getSessionContext(), value);
    }


    public String getUid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uid");
    }


    public String getUid()
    {
        return getUid(getSession().getSessionContext());
    }


    public void setUid(SessionContext ctx, String value)
    {
        setProperty(ctx, "uid", value);
    }


    public void setUid(String value)
    {
        setUid(getSession().getSessionContext(), value);
    }
}
