package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.Interceptor;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.Ordered;

public class InterceptorMapping implements Ordered
{
    private String typeCode;
    private Interceptor interceptor;
    private Collection<Interceptor> replacedInterceptors;
    private int order = Integer.MAX_VALUE;


    public InterceptorMapping()
    {
    }


    public InterceptorMapping(String typeCode, Interceptor interceptor, Collection<Interceptor> replacements)
    {
        this.typeCode = typeCode;
        this.interceptor = interceptor;
        this.replacedInterceptors = replacements;
    }


    public Interceptor getInterceptor()
    {
        return this.interceptor;
    }


    @Required
    public void setInterceptor(Interceptor interceptor)
    {
        this.interceptor = interceptor;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    @Required
    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public Collection<Interceptor> getReplacedInterceptors()
    {
        return (this.replacedInterceptors == null) ? Collections.EMPTY_LIST : this.replacedInterceptors;
    }


    public void setReplacedInterceptors(Collection<Interceptor> replacedInterceptors)
    {
        this.replacedInterceptors = replacedInterceptors;
    }


    public final void setOrder(int order)
    {
        this.order = order;
    }


    public final int getOrder()
    {
        return this.order;
    }
}
