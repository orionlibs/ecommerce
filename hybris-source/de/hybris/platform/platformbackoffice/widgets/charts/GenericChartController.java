package de.hybris.platform.platformbackoffice.widgets.charts;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.platformbackoffice.charts.ChartHandler;
import de.hybris.platform.platformbackoffice.charts.ChartInfoLabel;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.chart.Charts;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class GenericChartController extends DefaultWidgetController
{
    public static final String REFRESH_INPUT_SOCKET = "refresh";
    public static final String YW_GENERIC_CHART_INFO_WRAPPER = "yw-generic-chart-info-wrapper";
    public static final String YW_GENERIC_CHART_INFO_VALUE = "yw-generic-chart-info-value";
    public static final String YW_GENERIC_CHART_INFO_LABEL = "yw-generic-chart-info-label";
    private static final Logger LOG = LoggerFactory.getLogger(GenericChartController.class);
    @Wire("charts")
    private Charts charts;
    @Wire
    private Div infoContainer;
    private transient ChartHandler chartHandler;


    public void initialize(Component comp)
    {
        super.initialize(comp);
        this.chartHandler = getChartHandler();
        if(this.chartHandler != null)
        {
            this.charts.setVisible(true);
            this.chartHandler.initializeChart(getWidgetInstanceManager(), this.charts);
            this.charts.addEventListener("onPlotMouseOverPoint", e -> renderInfoLabels(this.chartHandler.getInfoLabels(this.charts, e.getPointIndex())));
            this.charts.addEventListener("onPlotMouseOutPoint", e -> renderInfoLabels(this.chartHandler.getDefaultInfoLabels(this.charts)));
            this.charts.addEventListener("onRefreshChart", e -> this.chartHandler.applyModel(getWidgetInstanceManager(), this.charts));
            this.chartHandler.applyModel(getWidgetInstanceManager(), this.charts);
            renderInfoLabels(this.chartHandler.getDefaultInfoLabels(this.charts));
        }
        else
        {
            this.charts.setVisible(false);
            this.infoContainer.getChildren().clear();
            Label errorLabel = new Label(getLabel("handler.load.error.info"));
            errorLabel.setSclass("yw-generic-chart-info-error");
            this.infoContainer.appendChild((Component)errorLabel);
        }
    }


    private ChartHandler getChartHandler()
    {
        String beanId = StringUtils.defaultIfBlank(getWidgetSettings().getString("handlerBeanId"), "linearCollectorChartHandler");
        if(StringUtils.isNotBlank(beanId))
        {
            Object bean = SpringUtil.getBean(beanId);
            if(bean instanceof ChartHandler)
            {
                return (ChartHandler)bean;
            }
        }
        LOG.warn("Could not find chart handler with bean id: {}", beanId);
        return null;
    }


    protected void renderInfoLabels(Collection<ChartInfoLabel> infoLabels)
    {
        this.infoContainer.getChildren().clear();
        for(ChartInfoLabel info : infoLabels)
        {
            Div container = new Div();
            container.setSclass("yw-generic-chart-info-wrapper");
            Label value = new Label(info.getValue());
            value.setSclass("yw-generic-chart-info-value");
            container.appendChild((Component)value);
            Label label = new Label(info.getLabel());
            label.setSclass("yw-generic-chart-info-label");
            container.appendChild((Component)label);
            this.infoContainer.appendChild((Component)container);
        }
    }


    @SocketEvent(socketId = "refresh")
    public void refresh()
    {
        Events.echoEvent("onRefreshChart", (Component)this.charts, null);
    }


    protected Charts getCharts()
    {
        return this.charts;
    }


    protected Div getInfoContainer()
    {
        return this.infoContainer;
    }
}
