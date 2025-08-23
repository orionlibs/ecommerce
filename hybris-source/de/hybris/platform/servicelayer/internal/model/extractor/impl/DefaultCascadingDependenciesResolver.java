package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import de.hybris.platform.servicelayer.internal.model.extractor.CascadingDependenciesResolver;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.WrapperRegistry;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCascadingDependenciesResolver implements CascadingDependenciesResolver
{
    private DefaultModelService modelService;


    public void resolveDependencies(ModelWrapper toProcess, WrapperRegistry wrapperRegistry)
    {
        CascadingModelWalker cascadingModelWalker = new CascadingModelWalker(this.modelService, (ModelWalkerCallback)new DependenciesResolvingModelWalkerCallback());
        cascadingModelWalker.walkThrough(toProcess, wrapperRegistry);
    }


    @Required
    public void setModelService(DefaultModelService modelService)
    {
        this.modelService = modelService;
    }
}
