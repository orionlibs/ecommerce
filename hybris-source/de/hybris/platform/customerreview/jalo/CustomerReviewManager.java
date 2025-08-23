package de.hybris.platform.customerreview.jalo;

import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.JspContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class CustomerReviewManager extends GeneratedCustomerReviewManager
{
    private static final Logger LOG = Logger.getLogger(CustomerReviewManager.class.getName());


    public static CustomerReviewManager getInstance()
    {
        ExtensionManager extensionManager = JaloSession.getCurrentSession().getExtensionManager();
        return (CustomerReviewManager)extensionManager.getExtension("customerreview");
    }


    public void createEssentialData(Map params, JspContext jspc)
    {
        TypeManager typeManager = getSession().getTypeManager();
        UserGroup customers = getSession().getUserManager().getUserGroupByGroupID(Constants.USER.CUSTOMER_USERGROUP);
        if(typeManager.getSearchRestriction(typeManager.getComposedType(CustomerReview.class), "CustomerReviewRestrictions") == null)
        {
            typeManager.createRestriction("CustomerReviewRestrictions", (Principal)customers, typeManager
                            .getComposedType(CustomerReview.class), "{blocked} = 0 OR {blocked} IS NULL");
        }
    }


    public CustomerReview createCustomerReview(Double rating, String headline, String comment, User user, Product product)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("comment", comment);
        params.put("headline", headline);
        params.put("rating", rating);
        params.put("user", user);
        params.put("product", product);
        return createCustomerReview(getSession().getSessionContext(), params);
    }


    public Double getAverageRating(SessionContext ctx, Product item)
    {
        String query = "SELECT avg({rating}) FROM {CustomerReview} WHERE {product} = ?product";
        Map<String, Product> values = Collections.singletonMap("product", item);
        List<Double> result = FlexibleSearch.getInstance().search("SELECT avg({rating}) FROM {CustomerReview} WHERE {product} = ?product", values, Collections.singletonList(Double.class), true, true, 0, -1).getResult();
        return result.iterator().next();
    }


    public Integer getNumberOfReviews(SessionContext ctx, Product item)
    {
        String query = "SELECT count(*) FROM {CustomerReview} WHERE {product} = ?product";
        Map<String, Product> values = Collections.singletonMap("product", item);
        List<Integer> result = FlexibleSearch.getInstance().search("SELECT count(*) FROM {CustomerReview} WHERE {product} = ?product", values, Collections.singletonList(Integer.class), true, true, 0, -1).getResult();
        return result.iterator().next();
    }


    public List<CustomerReview> getAllReviews(Product item)
    {
        return getAllReviews(JaloSession.getCurrentSession().getSessionContext(), item);
    }


    public List<CustomerReview> getAllReviews(SessionContext ctx, Product item)
    {
        String query = "SELECT {pk} FROM {CustomerReview} WHERE {product} = ?product ORDER BY {creationtime} DESC";
        Map<String, Product> values = Collections.singletonMap("product", item);
        List<CustomerReview> result = FlexibleSearch.getInstance().search(ctx, "SELECT {pk} FROM {CustomerReview} WHERE {product} = ?product ORDER BY {creationtime} DESC", values, Collections.singletonList(CustomerReview.class), true, true, 0, -1).getResult();
        return result;
    }
}
