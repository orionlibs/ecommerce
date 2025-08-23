package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import java.io.Serializable;
import java.util.List;

public class OrderHistoriesData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<OrderHistoryData> orders;
    private List<SortData> sorts;
    private PaginationData pagination;


    public void setOrders(List<OrderHistoryData> orders)
    {
        this.orders = orders;
    }


    public List<OrderHistoryData> getOrders()
    {
        return this.orders;
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
