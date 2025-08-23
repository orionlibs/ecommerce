package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsBoostItemConfiguration;
import de.hybris.platform.adaptivesearch.model.AbstractAsBoostItemConfigurationModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;

public class AsBoostItemConfigurationPopulator implements ContextAwarePopulator<AbstractAsBoostItemConfigurationModel, AbstractAsBoostItemConfiguration, AsItemConfigurationConverterContext>
{
    public void populate(AbstractAsBoostItemConfigurationModel source, AbstractAsBoostItemConfiguration target, AsItemConfigurationConverterContext context)
    {
        target.setItemPk(source.getItem().getPk());
    }
}
