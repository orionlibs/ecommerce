package com.hybris.backoffice.excel.template.filter;

import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class AndFilter<T> implements ExcelFilter<T>
{
    private List<ExcelFilter<T>> filters;


    public boolean test(T t)
    {
        return this.filters.stream().allMatch(filter -> filter.test(t));
    }


    @Required
    public void setFilters(List<ExcelFilter<T>> filters)
    {
        this.filters = filters;
    }


    public List<ExcelFilter<T>> getFilters()
    {
        return this.filters;
    }
}
