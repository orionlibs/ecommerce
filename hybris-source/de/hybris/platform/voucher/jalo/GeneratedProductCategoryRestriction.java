package de.hybris.platform.voucher.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedProductCategoryRestriction extends ProductRestriction
{
    public static final String CATEGORIES = "categories";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ProductRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("categories", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Category> getCategories(SessionContext ctx)
    {
        Collection<Category> coll = (Collection<Category>)getProperty(ctx, "categories");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Category> getCategories()
    {
        return getCategories(getSession().getSessionContext());
    }


    public void setCategories(SessionContext ctx, Collection<Category> value)
    {
        setProperty(ctx, "categories", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setCategories(Collection<Category> value)
    {
        setCategories(getSession().getSessionContext(), value);
    }
}
