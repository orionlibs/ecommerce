package de.hybris.platform.ordercancel;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import java.util.Map;

public interface OrderCancelCancelableEntriesStrategy
{
    Map<AbstractOrderEntryModel, Long> getAllCancelableEntries(OrderModel paramOrderModel, PrincipalModel paramPrincipalModel);
}
