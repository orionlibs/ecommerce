package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.internal.model.extractor.Cascader;
import de.hybris.platform.servicelayer.internal.model.impl.DefaultModelService;
import de.hybris.platform.servicelayer.internal.model.impl.InterceptorContextSnapshot;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.WrapperRegistry;
import java.util.Collection;
import java.util.LinkedHashSet;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCascader implements Cascader
{
    private DefaultModelService modelService;


    public Collection<ModelWrapper> getNewModels(ModelWrapper toProcess, InterceptorContextSnapshot ctxSnapshot, WrapperRegistry wrapperRegistry)
    {
        Collection<ModelWrapper> allNewModelWrappers = new LinkedHashSet<>();
        CascadingModelWalker cascadingModelWalker = new CascadingModelWalker(this.modelService, (ModelWalkerCallback)new ItemsCollectingModelWalkerCallback(allNewModelWrappers));
        cascadingModelWalker.walkThrough(toProcess, wrapperRegistry);
        Collection<ModelWrapper> onlyNewModelWrappers = new LinkedHashSet<>();
        for(ModelWrapper newOne : allNewModelWrappers)
        {
            if(!toProcess.equals(newOne) && isNotYetRegistered(ctxSnapshot, newOne))
            {
                onlyNewModelWrappers.add(newOne);
            }
        }
        return onlyNewModelWrappers;
    }


    private boolean isNotYetRegistered(InterceptorContextSnapshot ctxSnapshot, ModelWrapper newOne)
    {
        return !ctxSnapshot.contains(newOne.getModel(), PersistenceOperation.SAVE);
    }


    @Required
    public void setModelService(DefaultModelService modelService)
    {
        this.modelService = modelService;
    }
}
