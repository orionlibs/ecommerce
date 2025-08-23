package de.hybris.platform.cockpit.reports;

import de.hybris.platform.cockpit.reports.exceptions.JasperWidgetException;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import net.sf.jasperreports.engine.JasperReport;
import org.zkoss.image.AImage;

public interface JasperReportCacheService
{
    boolean remove(JasperWidgetPreferencesModel paramJasperWidgetPreferencesModel);


    AImage getImageForJasperWidgetPreferences(JasperWidgetPreferencesModel paramJasperWidgetPreferencesModel) throws JasperWidgetException;


    boolean update(JasperWidgetPreferencesModel paramJasperWidgetPreferencesModel);


    void invalidateAll();


    JasperReport getCompiledReport(JasperWidgetPreferencesModel paramJasperWidgetPreferencesModel);
}
