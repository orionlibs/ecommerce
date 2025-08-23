package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import java.util.List;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.chart.plotOptions.DataLabels;
import org.zkoss.chart.plotOptions.PiePlotOptions;
import org.zkoss.util.resource.Labels;

public class ProductStatisticChartComposer implements FacetChartComposer
{
    private static final Integer CHART_DATA_LABEL_PLACEMENT_BAUNDARY = Integer.valueOf(5);
    protected static final Integer CHART_DATA_LABEL_INSIDE_POSITION = Integer.valueOf(-42);
    protected static final Integer CHART_DATA_LABEL_OUTSIDE_POSITION = Integer.valueOf(30);


    public void composeChart(Charts charts)
    {
        charts.getChart().setType("pie");
        charts.getLegend().setEnabled(false);
        charts.getTooltip().setEnabled(false);
        PiePlotOptions plotOptions = charts.getPlotOptions().getPie();
        plotOptions.setAllowPointSelect(false);
        plotOptions.setCursor("pointer");
        plotOptions.setShadow(false);
        plotOptions.setInnerSize("50%");
        plotOptions.setShowInLegend(false);
        DataLabels dataLabels = plotOptions.getDataLabels();
        dataLabels.setFormat("{point.percentage:.1f} %");
        dataLabels.setDistance(CHART_DATA_LABEL_INSIDE_POSITION);
        dataLabels.setColor("white");
        charts.getXAxis().setType("category");
        charts.getXAxis().setTitle(getXAxisLabel());
        charts.getYAxis().setTitle(getYAxisLabel());
    }


    public void composeSeries(Series series)
    {
        series.getTooltip().setHeaderFormat("<span style=\"color:{point.color}\">●</span> {point.key}: <b>{point.y}</b>");
        series.getTooltip().setPointFormat("");
        List<Point> points = series.getData();
        if(points == null)
        {
            return;
        }
        Integer totalPointsValue = Integer.valueOf(points.stream()
                        .mapToInt(p -> p.getY().intValue())
                        .sum());
        points.stream()
                        .filter(p -> (p.getY().doubleValue() / totalPointsValue.intValue() * 100.0D < CHART_DATA_LABEL_PLACEMENT_BAUNDARY.intValue()))
                        .forEach(p -> {
                            p.getDataLabels().setDistance(CHART_DATA_LABEL_OUTSIDE_POSITION);
                            p.getDataLabels().setColor("black");
                        });
    }


    protected String getXAxisLabel()
    {
        return Labels.getLabel("solrchart.productstats.xaxis");
    }


    protected String getYAxisLabel()
    {
        return Labels.getLabel("solrchart.productstats.yaxis");
    }
}
