package de.hybris.platform.warehousing.labels.strategy.impl;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.labels.strategy.PrintExportFormStrategy;
import de.hybris.platform.warehousing.sourcing.context.PosSelectionStrategy;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPrintExportFormStrategy implements PrintExportFormStrategy
{
    private PosSelectionStrategy posSelectionStrategy;


    public boolean canPrintExportForm(ConsignmentModel consignmentModel)
    {
        ServicesUtil.validateParameterNotNull(consignmentModel, "Consignment cannot be null");
        boolean canPrint = true;
        if(consignmentModel.getDeliveryPointOfService() != null)
        {
            canPrint = false;
        }
        else
        {
            PointOfServiceModel pointOfServiceModel = getPosSelectionStrategy().getPointOfService(consignmentModel.getOrder(), consignmentModel.getWarehouse());
            if(pointOfServiceModel != null)
            {
                canPrint = !consignmentModel.getShippingAddress().getCountry().getIsocode().equals(pointOfServiceModel.getAddress().getCountry().getIsocode());
            }
        }
        return canPrint;
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
