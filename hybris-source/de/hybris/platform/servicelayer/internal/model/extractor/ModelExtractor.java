package de.hybris.platform.servicelayer.internal.model.extractor;

import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelServiceInterceptorContext;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.List;

public interface ModelExtractor
{
    List<ModelWrapper> process(DefaultModelServiceInterceptorContext paramDefaultModelServiceInterceptorContext, InterceptorRegistry paramInterceptorRegistry, ConverterRegistry paramConverterRegistry);
}
