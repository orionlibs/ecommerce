package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsFacetValueConfiguration;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetValueConfigurationModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;

public class AsFacetValueConfigurationReversePopulator implements ContextAwarePopulator<AbstractAsFacetValueConfiguration, AbstractAsFacetValueConfigurationModel, AsItemConfigurationReverseConverterContext>
{
    public void populate(AbstractAsFacetValueConfiguration source, AbstractAsFacetValueConfigurationModel target, AsItemConfigurationReverseConverterContext context)
    {
        target.setProperty("facetConfiguration", context.getParentConfiguration());
        target.setValue(source.getValue());
    }
}
