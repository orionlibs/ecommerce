package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsFacetConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.strategies.AsFacetsMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileResultFactory;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractAsFacetsMergeStrategy implements AsFacetsMergeStrategy
{
    private AsSearchProfileResultFactory asSearchProfileResultFactory;


    public AsSearchProfileResultFactory getAsSearchProfileResultFactory()
    {
        return this.asSearchProfileResultFactory;
    }


    @Required
    public void setAsSearchProfileResultFactory(AsSearchProfileResultFactory asSearchProfileResultFactory)
    {
        this.asSearchProfileResultFactory = asSearchProfileResultFactory;
    }


    protected <T extends AbstractAsFacetConfiguration> AsConfigurationHolder<T, AbstractAsFacetConfiguration> cloneConfigurationHolder(AsConfigurationHolder<T, AbstractAsFacetConfiguration> configurationHolder)
    {
        return this.asSearchProfileResultFactory.cloneConfigurationHolder(configurationHolder);
    }


    protected <T1 extends AbstractAsFacetConfiguration, T2 extends AbstractAsFacetConfiguration> void updateReplacedConfigurations(AsConfigurationHolder<T1, AbstractAsFacetConfiguration> configurationHolder, AsConfigurationHolder<T2, AbstractAsFacetConfiguration> replacedConfigurationHolder)
    {
        if(replacedConfigurationHolder != null)
        {
            List<AbstractAsFacetConfiguration> replacedConfigurations = configurationHolder.getReplacedConfigurations();
            replacedConfigurations.add((AbstractAsFacetConfiguration)replacedConfigurationHolder.getConfiguration());
            if(CollectionUtils.isNotEmpty(replacedConfigurationHolder.getReplacedConfigurations()))
            {
                replacedConfigurations.addAll(replacedConfigurationHolder.getReplacedConfigurations());
            }
        }
    }
}
