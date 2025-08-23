package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.strategies.AsBoostRulesMergeStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileResultFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractAsBoostRulesMergeStrategy implements AsBoostRulesMergeStrategy
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
}
