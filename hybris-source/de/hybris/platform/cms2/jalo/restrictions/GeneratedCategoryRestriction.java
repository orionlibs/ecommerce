package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.category.jalo.Category;
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

public abstract class GeneratedCategoryRestriction extends AbstractRestriction
{
    public static final String RECURSIVE = "recursive";
    public static final String CATEGORYCODES = "categoryCodes";
    public static final String CATEGORIES = "categories";
    protected static String CATEGORIESFORRESTRICTION_SRC_ORDERED = "relation.CategoriesForRestriction.source.ordered";
    protected static String CATEGORIESFORRESTRICTION_TGT_ORDERED = "relation.CategoriesForRestriction.target.ordered";
    protected static String CATEGORIESFORRESTRICTION_MARKMODIFIED = "relation.CategoriesForRestriction.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("recursive", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Category> getCategories(SessionContext ctx)
    {
        List<Category> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATEGORIESFORRESTRICTION, "Category", null, false, false);
        return items;
    }


    public Collection<Category> getCategories()
    {
        return getCategories(getSession().getSessionContext());
    }


    public long getCategoriesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.CATEGORIESFORRESTRICTION, "Category", null);
    }


    public long getCategoriesCount()
    {
        return getCategoriesCount(getSession().getSessionContext());
    }


    public void setCategories(SessionContext ctx, Collection<Category> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATEGORIESFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORRESTRICTION_MARKMODIFIED));
    }


    public void setCategories(Collection<Category> value)
    {
        setCategories(getSession().getSessionContext(), value);
    }


    public void addToCategories(SessionContext ctx, Category value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATEGORIESFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORRESTRICTION_MARKMODIFIED));
    }


    public void addToCategories(Category value)
    {
        addToCategories(getSession().getSessionContext(), value);
    }


    public void removeFromCategories(SessionContext ctx, Category value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATEGORIESFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromCategories(Category value)
    {
        removeFromCategories(getSession().getSessionContext(), value);
    }


    public List<String> getCategoryCodes()
    {
        return getCategoryCodes(getSession().getSessionContext());
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Category");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATEGORIESFORRESTRICTION_MARKMODIFIED);
        }
        return true;
    }


    public Boolean isRecursive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "recursive");
    }


    public Boolean isRecursive()
    {
        return isRecursive(getSession().getSessionContext());
    }


    public boolean isRecursiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRecursive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRecursiveAsPrimitive()
    {
        return isRecursiveAsPrimitive(getSession().getSessionContext());
    }


    public void setRecursive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "recursive", value);
    }


    public void setRecursive(Boolean value)
    {
        setRecursive(getSession().getSessionContext(), value);
    }


    public void setRecursive(SessionContext ctx, boolean value)
    {
        setRecursive(ctx, Boolean.valueOf(value));
    }


    public void setRecursive(boolean value)
    {
        setRecursive(getSession().getSessionContext(), value);
    }


    public abstract List<String> getCategoryCodes(SessionContext paramSessionContext);
}
