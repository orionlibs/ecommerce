package de.hybris.platform.platformbackoffice.charts;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.servicelayer.stats.StatisticsChart;
import de.hybris.platform.servicelayer.stats.StatisticsCollector;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class DefaultLinearCollectorChartHandler extends DefaultAbstractCollectorChartHandler
{
    public static final String ORG_ZKOSS_WEB_PREFERRED_TIME_ZONE = "org.zkoss.web.preferred.timeZone";
    public static final String POLL = "poll";
    public static final String GENERIC_CHART_SERIES_SUFFIX = ".series";
    public static final String GENERIC_CHART_SERIES_PREFIX = "generic.chart.";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultLinearCollectorChartHandler.class);


    public void applyModel(WidgetInstanceManager wim, Charts charts)
    {
        SortedMap<String, Object[][]> lines = (SortedMap)new TreeMap<>();
        String chartType = getChartType(wim);
        StatisticsChart stats = getStatisticsMetadataService().getChart(chartType);
        if(stats != null)
        {
            long utcOffset = getUtcOffset();
            for(StatisticsCollector collector : stats.getViewLines("poll"))
            {
                Object[][] data = getStatisticsDataService().getAllData(collector.getName(), 100, utcOffset);
                if(data != null && data.length > 0)
                {
                    lines.put(collector.getName(), data);
                }
            }
            Collection<String> seriesNames = getSeriesNames(wim);
            boolean refresh = (charts.getSeriesSize() == seriesNames.size());
            boolean roundToInt = isRoundValuesToInt(wim);
            if(refresh)
            {
                int counter = 0;
                for(String line : seriesNames)
                {
                    Object[][] data = lines.get(line);
                    if(data != null)
                    {
                        Point[] dataList = new Point[data.length];
                        int i = 0;
                        for(Object[] objects : data)
                        {
                            dataList[i++] = new Point((Number)objects[0], getNumericValue(objects[1], roundToInt));
                        }
                        charts.getSeries(counter++).setData(dataList);
                    }
                }
            }
            else
            {
                while(charts.getSeriesSize() > 0)
                {
                    charts.getSeries().remove();
                }
                for(String line : seriesNames)
                {
                    Object[][] data = lines.get(line);
                    if(data != null)
                    {
                        List<Point> dataList = Lists.newArrayList();
                        for(Object[] objects : data)
                        {
                            dataList.add(new Point((Number)objects[0], getNumericValue(objects[1], roundToInt)));
                        }
                        Series series = new Series(line, dataList);
                        series.setName(localizeSeries(wim, line));
                        charts.addSeries(series);
                    }
                }
            }
        }
        else
        {
            LOG.warn("Could not find statistics for chart type: {}", chartType);
        }
    }


    protected Number getNumericValue(Object object, boolean roundToInt)
    {
        if(object instanceof Number)
        {
            Number num = (Number)object;
            if(roundToInt)
            {
                return Integer.valueOf(num.intValue());
            }
            return num;
        }
        LOG.warn("NaN: {} returning: 0", object);
        return Integer.valueOf(0);
    }


    protected String localizeSeries(WidgetInstanceManager wim, String seriesName)
    {
        String key = "generic.chart." + seriesName + ".series";
        String label = wim.getLabel(key);
        if(StringUtils.isBlank(label))
        {
            LOG.warn("Could not find localization key: {}", key);
            return seriesName;
        }
        return label;
    }


    protected long getUtcOffset()
    {
        Session session = Sessions.getCurrent();
        Object profferedTimeZone = (session == null) ? null : session.getAttribute("org.zkoss.web.preferred.timeZone");
        if(profferedTimeZone instanceof TimeZone)
        {
            return -(((TimeZone)profferedTimeZone).getRawOffset() + ((TimeZone)profferedTimeZone).getDSTSavings());
        }
        Calendar calendar = Calendar.getInstance();
        return -(calendar.get(15) + calendar.get(16));
    }
}
