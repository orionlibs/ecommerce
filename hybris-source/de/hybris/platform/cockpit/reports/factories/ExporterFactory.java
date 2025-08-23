package de.hybris.platform.cockpit.reports.factories;

import java.awt.Graphics;
import java.io.OutputStream;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JasperPrint;

public interface ExporterFactory
{
    JRAbstractExporter createExcelExporter(JasperPrint paramJasperPrint, OutputStream paramOutputStream);


    JRAbstractExporter createPdfExporter(JasperPrint paramJasperPrint, OutputStream paramOutputStream);


    JRAbstractExporter createImageExporter(JasperPrint paramJasperPrint, Graphics paramGraphics);
}
