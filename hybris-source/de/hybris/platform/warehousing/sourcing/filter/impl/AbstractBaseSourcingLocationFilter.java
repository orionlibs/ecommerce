package de.hybris.platform.warehousing.sourcing.filter.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.sourcing.filter.SourcingFilterResultOperator;
import de.hybris.platform.warehousing.sourcing.filter.SourcingLocationFilter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractBaseSourcingLocationFilter implements SourcingLocationFilter
{
    protected SourcingFilterResultOperator filterResultOperator;


    public void filterLocations(AbstractOrderModel order, Set<WarehouseModel> locations)
    {
        if(order == null || locations == null)
        {
            throw new IllegalArgumentException("Parameters order and locations cannot be null");
        }
        if(this.filterResultOperator == null)
        {
            throw new IllegalArgumentException("Parameter filterResultOperator cannot be null");
        }
        Collection<WarehouseModel> filteredResults = applyFilter(order, locations);
        combineFilteredLocations(filteredResults, locations);
    }


    protected void combineFilteredLocations(Collection<WarehouseModel> filteredResults, Set<WarehouseModel> locations)
    {
        if(filteredResults != null)
        {
            if(this.filterResultOperator == SourcingFilterResultOperator.AND)
            {
                List<WarehouseModel> tmpLocations = (List<WarehouseModel>)locations.stream().filter(warehouse -> filteredResults.contains(warehouse)).collect(Collectors.toList());
                locations.clear();
                locations.addAll(tmpLocations);
            }
            else if(this.filterResultOperator == SourcingFilterResultOperator.NOT)
            {
                locations.removeAll(filteredResults);
            }
            else
            {
                locations.addAll(filteredResults);
            }
        }
    }


    protected SourcingFilterResultOperator getFilterResultOperator()
    {
        return this.filterResultOperator;
    }


    @Required
    public void setFilterResultOperator(SourcingFilterResultOperator operator)
    {
        this.filterResultOperator = operator;
    }


    public abstract Collection<WarehouseModel> applyFilter(AbstractOrderModel paramAbstractOrderModel, Set<WarehouseModel> paramSet);
}
