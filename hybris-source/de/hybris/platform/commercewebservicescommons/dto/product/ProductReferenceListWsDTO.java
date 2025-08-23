package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ProductReferenceList", description = "Representation of a Product Reference List")
public class ProductReferenceListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "references", value = "List of product references")
    private List<ProductReferenceWsDTO> references;


    public void setReferences(List<ProductReferenceWsDTO> references)
    {
        this.references = references;
    }


    public List<ProductReferenceWsDTO> getReferences()
    {
        return this.references;
    }
}
