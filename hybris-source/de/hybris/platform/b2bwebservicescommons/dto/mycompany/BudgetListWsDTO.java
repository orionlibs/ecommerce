package de.hybris.platform.b2bwebservicescommons.dto.mycompany;

import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.SortWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "BudgetList", description = "Representation of a Budget List")
public class BudgetListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "budgets", value = "List of Budgets")
    private List<BudgetWsDTO> budgets;
    @ApiModelProperty(name = "sorts", value = "List of sorts")
    private List<SortWsDTO> sorts;
    @ApiModelProperty(name = "pagination", value = "Pagination items")
    private PaginationWsDTO pagination;


    public void setBudgets(List<BudgetWsDTO> budgets)
    {
        this.budgets = budgets;
    }


    public List<BudgetWsDTO> getBudgets()
    {
        return this.budgets;
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
