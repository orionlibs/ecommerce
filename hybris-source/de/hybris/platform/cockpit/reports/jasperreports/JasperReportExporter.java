package de.hybris.platform.cockpit.reports.jasperreports;

import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import java.io.ByteArrayOutputStream;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Deprecated
public class JasperReportExporter
{
    private JasperReportCompiler jasperReportCompiler;
    private JasperReportsCache jasperReportsCache;
    private JRAbstractExporter exporter;


    @Deprecated
    public byte[] export(JasperWidgetPreferencesModel configuration)
    {
        try
        {
            JasperReport report = this.jasperReportsCache.getCompiledReport(configuration);
            JasperPrint jasperPrint = this.jasperReportCompiler.fillReport(report, configuration);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            this.exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
            this.exporter.exportReport();
            return byteArrayOutputStream.toByteArray();
        }
        catch(JRException ex)
        {
            throw new RuntimeException(ex);
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }


    public void setJasperReportsCache(JasperReportsCache jasperReportsCache)
    {
        this.jasperReportsCache = jasperReportsCache;
    }


    public void setJasperReportCompiler(JasperReportCompiler jasperReportCompiler)
    {
        this.jasperReportCompiler = jasperReportCompiler;
    }


    public void setExporter(JRAbstractExporter exporter)
    {
        this.exporter = exporter;
    }
}
