package de.hybris.platform.cockpit.reports.impl;

import de.hybris.platform.cockpit.reports.JasperReportCompileService;
import de.hybris.platform.cockpit.reports.exceptions.JasperReportCompileException;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import java.io.InputStream;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class DefaultJasperReportCompileService implements JasperReportCompileService
{
    private MediaService mediaService;


    public JasperReport compileReport(JasperMediaModel media) throws JasperReportCompileException
    {
        try
        {
            InputStream reportInputStream = this.mediaService.getStreamFromMedia((MediaModel)media);
            JasperDesign jDesign = JRXmlLoader.load(reportInputStream);
            JasperReport compiledReport = JasperCompileManager.compileReport(jDesign);
            return compiledReport;
        }
        catch(Throwable t)
        {
            throw new JasperReportCompileException(t);
        }
    }


    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
