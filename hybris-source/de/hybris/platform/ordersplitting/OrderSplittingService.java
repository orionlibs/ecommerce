package de.hybris.platform.ordersplitting;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import java.util.List;

public interface OrderSplittingService
{
    List<ConsignmentModel> splitOrderForConsignment(AbstractOrderModel paramAbstractOrderModel, List<AbstractOrderEntryModel> paramList) throws ConsignmentCreationException;


    List<ConsignmentModel> splitOrderForConsignmentNotPersist(AbstractOrderModel paramAbstractOrderModel, List<AbstractOrderEntryModel> paramList) throws ConsignmentCreationException;
}
