package de.hybris.platform.cockpit.reports.impl;

import de.hybris.platform.cockpit.reports.JasperReportExportService;
import de.hybris.platform.cockpit.reports.exceptions.JasperReportExportException;
import de.hybris.platform.cockpit.reports.factories.ExporterFactory;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public class DefaultJasperReportExportService implements JasperReportExportService
{
    private ExporterFactory exporterFactory;


    public byte[] exportToExcel(JasperPrint filledReport) throws JasperReportExportException
    {
        try
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JRAbstractExporter exporter = this.exporterFactory.createExcelExporter(filledReport, byteArrayOutputStream);
            exporter.exportReport();
            return byteArrayOutputStream.toByteArray();
        }
        catch(JRException ex)
        {
            throw new JasperReportExportException(ex);
        }
    }


    public BufferedImage exportToImage(JasperPrint filledReport) throws JasperReportExportException
    {
        try
        {
            BufferedImage pageImage = new BufferedImage(filledReport.getPageWidth(), filledReport.getPageHeight(), 1);
            JRAbstractExporter exporter = this.exporterFactory.createImageExporter(filledReport, pageImage.getGraphics());
            exporter.exportReport();
            return pageImage;
        }
        catch(JRException ex)
        {
            throw new JasperReportExportException(ex);
        }
    }


    public byte[] exportToPdf(JasperPrint filledReport) throws JasperReportExportException
    {
        try
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JRAbstractExporter exporter = this.exporterFactory.createPdfExporter(filledReport, byteArrayOutputStream);
            exporter.exportReport();
            return byteArrayOutputStream.toByteArray();
        }
        catch(JRException ex)
        {
            throw new JasperReportExportException(ex);
        }
    }


    public void setExporterFactory(ExporterFactory exporterFactory)
    {
        this.exporterFactory = exporterFactory;
    }
}
