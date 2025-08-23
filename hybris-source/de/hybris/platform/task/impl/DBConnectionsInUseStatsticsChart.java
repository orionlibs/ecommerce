package de.hybris.platform.task.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.servicelayer.stats.AggregatedStatisticsCollector;
import de.hybris.platform.servicelayer.stats.BasicStatisticsCollector;
import de.hybris.platform.servicelayer.stats.StatisticsCollector;
import de.hybris.platform.servicelayer.stats.StatisticsService;
import de.hybris.platform.servicelayer.stats.chart_impl.DefaultStatisticsChart;
import de.hybris.platform.servicelayer.stats.collector_impl.DefaultAggregatedStaitsicsCollector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

public class DBConnectionsInUseStatsticsChart extends DefaultStatisticsChart implements InitializingBean
{
    private static final Logger LOG = Logger.getLogger(DBConnectionsInUseStatsticsChart.class);


    public DBConnectionsInUseStatsticsChart(String name, String unit, StatisticsService statisticsService)
    {
        super(name, unit, statisticsService);
    }


    private Collection<HybrisDataSource> lookupDataSources()
    {
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        Collection<HybrisDataSource> ret = new ArrayList<>();
        ret.add(tenant.getMasterDataSource());
        ret.addAll(tenant.getAllAlternativeMasterDataSources());
        ret.addAll(tenant.getAllSlaveDataSources());
        return ret;
    }


    private void initLines(Collection<HybrisDataSource> dataSources)
    {
        DefaultAggregatedStaitsicsCollector defaultAggregatedStaitsicsCollector = new DefaultAggregatedStaitsicsCollector("dbConnectionsInUse", "Database Connections In Use", "#0256FF");
        for(HybrisDataSource ds : dataSources)
        {
            DBConnectionsInUseCollector dBConnectionsInUseCollector = new DBConnectionsInUseCollector(ds.getID(), ds.getID(), getRandomColor(), ds);
            addLine((StatisticsCollector)dBConnectionsInUseCollector);
            defaultAggregatedStaitsicsCollector.addContainedCollector((BasicStatisticsCollector)dBConnectionsInUseCollector);
            addLinetoView("zoom", (StatisticsCollector)dBConnectionsInUseCollector);
        }
        addAggregatedCollector((AggregatedStatisticsCollector)defaultAggregatedStaitsicsCollector);
        addLinetoView("poll", (StatisticsCollector)defaultAggregatedStaitsicsCollector);
    }


    protected String getLabel(HybrisDataSource ds)
    {
        return ds.getID();
    }


    private String getRandomColor()
    {
        String color = "#";
        Random random = new Random();
        color = color + color;
        color = color + color;
        color = color + color;
        return color;
    }


    public void afterPropertiesSet() throws Exception
    {
        try
        {
            initLines(lookupDataSources());
        }
        catch(Exception e)
        {
            LOG.info("Initialization of DBConnectionsInUseStatsticsChart failed.");
        }
    }
}
