package de.hybris.platform.media.url.impl;

import de.hybris.platform.media.url.PrettyUrlStrategy;

public final class PrettyUrlStrategyFactory
{
    private static final PrettyUrlStrategyFactory FACTORY = new PrettyUrlStrategyFactory();
    private volatile PrettyUrlStrategy defaultStrategy = null;


    public static PrettyUrlStrategyFactory getInstance()
    {
        return FACTORY;
    }


    public static PrettyUrlStrategy getDefaultPrettyUrlStrategy()
    {
        return getInstance().getDefaultStrategy();
    }


    public PrettyUrlStrategy getDefaultStrategy()
    {
        PrettyUrlStrategy currentStrategy = this.defaultStrategy;
        if(currentStrategy == null)
        {
            return this.defaultStrategy = (PrettyUrlStrategy)new DefaultPrettyUrlStrategy();
        }
        return currentStrategy;
    }


    void setDefaultStrategy(PrettyUrlStrategy strategy)
    {
        this.defaultStrategy = strategy;
    }
}
