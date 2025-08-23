package de.hybris.platform.cockpit.reports.widgets.util;

import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.widgets.converter.ReportWidgetConverter;
import de.hybris.platform.cockpit.reports.widgets.factory.ReportWidgetFactory;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;
import de.hybris.platform.cockpit.widgets.util.WidgetMapProvider;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zkplus.spring.SpringUtil;

public class ReportWidgetMapProvider implements WidgetMapProvider
{
    private ReportWidgetFactory reportWidgetFactory;


    @Required
    public void setReportWidgetFactory(ReportWidgetFactory reportWidgetFactory)
    {
        this.reportWidgetFactory = reportWidgetFactory;
    }


    @Deprecated
    public ReportWidgetFactory getReportWidgetFactory()
    {
        return this.reportWidgetFactory;
    }


    public Map<String, WidgetConfig> getWidgetMap()
    {
        ReportWidgetConverter widgetConverter = (ReportWidgetConverter)SpringUtil.getBean("reportWidgetConverter");
        Map<String, WidgetConfig> widgetConfigs = new HashMap<>();
        for(WidgetPreferencesModel preference : this.reportWidgetFactory.getConfigurations())
        {
            WidgetConfig<Widget<WidgetModel, WidgetController>> widgetConfig = widgetConverter.createWidgetConfig(preference);
            widgetConfigs.put(String.valueOf(preference.getPk()), widgetConfig);
        }
        return widgetConfigs;
    }
}
