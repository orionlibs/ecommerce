package de.hybris.platform.cockpit.reports.widgets;

import org.zkoss.zul.api.Div;

public interface ReportWidget<C extends de.hybris.platform.cockpit.model.WidgetPreferencesModel>
{
    void initialize(C paramC, Div paramDiv);
}
