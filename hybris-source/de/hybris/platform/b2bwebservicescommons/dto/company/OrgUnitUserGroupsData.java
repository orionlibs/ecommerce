package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import java.io.Serializable;
import java.util.List;

public class OrgUnitUserGroupsData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<B2BUserGroupData> orgUnitUserGroups;
    private List<SortData> sorts;
    private PaginationData pagination;


    public void setOrgUnitUserGroups(List<B2BUserGroupData> orgUnitUserGroups)
    {
        this.orgUnitUserGroups = orgUnitUserGroups;
    }


    public List<B2BUserGroupData> getOrgUnitUserGroups()
    {
        return this.orgUnitUserGroups;
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
