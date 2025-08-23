package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsFacetValueConfiguration;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetValueConfigurationModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;

public class AsFacetValueConfigurationPopulator implements ContextAwarePopulator<AbstractAsFacetValueConfigurationModel, AbstractAsFacetValueConfiguration, AsItemConfigurationConverterContext>
{
    public void populate(AbstractAsFacetValueConfigurationModel source, AbstractAsFacetValueConfiguration target, AsItemConfigurationConverterContext context)
    {
        target.setValue(source.getValue());
    }
}
