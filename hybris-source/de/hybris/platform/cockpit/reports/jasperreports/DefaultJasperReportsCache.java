package de.hybris.platform.cockpit.reports.jasperreports;

import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.exceptions.JasperWidgetException;
import de.hybris.platform.cockpit.reports.model.CompiledJasperMediaModel;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.localization.Localization;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.image.AImage;

@Deprecated
public class DefaultJasperReportsCache implements JasperReportsCache
{
    private JasperReportCompiler jasperReportCompiler;
    private MediaService mediaService;
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
            try
            {
                JasperReport jasperReport = (JasperReport)JRLoader.loadObject(new ByteArrayInputStream(this.mediaService
                                .getDataFromMedia(reportMedia)));
                this.compiledReportsCache.put(widget, jasperReport);
                return jasperReport;
            }
            catch(JRException e)
            {
                throw new JasperWidgetException(e);
            }
        }
        JasperReport compiledReport = this.jasperReportCompiler.compileReport(widget);
        this.compiledReportsCache.put(widget, compiledReport);
        return compiledReport;
    }


    public void remove(WidgetPreferencesModel widget)
    {
        this.compiledReportsCache.remove(widget);
        this.imagesCache.remove(widget);
    }


    public AImage getFilled(JasperWidgetPreferencesModel widget) throws JRException, IOException
    {
        if(this.imagesCache.containsKey(widget))
        {
            return this.imagesCache.get(widget);
        }
        JasperReport compiledReport = getCompiledReport(widget);
        JasperPrint jasperPrint = this.jasperReportCompiler.fillReport(compiledReport, widget);
        BufferedImage pageImage = exportReportToBufferedImage(jasperPrint);
        AImage image = exportBufferedImageToAImage(pageImage);
        this.imagesCache.put(widget, image);
        return image;
    }


    public void update(WidgetPreferencesModel widget)
    {
        this.imagesCache.remove(widget);
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


    private BufferedImage createNoDataImage(JasperPrint jasperPrint)
    {
        BufferedImage pageImage = new BufferedImage(jasperPrint.getPageWidth(), 30, 1);
        Graphics2D graphics = pageImage.createGraphics();
        graphics.setBackground(Color.WHITE);
        graphics.clearRect(0, 0, pageImage.getWidth(), 30);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("SansSerif", 1, 12));
        graphics.drawString(Localization.getLocalizedString("cockpit.reports.nodata"), 0, 20);
        graphics.dispose();
        return pageImage;
    }


    private BufferedImage exportReportToBufferedImage(JasperPrint jasperPrint) throws JRException
    {
        BufferedImage pageImage = null;
        if(CollectionUtils.isNotEmpty(jasperPrint.getPages()))
        {
            pageImage = new BufferedImage(jasperPrint.getPageWidth(), jasperPrint.getPageHeight(), 1);
            JRGraphics2DExporter exporter = new JRGraphics2DExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter((JRExporterParameter)JRGraphics2DExporterParameter.GRAPHICS_2D, pageImage.getGraphics());
            exporter.setParameter(JRExporterParameter.PAGE_INDEX, Integer.valueOf(0));
            exporter.exportReport();
        }
        else
        {
            pageImage = createNoDataImage(jasperPrint);
        }
        return pageImage;
    }


    public void setJasperReportCompiler(JasperReportCompiler jasperReportCompiler)
    {
        this.jasperReportCompiler = jasperReportCompiler;
    }


    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
