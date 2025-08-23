package de.hybris.platform.cockpit.reports.jasperreports;

import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Deprecated
public interface JasperReportCompiler
{
    @Deprecated
    JasperReport compileReport(JasperWidgetPreferencesModel paramJasperWidgetPreferencesModel);


    @Deprecated
    JasperPrint fillReport(JasperReport paramJasperReport, JasperWidgetPreferencesModel paramJasperWidgetPreferencesModel);
}
