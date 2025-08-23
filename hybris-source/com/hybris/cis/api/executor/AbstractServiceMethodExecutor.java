package com.hybris.cis.api.executor;

import com.hybris.cis.api.model.CisResult;
import com.hybris.cis.api.model.Identifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractServiceMethodExecutor<T, R extends ServiceMethodRequest<T>, U extends CisResult, V, W>
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractServiceMethodExecutor.class);
    private ServiceMethodValidator<T> validator;
    private ServiceMethodInterceptor<T, U> interceptor;


    protected abstract V convertRequest(R paramR);


    protected abstract U convertResponse(R paramR, V paramV, W paramW);


    protected abstract W process(V paramV);


    public U execute(R request)
    {
        logRequest((ServiceMethodRequest<T>)request);
        if(this.validator != null)
        {
            this.validator.validateRequest((ServiceMethodRequest)request);
        }
        if(this.interceptor != null)
        {
            this.interceptor.before((ServiceMethodRequest)request);
        }
        V vendorSpecificRequest = convertRequest(request);
        W vendorSpecificResponse = process(vendorSpecificRequest);
        U response = convertResponse(request, vendorSpecificRequest, vendorSpecificResponse);
        if(this.interceptor != null)
        {
            this.interceptor.after((Identifiable)response);
        }
        logResponse(response);
        return response;
    }


    private void logResponse(U response)
    {
        LOG.info("{} handling response {}", getClass().getSimpleName(), response);
    }


    private void logRequest(ServiceMethodRequest<T> request)
    {
        LOG.info("{} handling request {}", getClass().getSimpleName(), request);
    }


    public ServiceMethodValidator<T> getValidator()
    {
        return this.validator;
    }


    public void setValidator(ServiceMethodValidator<T> validator)
    {
        this.validator = validator;
    }


    public ServiceMethodInterceptor<T, U> getInterceptor()
    {
        return this.interceptor;
    }


    public void setInterceptor(ServiceMethodInterceptor<T, U> interceptor)
    {
        this.interceptor = interceptor;
    }
}
