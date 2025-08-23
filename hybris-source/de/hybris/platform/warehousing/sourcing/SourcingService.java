package de.hybris.platform.warehousing.sourcing;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;

public interface SourcingService
{
    SourcingResults sourceOrder(AbstractOrderModel paramAbstractOrderModel);
}
