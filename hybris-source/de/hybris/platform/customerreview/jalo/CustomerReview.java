package de.hybris.platform.customerreview.jalo;

import de.hybris.platform.customerreview.constants.CustomerReviewConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.localization.Localization;
import java.util.HashSet;
import java.util.Set;

public class CustomerReview extends GeneratedCustomerReview
{
    public CustomerReview createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        checkMandatoryAttribute("product", allAttributes, missing);
        checkMandatoryAttribute("user", allAttributes, missing);
        checkMandatoryAttribute("rating", allAttributes, missing);
        if(!missing.isEmpty())
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new CustomerReview", 0);
        }
        return (CustomerReview)super.createItem(ctx, type, allAttributes);
    }


    public void setRating(SessionContext ctx, Double rating)
    {
        if(rating == null)
        {
            throw new JaloInvalidParameterException(Localization.getLocalizedString("error.customerreview.invalidrating", new Object[] {"null",
                            Double.valueOf(CustomerReviewConstants.getInstance().getMinRating()),
                            Double.valueOf(CustomerReviewConstants.getInstance().getMaxRating())}), 0);
        }
        if(rating.doubleValue() < CustomerReviewConstants.getInstance().getMinRating() || rating
                        .doubleValue() > CustomerReviewConstants.getInstance().getMaxRating())
        {
            throw new JaloInvalidParameterException(Localization.getLocalizedString("error.customerreview.invalidrating", new Object[] {rating,
                            Double.valueOf(CustomerReviewConstants.getInstance().getMinRating()),
                            Double.valueOf(CustomerReviewConstants.getInstance().getMaxRating())}), 0);
        }
        super.setRating(ctx, rating);
    }
}
