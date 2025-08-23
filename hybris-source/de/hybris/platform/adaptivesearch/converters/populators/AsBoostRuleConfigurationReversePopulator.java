package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsBoostRuleConfiguration;
import de.hybris.platform.adaptivesearch.model.AbstractAsBoostRuleConfigurationModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;

public class AsBoostRuleConfigurationReversePopulator implements ContextAwarePopulator<AbstractAsBoostRuleConfiguration, AbstractAsBoostRuleConfigurationModel, AsItemConfigurationReverseConverterContext>
{
    public void populate(AbstractAsBoostRuleConfiguration source, AbstractAsBoostRuleConfigurationModel target, AsItemConfigurationReverseConverterContext context)
    {
        target.setProperty("searchConfiguration", context.getParentConfiguration());
    }
}
