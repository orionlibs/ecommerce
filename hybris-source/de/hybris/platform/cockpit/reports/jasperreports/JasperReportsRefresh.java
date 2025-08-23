package de.hybris.platform.cockpit.reports.jasperreports;

import de.hybris.platform.cockpit.enums.RefreshTimeOption;
import de.hybris.platform.cockpit.model.WidgetPreferencesModel;

@Deprecated
public interface JasperReportsRefresh
{
    @Deprecated
    void startRefreshing(WidgetPreferencesModel paramWidgetPreferencesModel, RefreshTimeOption paramRefreshTimeOption);


    @Deprecated
    void stopRefreshing(WidgetPreferencesModel paramWidgetPreferencesModel);


    @Deprecated
    boolean onRefresh();
}
