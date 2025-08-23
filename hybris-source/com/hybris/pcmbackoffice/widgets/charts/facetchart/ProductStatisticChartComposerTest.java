package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.chart.Tooltip;
import org.zkoss.chart.plotOptions.PiePlotOptions;
import org.zkoss.chart.plotOptions.PlotOptions;

@RunWith(MockitoJUnitRunner.class)
public class ProductStatisticChartComposerTest
{
    @InjectMocks
    @Spy
    private ProductStatisticChartComposer productStatisticChartComposer;


    @Before
    public void setUp()
    {
        CockpitTestUtil.mockZkEnvironment();
    }


    @Test
    public void composeSeries()
    {
        Point point1 = new Point("name", Integer.valueOf(1));
        point1.getDataLabels().setDistance(ProductStatisticChartComposer.CHART_DATA_LABEL_INSIDE_POSITION);
        Point point2 = new Point("name2", Integer.valueOf(100));
        point2.getDataLabels().setDistance(ProductStatisticChartComposer.CHART_DATA_LABEL_INSIDE_POSITION);
        List<Point> points = Arrays.asList(new Point[] {point1, point2});
        Series series = (Series)Mockito.mock(Series.class);
        Mockito.when(series.getData()).thenReturn(points);
        Mockito.when(series.getTooltip()).thenReturn(new Tooltip());
        this.productStatisticChartComposer.composeSeries(series);
        Assertions.assertThat(point1.getDataLabels().getDistance()).isEqualTo(ProductStatisticChartComposer.CHART_DATA_LABEL_OUTSIDE_POSITION);
        Assertions.assertThat(point1.getDataLabels().getColor().stringValue()).isEqualTo("black");
        Assertions.assertThat(point2.getDataLabels().getDistance()).isEqualTo(ProductStatisticChartComposer.CHART_DATA_LABEL_INSIDE_POSITION);
    }


    @Test
    public void shouldComposeWithoutData()
    {
        List<Point> points = null;
        Series series = (Series)Mockito.mock(Series.class);
        Mockito.when(series.getData()).thenReturn(points);
        Mockito.when(series.getTooltip()).thenReturn(Mockito.mock(Tooltip.class));
        this.productStatisticChartComposer.composeSeries(series);
        ((Tooltip)Mockito.verify(series.getTooltip())).setHeaderFormat((String)Matchers.any());
        ((Tooltip)Mockito.verify(series.getTooltip())).setPointFormat((String)Matchers.any());
    }


    @Test
    public void shouldNotAllowSelectingPoints()
    {
        Charts charts = (Charts)Mockito.spy(new Charts());
        PlotOptions plotOptions = (PlotOptions)Mockito.mock(PlotOptions.class);
        PiePlotOptions piePlotOptions = (PiePlotOptions)Mockito.spy(new PiePlotOptions());
        ((Charts)Mockito.doReturn(plotOptions).when(charts)).getPlotOptions();
        ((PlotOptions)Mockito.doReturn(piePlotOptions).when(plotOptions)).getPie();
        this.productStatisticChartComposer.composeChart(charts);
        ((PiePlotOptions)Mockito.verify(piePlotOptions)).setAllowPointSelect(false);
    }
}
