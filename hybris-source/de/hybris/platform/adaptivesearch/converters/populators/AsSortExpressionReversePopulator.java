package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AsSortExpression;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsSortExpressionModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;

public class AsSortExpressionReversePopulator implements ContextAwarePopulator<AsSortExpression, AsSortExpressionModel, AsItemConfigurationReverseConverterContext>
{
    public void populate(AsSortExpression source, AsSortExpressionModel target, AsItemConfigurationReverseConverterContext context)
    {
        target.setSortConfiguration((AbstractAsSortConfigurationModel)context.getParentConfiguration());
        target.setExpression(source.getExpression());
        target.setOrder(source.getOrder());
    }
}
