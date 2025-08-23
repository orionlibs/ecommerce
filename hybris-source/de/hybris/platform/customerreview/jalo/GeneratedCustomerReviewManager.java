package de.hybris.platform.customerreview.jalo;

import de.hybris.platform.customerreview.constants.GeneratedCustomerReviewConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCustomerReviewManager extends Extension
{
    protected static final OneToManyHandler<CustomerReview> REVIEWTOPRODUCTRELPRODUCTREVIEWSHANDLER = new OneToManyHandler(GeneratedCustomerReviewConstants.TC.CUSTOMERREVIEW, true, "product", null, false, true, 0);
    protected static final OneToManyHandler<CustomerReview> REVIEWTOUSERRELCUSTOMERREVIEWSHANDLER = new OneToManyHandler(GeneratedCustomerReviewConstants.TC.CUSTOMERREVIEW, false, "user", null, false, true, 0);
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


    public Double getAverageRating(Product item)
    {
        return getAverageRating(getSession().getSessionContext(), item);
    }


    public double getAverageRatingAsPrimitive(SessionContext ctx, Product item)
    {
        Double value = getAverageRating(ctx, item);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getAverageRatingAsPrimitive(Product item)
    {
        return getAverageRatingAsPrimitive(getSession().getSessionContext(), item);
    }


    public CustomerReview createCustomerReview(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCustomerReviewConstants.TC.CUSTOMERREVIEW);
            return (CustomerReview)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CustomerReview : " + e.getMessage(), 0);
        }
    }


    public CustomerReview createCustomerReview(Map attributeValues)
    {
        return createCustomerReview(getSession().getSessionContext(), attributeValues);
    }


    public Collection<CustomerReview> getCustomerReviews(SessionContext ctx, User item)
    {
        return REVIEWTOUSERRELCUSTOMERREVIEWSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CustomerReview> getCustomerReviews(User item)
    {
        return getCustomerReviews(getSession().getSessionContext(), item);
    }


    protected void setCustomerReviews(SessionContext ctx, User item, Collection<CustomerReview> value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + GeneratedCustomerReviewConstants.Attributes.User.CUSTOMERREVIEWS + "' is not changeable", 0);
        }
        REVIEWTOUSERRELCUSTOMERREVIEWSHANDLER.setValues(ctx, (Item)item, value);
    }


    protected void setCustomerReviews(User item, Collection<CustomerReview> value)
    {
        setCustomerReviews(getSession().getSessionContext(), item, value);
    }


    protected void addToCustomerReviews(SessionContext ctx, User item, CustomerReview value)
    {
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + GeneratedCustomerReviewConstants.Attributes.User.CUSTOMERREVIEWS + "' is not changeable", 0);
        }
        REVIEWTOUSERRELCUSTOMERREVIEWSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    protected void addToCustomerReviews(User item, CustomerReview value)
    {
        addToCustomerReviews(getSession().getSessionContext(), item, value);
    }


    protected void removeFromCustomerReviews(SessionContext ctx, User item, CustomerReview value)
    {
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute '" + GeneratedCustomerReviewConstants.Attributes.User.CUSTOMERREVIEWS + "' is not changeable", 0);
        }
        REVIEWTOUSERRELCUSTOMERREVIEWSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    protected void removeFromCustomerReviews(User item, CustomerReview value)
    {
        removeFromCustomerReviews(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "customerreview";
    }


    public Integer getNumberOfReviews(Product item)
    {
        return getNumberOfReviews(getSession().getSessionContext(), item);
    }


    public int getNumberOfReviewsAsPrimitive(SessionContext ctx, Product item)
    {
        Integer value = getNumberOfReviews(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getNumberOfReviewsAsPrimitive(Product item)
    {
        return getNumberOfReviewsAsPrimitive(getSession().getSessionContext(), item);
    }


    public Collection<CustomerReview> getProductReviews(SessionContext ctx, Product item)
    {
        return REVIEWTOPRODUCTRELPRODUCTREVIEWSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CustomerReview> getProductReviews(Product item)
    {
        return getProductReviews(getSession().getSessionContext(), item);
    }


    public void setProductReviews(SessionContext ctx, Product item, Collection<CustomerReview> value)
    {
        REVIEWTOPRODUCTRELPRODUCTREVIEWSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setProductReviews(Product item, Collection<CustomerReview> value)
    {
        setProductReviews(getSession().getSessionContext(), item, value);
    }


    public void addToProductReviews(SessionContext ctx, Product item, CustomerReview value)
    {
        REVIEWTOPRODUCTRELPRODUCTREVIEWSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToProductReviews(Product item, CustomerReview value)
    {
        addToProductReviews(getSession().getSessionContext(), item, value);
    }


    public void removeFromProductReviews(SessionContext ctx, Product item, CustomerReview value)
    {
        REVIEWTOPRODUCTRELPRODUCTREVIEWSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromProductReviews(Product item, CustomerReview value)
    {
        removeFromProductReviews(getSession().getSessionContext(), item, value);
    }


    public abstract Double getAverageRating(SessionContext paramSessionContext, Product paramProduct);


    public abstract Integer getNumberOfReviews(SessionContext paramSessionContext, Product paramProduct);
}
