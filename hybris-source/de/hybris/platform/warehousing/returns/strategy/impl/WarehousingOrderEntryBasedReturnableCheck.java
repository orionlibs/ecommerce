package de.hybris.platform.warehousing.returns.strategy.impl;

import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.strategy.ReturnableCheck;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class WarehousingOrderEntryBasedReturnableCheck implements ReturnableCheck
{
    private ReturnService returnService;
    private ModelService modelService;
    private Set<ReturnStatus> invalidReturnStatusForIncompleteReturns;


    public boolean perform(OrderModel order, AbstractOrderEntryModel orderEntry, long returnQuantity)
    {
        if(getModelService().isNew(orderEntry) || returnQuantity < 1L || !order.getEntries().contains(orderEntry))
        {
            return false;
        }
        List<ReturnEntryModel> returnEntries = getReturnService().getReturnEntry(orderEntry);
        long incompleteReturns = returnEntries.stream().filter(entry -> !getInvalidReturnStatusForIncompleteReturns().contains(entry.getStatus())).mapToLong(entry -> (entry.getExpectedQuantity() != null) ? entry.getExpectedQuantity().longValue() : 0L).sum();
        long completeReturns = ((OrderEntryModel)orderEntry).getQuantityReturned().longValue();
        long quantityAvailable = ((OrderEntryModel)orderEntry).getQuantityShipped().longValue() - incompleteReturns - completeReturns;
        return (quantityAvailable >= returnQuantity);
    }


    protected ReturnService getReturnService()
    {
        return this.returnService;
    }


    @Required
    public void setReturnService(ReturnService returnService)
    {
        this.returnService = returnService;
    }


    protected Set<ReturnStatus> getInvalidReturnStatusForIncompleteReturns()
    {
        return this.invalidReturnStatusForIncompleteReturns;
    }


    @Required
    public void setInvalidReturnStatusForIncompleteReturns(Set<ReturnStatus> invalidReturnStatusForIncompleteReturns)
    {
        this.invalidReturnStatusForIncompleteReturns = invalidReturnStatusForIncompleteReturns;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
