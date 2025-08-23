package de.hybris.platform.returns.dao;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.OrderEntryReturnRecordEntryModel;
import de.hybris.platform.returns.model.OrderReturnRecordEntryModel;
import de.hybris.platform.returns.model.OrderReturnRecordModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;

public interface OrderReturnDao extends Dao
{
    OrderReturnRecordModel getOrderReturnRecord(OrderModel paramOrderModel);


    Collection<OrderReturnRecordEntryModel> getOrderReturnRecordEntries(OrderModel paramOrderModel);


    OrderEntryReturnRecordEntryModel getOrderEntryReturnRecord(OrderEntryModel paramOrderEntryModel, OrderReturnRecordEntryModel paramOrderReturnRecordEntryModel);
}
