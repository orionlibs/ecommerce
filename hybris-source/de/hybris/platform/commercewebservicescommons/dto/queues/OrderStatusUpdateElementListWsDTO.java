package de.hybris.platform.commercewebservicescommons.dto.queues;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrderStatusUpdateElementList", description = "Representation of an Order Status Update Element List")
public class OrderStatusUpdateElementListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orderStatusUpdateElements", value = "List of order status update elements")
    private List<OrderStatusUpdateElementWsDTO> orderStatusUpdateElements;


    public void setOrderStatusUpdateElements(List<OrderStatusUpdateElementWsDTO> orderStatusUpdateElements)
    {
        this.orderStatusUpdateElements = orderStatusUpdateElements;
    }


    public List<OrderStatusUpdateElementWsDTO> getOrderStatusUpdateElements()
    {
        return this.orderStatusUpdateElements;
    }
}
