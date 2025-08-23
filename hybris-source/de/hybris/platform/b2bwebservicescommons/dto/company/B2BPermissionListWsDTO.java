package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrderApprovalPermissionList", description = "Representation of a Order Approval Permission List")
public class B2BPermissionListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orderApprovalPermissions", value = "List of Order Approval Permissions", required = true)
    private List<B2BPermissionWsDTO> orderApprovalPermissions;
    @ApiModelProperty(name = "sorts", value = "List of sorts", required = true)
    private List<SortWsDTO> sorts;
    @ApiModelProperty(name = "pagination", value = "Pagination items", required = true)
    private PaginationWsDTO pagination;


    public void setOrderApprovalPermissions(List<B2BPermissionWsDTO> orderApprovalPermissions)
    {
        this.orderApprovalPermissions = orderApprovalPermissions;
    }


    public List<B2BPermissionWsDTO> getOrderApprovalPermissions()
    {
        return this.orderApprovalPermissions;
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
