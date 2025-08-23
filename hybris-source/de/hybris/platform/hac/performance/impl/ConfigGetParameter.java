package de.hybris.platform.hac.performance.impl;

import de.hybris.platform.util.Config;

public class ConfigGetParameter extends AbstractPerformanceTest
{
    public void executeBlock()
    {
        Config.getParameter("db.url");
    }


    public String getTestName()
    {
        return "Config.getParam()";
    }


    public void cleanup()
    {
    }


    public void prepare()
    {
    }
}
