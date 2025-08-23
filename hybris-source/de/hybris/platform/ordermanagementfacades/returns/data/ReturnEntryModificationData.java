package de.hybris.platform.ordermanagementfacades.returns.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReturnEntryModificationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String productCode;
    private String deliveryModeCode;
    private BigDecimal refundAmount;


    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }


    public String getProductCode()
    {
        return this.productCode;
    }


    public void setDeliveryModeCode(String deliveryModeCode)
    {
        this.deliveryModeCode = deliveryModeCode;
    }


    public String getDeliveryModeCode()
    {
        return this.deliveryModeCode;
    }


    public void setRefundAmount(BigDecimal refundAmount)
    {
        this.refundAmount = refundAmount;
    }


    public BigDecimal getRefundAmount()
    {
        return this.refundAmount;
    }
}
