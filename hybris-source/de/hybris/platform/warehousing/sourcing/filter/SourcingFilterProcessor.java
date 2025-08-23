package de.hybris.platform.warehousing.sourcing.filter;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class SourcingFilterProcessor implements InitializingBean
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SourcingFilterProcessor.class);
    private Collection<SourcingLocationFilter> filters;


    public void filterLocations(AbstractOrderModel order, Set<WarehouseModel> locations) throws IllegalArgumentException, IllegalStateException
    {
        if(order == null || locations == null)
        {
            throw new IllegalArgumentException("Parameters order and locations cannot be null");
        }
        if(getFilters() == null || getFilters().isEmpty())
        {
            throw new IllegalStateException("At least one sourcing filter must be specified");
        }
        LOGGER.debug("Start filtering locations");
        for(SourcingLocationFilter filter : getFilters())
        {
            filter.filterLocations(order, locations);
        }
    }


    public void afterPropertiesSet() throws Exception
    {
        if(CollectionUtils.isEmpty(getFilters()))
        {
            throw new IllegalArgumentException("Filters collection cannot be empty.");
        }
    }


    protected Collection<SourcingLocationFilter> getFilters()
    {
        return this.filters;
    }


    public void setFilters(Collection<SourcingLocationFilter> filters)
    {
        this.filters = filters;
    }
}
