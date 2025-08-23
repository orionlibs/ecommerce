package de.hybris.platform.warehousing.consignmententry.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.warehousing.consignmententry.service.ConsignmentEntryQuantityService;
import de.hybris.platform.warehousing.daos.WarehousingConsignmentEntryQuantityDao;
import org.springframework.beans.factory.annotation.Required;

public class DefaultConsignmentEntryQuantityService implements ConsignmentEntryQuantityService
{
    private WarehousingConsignmentEntryQuantityDao warehousingConsignmentEntryQuantityDao;


    public Long getQuantityShipped(ConsignmentEntryModel consignmentEntryModel)
    {
        return getWarehousingConsignmentEntryQuantityDao().getQuantityShipped(consignmentEntryModel);
    }


    public Long getQuantityPending(ConsignmentEntryModel consignmentEntryModel)
    {
        Long pendingQty = Long.valueOf(0L);
        if(!consignmentEntryModel.getConsignment().getStatus().equals(ConsignmentStatus.CANCELLED))
        {
            pendingQty = Long.valueOf(consignmentEntryModel.getQuantity().longValue() - getQuantityShipped(consignmentEntryModel).longValue());
        }
        return pendingQty;
    }


    public Long getQuantityDeclined(ConsignmentEntryModel consignmentEntry)
    {
        Long result = Long.valueOf(0L);
        if(!consignmentEntry.getConsignment().getStatus().equals(ConsignmentStatus.CANCELLED))
        {
            result = getWarehousingConsignmentEntryQuantityDao().getQuantityDeclined(consignmentEntry);
        }
        return result;
    }


    protected WarehousingConsignmentEntryQuantityDao getWarehousingConsignmentEntryQuantityDao()
    {
        return this.warehousingConsignmentEntryQuantityDao;
    }


    @Required
    public void setWarehousingConsignmentEntryQuantityDao(WarehousingConsignmentEntryQuantityDao warehousingConsignmentEntryQuantityDao)
    {
        this.warehousingConsignmentEntryQuantityDao = warehousingConsignmentEntryQuantityDao;
    }
}
