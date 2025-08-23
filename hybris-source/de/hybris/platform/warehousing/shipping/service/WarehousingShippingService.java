package de.hybris.platform.warehousing.shipping.service;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.warehousing.process.BusinessProcessException;

public interface WarehousingShippingService
{
    boolean isConsignmentConfirmable(ConsignmentModel paramConsignmentModel);


    void confirmShipConsignment(ConsignmentModel paramConsignmentModel) throws BusinessProcessException;


    void confirmPickupConsignment(ConsignmentModel paramConsignmentModel) throws BusinessProcessException;
}
