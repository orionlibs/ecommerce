package com.hybris.cis.api.executor;

import com.hybris.cis.api.model.CisResult;

public abstract class AbstractSimpleServiceMethodExecutor<T, U extends CisResult, V, W> extends AbstractServiceMethodExecutor<T, ServiceMethodRequest<T>, U, V, W>
{
    protected V convertRequest(ServiceMethodRequest<T> request)
    {
        return convertRequest((T)request.getModel());
    }


    protected abstract V convertRequest(T paramT);


    protected U convertResponse(ServiceMethodRequest<T> request, V vendorSpecificRequest, W vendorSpecificResponse)
    {
        return convertResponse((T)request.getModel(), vendorSpecificRequest, vendorSpecificResponse);
    }


    protected abstract U convertResponse(T paramT, V paramV, W paramW);


    public U execute(T request)
    {
        return (U)execute(ServiceMethodRequest.create(request));
    }
}
