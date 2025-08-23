package de.hybris.platform.adaptivesearchbackoffice.common.impl;

import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearchbackoffice.common.DataProvider;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRuntimeException;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractAsDataProvider<D, V> implements DataProvider<D, V>
{
    private ModelService modelService;


    protected AbstractAsSearchProfileModel resolveSearchProfile(AbstractAsConfigurableSearchConfigurationModel searchConfiguration)
    {
        Object searchProfile = this.modelService.getAttributeValue(searchConfiguration, "searchProfile");
        if(!(searchProfile instanceof AbstractAsSearchProfileModel))
        {
            throw new EditorRuntimeException("Search profile not valid");
        }
        return (AbstractAsSearchProfileModel)searchProfile;
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
}
