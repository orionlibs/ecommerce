package de.hybris.platform.cockpit.reports.widgets.factory;

import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.widgets.ReportWidget;
import java.util.Collection;

public interface ReportWidgetFactory
{
    ReportWidget getWidget(WidgetPreferencesModel paramWidgetPreferencesModel);


    Collection<WidgetPreferencesModel> getConfigurations();
}
