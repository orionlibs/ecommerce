package de.hybris.platform.warehousing.returns.strategy.impl;

import com.google.common.collect.Sets;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.returns.strategy.RestockWarehouseSelectionStrategy;
import de.hybris.platform.warehousing.sourcing.filter.SourcingFilterProcessor;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRestockWarehouseSelectionStrategy implements RestockWarehouseSelectionStrategy
{
    private SourcingFilterProcessor restockFilterProcessor;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRestockWarehouseSelectionStrategy.class);


    public WarehouseModel performStrategy(ReturnRequestModel returnRequestModel)
    {
        ServicesUtil.validateParameterNotNull(returnRequestModel, "Parameter returnRequestModel cannot be null.");
        ServicesUtil.validateParameterNotNull(returnRequestModel.getReturnEntries(), "Parameter returnEntries cannot be null.");
        Optional<ConsignmentEntryModel> consignmentEntryModel = returnRequestModel.getReturnEntries().stream().flatMap(returnEntry -> returnEntry.getOrderEntry().getConsignmentEntries().stream())
                        .filter(consignmentEntry -> Boolean.TRUE.equals(consignmentEntry.getConsignment().getWarehouse().getIsAllowRestock())).findFirst();
        if(consignmentEntryModel.isPresent())
        {
            return ((ConsignmentEntryModel)consignmentEntryModel.get()).getConsignment().getWarehouse();
        }
        Set<WarehouseModel> locations = Sets.newHashSet();
        getRestockFilterProcessor().filterLocations((AbstractOrderModel)returnRequestModel.getOrder(), locations);
        if(CollectionUtils.isNotEmpty(locations))
        {
            Optional<WarehouseModel> location = locations.stream().findFirst();
            if(location.isPresent())
            {
                return location.get();
            }
        }
        LOG.info("cannot find any warehouse for restock");
        return null;
    }


    @Required
    public void setRestockFilterProcessor(SourcingFilterProcessor restockFilterProcessor)
    {
        this.restockFilterProcessor = restockFilterProcessor;
    }


    protected SourcingFilterProcessor getRestockFilterProcessor()
    {
        return this.restockFilterProcessor;
    }
}
