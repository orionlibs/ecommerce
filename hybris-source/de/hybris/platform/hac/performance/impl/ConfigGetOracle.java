package de.hybris.platform.hac.performance.impl;

import de.hybris.platform.util.Config;

public class ConfigGetOracle extends AbstractPerformanceTest
{
    public void executeBlock()
    {
        Config.isOracleUsed();
    }


    public String getTestName()
    {
        return "Config.isOracleUsed()";
    }


    public void cleanup()
    {
    }


    public void prepare()
    {
    }
}
