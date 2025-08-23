package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "SaveCartResult", description = "Representation of a Save Cart Result")
public class SaveCartResultWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "savedCartData", value = "Cart data information for saved cart")
    private CartWsDTO savedCartData;


    public void setSavedCartData(CartWsDTO savedCartData)
    {
        this.savedCartData = savedCartData;
    }


    public CartWsDTO getSavedCartData()
    {
        return this.savedCartData;
    }
}
