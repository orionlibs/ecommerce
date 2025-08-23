package de.hybris.platform.customerreview.jalo;

import de.hybris.platform.customerreview.constants.GeneratedCustomerReviewConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCustomerReview extends GenericItem
{
    public static final String HEADLINE = "headline";
    public static final String COMMENT = "comment";
    public static final String RATING = "rating";
    public static final String BLOCKED = "blocked";
    public static final String ALIAS = "alias";
    public static final String APPROVALSTATUS = "approvalStatus";
    public static final String LANGUAGE = "language";
    public static final String USER = "user";
    public static final String PRODUCT = "product";
    protected static final BidirectionalOneToManyHandler<GeneratedCustomerReview> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedCustomerReviewConstants.TC.CUSTOMERREVIEW, false, "user", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedCustomerReview> PRODUCTHANDLER = new BidirectionalOneToManyHandler(GeneratedCustomerReviewConstants.TC.CUSTOMERREVIEW, false, "product", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("headline", Item.AttributeMode.INITIAL);
        tmp.put("comment", Item.AttributeMode.INITIAL);
        tmp.put("rating", Item.AttributeMode.INITIAL);
        tmp.put("blocked", Item.AttributeMode.INITIAL);
        tmp.put("alias", Item.AttributeMode.INITIAL);
        tmp.put("approvalStatus", Item.AttributeMode.INITIAL);
        tmp.put("language", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        tmp.put("product", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getAlias(SessionContext ctx)
    {
        return (String)getProperty(ctx, "alias");
    }


    public String getAlias()
    {
        return getAlias(getSession().getSessionContext());
    }


    public void setAlias(SessionContext ctx, String value)
    {
        setProperty(ctx, "alias", value);
    }


    public void setAlias(String value)
    {
        setAlias(getSession().getSessionContext(), value);
    }


    public EnumerationValue getApprovalStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "approvalStatus");
    }


    public EnumerationValue getApprovalStatus()
    {
        return getApprovalStatus(getSession().getSessionContext());
    }


    public void setApprovalStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "approvalStatus", value);
    }


    public void setApprovalStatus(EnumerationValue value)
    {
        setApprovalStatus(getSession().getSessionContext(), value);
    }


    public Boolean isBlocked(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "blocked");
    }


    public Boolean isBlocked()
    {
        return isBlocked(getSession().getSessionContext());
    }


    public boolean isBlockedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isBlocked(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isBlockedAsPrimitive()
    {
        return isBlockedAsPrimitive(getSession().getSessionContext());
    }


    public void setBlocked(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "blocked", value);
    }


    public void setBlocked(Boolean value)
    {
        setBlocked(getSession().getSessionContext(), value);
    }


    public void setBlocked(SessionContext ctx, boolean value)
    {
        setBlocked(ctx, Boolean.valueOf(value));
    }


    public void setBlocked(boolean value)
    {
        setBlocked(getSession().getSessionContext(), value);
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
        USERHANDLER.newInstance(ctx, allAttributes);
        PRODUCTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getHeadline(SessionContext ctx)
    {
        return (String)getProperty(ctx, "headline");
    }


    public String getHeadline()
    {
        return getHeadline(getSession().getSessionContext());
    }


    public void setHeadline(SessionContext ctx, String value)
    {
        setProperty(ctx, "headline", value);
    }


    public void setHeadline(String value)
    {
        setHeadline(getSession().getSessionContext(), value);
    }


    public Language getLanguage(SessionContext ctx)
    {
        return (Language)getProperty(ctx, "language");
    }


    public Language getLanguage()
    {
        return getLanguage(getSession().getSessionContext());
    }


    public void setLanguage(SessionContext ctx, Language value)
    {
        setProperty(ctx, "language", value);
    }


    public void setLanguage(Language value)
    {
        setLanguage(getSession().getSessionContext(), value);
    }


    public Product getProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "product");
    }


    public Product getProduct()
    {
        return getProduct(getSession().getSessionContext());
    }


    protected void setProduct(SessionContext ctx, Product value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'product' is not changeable", 0);
        }
        PRODUCTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setProduct(Product value)
    {
        setProduct(getSession().getSessionContext(), value);
    }


    public Double getRating(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "rating");
    }


    public Double getRating()
    {
        return getRating(getSession().getSessionContext());
    }


    public double getRatingAsPrimitive(SessionContext ctx)
    {
        Double value = getRating(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getRatingAsPrimitive()
    {
        return getRatingAsPrimitive(getSession().getSessionContext());
    }


    public void setRating(SessionContext ctx, Double value)
    {
        setProperty(ctx, "rating", value);
    }


    public void setRating(Double value)
    {
        setRating(getSession().getSessionContext(), value);
    }


    public void setRating(SessionContext ctx, double value)
    {
        setRating(ctx, Double.valueOf(value));
    }


    public void setRating(double value)
    {
        setRating(getSession().getSessionContext(), value);
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
