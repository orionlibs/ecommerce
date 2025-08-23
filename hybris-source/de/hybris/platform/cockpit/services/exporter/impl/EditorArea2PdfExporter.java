package de.hybris.platform.cockpit.services.exporter.impl;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.reports.ReportsService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

public class EditorArea2PdfExporter extends AbstractEditorAreaExporter
{
    private static final Logger LOG = LoggerFactory.getLogger(EditorArea2PdfExporter.class);
    private ReportsService reportsService;


    public byte[] export(List<EditorSectionConfiguration> ediorsSections, TypedObject curObj)
    {
        byte[] pdf = new byte[0];
        byte[] xml = generateXml(ediorsSections, curObj);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(new String(xml));
        }
        InputStream data = new ByteArrayInputStream(xml);
        try
        {
            JasperReport mainReport = getReportsService().getMainReport(getPreferencesTitle());
            Map<String, Object> params = getReportsService().getMainReportParameters(getPreferencesTitle());
            String mainTitle = (String)params.get("MAIN_REPORT_TITLE");
            params.put("MAIN_REPORT_TITLE", Labels.getLabel(mainTitle));
            String mainReportDSExp = (String)params.get(getDataSourceExpParamName());
            JasperPrint mainPrint = JasperFillManager.fillReport(mainReport, params, (JRDataSource)new JRXmlDataSource(data, mainReportDSExp));
            pdf = JasperExportManager.exportReportToPdf(mainPrint);
        }
        catch(JRException e)
        {
            LOG.error("Exception occurred while filling/exporting report for " + GeneratedCockpitConstants.TC.WIDGETPREFERENCES + " object with title: " +
                            getPreferencesTitle(), (Throwable)e);
        }
        return pdf;
    }


    public ReportsService getReportsService()
    {
        return this.reportsService;
    }


    @Required
    public void setReportsService(ReportsService reportsService)
    {
        this.reportsService = reportsService;
    }


    public String getExportContentType()
    {
        return "application/pdf";
    }
}
