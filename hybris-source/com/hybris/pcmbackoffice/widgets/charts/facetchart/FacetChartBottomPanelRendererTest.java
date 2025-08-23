package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.zkoss.chart.Point;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;

@RunWith(MockitoJUnitRunner.class)
public class FacetChartBottomPanelRendererTest
{
    @InjectMocks
    private FacetChartBottomPanelRenderer facetChartBottomPanelRenderer;
    @Spy
    private Div container = new Div();
    @Mock
    private EventListener<? super Event> onClickListener;


    @Test
    public void shouldRenderAllPointsData()
    {
        List<Point> points = new ArrayList<>();
        points.add(new Point("p1", Integer.valueOf(4)));
        points.add(new Point("p2", Integer.valueOf(7)));
        this.facetChartBottomPanelRenderer.render(this.container, points, this.onClickListener);
        Assertions.assertThat(this.container.getChildren()).hasSize(points.size());
    }


    @Test
    public void shouldRenderAllPointsDataWithColor()
    {
        List<Point> points = new ArrayList<>();
        Point newPoint = new Point("p1", Integer.valueOf(4));
        newPoint.setColor("red");
        points.add(newPoint);
        this.facetChartBottomPanelRenderer.render(this.container, points, this.onClickListener);
        Assertions.assertThat(this.container.getChildren()).hasSize(points.size());
        Assertions.assertThat(CockpitTestUtil.find((Component)this.container, this::isRedDiv)).isPresent();
    }


    private boolean isRedDiv(Component component)
    {
        return (component instanceof Div && ((Div)component).getStyle() != null && ((Div)component)
                        .getStyle().contains("red"));
    }
}
