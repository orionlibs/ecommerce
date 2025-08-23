package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrgUnitUserGroupList", description = "Representation of an user list used in organizational units")
public class OrgUnitUserGroupListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "orgUnitUserGroups", value = "List of organizational unit user groups", required = true)
    private List<OrgUnitUserGroupWsDTO> orgUnitUserGroups;
    @ApiModelProperty(name = "sorts", value = "List of sorts", required = true)
    private List<SortWsDTO> sorts;
    @ApiModelProperty(name = "pagination", value = "Pagination items", required = true)
    private PaginationWsDTO pagination;


    public void setOrgUnitUserGroups(List<OrgUnitUserGroupWsDTO> orgUnitUserGroups)
    {
        this.orgUnitUserGroups = orgUnitUserGroups;
    }


    public List<OrgUnitUserGroupWsDTO> getOrgUnitUserGroups()
    {
        return this.orgUnitUserGroups;
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
