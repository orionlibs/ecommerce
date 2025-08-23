package de.hybris.platform.warehousing.daos;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import java.util.Map;

public interface WarehousingConsignmentEntryQuantityDao
{
    Long getQuantityShipped(ConsignmentEntryModel paramConsignmentEntryModel);


    Long getQuantityDeclined(ConsignmentEntryModel paramConsignmentEntryModel);


    Long processRequestWithParams(String paramString, Map<String, Object> paramMap);
}
