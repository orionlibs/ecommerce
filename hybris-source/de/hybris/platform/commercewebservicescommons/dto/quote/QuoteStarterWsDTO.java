package de.hybris.platform.commercewebservicescommons.dto.quote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "QuoteStarter", description = "Object for the quote creation, either cartId or quoteCode must be specified.")
public class QuoteStarterWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "cartId", value = "CartId of the cart from which the quote will be created.", required = false, example = "0003050")
    private String cartId;
    @ApiModelProperty(name = "quoteCode", value = "Code of the quote for the requote action.", required = false, example = "0003060")
    private String quoteCode;


    public void setCartId(String cartId)
    {
        this.cartId = cartId;
    }


    public String getCartId()
    {
        return this.cartId;
    }


    public void setQuoteCode(String quoteCode)
    {
        this.quoteCode = quoteCode;
    }


    public String getQuoteCode()
    {
        return this.quoteCode;
    }
}
