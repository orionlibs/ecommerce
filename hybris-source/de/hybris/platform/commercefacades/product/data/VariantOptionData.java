package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import java.util.Collection;

public class VariantOptionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private StockData stock;
    private String url;
    private PriceData priceData;
    private Collection<VariantOptionQualifierData> variantOptionQualifiers;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setStock(StockData stock)
    {
        this.stock = stock;
    }


    public StockData getStock()
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


    public void setPriceData(PriceData priceData)
    {
        this.priceData = priceData;
    }


    public PriceData getPriceData()
    {
        return this.priceData;
    }


    public void setVariantOptionQualifiers(Collection<VariantOptionQualifierData> variantOptionQualifiers)
    {
        this.variantOptionQualifiers = variantOptionQualifiers;
    }


    public Collection<VariantOptionQualifierData> getVariantOptionQualifiers()
    {
        return this.variantOptionQualifiers;
    }
}
