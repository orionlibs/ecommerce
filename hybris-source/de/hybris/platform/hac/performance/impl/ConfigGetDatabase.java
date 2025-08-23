package de.hybris.platform.hac.performance.impl;

import de.hybris.platform.util.Config;

public class ConfigGetDatabase extends AbstractPerformanceTest
{
    public void executeBlock()
    {
        Config.getDatabase();
    }


    public String getTestName()
    {
        return "Config.getDatabase()";
    }


    public void cleanup()
    {
    }


    public void prepare()
    {
    }
}
