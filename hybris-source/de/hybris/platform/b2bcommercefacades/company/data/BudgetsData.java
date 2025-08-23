package de.hybris.platform.b2bcommercefacades.company.data;

import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import java.io.Serializable;
import java.util.List;

public class BudgetsData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<B2BBudgetData> budgets;
    private List<SortData> sorts;
    private PaginationData pagination;


    public void setBudgets(List<B2BBudgetData> budgets)
    {
        this.budgets = budgets;
    }


    public List<B2BBudgetData> getBudgets()
    {
        return this.budgets;
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
