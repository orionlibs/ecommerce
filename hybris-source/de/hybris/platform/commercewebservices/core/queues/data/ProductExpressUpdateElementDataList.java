package de.hybris.platform.commercewebservices.core.queues.data;

import java.io.Serializable;
import java.util.List;

public class ProductExpressUpdateElementDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ProductExpressUpdateElementData> productExpressUpdateElements;


    public void setProductExpressUpdateElements(List<ProductExpressUpdateElementData> productExpressUpdateElements)
    {
        this.productExpressUpdateElements = productExpressUpdateElements;
    }


    public List<ProductExpressUpdateElementData> getProductExpressUpdateElements()
    {
        return this.productExpressUpdateElements;
    }
}
