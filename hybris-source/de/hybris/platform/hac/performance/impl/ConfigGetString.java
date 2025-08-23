package de.hybris.platform.hac.performance.impl;

import de.hybris.platform.util.Config;

public class ConfigGetString extends AbstractPerformanceTest
{
    public void executeBlock()
    {
        Config.getString("unknown.key", "bla");
    }


    public String getTestName()
    {
        return "Config.getString()";
    }


    public void cleanup()
    {
    }


    public void prepare()
    {
    }
}
