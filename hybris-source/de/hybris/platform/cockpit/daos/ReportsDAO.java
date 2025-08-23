package de.hybris.platform.cockpit.daos;

import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.List;

public interface ReportsDAO extends Dao
{
    JasperMediaModel getMainReport(String paramString);


    WidgetPreferencesModel getWidgetPreferences(String paramString);


    List<JasperMediaModel> findJasperMediasByMediaFolder(String paramString);
}
