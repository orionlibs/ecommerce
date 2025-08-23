package de.hybris.platform.customerreview.dao.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.dao.CustomerReviewDao;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collections;
import java.util.List;

public class DefaultCustomerReviewDao extends AbstractItemDao implements CustomerReviewDao
{
    private static final String FIND_REVIEWS_BY_USER = "SELECT {" + Item.PK + "} FROM {CustomerReview} WHERE {user}=?user ORDER BY {creationtime} DESC";


    public List<CustomerReviewModel> getReviewsForProduct(ProductModel product)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        String query = "SELECT {" + Item.PK + "} FROM {CustomerReview} WHERE {product}=?product ORDER BY {creationtime} DESC";
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query);
        fsQuery.addQueryParameter("product", product);
        fsQuery.setResultClassList(Collections.singletonList(CustomerReviewModel.class));
        SearchResult<CustomerReviewModel> searchResult = getFlexibleSearchService().search(fsQuery);
        return searchResult.getResult();
    }


    public List<CustomerReviewModel> getReviewsForProductAndLanguage(ProductModel product, LanguageModel language)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        ServicesUtil.validateParameterNotNullStandardMessage("language", language);
        String query = "SELECT {" + Item.PK + "} FROM {CustomerReview} WHERE {product}=?product AND {language}=?language ORDER BY {creationtime} DESC";
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query);
        fsQuery.addQueryParameter("product", product);
        fsQuery.addQueryParameter("language", language);
        fsQuery.setResultClassList(Collections.singletonList(CustomerReviewModel.class));
        SearchResult<CustomerReviewModel> searchResult = getFlexibleSearchService().search(fsQuery);
        return searchResult.getResult();
    }


    public List<CustomerReviewModel> getReviewsForCustomer(UserModel customer)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("customer", customer);
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(FIND_REVIEWS_BY_USER);
        fsQuery.addQueryParameter("user", customer);
        fsQuery.setResultClassList(Collections.singletonList(CustomerReviewModel.class));
        SearchResult<CustomerReviewModel> searchResult = getFlexibleSearchService().search(fsQuery);
        return searchResult.getResult();
    }


    public Integer getNumberOfReviews(ProductModel product)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery("SELECT count(*) FROM {CustomerReview} WHERE {product} = ?product");
        fsQuery.addQueryParameter("product", product);
        fsQuery.setResultClassList(Collections.singletonList(Integer.class));
        SearchResult<Integer> searchResult = getFlexibleSearchService().search(fsQuery);
        return searchResult.getResult().iterator().next();
    }


    public Double getAverageRating(ProductModel product)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery("SELECT avg({rating}) FROM {CustomerReview} WHERE {product} = ?product");
        fsQuery.addQueryParameter("product", product);
        fsQuery.setResultClassList(Collections.singletonList(Double.class));
        SearchResult<Double> searchResult = getFlexibleSearchService().search(fsQuery);
        return searchResult.getResult().iterator().next();
    }
}
