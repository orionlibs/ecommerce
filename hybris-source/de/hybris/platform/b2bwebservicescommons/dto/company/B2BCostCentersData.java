package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.b2bcommercefacades.company.data.B2BCostCenterData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import java.io.Serializable;
import java.util.List;

public class B2BCostCentersData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<B2BCostCenterData> costCenters;
    private List<SortData> sorts;
    private PaginationData pagination;


    public void setCostCenters(List<B2BCostCenterData> costCenters)
    {
        this.costCenters = costCenters;
    }


    public List<B2BCostCenterData> getCostCenters()
    {
        return this.costCenters;
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
