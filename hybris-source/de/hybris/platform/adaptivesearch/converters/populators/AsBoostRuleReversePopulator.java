package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AsBoostRule;
import de.hybris.platform.adaptivesearch.model.AsBoostRuleModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;

public class AsBoostRuleReversePopulator implements ContextAwarePopulator<AsBoostRule, AsBoostRuleModel, AsItemConfigurationReverseConverterContext>
{
    public void populate(AsBoostRule source, AsBoostRuleModel target, AsItemConfigurationReverseConverterContext context)
    {
        target.setIndexProperty(source.getIndexProperty());
        target.setOperator(source.getOperator());
        target.setValue(source.getValue());
        target.setBoostType(source.getBoostType());
        target.setBoost(source.getBoost());
    }
}
