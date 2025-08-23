package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileActivationMapping;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileActivationStrategy;

public class DefaultAsSearchProfileActivationMapping implements AsSearchProfileActivationMapping
{
    private int priority;
    private AsSearchProfileActivationStrategy activationStrategy;


    public int getPriority()
    {
        return this.priority;
    }


    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    public AsSearchProfileActivationStrategy getActivationStrategy()
    {
        return this.activationStrategy;
    }


    public void setActivationStrategy(AsSearchProfileActivationStrategy activationStrategy)
    {
        this.activationStrategy = activationStrategy;
    }
}
