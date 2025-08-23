package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;

@ApiModel(value = "VariantOption", description = "Representation of a Variant Option")
public class VariantOptionWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of the variant option")
    private String code;
    @ApiModelProperty(name = "stock", value = "Stock value of the variant option")
    private StockWsDTO stock;
    @ApiModelProperty(name = "url", value = "Url address of the variant option")
    private String url;
    @ApiModelProperty(name = "priceData", value = "Price data information of the variant option")
    private PriceWsDTO priceData;
    @ApiModelProperty(name = "variantOptionQualifiers", value = "List of variant option qualifiers")
    private Collection<VariantOptionQualifierWsDTO> variantOptionQualifiers;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setStock(StockWsDTO stock)
    {
        this.stock = stock;
    }


    public StockWsDTO getStock()
    {
        return this.stock;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setPriceData(PriceWsDTO priceData)
    {
        this.priceData = priceData;
    }


    public PriceWsDTO getPriceData()
    {
        return this.priceData;
    }


    public void setVariantOptionQualifiers(Collection<VariantOptionQualifierWsDTO> variantOptionQualifiers)
    {
        this.variantOptionQualifiers = variantOptionQualifiers;
    }


    public Collection<VariantOptionQualifierWsDTO> getVariantOptionQualifiers()
    {
        return this.variantOptionQualifiers;
    }
}
