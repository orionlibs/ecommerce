package de.hybris.platform.customerreview;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import java.util.List;

public interface CustomerReviewService
{
    CustomerReviewModel createCustomerReview(Double paramDouble, String paramString1, String paramString2, UserModel paramUserModel, ProductModel paramProductModel);


    Double getAverageRating(ProductModel paramProductModel);


    Integer getNumberOfReviews(ProductModel paramProductModel);


    List<CustomerReviewModel> getReviewsForProduct(ProductModel paramProductModel);


    List<CustomerReviewModel> getReviewsForCustomer(UserModel paramUserModel);


    List<CustomerReviewModel> getReviewsForProductAndLanguage(ProductModel paramProductModel, LanguageModel paramLanguageModel);
}
