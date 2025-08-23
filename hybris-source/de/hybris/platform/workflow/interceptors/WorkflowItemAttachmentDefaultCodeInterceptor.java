package de.hybris.platform.workflow.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;

public class WorkflowItemAttachmentDefaultCodeInterceptor implements PrepareInterceptor
{
    private KeyGenerator keyGenerator;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(!(model instanceof WorkflowItemAttachmentModel))
        {
            throw new IllegalArgumentException("Parameter not instanceof WorkflowItemAttachmentModel");
        }
        WorkflowItemAttachmentModel action = (WorkflowItemAttachmentModel)model;
        if(action.getCode() == null)
        {
            action.setCode((String)this.keyGenerator.generate());
        }
    }


    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }
}
