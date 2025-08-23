package de.hybris.platform.ordersplitting;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import java.util.List;

public interface ConsignmentService
{
    ConsignmentModel createConsignment(AbstractOrderModel paramAbstractOrderModel, String paramString, List<AbstractOrderEntryModel> paramList) throws ConsignmentCreationException;


    WarehouseModel getWarehouse(List<AbstractOrderEntryModel> paramList);
}
