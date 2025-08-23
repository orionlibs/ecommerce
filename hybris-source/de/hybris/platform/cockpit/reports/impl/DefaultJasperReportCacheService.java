package de.hybris.platform.cockpit.reports.impl;

import de.hybris.platform.cockpit.reports.JasperMediaService;
import de.hybris.platform.cockpit.reports.JasperReportCacheService;
import de.hybris.platform.cockpit.reports.JasperReportCompileService;
import de.hybris.platform.cockpit.reports.JasperReportExportService;
import de.hybris.platform.cockpit.reports.JasperReportFillService;
import de.hybris.platform.cockpit.reports.exceptions.JasperWidgetException;
import de.hybris.platform.cockpit.reports.model.CompiledJasperMediaModel;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import de.hybris.platform.core.model.media.MediaModel;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.zkoss.image.AImage;

public class DefaultJasperReportCacheService implements JasperReportCacheService
{
    private JasperReportCompileService jasperReportCompileService;
    private JasperReportFillService jasperReportFillService;
    private JasperMediaService jasperMediaService;
    private JasperReportExportService jasperReportExportService;
    private final Map<JasperWidgetPreferencesModel, JasperReport> compiledReportsCache = new HashMap<>();
    private Map<JasperWidgetPreferencesModel, AImage> imagesCache = new HashMap<>();


    public JasperReport getCompiledReport(JasperWidgetPreferencesModel widget)
    {
        if(this.compiledReportsCache.containsKey(widget))
        {
            return this.compiledReportsCache.get(widget);
        }
        if(widget.getReport() instanceof CompiledJasperMediaModel)
        {
            MediaModel reportMedia = ((CompiledJasperMediaModel)widget.getReport()).getCompiledReport();
            JasperReport jasperReport = this.jasperMediaService.getReportFromMedia(reportMedia);
            this.compiledReportsCache.put(widget, jasperReport);
            return jasperReport;
        }
        JasperReport compiledReport = this.jasperReportCompileService.compileReport(widget.getReport());
        this.compiledReportsCache.put(widget, compiledReport);
        return compiledReport;
    }


    public boolean remove(JasperWidgetPreferencesModel widget)
    {
        return ((this.compiledReportsCache.remove(widget) != null)) | ((this.imagesCache.remove(widget) != null));
    }


    public AImage getImageForJasperWidgetPreferences(JasperWidgetPreferencesModel widget) throws JasperWidgetException
    {
        if(this.imagesCache.containsKey(widget))
        {
            return this.imagesCache.get(widget);
        }
        JasperReport compiledReport = getCompiledReport(widget);
        JasperPrint jasperPrint = this.jasperReportFillService.fillReport(compiledReport, widget);
        BufferedImage pageImage = this.jasperReportExportService.exportToImage(jasperPrint);
        try
        {
            AImage image = exportBufferedImageToAImage(pageImage);
            this.imagesCache.put(widget, image);
            return image;
        }
        catch(IOException ex)
        {
            throw new JasperWidgetException(ex);
        }
    }


    public boolean update(JasperWidgetPreferencesModel widget)
    {
        return (this.imagesCache.remove(widget) != null);
    }


    public void invalidateAll()
    {
        this.imagesCache = new HashMap<>();
    }


    private AImage exportBufferedImageToAImage(BufferedImage pageImage) throws IOException
    {
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        ImageIO.write(pageImage, "jpeg", out3);
        byte[] data = out3.toByteArray();
        out3.close();
        return new AImage("Report", data);
    }


    public void setJasperMediaService(JasperMediaService jasperMediaService)
    {
        this.jasperMediaService = jasperMediaService;
    }


    public void setJasperReportCompileService(JasperReportCompileService jasperReportCompileService)
    {
        this.jasperReportCompileService = jasperReportCompileService;
    }


    public void setJasperReportFillService(JasperReportFillService jasperReportFillService)
    {
        this.jasperReportFillService = jasperReportFillService;
    }


    public void setJasperReportExportService(JasperReportExportService jasperReportExportService)
    {
        this.jasperReportExportService = jasperReportExportService;
    }
}
