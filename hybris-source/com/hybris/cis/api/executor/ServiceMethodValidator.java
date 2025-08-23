package com.hybris.cis.api.executor;

import com.hybris.cis.api.exception.ServicePreconditionFailedException;

public interface ServiceMethodValidator<T>
{
    void validateRequest(ServiceMethodRequest<T> paramServiceMethodRequest) throws ServicePreconditionFailedException;
}
