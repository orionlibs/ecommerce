package de.hybris.platform.commercewebservices.core.product.data;

import de.hybris.platform.commercefacades.product.data.ReviewData;
import java.io.Serializable;
import java.util.List;

public class ReviewDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ReviewData> reviews;


    public void setReviews(List<ReviewData> reviews)
    {
        this.reviews = reviews;
    }


    public List<ReviewData> getReviews()
    {
        return this.reviews;
    }
}
