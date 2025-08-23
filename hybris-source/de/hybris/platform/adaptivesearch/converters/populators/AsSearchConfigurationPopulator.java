package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsSearchConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsSearchConfiguration;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;

public class AsSearchConfigurationPopulator implements ContextAwarePopulator<AbstractAsSearchConfigurationModel, AbstractAsSearchConfiguration, AsSearchConfigurationConverterContext>
{
    public void populate(AbstractAsSearchConfigurationModel source, AbstractAsSearchConfiguration target, AsSearchConfigurationConverterContext context)
    {
        target.setSearchProfileCode(context.getSearchProfileCode());
    }
}
