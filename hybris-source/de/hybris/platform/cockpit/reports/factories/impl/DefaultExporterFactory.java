package de.hybris.platform.cockpit.reports.factories.impl;

import de.hybris.platform.cockpit.reports.exceptions.JasperReportExportException;
import de.hybris.platform.cockpit.reports.factories.ExporterFactory;
import java.awt.Graphics;
import java.io.OutputStream;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

public class DefaultExporterFactory implements ExporterFactory
{
    public JRAbstractExporter createExcelExporter(JasperPrint jasperPrint, OutputStream outputstream)
    {
        JRXlsExporter jRXlsExporter = new JRXlsExporter();
        jRXlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        jRXlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputstream);
        jRXlsExporter.setParameter(JRExporterParameter.START_PAGE_INDEX, Integer.valueOf(0));
        return (JRAbstractExporter)jRXlsExporter;
    }


    public JRAbstractExporter createPdfExporter(JasperPrint jasperPrint, OutputStream outputstream)
    {
        JRPdfExporter jRPdfExporter = new JRPdfExporter();
        jRPdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        jRPdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputstream);
        jRPdfExporter.setParameter(JRExporterParameter.START_PAGE_INDEX, Integer.valueOf(0));
        return (JRAbstractExporter)jRPdfExporter;
    }


    public JRAbstractExporter createImageExporter(JasperPrint jasperPrint, Graphics graphics)
    {
        try
        {
            JRGraphics2DExporter jRGraphics2DExporter = new JRGraphics2DExporter();
            jRGraphics2DExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            jRGraphics2DExporter.setParameter((JRExporterParameter)JRGraphics2DExporterParameter.GRAPHICS_2D, graphics);
            jRGraphics2DExporter.setParameter(JRExporterParameter.PAGE_INDEX, Integer.valueOf(0));
            return (JRAbstractExporter)jRGraphics2DExporter;
        }
        catch(JRException ex)
        {
            throw new JasperReportExportException(ex);
        }
    }
}
