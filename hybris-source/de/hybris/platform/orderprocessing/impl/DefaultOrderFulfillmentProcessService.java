package de.hybris.platform.orderprocessing.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.OrderFulfillmentProcessService;
import de.hybris.platform.orderprocessing.exception.FullfilmentProcessStaringException;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOrderFulfillmentProcessService implements OrderFulfillmentProcessService
{
    private BusinessProcessService businessProcessService;
    private KeyGenerator fulfillmentProcessCodeGenerator;
    private ModelService modelService;


    public OrderProcessModel startFulfillmentProcessForOrder(OrderModel order)
    {
        if(order == null)
        {
            throw new IllegalArgumentException("Order cannot be null");
        }
        String processDefinitionName = Config.getString("basecommerce.fulfillmentprocess.name", null);
        if(processDefinitionName == null || StringUtils.isBlank(processDefinitionName))
        {
            throw new FullfilmentProcessStaringException(order.getCode(), "", "No definition name found");
        }
        OrderProcessModel process = (OrderProcessModel)getBusinessProcessService().createProcess(getFulfillmentProcessCodeGenerator().generate().toString(), processDefinitionName);
        process.setOrder(order);
        getModelService().save(order);
        getBusinessProcessService().startProcess((BusinessProcessModel)process);
        return process;
    }


    protected BusinessProcessService getBusinessProcessService()
    {
        return this.businessProcessService;
    }


    @Required
    public void setBusinessProcessService(BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }


    protected KeyGenerator getFulfillmentProcessCodeGenerator()
    {
        return this.fulfillmentProcessCodeGenerator;
    }


    @Required
    public void setFulfillmentProcessCodeGenerator(KeyGenerator fulfillmentProcessCodeGenerator)
    {
        this.fulfillmentProcessCodeGenerator = fulfillmentProcessCodeGenerator;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
