package de.hybris.platform.cockpit.reports;

import de.hybris.platform.cockpit.reports.exceptions.JasperReportCompileException;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import net.sf.jasperreports.engine.JasperReport;

public interface JasperReportCompileService
{
    JasperReport compileReport(JasperMediaModel paramJasperMediaModel) throws JasperReportCompileException;
}
