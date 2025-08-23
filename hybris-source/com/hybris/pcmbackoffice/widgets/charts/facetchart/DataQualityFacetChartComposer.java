package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import org.zkoss.chart.Charts;
import org.zkoss.chart.Series;
import org.zkoss.chart.plotOptions.ColumnPlotOptions;
import org.zkoss.chart.plotOptions.DataLabels;
import org.zkoss.util.resource.Labels;

public class DataQualityFacetChartComposer implements FacetChartComposer
{
    public void composeChart(Charts charts)
    {
        charts.getChart().setType("column");
        charts.getLegend().setEnabled(false);
        charts.getTooltip().setEnabled(false);
        ColumnPlotOptions plotOptions = charts.getPlotOptions().getColumn();
        plotOptions.setAllowPointSelect(false);
        plotOptions.setCursor("pointer");
        plotOptions.setColorByPoint(true);
        plotOptions.setShadow(true);
        DataLabels dataLabels = plotOptions.getDataLabels();
        dataLabels.setEnabled(true);
        dataLabels.setEnabled(true);
        dataLabels.setFormat("{point.value}");
        dataLabels.setColor("#000000");
        dataLabels.setAlign("center");
        dataLabels.setShadow(false);
        charts.getXAxis().setType("category");
        charts.getXAxis().setTitle(getXAxisLabel());
        charts.getYAxis().setTitle(getYAxisLabel());
    }


    public void composeSeries(Series series)
    {
        series.setType("column");
        series.getTooltip().setHeaderFormat("<span style=\"color:{point.color}\">●</span> {point.key}: <b>{point.y}</b><br/>");
        series.getTooltip().setPointFormat("{series.name}");
    }


    protected String getXAxisLabel()
    {
        return Labels.getLabel("solrchart.dataquality.xaxis");
    }


    protected String getYAxisLabel()
    {
        return Labels.getLabel("solrchart.dataquality.yaxis");
    }
}
