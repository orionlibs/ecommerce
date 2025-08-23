package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;

public class PurchaseOrderData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String purchaseOrderNumber;


    public void setPurchaseOrderNumber(String purchaseOrderNumber)
    {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }


    public String getPurchaseOrderNumber()
    {
        return this.purchaseOrderNumber;
    }
}
