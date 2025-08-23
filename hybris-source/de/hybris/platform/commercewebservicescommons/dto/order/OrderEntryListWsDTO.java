package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrderEntryList", description = "Representation of an Order entry list consumed")
public class OrderEntryListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orderEntries", value = "List of order entries")
    private List<OrderEntryWsDTO> orderEntries;


    public void setOrderEntries(List<OrderEntryWsDTO> orderEntries)
    {
        this.orderEntries = orderEntries;
    }


    public List<OrderEntryWsDTO> getOrderEntries()
    {
        return this.orderEntries;
    }
}
