package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsItemConfiguration;
import de.hybris.platform.adaptivesearch.model.AbstractAsItemConfigurationModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;

public class AsItemConfigurationPopulator implements ContextAwarePopulator<AbstractAsItemConfigurationModel, AbstractAsItemConfiguration, AsItemConfigurationConverterContext>
{
    public void populate(AbstractAsItemConfigurationModel source, AbstractAsItemConfiguration target, AsItemConfigurationConverterContext context)
    {
        target.setSearchProfileCode(context.getSearchProfileCode());
        target.setSearchConfigurationUid(context.getSearchConfigurationUid());
    }
}
