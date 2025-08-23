package de.hybris.platform.adaptivesearch.model.attributes;

import de.hybris.platform.adaptivesearch.strategies.AsItemModelHelper;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractAsAttributeHandler<V, M extends AbstractItemModel> implements DynamicAttributeHandler<V, M>
{
    private ModelService modelService;
    private AsItemModelHelper asItemModelHelper;


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public AsItemModelHelper getAsItemModelHelper()
    {
        return this.asItemModelHelper;
    }


    @Required
    public void setAsItemModelHelper(AsItemModelHelper asItemModelHelper)
    {
        this.asItemModelHelper = asItemModelHelper;
    }
}
