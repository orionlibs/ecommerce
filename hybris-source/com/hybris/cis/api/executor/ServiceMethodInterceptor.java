package com.hybris.cis.api.executor;

public interface ServiceMethodInterceptor<T, U extends com.hybris.cis.api.model.Identifiable>
{
    void before(ServiceMethodRequest<T> paramServiceMethodRequest);


    void after(U paramU);
}
