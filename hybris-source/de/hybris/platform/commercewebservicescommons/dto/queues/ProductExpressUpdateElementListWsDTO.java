package de.hybris.platform.commercewebservicescommons.dto.queues;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ProductExpressUpdateElementList", description = "Representation of a Product Express Update Element List")
public class ProductExpressUpdateElementListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "productExpressUpdateElements", value = "List of product express update element")
    private List<ProductExpressUpdateElementWsDTO> productExpressUpdateElements;


    public void setProductExpressUpdateElements(List<ProductExpressUpdateElementWsDTO> productExpressUpdateElements)
    {
        this.productExpressUpdateElements = productExpressUpdateElements;
    }


    public List<ProductExpressUpdateElementWsDTO> getProductExpressUpdateElements()
    {
        return this.productExpressUpdateElements;
    }
}
