package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import java.io.Serializable;
import java.util.List;

public class OrgUnitUsersData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CustomerData> users;
    private List<SortData> sorts;
    private PaginationData pagination;


    public void setUsers(List<CustomerData> users)
    {
        this.users = users;
    }


    public List<CustomerData> getUsers()
    {
        return this.users;
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
