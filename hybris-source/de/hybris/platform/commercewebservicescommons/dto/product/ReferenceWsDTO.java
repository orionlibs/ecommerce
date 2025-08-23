package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Reference", description = "Representation of a Reference")
public class ReferenceWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "referenceType", value = "Reference type")
    private String referenceType;
    @ApiModelProperty(name = "description", value = "Reference description")
    private String description;
    @ApiModelProperty(name = "quantity", value = "Reference quantity")
    private Integer quantity;
    @ApiModelProperty(name = "target", value = "Target product")
    private ProductWsDTO target;


    public void setReferenceType(String referenceType)
    {
        this.referenceType = referenceType;
    }


    public String getReferenceType()
    {
        return this.referenceType;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }


    public Integer getQuantity()
    {
        return this.quantity;
    }


    public void setTarget(ProductWsDTO target)
    {
        this.target = target;
    }


    public ProductWsDTO getTarget()
    {
        return this.target;
    }
}
