package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.strategies.AsCloneStrategy;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsCloneStrategy implements AsCloneStrategy
{
    private ModelService modelService;
    private ModelCloningContext modelCloningContext;


    public <T extends de.hybris.platform.core.model.ItemModel> T clone(T objectToClone)
    {
        return (T)this.modelService.clone(objectToClone, this.modelCloningContext);
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelCloningContext getModelCloningContext()
    {
        return this.modelCloningContext;
    }


    @Required
    public void setModelCloningContext(ModelCloningContext modelCloningContext)
    {
        this.modelCloningContext = modelCloningContext;
    }
}
