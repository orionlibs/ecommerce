package de.hybris.platform.returns.dao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.util.List;

public interface ReturnRequestDao
{
    ReturnRequestModel createReturnRequest(OrderModel paramOrderModel);


    List<ReturnRequestModel> getReturnRequests(String paramString);
}
