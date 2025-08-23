package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;

@ApiModel(value = "OrderEntryGroup", description = "Representation of an Order Entry Group")
public class OrderEntryGroupWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "totalPriceWithTax", value = "Total price with tax")
    private PriceWsDTO totalPriceWithTax;
    @ApiModelProperty(name = "entries", value = "List of order entries")
    private Collection<OrderEntryWsDTO> entries;
    @ApiModelProperty(name = "quantity", value = "Quantity of order entries in a group")
    private Long quantity;


    public void setTotalPriceWithTax(PriceWsDTO totalPriceWithTax)
    {
        this.totalPriceWithTax = totalPriceWithTax;
    }


    public PriceWsDTO getTotalPriceWithTax()
    {
        return this.totalPriceWithTax;
    }


    public void setEntries(Collection<OrderEntryWsDTO> entries)
    {
        this.entries = entries;
    }


    public Collection<OrderEntryWsDTO> getEntries()
    {
        return this.entries;
    }


    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }


    public Long getQuantity()
    {
        return this.quantity;
    }
}
