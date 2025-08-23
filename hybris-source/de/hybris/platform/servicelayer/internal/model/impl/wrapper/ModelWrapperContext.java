package de.hybris.platform.servicelayer.internal.model.impl.wrapper;

import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.JaloAccessorsService;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.model.extractor.Cascader;
import java.util.Collection;

public class ModelWrapperContext
{
    private final ConverterRegistry converterRegistry;
    private final InterceptorRegistry interceptorRegistry;
    private final Cascader cascader;
    private final JaloAccessorsService jaloAccessorsService;


    public ModelWrapperContext(ConverterRegistry converterRegistry, InterceptorRegistry interceptorRegistry, Cascader cascader)
    {
        this.cascader = cascader;
        this.converterRegistry = converterRegistry;
        this.interceptorRegistry = interceptorRegistry;
        this.jaloAccessorsService = (JaloAccessorsService)Registry.getApplicationContext().getBean("jaloAccessorsService", JaloAccessorsService.class);
    }


    public ConverterRegistry getConverterRegistry()
    {
        return this.converterRegistry;
    }


    public InterceptorRegistry getInterceptorRegistry()
    {
        return this.interceptorRegistry;
    }


    public Collection<PrepareInterceptor> getPreparers(String type)
    {
        return getInterceptorRegistry().getPrepareInterceptors(type);
    }


    public Collection<ValidateInterceptor> getValidators(String type)
    {
        return getInterceptorRegistry().getValidateInterceptors(type);
    }


    public Collection<RemoveInterceptor> getRemovers(String type)
    {
        return getInterceptorRegistry().getRemoveInterceptors(type);
    }


    public Cascader getCascader()
    {
        return this.cascader;
    }


    public JaloAccessorsService getJaloAccessorsService()
    {
        return this.jaloAccessorsService;
    }
}
