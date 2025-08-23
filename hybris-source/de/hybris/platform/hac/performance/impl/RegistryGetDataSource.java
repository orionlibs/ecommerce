package de.hybris.platform.hac.performance.impl;

import de.hybris.platform.core.Registry;

public class RegistryGetDataSource extends AbstractPerformanceTest
{
    public void executeBlock()
    {
        Registry.getCurrentTenant().getDataSource();
    }


    public String getTestName()
    {
        return "Registry.getCurrentTenant().getDataSource()";
    }


    public void cleanup()
    {
    }


    public void prepare()
    {
    }
}
