package de.hybris.platform.servicelayer.stats.chart_impl;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.stats.AggregatedStatisticsCollector;
import de.hybris.platform.servicelayer.stats.BasicStatisticsCollector;
import de.hybris.platform.servicelayer.stats.StatisticsCollector;
import de.hybris.platform.servicelayer.stats.collector_impl.DefaultAggregatedStaitsicsCollector;
import de.hybris.platform.servicelayer.stats.collector_impl.HTTPSessionCollector;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

public class SessionsStatisticsChart extends DefaultStatisticsChart implements InitializingBean
{
    private static final Logger LOG = Logger.getLogger(SessionsStatisticsChart.class);
    private SortedMap<String, String> webroots;


    public SessionsStatisticsChart(String name, String unit)
    {
        super(name, unit);
    }


    private void initWebroots()
    {
        List<ExtensionInfo> allExtensions = ConfigUtil.getPlatformConfig(Registry.class).getExtensionInfosInBuildOrder();
        this.webroots = new TreeMap<>();
        for(ExtensionInfo extensionInfo : allExtensions)
        {
            if(!extensionInfo.isExcluded())
            {
                if(extensionInfo.getWebModule() != null)
                {
                    String webroot = extensionInfo.getWebModule().getWebRoot();
                    webroot = StringUtils.isNotBlank(webroot) ? webroot : "/";
                    this.webroots.put(extensionInfo.getName(), webroot);
                }
            }
        }
    }


    private void initLines()
    {
        DefaultAggregatedStaitsicsCollector defaultAggregatedStaitsicsCollector = new DefaultAggregatedStaitsicsCollector("httpSessions", "HTTP Sessions", "#0256FF");
        for(String key : this.webroots.keySet())
        {
            HTTPSessionCollector hTTPSessionCollector = new HTTPSessionCollector(key, key, this.webroots.get(key), getRandomColor());
            addLine((StatisticsCollector)hTTPSessionCollector);
            defaultAggregatedStaitsicsCollector.addContainedCollector((BasicStatisticsCollector)hTTPSessionCollector);
            addLinetoView("zoom", (StatisticsCollector)hTTPSessionCollector);
        }
        addAggregatedCollector((AggregatedStatisticsCollector)defaultAggregatedStaitsicsCollector);
        addLinetoView("poll", (StatisticsCollector)defaultAggregatedStaitsicsCollector);
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
            initWebroots();
            initLines();
        }
        catch(Exception e)
        {
            LOG.info("Initialization of SessionStatisticsChart failed.");
        }
    }
}
