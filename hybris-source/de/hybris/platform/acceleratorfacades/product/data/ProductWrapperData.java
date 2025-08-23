package de.hybris.platform.acceleratorfacades.product.data;

import de.hybris.platform.commercefacades.product.data.ProductData;
import java.io.Serializable;

public class ProductWrapperData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ProductData productData;
    private String errorMsg;


    public void setProductData(ProductData productData)
    {
        this.productData = productData;
    }


    public ProductData getProductData()
    {
        return this.productData;
    }


    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }


    public String getErrorMsg()
    {
        return this.errorMsg;
    }
}
