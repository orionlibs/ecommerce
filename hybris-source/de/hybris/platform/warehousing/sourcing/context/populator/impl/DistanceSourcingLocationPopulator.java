package de.hybris.platform.warehousing.sourcing.context.populator.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.context.PosSelectionStrategy;
import de.hybris.platform.warehousing.sourcing.context.populator.SourcingLocationPopulator;
import de.hybris.platform.warehousing.sourcing.context.util.HaversineCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DistanceSourcingLocationPopulator implements SourcingLocationPopulator
{
    private static Logger LOG = LoggerFactory.getLogger(DistanceSourcingLocationPopulator.class);
    private PosSelectionStrategy posSelectionStrategy;


    public void populate(WarehouseModel source, SourcingLocation target)
    {
        AbstractOrderEntryModel orderEntry = target.getContext().getOrderEntries().iterator().next();
        AddressModel deliveryAddress = orderEntry.getOrder().getDeliveryAddress();
        if(deliveryAddress == null)
        {
            LOG.info("Order did not have a delivery address; setting target distance to null.");
            target.setDistance(null);
            return;
        }
        PointOfServiceModel pointOfService = getPosSelectionStrategy().getPointOfService(orderEntry.getOrder(), source);
        if(pointOfService == null)
        {
            LOG.info("Warehouse did not have a valid point of service; setting target distance to null.");
            target.setDistance(null);
            return;
        }
        if(pointOfService.getAddress() == null)
        {
            LOG.info("Point of service did not have an address defined; setting target distance to null.");
            target.setDistance(null);
            return;
        }
        Double orderLat = deliveryAddress.getLatitude();
        Double orderLon = deliveryAddress.getLongitude();
        Double posLat = pointOfService.getLatitude();
        Double posLon = pointOfService.getLongitude();
        if(orderLat == null || orderLon == null)
        {
            LOG.info("Order did not have lat/long value; setting target distance to null.");
            target.setDistance(null);
        }
        else if(posLat == null || posLon == null)
        {
            LOG.info("PointOfServiceModel with [name: {}] did not have lat/long value; setting target distance to null.", source
                            .getCode());
            target.setDistance(null);
        }
        else
        {
            target.setDistance(Double.valueOf(HaversineCalculator.calculate(posLat.doubleValue(), posLon.doubleValue(), orderLat.doubleValue(), orderLon.doubleValue())));
        }
    }


    protected PosSelectionStrategy getPosSelectionStrategy()
    {
        return this.posSelectionStrategy;
    }


    @Required
    public void setPosSelectionStrategy(PosSelectionStrategy posSelectionStrategy)
    {
        this.posSelectionStrategy = posSelectionStrategy;
    }
}
