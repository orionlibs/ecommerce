package de.hybris.platform.customerreview.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.enums.CustomerReviewApprovalType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CustomerReviewModel extends ItemModel
{
    public static final String _TYPECODE = "CustomerReview";
    public static final String _REVIEWTOUSERREL = "ReviewToUserRel";
    public static final String _REVIEWTOPRODUCTREL = "ReviewToProductRel";
    public static final String HEADLINE = "headline";
    public static final String COMMENT = "comment";
    public static final String RATING = "rating";
    public static final String BLOCKED = "blocked";
    public static final String ALIAS = "alias";
    public static final String APPROVALSTATUS = "approvalStatus";
    public static final String LANGUAGE = "language";
    public static final String USER = "user";
    public static final String PRODUCT = "product";


    public CustomerReviewModel()
    {
    }


    public CustomerReviewModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CustomerReviewModel(ProductModel _product, Double _rating, UserModel _user)
    {
        setProduct(_product);
        setRating(_rating);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CustomerReviewModel(ItemModel _owner, ProductModel _product, Double _rating, UserModel _user)
    {
        setOwner(_owner);
        setProduct(_product);
        setRating(_rating);
        setUser(_user);
    }


    @Accessor(qualifier = "alias", type = Accessor.Type.GETTER)
    public String getAlias()
    {
        return (String)getPersistenceContext().getPropertyValue("alias");
    }


    @Accessor(qualifier = "approvalStatus", type = Accessor.Type.GETTER)
    public CustomerReviewApprovalType getApprovalStatus()
    {
        return (CustomerReviewApprovalType)getPersistenceContext().getPropertyValue("approvalStatus");
    }


    @Accessor(qualifier = "blocked", type = Accessor.Type.GETTER)
    public Boolean getBlocked()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("blocked");
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
    public String getComment()
    {
        return (String)getPersistenceContext().getPropertyValue("comment");
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.GETTER)
    public String getHeadline()
    {
        return (String)getPersistenceContext().getPropertyValue("headline");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "product", type = Accessor.Type.GETTER)
    public ProductModel getProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("product");
    }


    @Accessor(qualifier = "rating", type = Accessor.Type.GETTER)
    public Double getRating()
    {
        return (Double)getPersistenceContext().getPropertyValue("rating");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "alias", type = Accessor.Type.SETTER)
    public void setAlias(String value)
    {
        getPersistenceContext().setPropertyValue("alias", value);
    }


    @Accessor(qualifier = "approvalStatus", type = Accessor.Type.SETTER)
    public void setApprovalStatus(CustomerReviewApprovalType value)
    {
        getPersistenceContext().setPropertyValue("approvalStatus", value);
    }


    @Accessor(qualifier = "blocked", type = Accessor.Type.SETTER)
    public void setBlocked(Boolean value)
    {
        getPersistenceContext().setPropertyValue("blocked", value);
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
    public void setComment(String value)
    {
        getPersistenceContext().setPropertyValue("comment", value);
    }


    @Accessor(qualifier = "headline", type = Accessor.Type.SETTER)
    public void setHeadline(String value)
    {
        getPersistenceContext().setPropertyValue("headline", value);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("product", value);
    }


    @Accessor(qualifier = "rating", type = Accessor.Type.SETTER)
    public void setRating(Double value)
    {
        getPersistenceContext().setPropertyValue("rating", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
