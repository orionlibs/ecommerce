package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCatalogRestriction extends AbstractRestriction
{
    public static final String CATALOGS = "catalogs";
    protected static String CATALOGSFORRESTRICTION_SRC_ORDERED = "relation.CatalogsForRestriction.source.ordered";
    protected static String CATALOGSFORRESTRICTION_TGT_ORDERED = "relation.CatalogsForRestriction.target.ordered";
    protected static String CATALOGSFORRESTRICTION_MARKMODIFIED = "relation.CatalogsForRestriction.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Catalog> getCatalogs(SessionContext ctx)
    {
        List<Catalog> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATALOGSFORRESTRICTION, "Catalog", null, false, false);
        return items;
    }


    public Collection<Catalog> getCatalogs()
    {
        return getCatalogs(getSession().getSessionContext());
    }


    public long getCatalogsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.CATALOGSFORRESTRICTION, "Catalog", null);
    }


    public long getCatalogsCount()
    {
        return getCatalogsCount(getSession().getSessionContext());
    }


    public void setCatalogs(SessionContext ctx, Collection<Catalog> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATALOGSFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORRESTRICTION_MARKMODIFIED));
    }


    public void setCatalogs(Collection<Catalog> value)
    {
        setCatalogs(getSession().getSessionContext(), value);
    }


    public void addToCatalogs(SessionContext ctx, Catalog value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATALOGSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORRESTRICTION_MARKMODIFIED));
    }


    public void addToCatalogs(Catalog value)
    {
        addToCatalogs(getSession().getSessionContext(), value);
    }


    public void removeFromCatalogs(SessionContext ctx, Catalog value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATALOGSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromCatalogs(Catalog value)
    {
        removeFromCatalogs(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Catalog");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATALOGSFORRESTRICTION_MARKMODIFIED);
        }
        return true;
    }
}
