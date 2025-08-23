package de.hybris.platform.cockpit.reports;

import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public interface JasperReportFillService
{
    JasperPrint fillReport(JasperReport paramJasperReport, JasperWidgetPreferencesModel paramJasperWidgetPreferencesModel);
}
