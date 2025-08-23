package de.hybris.platform.returns.strategy.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.strategy.ReturnableCheck;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;

public class DefaultConsignmentBasedReturnableCheck implements ReturnableCheck
{
    @Resource
    private ModelService modelService;


    public boolean perform(OrderModel order, AbstractOrderEntryModel orderentry, long returnQuantity)
    {
        if(returnQuantity < 1L || orderentry.getQuantity().longValue() < returnQuantity)
        {
            return false;
        }
        return (maxReturnQuantity(order, orderentry) >= returnQuantity);
    }


    public long maxReturnQuantity(OrderModel order, AbstractOrderEntryModel orderentry)
    {
        Set<ConsignmentModel> consignments = order.getConsignments();
        if(CollectionUtils.isEmpty(consignments))
        {
            return -1L;
        }
        Optional<ConsignmentEntryModel> matchingConsignmentEntry = consignments.stream().filter(c -> c.getStatus().getCode().equals(ConsignmentStatus.SHIPPED.getCode())).flatMap(c -> c.getConsignmentEntries().stream()).filter(ce -> ce.getOrderEntry().equals(orderentry)).findFirst();
        if(matchingConsignmentEntry.isPresent())
        {
            return ((ConsignmentEntryModel)matchingConsignmentEntry.get()).getShippedQuantity().longValue();
        }
        return -1L;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
