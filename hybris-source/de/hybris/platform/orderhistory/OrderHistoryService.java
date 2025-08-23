package de.hybris.platform.orderhistory;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import java.util.Collection;
import java.util.Date;

public interface OrderHistoryService
{
    OrderModel createHistorySnapshot(OrderModel paramOrderModel);


    void saveHistorySnapshot(OrderModel paramOrderModel);


    Collection<OrderModel> getHistorySnapshots(OrderModel paramOrderModel);


    Collection<OrderHistoryEntryModel> getHistoryEntries(OrderModel paramOrderModel, Date paramDate1, Date paramDate2);


    Collection<String> getHistoryEntriesDescriptions(OrderModel paramOrderModel, Date paramDate1, Date paramDate2);


    Collection<OrderHistoryEntryModel> getHistoryEntries(OrderModel paramOrderModel, EmployeeModel paramEmployeeModel);


    Collection<OrderHistoryEntryModel> getHistoryEntries(UserModel paramUserModel, Date paramDate1, Date paramDate2);
}
