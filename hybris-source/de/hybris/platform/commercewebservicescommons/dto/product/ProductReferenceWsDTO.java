package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ProductReference", description = "Representation of a Product Reference")
public class ProductReferenceWsDTO extends ReferenceWsDTO
{
    @ApiModelProperty(name = "preselected", value = "Flag stating if product reference is preselected")
    private Boolean preselected;


    public void setPreselected(Boolean preselected)
    {
        this.preselected = preselected;
    }


    public Boolean getPreselected()
    {
        return this.preselected;
    }
}
