package de.hybris.platform.servicelayer.internal.model.extractor;

import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.WrapperRegistry;

public interface CascadingDependenciesResolver
{
    void resolveDependencies(ModelWrapper paramModelWrapper, WrapperRegistry paramWrapperRegistry);
}
