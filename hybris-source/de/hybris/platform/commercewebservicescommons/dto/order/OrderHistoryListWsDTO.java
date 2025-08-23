package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrderHistoryList", description = "Representation of an Order History List")
public class OrderHistoryListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orders", value = "List of order history items")
    private List<OrderHistoryWsDTO> orders;
    @ApiModelProperty(name = "sorts", value = "List of sorts")
    private List<SortWsDTO> sorts;
    @ApiModelProperty(name = "pagination", value = "Pagination items")
    private PaginationWsDTO pagination;


    public void setOrders(List<OrderHistoryWsDTO> orders)
    {
        this.orders = orders;
    }


    public List<OrderHistoryWsDTO> getOrders()
    {
        return this.orders;
    }


    public void setSorts(List<SortWsDTO> sorts)
    {
        this.sorts = sorts;
    }


    public List<SortWsDTO> getSorts()
    {
        return this.sorts;
    }


    public void setPagination(PaginationWsDTO pagination)
    {
        this.pagination = pagination;
    }


    public PaginationWsDTO getPagination()
    {
        return this.pagination;
    }
}
