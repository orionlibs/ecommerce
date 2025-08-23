package de.hybris.platform.customerreview.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.CustomerReviewService;
import de.hybris.platform.customerreview.dao.CustomerReviewDao;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCustomerReviewService implements CustomerReviewService
{
    private CustomerReviewDao customerReviewDao;
    private ModelService modelService;
    private static final String PRODUCT = "product";
    private static final String USERMODEL = "userModel";
    private static final String LANGUAGE = "language";


    public CustomerReviewModel createCustomerReview(Double rating, String headline, String comment, UserModel user, ProductModel product)
    {
        CustomerReviewModel review = (CustomerReviewModel)getModelService().create(CustomerReviewModel.class);
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setHeadline(headline);
        review.setComment(comment);
        getModelService().save(review);
        return review;
    }


    public Double getAverageRating(ProductModel product)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        return getCustomerReviewDao().getAverageRating(product);
    }


    public Integer getNumberOfReviews(ProductModel product)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        return getCustomerReviewDao().getNumberOfReviews(product);
    }


    public List<CustomerReviewModel> getReviewsForProduct(ProductModel product)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        return getCustomerReviewDao().getReviewsForProduct(product);
    }


    public List<CustomerReviewModel> getReviewsForCustomer(UserModel userModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("userModel", userModel);
        return getCustomerReviewDao().getReviewsForCustomer(userModel);
    }


    public List<CustomerReviewModel> getReviewsForProductAndLanguage(ProductModel product, LanguageModel language)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        ServicesUtil.validateParameterNotNullStandardMessage("language", language);
        return getCustomerReviewDao().getReviewsForProductAndLanguage(product, language);
    }


    protected CustomerReviewDao getCustomerReviewDao()
    {
        return this.customerReviewDao;
    }


    @Required
    public void setCustomerReviewDao(CustomerReviewDao customerReviewDao)
    {
        this.customerReviewDao = customerReviewDao;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
