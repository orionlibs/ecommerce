package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "CartList", description = "Representation of a Cart list")
public class CartListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "carts", value = "List of carts")
    private List<CartWsDTO> carts;


    public void setCarts(List<CartWsDTO> carts)
    {
        this.carts = carts;
    }


    public List<CartWsDTO> getCarts()
    {
        return this.carts;
    }
}
