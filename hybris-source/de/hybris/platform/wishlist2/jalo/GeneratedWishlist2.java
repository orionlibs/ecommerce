package de.hybris.platform.wishlist2.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.wishlist2.constants.GeneratedWishlist2Constants;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedWishlist2 extends GenericItem
{
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String DEFAULT = "default";
    public static final String USER = "user";
    public static final String ENTRIES = "entries";
    protected static final BidirectionalOneToManyHandler<GeneratedWishlist2> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedWishlist2Constants.TC.WISHLIST2, false, "user", null, false, true, 2);
    protected static final OneToManyHandler<Wishlist2Entry> ENTRIESHANDLER = new OneToManyHandler(GeneratedWishlist2Constants.TC.WISHLIST2ENTRY, true, "wishlist", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("default", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        USERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isDefault(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "default");
    }


    public Boolean isDefault()
    {
        return isDefault(getSession().getSessionContext());
    }


    public boolean isDefaultAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDefault(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDefaultAsPrimitive()
    {
        return isDefaultAsPrimitive(getSession().getSessionContext());
    }


    public void setDefault(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "default", value);
    }


    public void setDefault(Boolean value)
    {
        setDefault(getSession().getSessionContext(), value);
    }


    public void setDefault(SessionContext ctx, boolean value)
    {
        setDefault(ctx, Boolean.valueOf(value));
    }


    public void setDefault(boolean value)
    {
        setDefault(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public List<Wishlist2Entry> getEntries(SessionContext ctx)
    {
        return (List<Wishlist2Entry>)ENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public List<Wishlist2Entry> getEntries()
    {
        return getEntries(getSession().getSessionContext());
    }


    public void setEntries(SessionContext ctx, List<Wishlist2Entry> value)
    {
        ENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setEntries(List<Wishlist2Entry> value)
    {
        setEntries(getSession().getSessionContext(), value);
    }


    public void addToEntries(SessionContext ctx, Wishlist2Entry value)
    {
        ENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToEntries(Wishlist2Entry value)
    {
        addToEntries(getSession().getSessionContext(), value);
    }


    public void removeFromEntries(SessionContext ctx, Wishlist2Entry value)
    {
        ENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromEntries(Wishlist2Entry value)
    {
        removeFromEntries(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        USERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }
}
