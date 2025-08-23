package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "UserGroupList", description = "Representation of an User Group List")
public class UserGroupListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "userGroups", value = "List of user groups")
    private List<UserGroupWsDTO> userGroups;
    @ApiModelProperty(name = "totalNumber", value = "Total number")
    private Integer totalNumber;
    @ApiModelProperty(name = "pageSize", value = "Page size")
    private Integer pageSize;
    @ApiModelProperty(name = "numberOfPages", value = "Number of pages")
    private Integer numberOfPages;
    @ApiModelProperty(name = "currentPage", value = "Current page")
    private Integer currentPage;


    public void setUserGroups(List<UserGroupWsDTO> userGroups)
    {
        this.userGroups = userGroups;
    }


    public List<UserGroupWsDTO> getUserGroups()
    {
        return this.userGroups;
    }


    public void setTotalNumber(Integer totalNumber)
    {
        this.totalNumber = totalNumber;
    }


    public Integer getTotalNumber()
    {
        return this.totalNumber;
    }


    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }


    public Integer getPageSize()
    {
        return this.pageSize;
    }


    public void setNumberOfPages(Integer numberOfPages)
    {
        this.numberOfPages = numberOfPages;
    }


    public Integer getNumberOfPages()
    {
        return this.numberOfPages;
    }


    public void setCurrentPage(Integer currentPage)
    {
        this.currentPage = currentPage;
    }


    public Integer getCurrentPage()
    {
        return this.currentPage;
    }
}
