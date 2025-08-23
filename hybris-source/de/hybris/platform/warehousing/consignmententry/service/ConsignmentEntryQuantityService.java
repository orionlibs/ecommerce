package de.hybris.platform.warehousing.consignmententry.service;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

public interface ConsignmentEntryQuantityService
{
    Long getQuantityShipped(ConsignmentEntryModel paramConsignmentEntryModel);


    Long getQuantityPending(ConsignmentEntryModel paramConsignmentEntryModel);


    Long getQuantityDeclined(ConsignmentEntryModel paramConsignmentEntryModel);
}
