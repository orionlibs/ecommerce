package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;

public class PurchaseOrderWsDTO implements Serializable
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
