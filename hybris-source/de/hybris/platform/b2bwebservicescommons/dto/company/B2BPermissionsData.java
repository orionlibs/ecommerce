package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import java.io.Serializable;
import java.util.List;

public class B2BPermissionsData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<B2BPermissionData> orderApprovalPermissions;
    private List<SortData> sorts;
    private PaginationData pagination;


    public void setOrderApprovalPermissions(List<B2BPermissionData> orderApprovalPermissions)
    {
        this.orderApprovalPermissions = orderApprovalPermissions;
    }


    public List<B2BPermissionData> getOrderApprovalPermissions()
    {
        return this.orderApprovalPermissions;
    }


    public void setSorts(List<SortData> sorts)
    {
        this.sorts = sorts;
    }


    public List<SortData> getSorts()
    {
        return this.sorts;
    }


    public void setPagination(PaginationData pagination)
    {
        this.pagination = pagination;
    }


    public PaginationData getPagination()
    {
        return this.pagination;
    }
}
