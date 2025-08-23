package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileResultFactory;
import de.hybris.platform.adaptivesearch.strategies.AsSortsMergeStrategy;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractAsSortsMergeStrategy implements AsSortsMergeStrategy
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


    protected <T extends AbstractAsSortConfiguration> AsConfigurationHolder<T, AbstractAsSortConfiguration> cloneConfigurationHolder(AsConfigurationHolder<T, AbstractAsSortConfiguration> configurationHolder)
    {
        return this.asSearchProfileResultFactory.cloneConfigurationHolder(configurationHolder);
    }


    protected <T1 extends AbstractAsSortConfiguration, T2 extends AbstractAsSortConfiguration> void updateReplacedConfigurations(AsConfigurationHolder<T1, AbstractAsSortConfiguration> configurationHolder, AsConfigurationHolder<T2, AbstractAsSortConfiguration> replacedConfigurationHolder)
    {
        if(replacedConfigurationHolder != null)
        {
            List<AbstractAsSortConfiguration> replacedConfigurations = configurationHolder.getReplacedConfigurations();
            replacedConfigurations.add((AbstractAsSortConfiguration)replacedConfigurationHolder.getConfiguration());
            if(CollectionUtils.isNotEmpty(replacedConfigurationHolder.getReplacedConfigurations()))
            {
                replacedConfigurations.addAll(replacedConfigurationHolder.getReplacedConfigurations());
            }
        }
    }
}
