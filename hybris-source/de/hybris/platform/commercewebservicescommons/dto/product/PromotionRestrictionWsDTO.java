package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "PromotionRestriction", description = "Representation of a Promotion Restriction")
public class PromotionRestrictionWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "restrictionType", value = "Type of the promotion restriction")
    private String restrictionType;
    @ApiModelProperty(name = "description", value = "Description of the promotion restriction")
    private String description;


    public void setRestrictionType(String restrictionType)
    {
        this.restrictionType = restrictionType;
    }


    public String getRestrictionType()
    {
        return this.restrictionType;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }
}
