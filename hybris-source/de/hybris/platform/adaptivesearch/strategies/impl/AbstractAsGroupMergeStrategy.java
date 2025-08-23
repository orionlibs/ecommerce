package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.data.AsGroup;
import de.hybris.platform.adaptivesearch.strategies.AsGroupMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileResultFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractAsGroupMergeStrategy implements AsGroupMergeStrategy
{
    private AsSearchProfileResultFactory asSearchProfileResultFactory;


    protected AsConfigurationHolder<AsGroup, AsGroup> cloneConfigurationHolder(AsConfigurationHolder<AsGroup, AsGroup> configurationHolder)
    {
        return this.asSearchProfileResultFactory.cloneConfigurationHolder(configurationHolder);
    }


    public AsSearchProfileResultFactory getAsSearchProfileResultFactory()
    {
        return this.asSearchProfileResultFactory;
    }


    @Required
    public void setAsSearchProfileResultFactory(AsSearchProfileResultFactory asSearchProfileResultFactory)
    {
        this.asSearchProfileResultFactory = asSearchProfileResultFactory;
    }
}
