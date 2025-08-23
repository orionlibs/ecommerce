package de.hybris.platform.warehousing.process.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.warehousing.process.AbstractWarehousingBusinessProcessService;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrderProcessService extends AbstractWarehousingBusinessProcessService<OrderModel>
{
    private transient BaseStoreService baseStoreService;


    public String getProcessCode(OrderModel order)
    {
        if(CollectionUtils.isEmpty(order.getOrderProcess()))
        {
            throw new BusinessProcessException("Unable to process event for order [" + order.getCode() + "]. No processes associated to the order.");
        }
        BaseStoreModel store = (BaseStoreModel)Optional.<BaseStoreModel>ofNullable(Optional.<BaseStoreModel>ofNullable(order.getStore()).orElseGet(() -> getBaseStoreService().getCurrentBaseStore()))
                        .orElseThrow(() -> new BusinessProcessException("Unable to process event for order [" + order.getCode() + "]. No base store associated to the order."));
        String fulfilmentProcessDefinitionName = (String)Optional.<String>ofNullable(store.getSubmitOrderProcessCode()).orElseThrow(() -> new BusinessProcessException("Unable to process event for order [" + order.getCode() + "]. No fulfillment process definition for base store."));
        String expectedCodePrefix = fulfilmentProcessDefinitionName;
        Collection<String> codes = (Collection<String>)order.getOrderProcess().stream().map(BusinessProcessModel::getCode).filter(code -> code.startsWith(expectedCodePrefix)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(codes))
        {
            throw new BusinessProcessException("Unable to process event for order [" + order.getCode() + "]. No fulfillment processes associated to the order with prefix [" + fulfilmentProcessDefinitionName + "].");
        }
        if(codes.size() > 1)
        {
            throw new BusinessProcessException("Unable to process event for order [" + order.getCode() + "]. Expected only 1 process with prefix [" + fulfilmentProcessDefinitionName + "] but there were " + codes
                            .size() + ".");
        }
        return codes.iterator().next();
    }


    public BaseStoreService getBaseStoreService()
    {
        return this.baseStoreService;
    }


    @Required
    public void setBaseStoreService(BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }
}
