package de.hybris.platform.b2bwebservicescommons.dto.company;

import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "B2BCostCenterList", description = "Representation of a cost center list")
public class B2BCostCenterListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "costCenters", value = "List of cost centers")
    private List<B2BCostCenterWsDTO> costCenters;
    @ApiModelProperty(name = "sorts", value = "List of sorts")
    private List<SortWsDTO> sorts;
    @ApiModelProperty(name = "pagination", value = "Pagination items")
    private PaginationWsDTO pagination;


    public void setCostCenters(List<B2BCostCenterWsDTO> costCenters)
    {
        this.costCenters = costCenters;
    }


    public List<B2BCostCenterWsDTO> getCostCenters()
    {
        return this.costCenters;
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
