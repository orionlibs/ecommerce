package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsBoostItemConfiguration;
import de.hybris.platform.adaptivesearch.model.AbstractAsBoostItemConfigurationModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class AsBoostItemConfigurationReversePopulator implements ContextAwarePopulator<AbstractAsBoostItemConfiguration, AbstractAsBoostItemConfigurationModel, AsItemConfigurationReverseConverterContext>
{
    private ModelService modelService;


    public void populate(AbstractAsBoostItemConfiguration source, AbstractAsBoostItemConfigurationModel target, AsItemConfigurationReverseConverterContext context)
    {
        target.setProperty("searchConfiguration", context.getParentConfiguration());
        ItemModel item = (ItemModel)this.modelService.get(source.getItemPk());
        target.setItem(item);
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
