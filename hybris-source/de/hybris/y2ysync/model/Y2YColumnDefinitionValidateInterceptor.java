package de.hybris.y2ysync.model;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

public class Y2YColumnDefinitionValidateInterceptor implements ValidateInterceptor<Y2YColumnDefinitionModel>
{
    public void onValidate(Y2YColumnDefinitionModel model, InterceptorContext ctx) throws InterceptorException
    {
        if(model.getAttributeDescriptor() == null && model.getColumnName() == null)
        {
            throw new InterceptorException("Y2YColumnDefinition is invalid [reason: either attributeDescriptor or customColumnName must be present]");
        }
    }
}
