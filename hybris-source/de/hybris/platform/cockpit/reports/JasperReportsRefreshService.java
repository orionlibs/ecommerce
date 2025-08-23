package de.hybris.platform.cockpit.reports;

import de.hybris.platform.cockpit.enums.RefreshTimeOption;
import de.hybris.platform.cockpit.model.WidgetPreferencesModel;

public interface JasperReportsRefreshService
{
    void startRefreshing(WidgetPreferencesModel paramWidgetPreferencesModel, RefreshTimeOption paramRefreshTimeOption);


    void stopRefreshing(WidgetPreferencesModel paramWidgetPreferencesModel);


    boolean onRefresh();
}
