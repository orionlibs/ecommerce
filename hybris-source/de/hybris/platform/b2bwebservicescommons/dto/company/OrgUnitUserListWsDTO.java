package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrgUnitUserList", description = "Representation of an user list used in organizational units")
public class OrgUnitUserListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "users", value = "List of users", required = true)
    private List<UserWsDTO> users;
    @ApiModelProperty(name = "sorts", value = "List of sorts", required = true)
    private List<SortWsDTO> sorts;
    @ApiModelProperty(name = "pagination", value = "Pagination items", required = true)
    private PaginationWsDTO pagination;


    public void setUsers(List<UserWsDTO> users)
    {
        this.users = users;
    }


    public List<UserWsDTO> getUsers()
    {
        return this.users;
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
