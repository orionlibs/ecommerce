package de.hybris.platform.platformbackoffice.charts;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.servicelayer.stats.MonitoringFacade;
import de.hybris.platform.servicelayer.stats.StatisticsData;
import de.hybris.platform.servicelayer.stats.StatisticsService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Color;
import org.zkoss.chart.Series;
import org.zkoss.chart.Title;
import org.zkoss.chart.XAxis;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.plotOptions.AreaPlotOptions;
import org.zkoss.chart.plotOptions.PlotOptions;

public abstract class DefaultAbstractCollectorChartHandler implements ChartHandler
{
    public static final String EXPORT_ENABLED = "exportEnabled";
    public static final String ZOOM_TYPE = "zoomType";
    public static final String PLOT_TYPE = "plotType";
    public static final String SERIES = "series";
    public static final String CHART_TYPE = "chartType";
    public static final String CHART_TITLE = "chartTitle";
    public static final String ROUND_VALUES_TO_INT = "roundValuesToInt";
    public static final String TITLE_SPACING_TOP = "spacingTop";
    public static final String TITLE_SPACING_BOTTOM = "spacingBottom";
    public static final String TITLE_SPACING_LEFT = "spacingLeft";
    public static final String TITLE_SPACING_RIGHT = "spacingRight";
    public static final String TITLE_HORIZONTAL_ALIGNMENT = "titleHorizontalAlignment";
    public static final String TITLE_VERTICAL_ALIGNMENT = "titleVerticalAlignment";
    public static final String TITLE_STYLE = "titleStyle";
    public static final String TITLE_AXIS_X = "titleAxisX";
    public static final String TITLE_AXIS_Y = "titleAxisY";
    public static final String X_AXIS_LABEL = "xAxisLabel";
    public static final String X_AXIS_TYPE = "xAxisType";
    public static final String X_MAX = "xMax";
    public static final String X_MIN = "xMin";
    public static final String Y_AXIS_LABEL = "yAxisLabel";
    public static final String Y_AXIS_TYPE = "yAxisType";
    public static final String Y_MAX = "yMax";
    public static final String Y_MIN = "yMin";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAbstractCollectorChartHandler.class);
    public static final String COLORS_DELIMITER = ";";
    public static final String CHART_COLORS_LIST = "chartColors";
    private StatisticsService statisticsMetadataService;
    private StatisticsData statisticsDataService;
    private MonitoringFacade monitoringFacade;


    public void initializeChart(WidgetInstanceManager wim, Charts charts)
    {
        charts.setZoomType(getZoomType(wim));
        charts.setType(getPlotType(wim));
        charts.getExporting().setEnabled(isExportButtonEnabled(wim));
        charts.getLegend().setEnabled(false);
        Title title = charts.getTitle();
        title.setText(getChartTitle(wim));
        applyTitleStyles(title, wim);
        adjustXYAxises(wim, charts);
        adjustSpacing(wim, charts);
        applyChartColors(wim, charts);
        applyDefaultPlotOptions(charts.getPlotOptions());
    }


    protected void applyChartColors(WidgetInstanceManager wim, Charts charts)
    {
        List<Color> colors = new ArrayList<>();
        String colorsCollection = wim.getWidgetSettings().getString("chartColors");
        if(StringUtils.isNotBlank(colorsCollection))
        {
            String[] colorsList = colorsCollection.split(";");
            for(String color : colorsList)
            {
                colors.add(new Color(color));
            }
            charts.setColors(colors);
        }
    }


    protected void adjustXYAxises(WidgetInstanceManager wim, Charts charts)
    {
        XAxis xAxis = charts.getXAxis();
        xAxis.setType(getXAxisType(wim));
        xAxis.setTitle(getXAxisLabel(wim));
        Number min = getXAxisMin(wim);
        if(min != null)
        {
            xAxis.setMin(min);
        }
        Number max = getXAxisMax(wim);
        if(max != null)
        {
            xAxis.setMax(max);
        }
        YAxis yAxis = charts.getYAxis();
        yAxis.setTitle(getYAxisLabel(wim));
        yAxis.setType(getYAxisType(wim));
        yAxis.setMin(getYAxisMin(wim));
        yAxis.setMax(getYAxisMax(wim));
    }


    public Collection<ChartInfoLabel> getDefaultInfoLabels(Charts charts)
    {
        Series series = charts.getSeries();
        if(series != null && CollectionUtils.isNotEmpty(series.getData()))
        {
            return getInfoLabels(charts, series.getData().size() - 1);
        }
        LOG.warn("Could not find series values");
        return Collections.emptyList();
    }


    public Collection<ChartInfoLabel> getInfoLabels(Charts charts, int pointIndex)
    {
        return (Collection<ChartInfoLabel>)IntStream.range(0, charts.getSeriesSize()).mapToObj(i -> {
            Series series = charts.getSeries(i);
            return new ChartInfoLabel(series.getName(), Objects.toString(series.getPoint(pointIndex).getY(), "-"));
        }).collect(Collectors.toList());
    }


    public boolean isExportButtonEnabled(WidgetInstanceManager wim)
    {
        return wim.getWidgetSettings().getBoolean("exportEnabled");
    }


    public void applyTitleStyles(Title title, WidgetInstanceManager wim)
    {
        adjustTitleAlignment(title, wim);
        adjustTitleXYCoordinates(title, wim);
        String titleStyle = getTitleStyle(wim);
        if(StringUtils.isNotBlank(titleStyle))
        {
            title.setStyle(titleStyle);
        }
    }


    protected void adjustSpacing(WidgetInstanceManager wim, Charts charts)
    {
        charts.setSpacingTop(Integer.valueOf(wim.getWidgetSettings().getInt("spacingTop")));
        charts.setSpacingBottom(Integer.valueOf(wim.getWidgetSettings().getInt("spacingBottom")));
        charts.setSpacingLeft(Integer.valueOf(wim.getWidgetSettings().getInt("spacingLeft")));
        charts.setSpacingRight(Integer.valueOf(wim.getWidgetSettings().getInt("spacingRight")));
    }


    protected void adjustTitleAlignment(Title title, WidgetInstanceManager wim)
    {
        String horizontalAlignment = wim.getWidgetSettings().getString("titleHorizontalAlignment");
        if(StringUtils.isNotBlank(horizontalAlignment))
        {
            title.setAlign(horizontalAlignment);
        }
        String verticalAlignment = wim.getWidgetSettings().getString("titleVerticalAlignment");
        if(StringUtils.isNotBlank(verticalAlignment))
        {
            title.setVerticalAlign(verticalAlignment);
        }
    }


    protected void adjustTitleXYCoordinates(Title title, WidgetInstanceManager wim)
    {
        Number x = Integer.valueOf(wim.getWidgetSettings().getInt("titleAxisX"));
        Number y = Integer.valueOf(wim.getWidgetSettings().getInt("titleAxisY"));
        title.setY(x);
        title.setY(y);
    }


    public void applyDefaultPlotOptions(PlotOptions options)
    {
        AreaPlotOptions plotOptions = options.getArea();
        plotOptions.getMarker().setRadius(Integer.valueOf(2));
        plotOptions.setLineWidth(Integer.valueOf(1));
        plotOptions.setShadow(false);
        plotOptions.getStates().getHover().setLineWidth(Integer.valueOf(1));
        plotOptions.setThreshold(null);
    }


    public Number getYAxisMax(WidgetInstanceManager wim)
    {
        return parseNumber(wim, "yMax");
    }


    public Number getXAxisMax(WidgetInstanceManager wim)
    {
        return parseNumber(wim, "xMax");
    }


    public Number getXAxisMin(WidgetInstanceManager wim)
    {
        return parseNumber(wim, "xMin");
    }


    public Number getYAxisMin(WidgetInstanceManager wim)
    {
        return parseNumber(wim, "yMin");
    }


    public String getYAxisType(WidgetInstanceManager wim)
    {
        return wim.getWidgetSettings().getString("yAxisType");
    }


    public String getYAxisLabel(WidgetInstanceManager wim)
    {
        return getLocalizedValue(wim, "yAxisLabel", "");
    }


    public String getXAxisLabel(WidgetInstanceManager wim)
    {
        return getLocalizedValue(wim, "xAxisLabel", "");
    }


    public String getXAxisType(WidgetInstanceManager wim)
    {
        return wim.getWidgetSettings().getString("xAxisType");
    }


    public String getChartTitle(WidgetInstanceManager wim)
    {
        return getLocalizedValue(wim, "chartTitle", "");
    }


    public String getTitleStyle(WidgetInstanceManager wim)
    {
        return wim.getWidgetSettings().getString("titleStyle");
    }


    public String getZoomType(WidgetInstanceManager wim)
    {
        return wim.getWidgetSettings().getString("zoomType");
    }


    public String getPlotType(WidgetInstanceManager wim)
    {
        return wim.getWidgetSettings().getString("plotType");
    }


    public Collection<String> getSeriesNames(WidgetInstanceManager wim)
    {
        String series = wim.getWidgetSettings().getString("series");
        if(StringUtils.isNotBlank(series))
        {
            return Arrays.asList(series.split(","));
        }
        return Collections.emptyList();
    }


    public String getChartType(WidgetInstanceManager wim)
    {
        return wim.getWidgetSettings().getString("chartType");
    }


    public boolean isRoundValuesToInt(WidgetInstanceManager wim)
    {
        return wim.getWidgetSettings().getBoolean("roundValuesToInt");
    }


    protected Number parseNumber(WidgetInstanceManager wim, String key)
    {
        Validate.notNull("Widget instance manager may not be null", new Object[] {wim});
        Object obj = wim.getWidgetSettings().get(key);
        if(obj instanceof Number)
        {
            return (Number)obj;
        }
        try
        {
            if(obj instanceof String && StringUtils.isNotBlank((String)obj))
            {
                return Float.valueOf((String)obj);
            }
        }
        catch(NumberFormatException nfe)
        {
            LOG.warn(nfe.getMessage(), nfe);
        }
        return null;
    }


    protected String getLocalizedValue(WidgetInstanceManager wim, String key, String defaultValue)
    {
        if(wim != null)
        {
            String l10n_key = wim.getWidgetSettings().getString(key);
            if(StringUtils.isNotBlank(l10n_key))
            {
                return StringUtils.defaultIfBlank(wim.getLabel(l10n_key), "[" + l10n_key + "]");
            }
        }
        return defaultValue;
    }


    public StatisticsService getStatisticsMetadataService()
    {
        return this.statisticsMetadataService;
    }


    @Required
    public void setStatisticsMetadataService(StatisticsService statisticsMetadataService)
    {
        this.statisticsMetadataService = statisticsMetadataService;
    }


    public StatisticsData getStatisticsDataService()
    {
        return this.statisticsDataService;
    }


    @Required
    public void setStatisticsDataService(StatisticsData statisticsDataService)
    {
        this.statisticsDataService = statisticsDataService;
    }


    public MonitoringFacade getMonitoringFacade()
    {
        return this.monitoringFacade;
    }


    @Required
    public void setMonitoringFacade(MonitoringFacade monitoringFacade)
    {
        this.monitoringFacade = monitoringFacade;
    }
}
