package de.hybris.platform.cockpit.reports;

import de.hybris.platform.cockpit.reports.exceptions.JasperReportExportException;
import java.awt.image.BufferedImage;
import net.sf.jasperreports.engine.JasperPrint;

public interface JasperReportExportService
{
    byte[] exportToExcel(JasperPrint paramJasperPrint) throws JasperReportExportException;


    byte[] exportToPdf(JasperPrint paramJasperPrint) throws JasperReportExportException;


    BufferedImage exportToImage(JasperPrint paramJasperPrint) throws JasperReportExportException;
}
