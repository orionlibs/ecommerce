package de.hybris.platform.commercewebservices.core.returns.data;

import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.ordermanagementfacades.returns.data.ReturnRequestData;
import java.io.Serializable;
import java.util.List;

public class ReturnRequestsData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ReturnRequestData> returnRequests;
    private List<SortData> sorts;
    private PaginationData pagination;


    public void setReturnRequests(List<ReturnRequestData> returnRequests)
    {
        this.returnRequests = returnRequests;
    }


    public List<ReturnRequestData> getReturnRequests()
    {
        return this.returnRequests;
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
