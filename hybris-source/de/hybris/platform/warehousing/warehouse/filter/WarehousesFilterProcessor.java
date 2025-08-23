package de.hybris.platform.warehousing.warehouse.filter;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class WarehousesFilterProcessor
{
    private Collection<WarehousesFilter> filters;


    public Set<WarehouseModel> filterLocations(Set<WarehouseModel> warehouses)
    {
        Set<WarehouseModel> finalWarehouses = warehouses;
        if(CollectionUtils.isNotEmpty(finalWarehouses) && CollectionUtils.isNotEmpty(getFilters()))
        {
            for(WarehousesFilter warehousesFilter : getFilters())
            {
                finalWarehouses = warehousesFilter.applyFilter(finalWarehouses);
            }
        }
        return finalWarehouses;
    }


    protected Collection<WarehousesFilter> getFilters()
    {
        return this.filters;
    }


    @Required
    public void setFilters(Collection<WarehousesFilter> filters)
    {
        this.filters = filters;
    }
}
