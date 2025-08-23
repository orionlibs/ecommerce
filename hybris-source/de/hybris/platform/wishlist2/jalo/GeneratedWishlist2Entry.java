package de.hybris.platform.wishlist2.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.wishlist2.constants.GeneratedWishlist2Constants;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedWishlist2Entry extends GenericItem
{
    public static final String PRODUCT = "product";
    public static final String DESIRED = "desired";
    public static final String RECEIVED = "received";
    public static final String PRIORITY = "priority";
    public static final String ADDEDDATE = "addedDate";
    public static final String COMMENT = "comment";
    public static final String WISHLIST = "wishlist";
    protected static final BidirectionalOneToManyHandler<GeneratedWishlist2Entry> WISHLISTHANDLER = new BidirectionalOneToManyHandler(GeneratedWishlist2Constants.TC.WISHLIST2ENTRY, false, "wishlist", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("product", Item.AttributeMode.INITIAL);
        tmp.put("desired", Item.AttributeMode.INITIAL);
        tmp.put("received", Item.AttributeMode.INITIAL);
        tmp.put("priority", Item.AttributeMode.INITIAL);
        tmp.put("addedDate", Item.AttributeMode.INITIAL);
        tmp.put("comment", Item.AttributeMode.INITIAL);
        tmp.put("wishlist", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Date getAddedDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "addedDate");
    }


    public Date getAddedDate()
    {
        return getAddedDate(getSession().getSessionContext());
    }


    public void setAddedDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "addedDate", value);
    }


    public void setAddedDate(Date value)
    {
        setAddedDate(getSession().getSessionContext(), value);
    }


    public String getComment(SessionContext ctx)
    {
        return (String)getProperty(ctx, "comment");
    }


    public String getComment()
    {
        return getComment(getSession().getSessionContext());
    }


    public void setComment(SessionContext ctx, String value)
    {
        setProperty(ctx, "comment", value);
    }


    public void setComment(String value)
    {
        setComment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        WISHLISTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Integer getDesired(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "desired");
    }


    public Integer getDesired()
    {
        return getDesired(getSession().getSessionContext());
    }


    public int getDesiredAsPrimitive(SessionContext ctx)
    {
        Integer value = getDesired(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getDesiredAsPrimitive()
    {
        return getDesiredAsPrimitive(getSession().getSessionContext());
    }


    public void setDesired(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "desired", value);
    }


    public void setDesired(Integer value)
    {
        setDesired(getSession().getSessionContext(), value);
    }


    public void setDesired(SessionContext ctx, int value)
    {
        setDesired(ctx, Integer.valueOf(value));
    }


    public void setDesired(int value)
    {
        setDesired(getSession().getSessionContext(), value);
    }


    public EnumerationValue getPriority(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "priority");
    }


    public EnumerationValue getPriority()
    {
        return getPriority(getSession().getSessionContext());
    }


    public void setPriority(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "priority", value);
    }


    public void setPriority(EnumerationValue value)
    {
        setPriority(getSession().getSessionContext(), value);
    }


    public Product getProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "product");
    }


    public Product getProduct()
    {
        return getProduct(getSession().getSessionContext());
    }


    public void setProduct(SessionContext ctx, Product value)
    {
        setProperty(ctx, "product", value);
    }


    public void setProduct(Product value)
    {
        setProduct(getSession().getSessionContext(), value);
    }


    public Integer getReceived(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "received");
    }


    public Integer getReceived()
    {
        return getReceived(getSession().getSessionContext());
    }


    public int getReceivedAsPrimitive(SessionContext ctx)
    {
        Integer value = getReceived(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getReceivedAsPrimitive()
    {
        return getReceivedAsPrimitive(getSession().getSessionContext());
    }


    public void setReceived(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "received", value);
    }


    public void setReceived(Integer value)
    {
        setReceived(getSession().getSessionContext(), value);
    }


    public void setReceived(SessionContext ctx, int value)
    {
        setReceived(ctx, Integer.valueOf(value));
    }


    public void setReceived(int value)
    {
        setReceived(getSession().getSessionContext(), value);
    }


    public Wishlist2 getWishlist(SessionContext ctx)
    {
        return (Wishlist2)getProperty(ctx, "wishlist");
    }


    public Wishlist2 getWishlist()
    {
        return getWishlist(getSession().getSessionContext());
    }


    public void setWishlist(SessionContext ctx, Wishlist2 value)
    {
        WISHLISTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setWishlist(Wishlist2 value)
    {
        setWishlist(getSession().getSessionContext(), value);
    }
}
