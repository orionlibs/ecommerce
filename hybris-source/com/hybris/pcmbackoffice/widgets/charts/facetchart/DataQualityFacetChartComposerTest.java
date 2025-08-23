package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.zkoss.chart.Charts;
import org.zkoss.chart.plotOptions.ColumnPlotOptions;
import org.zkoss.chart.plotOptions.PlotOptions;

@RunWith(MockitoJUnitRunner.class)
public class DataQualityFacetChartComposerTest
{
    @InjectMocks
    @Spy
    private DataQualityFacetChartComposer dataQualityFacetChartComposer;


    @Before
    public void setUp()
    {
        CockpitTestUtil.mockZkEnvironment();
        Mockito.when(this.dataQualityFacetChartComposer.getXAxisLabel()).thenReturn("xLabel");
    }


    @Test
    public void composeChart()
    {
        Charts charts = new Charts();
        this.dataQualityFacetChartComposer.composeChart(charts);
        Assertions.assertThat(charts.getXAxis().getTitle().getText()).isEqualTo("xLabel");
    }


    @Test
    public void shouldNotAllowSelectingPoints()
    {
        Charts charts = (Charts)Mockito.spy(new Charts());
        PlotOptions plotOptions = (PlotOptions)Mockito.mock(PlotOptions.class);
        ColumnPlotOptions columnPlotOptions = (ColumnPlotOptions)Mockito.spy(new ColumnPlotOptions());
        ((Charts)Mockito.doReturn(plotOptions).when(charts)).getPlotOptions();
        ((PlotOptions)Mockito.doReturn(columnPlotOptions).when(plotOptions)).getColumn();
        this.dataQualityFacetChartComposer.composeChart(charts);
        ((ColumnPlotOptions)Mockito.verify(columnPlotOptions)).setAllowPointSelect(false);
    }
}
