package de.hybris.platform.b2bwebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "ReplenishmentOrderList", description = "Representation of a Replenishment Order List")
public class ReplenishmentOrderListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "replenishmentOrders", value = "List of Replenishment Orders")
    private List<ReplenishmentOrderWsDTO> replenishmentOrders;
    @ApiModelProperty(name = "sorts", value = "List of sorts")
    private List<SortWsDTO> sorts;
    @ApiModelProperty(name = "pagination", value = "Pagination items")
    private PaginationWsDTO pagination;


    public void setReplenishmentOrders(List<ReplenishmentOrderWsDTO> replenishmentOrders)
    {
        this.replenishmentOrders = replenishmentOrders;
    }


    public List<ReplenishmentOrderWsDTO> getReplenishmentOrders()
    {
        return this.replenishmentOrders;
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
