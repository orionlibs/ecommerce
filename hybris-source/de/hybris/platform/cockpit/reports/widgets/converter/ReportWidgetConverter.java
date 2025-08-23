package de.hybris.platform.cockpit.reports.widgets.converter;

import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;

public interface ReportWidgetConverter
{
    WidgetConfig<Widget<WidgetModel, WidgetController>> createWidgetConfig(WidgetPreferencesModel paramWidgetPreferencesModel);
}
