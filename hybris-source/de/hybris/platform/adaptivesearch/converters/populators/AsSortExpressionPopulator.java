package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AsSortExpression;
import de.hybris.platform.adaptivesearch.model.AsSortExpressionModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;

public class AsSortExpressionPopulator implements ContextAwarePopulator<AsSortExpressionModel, AsSortExpression, AsItemConfigurationConverterContext>
{
    public void populate(AsSortExpressionModel source, AsSortExpression target, AsItemConfigurationConverterContext context)
    {
        target.setExpression(source.getExpression());
        target.setOrder(source.getOrder());
    }
}
