package de.hybris.platform.warehousing.allocation.impl;

import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.warehousing.consignmententry.service.ConsignmentEntryQuantityService;
import org.springframework.beans.factory.annotation.Required;

public class ConsignmentEntryPendingQuantityHandler implements DynamicAttributeHandler<Long, ConsignmentEntryModel>
{
    private ConsignmentEntryQuantityService consignmentEntryQuantityService;


    public Long get(ConsignmentEntryModel consignmentEntry)
    {
        return getConsignmentEntryQuantityService().getQuantityPending(consignmentEntry);
    }


    public void set(ConsignmentEntryModel consignmentEntry, Long value)
    {
        throw new UnsupportedOperationException();
    }


    protected ConsignmentEntryQuantityService getConsignmentEntryQuantityService()
    {
        return this.consignmentEntryQuantityService;
    }


    @Required
    public void setConsignmentEntryQuantityService(ConsignmentEntryQuantityService consignmentEntryQuantityService)
    {
        this.consignmentEntryQuantityService = consignmentEntryQuantityService;
    }
}
