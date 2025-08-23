package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.model.extractor.CascadingDependenciesResolver;
import de.hybris.platform.servicelayer.internal.model.extractor.ModelExtractor;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelServiceInterceptorContext;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultModelExtractor implements ModelExtractor
{
    private CascadingDependenciesResolver dependenciesResolver;


    public List<ModelWrapper> process(DefaultModelServiceInterceptorContext ctx, InterceptorRegistry interceptorRegistry, ConverterRegistry converterRegistry)
    {
        for(ModelWrapper wrapper : ctx.getInitialWrappers())
        {
            wrapper.executeInterceptorsAndCascade(ctx);
            ctx.schedule(wrapper);
        }
        for(ModelWrapper wrapper : ctx.getSchedule())
        {
            wrapper.resolveDependencies(this.dependenciesResolver, ctx.getWrapperRegistry());
        }
        RejectRegistrationInterceptorContext ctxForValidation = new RejectRegistrationInterceptorContext(this, (InterceptorContext)ctx);
        for(ModelWrapper wrapper : ctx.getSchedule())
        {
            wrapper.validate((InterceptorContext)ctxForValidation);
        }
        return (List<ModelWrapper>)ImmutableList.copyOf((Iterable)ctx.getSchedule());
    }


    @Required
    public void setDependenciesResolver(CascadingDependenciesResolver dependenciesResolver)
    {
        this.dependenciesResolver = dependenciesResolver;
    }
}
