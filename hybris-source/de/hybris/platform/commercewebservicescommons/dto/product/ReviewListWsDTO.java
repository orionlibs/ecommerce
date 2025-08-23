package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ReviewList", description = "Representation of a Review List")
public class ReviewListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "reviews", value = "List of reviews")
    private List<ReviewWsDTO> reviews;


    public void setReviews(List<ReviewWsDTO> reviews)
    {
        this.reviews = reviews;
    }


    public List<ReviewWsDTO> getReviews()
    {
        return this.reviews;
    }
}
