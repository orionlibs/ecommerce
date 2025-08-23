package de.hybris.platform.commercewebservicescommons.dto.product;

import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "Review", description = "Representation of a Review")
public class ReviewWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "id", value = "Identifier of review")
    private String id;
    @ApiModelProperty(name = "headline", value = "Review headline")
    private String headline;
    @ApiModelProperty(name = "comment", value = "Review comment")
    private String comment;
    @ApiModelProperty(name = "rating", value = "Review rating value")
    private Double rating;
    @ApiModelProperty(name = "date", value = "Date of the review")
    private Date date;
    @ApiModelProperty(name = "alias", value = "Alias name for the review")
    private String alias;
    @ApiModelProperty(name = "principal", value = "Person related to the review")
    private UserWsDTO principal;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setHeadline(String headline)
    {
        this.headline = headline;
    }


    public String getHeadline()
    {
        return this.headline;
    }


    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public String getComment()
    {
        return this.comment;
    }


    public void setRating(Double rating)
    {
        this.rating = rating;
    }


    public Double getRating()
    {
        return this.rating;
    }


    public void setDate(Date date)
    {
        this.date = date;
    }


    public Date getDate()
    {
        return this.date;
    }


    public void setAlias(String alias)
    {
        this.alias = alias;
    }


    public String getAlias()
    {
        return this.alias;
    }


    public void setPrincipal(UserWsDTO principal)
    {
        this.principal = principal;
    }


    public UserWsDTO getPrincipal()
    {
        return this.principal;
    }
}
