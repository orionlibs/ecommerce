package com.hybris.pcmbackoffice.widgets.charts.facetchart;

import com.hybris.cockpitng.util.YTestTools;
import java.util.List;
import org.zkoss.chart.Point;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class FacetChartBottomPanelRenderer
{
    private static final String SCLASS_CHART_ONE_POINT_CONTAINER = "yw-solrfacetchart-bottompanel-onepoint";
    private static final String SCLASS_CHART_INFO_LABEL = "yw-solrfacetchart-bottompanel-onepoint-label";
    private static final String SCLASS_CHART_INFO_VALUE = "yw-solrfacetchart-bottompanel-onepoint-value";
    private static final String SCLASS_CHART_COLOR_LABEL = "yw-solrfacetchart-bottompanel-onepoint-color-label";


    public void render(Div parent, List<Point> points, EventListener<? super Event> onClickListener)
    {
        parent.getChildren().clear();
        points.forEach(p -> renderPoint(p, parent, onClickListener));
    }


    private static void renderPoint(Point pointData, Div parent, EventListener<? super Event> onClickListener)
    {
        Div onePointContainer = new Div();
        onePointContainer.setSclass("yw-solrfacetchart-bottompanel-onepoint");
        Label colorLabel = new Label();
        colorLabel.setSclass("yw-solrfacetchart-bottompanel-onepoint-color-label");
        if(pointData.getColor() != null)
        {
            onePointContainer.setStyle("color: " + pointData.getColor().stringValue());
        }
        Label infoLabel = new Label(pointData.getName());
        infoLabel.setSclass("yw-solrfacetchart-bottompanel-onepoint-label");
        Label valueLabel = new Label(pointData.getY().toString());
        valueLabel.setSclass("yw-solrfacetchart-bottompanel-onepoint-value");
        onePointContainer.getChildren().add(colorLabel);
        onePointContainer.getChildren().add(infoLabel);
        onePointContainer.getChildren().add(valueLabel);
        onePointContainer.setParent((Component)parent);
        YTestTools.modifyYTestId((Component)onePointContainer, String.format("solr-chart-data-point_%s", new Object[] {pointData.getName()}));
        onePointContainer.addEventListener("onClick", event -> {
            Event myEvent = new Event("onPointClick", (Component)onePointContainer, pointData);
            onClickListener.onEvent(myEvent);
        });
    }
}
