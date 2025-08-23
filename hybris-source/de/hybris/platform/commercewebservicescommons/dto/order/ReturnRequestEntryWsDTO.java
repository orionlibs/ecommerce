package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "ReturnRequestEntry", description = "Representation of a return request entry which contains information about the returned product")
public class ReturnRequestEntryWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orderEntry", value = "Order entry related to the return request entry")
    private OrderEntryWsDTO orderEntry;
    @ApiModelProperty(name = "expectedQuantity", value = "Quantity which is expected to be returned for this return request entry", example = "5")
    private Long expectedQuantity;
    @ApiModelProperty(name = "refundAmount", value = "Refund amount of the entry")
    private PriceWsDTO refundAmount;


    public void setOrderEntry(OrderEntryWsDTO orderEntry)
    {
        this.orderEntry = orderEntry;
    }


    public OrderEntryWsDTO getOrderEntry()
    {
        return this.orderEntry;
    }


    public void setExpectedQuantity(Long expectedQuantity)
    {
        this.expectedQuantity = expectedQuantity;
    }


    public Long getExpectedQuantity()
    {
        return this.expectedQuantity;
    }


    public void setRefundAmount(PriceWsDTO refundAmount)
    {
        this.refundAmount = refundAmount;
    }


    public PriceWsDTO getRefundAmount()
    {
        return this.refundAmount;
    }
}
