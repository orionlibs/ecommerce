package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "CartModificationList", description = "Representation of a Cart modification list")
public class CartModificationListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "cartModifications", value = "List of cart modifications")
    private List<CartModificationWsDTO> cartModifications;


    public void setCartModifications(List<CartModificationWsDTO> cartModifications)
    {
        this.cartModifications = cartModifications;
    }


    public List<CartModificationWsDTO> getCartModifications()
    {
        return this.cartModifications;
    }
}
