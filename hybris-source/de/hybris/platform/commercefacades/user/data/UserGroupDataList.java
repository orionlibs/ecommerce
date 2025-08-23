package de.hybris.platform.commercefacades.user.data;

import java.io.Serializable;
import java.util.List;

public class UserGroupDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<UserGroupData> userGroups;
    private Integer totalNumber;
    private Integer pageSize;
    private Integer numberOfPages;
    private Integer currentPage;


    public void setUserGroups(List<UserGroupData> userGroups)
    {
        this.userGroups = userGroups;
    }


    public List<UserGroupData> getUserGroups()
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
