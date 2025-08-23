package de.hybris.platform.warehousing.orderentry.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.warehousing.consignmententry.service.ConsignmentEntryQuantityService;
import de.hybris.platform.warehousing.daos.WarehousingOrderEntryQuantityDao;
import de.hybris.platform.warehousing.orderentry.service.OrderEntryQuantityService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrderEntryQuantityService implements OrderEntryQuantityService
{
    private WarehousingOrderEntryQuantityDao warehousingOrderEntryQuantityDao;
    private ConsignmentEntryQuantityService consignmentEntryQuantityService;


    public Long getQuantityShipped(OrderEntryModel orderEntryModel)
    {
        long shippedquantity = 0L;
        if(orderEntryModel.getConsignmentEntries() != null)
        {
            shippedquantity = orderEntryModel.getConsignmentEntries().stream().mapToLong(consEntry -> consEntry.getQuantityShipped().longValue()).sum();
        }
        return Long.valueOf(shippedquantity);
    }


    public Long getQuantityCancelled(OrderEntryModel orderEntryModel)
    {
        return getWarehousingOrderEntryQuantityDao().getCancelledQuantity(orderEntryModel);
    }


    public Long getQuantityAllocated(OrderEntryModel orderEntryModel)
    {
        if(orderEntryModel.getConsignmentEntries() == null)
        {
            return Long.valueOf(0L);
        }
        return Long.valueOf(orderEntryModel.getConsignmentEntries().stream()
                        .filter(consignmentEntry -> !consignmentEntry.getConsignment().getStatus().equals(ConsignmentStatus.CANCELLED))
                        .mapToLong(consEntry -> consEntry.getQuantity().longValue()).sum());
    }


    public Long getQuantityUnallocated(OrderEntryModel orderEntryModel)
    {
        long quantityUnallocated = orderEntryModel.getQuantity().longValue() - getQuantityAllocated(orderEntryModel).longValue();
        return Long.valueOf((quantityUnallocated >= 0L) ? quantityUnallocated : 0L);
    }


    public Long getQuantityPending(OrderEntryModel orderEntryModel)
    {
        return Long.valueOf(orderEntryModel.getQuantity().longValue() - getQuantityShipped(orderEntryModel).longValue());
    }


    public Long getQuantityReturned(OrderEntryModel orderEntryModel)
    {
        return getWarehousingOrderEntryQuantityDao().getQuantityReturned(orderEntryModel);
    }


    public Long getQuantityDeclined(OrderEntryModel orderEntryModel)
    {
        long declinedQuantity = 0L;
        if(orderEntryModel.getConsignmentEntries() != null)
        {
            declinedQuantity = orderEntryModel.getConsignmentEntries().stream().mapToLong(entry -> getConsignmentEntryQuantityService().getQuantityDeclined(entry).longValue()).sum();
        }
        return Long.valueOf(declinedQuantity);
    }


    protected WarehousingOrderEntryQuantityDao getWarehousingOrderEntryQuantityDao()
    {
        return this.warehousingOrderEntryQuantityDao;
    }


    @Required
    public void setWarehousingOrderEntryQuantityDao(WarehousingOrderEntryQuantityDao warehousingOrderEntryQuantityDao)
    {
        this.warehousingOrderEntryQuantityDao = warehousingOrderEntryQuantityDao;
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
