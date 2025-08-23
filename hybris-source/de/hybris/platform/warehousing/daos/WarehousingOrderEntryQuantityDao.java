package de.hybris.platform.warehousing.daos;

import de.hybris.platform.core.model.order.OrderEntryModel;
import java.util.Map;

public interface WarehousingOrderEntryQuantityDao
{
    Long getCancelledQuantity(OrderEntryModel paramOrderEntryModel);


    Long getQuantityReturned(OrderEntryModel paramOrderEntryModel);


    Long processRequestWithParams(String paramString, Map<String, Object> paramMap);
}
