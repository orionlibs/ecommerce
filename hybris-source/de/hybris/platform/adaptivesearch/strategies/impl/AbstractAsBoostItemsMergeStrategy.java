package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsBoostItemConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.strategies.AsBoostItemsMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileResultFactory;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractAsBoostItemsMergeStrategy implements AsBoostItemsMergeStrategy
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


    protected <T extends AbstractAsBoostItemConfiguration> AsConfigurationHolder<T, AbstractAsBoostItemConfiguration> cloneConfigurationHolder(AsConfigurationHolder<T, AbstractAsBoostItemConfiguration> configurationHolder)
    {
        return this.asSearchProfileResultFactory.cloneConfigurationHolder(configurationHolder);
    }


    protected <T1 extends AbstractAsBoostItemConfiguration, T2 extends AbstractAsBoostItemConfiguration> void updateReplacedConfigurations(AsConfigurationHolder<T1, AbstractAsBoostItemConfiguration> configurationHolder,
                    AsConfigurationHolder<T2, AbstractAsBoostItemConfiguration> replacedConfigurationHolder)
    {
        if(replacedConfigurationHolder != null)
        {
            List<AbstractAsBoostItemConfiguration> replacedConfigurations = configurationHolder.getReplacedConfigurations();
            replacedConfigurations.add((AbstractAsBoostItemConfiguration)replacedConfigurationHolder.getConfiguration());
            if(CollectionUtils.isNotEmpty(replacedConfigurationHolder.getReplacedConfigurations()))
            {
                replacedConfigurations.addAll(replacedConfigurationHolder.getReplacedConfigurations());
            }
        }
    }
}
