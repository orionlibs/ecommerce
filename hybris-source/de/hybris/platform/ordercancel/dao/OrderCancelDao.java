package de.hybris.platform.ordercancel.dao;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;
import de.hybris.platform.ordercancel.model.OrderEntryCancelRecordEntryModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.Collection;

public interface OrderCancelDao extends Dao
{
    OrderCancelRecordModel getOrderCancelRecord(OrderModel paramOrderModel);


    Collection<OrderCancelRecordEntryModel> getOrderCancelRecordEntries(OrderModel paramOrderModel);


    Collection<OrderCancelRecordEntryModel> getOrderCancelRecordEntries(EmployeeModel paramEmployeeModel);


    OrderCancelConfigModel getOrderCancelConfiguration();


    OrderEntryCancelRecordEntryModel getOrderEntryCancelRecord(OrderEntryModel paramOrderEntryModel, OrderCancelRecordEntryModel paramOrderCancelRecordEntryModel);
}
