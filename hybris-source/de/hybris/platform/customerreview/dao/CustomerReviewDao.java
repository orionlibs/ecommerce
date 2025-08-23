package de.hybris.platform.customerreview.dao;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import java.util.List;

public interface CustomerReviewDao
{
    List<CustomerReviewModel> getReviewsForProduct(ProductModel paramProductModel);


    List<CustomerReviewModel> getReviewsForProductAndLanguage(ProductModel paramProductModel, LanguageModel paramLanguageModel);


    List<CustomerReviewModel> getReviewsForCustomer(UserModel paramUserModel);


    Integer getNumberOfReviews(ProductModel paramProductModel);


    Double getAverageRating(ProductModel paramProductModel);
}
