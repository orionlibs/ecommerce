package de.hybris.platform.servicelayer.internal.model.extractor;

import de.hybris.platform.servicelayer.internal.model.impl.InterceptorContextSnapshot;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.WrapperRegistry;
import java.util.Collection;

public interface Cascader
{
    Collection<ModelWrapper> getNewModels(ModelWrapper paramModelWrapper, InterceptorContextSnapshot paramInterceptorContextSnapshot, WrapperRegistry paramWrapperRegistry);
}
