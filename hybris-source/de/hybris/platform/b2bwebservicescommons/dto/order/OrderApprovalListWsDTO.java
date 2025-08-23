package de.hybris.platform.b2bwebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrderApprovalList", description = "Representation of a Order Approval List")
public class OrderApprovalListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orderApprovals", value = "List of Order Approvals", required = true)
    private List<OrderApprovalWsDTO> orderApprovals;
    @ApiModelProperty(name = "sorts", value = "List of sorts", required = true)
    private List<SortWsDTO> sorts;
    @ApiModelProperty(name = "pagination", value = "Pagination items", required = true)
    private PaginationWsDTO pagination;


    public void setOrderApprovals(List<OrderApprovalWsDTO> orderApprovals)
    {
        this.orderApprovals = orderApprovals;
    }


    public List<OrderApprovalWsDTO> getOrderApprovals()
    {
        return this.orderApprovals;
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
