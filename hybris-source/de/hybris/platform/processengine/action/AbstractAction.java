package de.hybris.platform.processengine.action;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.helpers.ProcessParameterHelper;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.spring.Action;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractAction<T extends BusinessProcessModel> implements Action<T>
{
    protected ModelService modelService;
    protected ProcessParameterHelper processParameterHelper;


    protected static Set<String> createTransitions(String... transitions)
    {
        return new HashSet<>(Arrays.asList(transitions));
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setProcessParameterHelper(ProcessParameterHelper processParameterHelper)
    {
        this.processParameterHelper = processParameterHelper;
    }


    protected void save(Object model)
    {
        this.modelService.save(model);
    }


    protected void refresh(Object model)
    {
        this.modelService.refresh(model);
    }


    protected <E> E getProcessParameterValue(BusinessProcessModel process, String parameterName)
    {
        return (E)this.processParameterHelper.getProcessParameterByName(process, parameterName).getValue();
    }


    protected void setOrderStatus(OrderModel order, OrderStatus orderStatus)
    {
        order.setStatus(orderStatus);
        this.modelService.save(order);
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public ProcessParameterHelper getProcessParameterHelper()
    {
        return this.processParameterHelper;
    }
}
