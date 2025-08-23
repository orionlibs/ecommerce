package de.hybris.platform.warehousing.sourcing.filter;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import java.util.Set;

public interface SourcingLocationFilter
{
    void filterLocations(AbstractOrderModel paramAbstractOrderModel, Set<WarehouseModel> paramSet);


    void setFilterResultOperator(SourcingFilterResultOperator paramSourcingFilterResultOperator);
}
