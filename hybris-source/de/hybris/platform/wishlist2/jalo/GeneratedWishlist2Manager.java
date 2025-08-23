package de.hybris.platform.wishlist2.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.wishlist2.constants.GeneratedWishlist2Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedWishlist2Manager extends Extension
{
    protected static final OneToManyHandler<Wishlist2> USER2WISHLIST2WISHLISTHANDLER = new OneToManyHandler(GeneratedWishlist2Constants.TC.WISHLIST2, true, "user", null, false, true, 2);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public Wishlist2 createWishlist2(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWishlist2Constants.TC.WISHLIST2);
            return (Wishlist2)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Wishlist2 : " + e.getMessage(), 0);
        }
    }


    public Wishlist2 createWishlist2(Map attributeValues)
    {
        return createWishlist2(getSession().getSessionContext(), attributeValues);
    }


    public Wishlist2Entry createWishlist2Entry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedWishlist2Constants.TC.WISHLIST2ENTRY);
            return (Wishlist2Entry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Wishlist2Entry : " + e.getMessage(), 0);
        }
    }


    public Wishlist2Entry createWishlist2Entry(Map attributeValues)
    {
        return createWishlist2Entry(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "wishlist";
    }


    public List<Wishlist2> getWishlist(SessionContext ctx, User item)
    {
        return (List<Wishlist2>)USER2WISHLIST2WISHLISTHANDLER.getValues(ctx, (Item)item);
    }


    public List<Wishlist2> getWishlist(User item)
    {
        return getWishlist(getSession().getSessionContext(), item);
    }


    public void setWishlist(SessionContext ctx, User item, List<Wishlist2> value)
    {
        USER2WISHLIST2WISHLISTHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setWishlist(User item, List<Wishlist2> value)
    {
        setWishlist(getSession().getSessionContext(), item, value);
    }


    public void addToWishlist(SessionContext ctx, User item, Wishlist2 value)
    {
        USER2WISHLIST2WISHLISTHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToWishlist(User item, Wishlist2 value)
    {
        addToWishlist(getSession().getSessionContext(), item, value);
    }


    public void removeFromWishlist(SessionContext ctx, User item, Wishlist2 value)
    {
        USER2WISHLIST2WISHLISTHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromWishlist(User item, Wishlist2 value)
    {
        removeFromWishlist(getSession().getSessionContext(), item, value);
    }
}
