package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.daos.AsSearchConfigurationDao;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchConfigurationStrategy;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractAsSearchConfigurationStrategy<P extends AbstractAsSearchProfileModel, C extends AbstractAsSearchConfigurationModel> implements AsSearchConfigurationStrategy<P, C>
{
    private ModelService modelService;
    private L10NService l10nService;
    private AsSearchConfigurationDao asSearchConfigurationDao;


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public L10NService getL10nService()
    {
        return this.l10nService;
    }


    @Required
    public void setL10nService(L10NService l10nService)
    {
        this.l10nService = l10nService;
    }


    public AsSearchConfigurationDao getAsSearchConfigurationDao()
    {
        return this.asSearchConfigurationDao;
    }


    @Required
    public void setAsSearchConfigurationDao(AsSearchConfigurationDao asSearchConfigurationDao)
    {
        this.asSearchConfigurationDao = asSearchConfigurationDao;
    }
}
