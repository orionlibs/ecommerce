package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AsBoostRule;
import de.hybris.platform.adaptivesearch.model.AsBoostRuleModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;

public class AsBoostRulePopulator implements ContextAwarePopulator<AsBoostRuleModel, AsBoostRule, AsItemConfigurationConverterContext>
{
    public void populate(AsBoostRuleModel source, AsBoostRule target, AsItemConfigurationConverterContext context)
    {
        target.setIndexProperty(source.getIndexProperty());
        target.setOperator(source.getOperator());
        target.setValue(source.getValue());
        target.setBoostType(source.getBoostType());
        target.setBoost(source.getBoost());
    }
}
