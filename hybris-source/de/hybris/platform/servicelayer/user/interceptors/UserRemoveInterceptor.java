package de.hybris.platform.servicelayer.user.interceptors;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import java.util.Set;

public class UserRemoveInterceptor implements RemoveInterceptor
{
    public void onRemove(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof UserModel)
        {
            UserModel user = (UserModel)model;
            for(OrderModel order : user.getOrders())
            {
                if(!ctx.getModelService().isRemoved(order))
                {
                    Set<Object> registeredElements = ctx.getAllRegisteredElements();
                    if(registeredElements.contains(order))
                    {
                        continue;
                    }
                    throw new InterceptorException("Can not remove an User(" + user + ") while it is assigned to (at least one) existing Order(" + order + ")");
                }
            }
        }
    }
}
