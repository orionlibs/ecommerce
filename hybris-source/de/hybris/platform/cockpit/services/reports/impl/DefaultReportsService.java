package de.hybris.platform.cockpit.services.reports.impl;

import de.hybris.platform.cockpit.daos.ReportsDAO;
import de.hybris.platform.cockpit.model.DynamicWidgetPreferencesModel;
import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.model.CompiledJasperMediaModel;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.services.reports.ReportsService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultReportsService extends AbstractServiceImpl implements ReportsService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultReportsService.class);
    private MediaService mediaService;
    private ReportsDAO reportsDAO;


    public JasperReport getMainReport(String title)
    {
        JasperMediaModel jasperMedia = getReportsDAO().getMainReport(title);
        return compileReport(jasperMedia);
    }


    public Map<String, Object> getMainReportParameters(String mainRepPreferencesTitle)
    {
        WidgetPreferencesModel widgetModel = getReportsDAO().getWidgetPreferences(mainRepPreferencesTitle);
        if(widgetModel instanceof DynamicWidgetPreferencesModel)
        {
            Collection<WidgetParameterModel> paramsModels = ((DynamicWidgetPreferencesModel)widgetModel).getParameters();
            Map<String, Object> params = new HashMap<>(paramsModels.size());
            for(WidgetParameterModel widgetParameterModel : paramsModels)
            {
                String name = widgetParameterModel.getName();
                Object value = widgetParameterModel.getValue();
                if(value instanceof JasperMediaModel)
                {
                    value = compileReport((JasperMediaModel)value);
                }
                params.put(name, value);
            }
            return params;
        }
        return Collections.EMPTY_MAP;
    }


    public JasperReport compileReport(JasperMediaModel jasperMedia)
    {
        JasperReport report = null;
        if(jasperMedia instanceof CompiledJasperMediaModel)
        {
            MediaModel reportMedia = ((CompiledJasperMediaModel)jasperMedia).getCompiledReport();
            if(reportMedia == null)
            {
                reportMedia = createCompiledCounterpart((CompiledJasperMediaModel)jasperMedia);
            }
            try
            {
                report = (JasperReport)JRLoader.loadObject(new ByteArrayInputStream(getMediaService().getDataFromMedia(reportMedia)));
            }
            catch(JRException e)
            {
                LOG.error("Cannot read compiled report: " + jasperMedia.getCode() + " (" + jasperMedia.getTitle() + ")", (Throwable)e);
            }
        }
        else
        {
            try
            {
                JasperDesign sectionsDesign = JRXmlLoader.load(new ByteArrayInputStream(getMediaService().getDataFromMedia((MediaModel)jasperMedia)));
                report = JasperCompileManager.compileReport(sectionsDesign);
            }
            catch(Throwable t)
            {
                LOG.error("Exception occurred while compiling report " + jasperMedia.getCode() + " (" + jasperMedia.getTitle() + ")", t);
            }
        }
        return report;
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public ReportsDAO getReportsDAO()
    {
        return this.reportsDAO;
    }


    @Required
    public void setReportsDAO(ReportsDAO reportsDAO)
    {
        this.reportsDAO = reportsDAO;
    }


    public MediaModel createCompiledCounterpart(CompiledJasperMediaModel xmlReportModel)
    {
        if(xmlReportModel.getCompiledReport() != null)
        {
            getModelService().remove(xmlReportModel.getCompiledReport());
            getModelService().saveAll();
        }
        MediaModel compiledReportMedia = (MediaModel)getModelService().create("Media");
        compiledReportMedia.setCode(xmlReportModel.getCode() + " (compiled) for " + xmlReportModel.getCode());
        compiledReportMedia.setFolder(xmlReportModel.getFolder());
        compiledReportMedia.setCatalogVersion(xmlReportModel.getCatalogVersion());
        getModelService().save(compiledReportMedia);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            JasperCompileManager.compileReportToStream(new ByteArrayInputStream(getMediaService().getDataFromMedia((MediaModel)xmlReportModel)), out);
        }
        catch(Throwable t)
        {
            LOG.error("Exception occurred while compiling report " + xmlReportModel.getCode() + " (" + xmlReportModel.getTitle() + ")", t);
        }
        getMediaService().setDataForMedia(compiledReportMedia, out.toByteArray());
        xmlReportModel.setCompiledReport(compiledReportMedia);
        getModelService().saveAll();
        return compiledReportMedia;
    }


    public List<JasperMediaModel> findJasperMediasByMediaFolder(String folderName)
    {
        return this.reportsDAO.findJasperMediasByMediaFolder(folderName);
    }
}
