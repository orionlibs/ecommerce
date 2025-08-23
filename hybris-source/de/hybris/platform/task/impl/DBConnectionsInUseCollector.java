package de.hybris.platform.task.impl;

import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.servicelayer.stats.AbstractStatisticsCollector;
import de.hybris.platform.servicelayer.stats.BasicStatisticsCollector;

public class DBConnectionsInUseCollector extends AbstractStatisticsCollector implements BasicStatisticsCollector
{
    private final HybrisDataSource dataSource;


    public DBConnectionsInUseCollector(String name, String label, String color, HybrisDataSource ds)
    {
        super(name + "_" + name, label, color);
        this.dataSource = ds;
    }


    public float collect()
    {
        float s = getCurrentConnectionsInUse();
        return s;
    }


    protected int getCurrentConnectionsInUse()
    {
        try
        {
            return this.dataSource.getNumInUse();
        }
        catch(Exception e)
        {
            return -1;
        }
    }


    public boolean evaluateValue(float value)
    {
        return (value >= 0.0F);
    }
}
