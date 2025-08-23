package de.hybris.platform.b2bwebservicescommons.dto.order;

import de.hybris.platform.b2bacceleratorfacades.order.data.ScheduledCartData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import java.io.Serializable;
import java.util.List;

public class ReplenishmentOrderData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<ScheduledCartData> replenishmentOrders;
    private List<SortData> sorts;
    private PaginationData pagination;


    public void setReplenishmentOrders(List<ScheduledCartData> replenishmentOrders)
    {
        this.replenishmentOrders = replenishmentOrders;
    }


    public List<ScheduledCartData> getReplenishmentOrders()
    {
        return this.replenishmentOrders;
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
