package de.hybris.platform.platformbackoffice.charts;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Collection;
import org.zkoss.chart.Charts;

public interface ChartHandler
{
    public static final String ON_REFRESH_CHART = "onRefreshChart";


    void initializeChart(WidgetInstanceManager paramWidgetInstanceManager, Charts paramCharts);


    void applyModel(WidgetInstanceManager paramWidgetInstanceManager, Charts paramCharts);


    Collection<ChartInfoLabel> getDefaultInfoLabels(Charts paramCharts);


    Collection<ChartInfoLabel> getInfoLabels(Charts paramCharts, int paramInt);
}
