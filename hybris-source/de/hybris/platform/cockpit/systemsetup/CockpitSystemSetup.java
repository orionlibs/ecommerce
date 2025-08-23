package de.hybris.platform.cockpit.systemsetup;

import de.hybris.platform.cockpit.components.editorarea.export.ReportsConfiguration;
import de.hybris.platform.cockpit.model.DynamicWidgetPreferencesModel;
import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

@SystemSetup(extension = "cockpit")
public class CockpitSystemSetup
{
    private static final Logger LOG = LoggerFactory.getLogger(CockpitSystemSetup.class);
    private ModelService modelService;
    private MediaService mediaService;
    private I18NService i18nService;
    private FlexibleSearchService flexibleSearchService;
    private ReportsConfiguration reportsConfig;


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public void createEditorAreaJasperReports()
    {
        LOG.info("Import of jasper reports for editor area: Started...");
        String mainRepPreferencesTitle = this.reportsConfig.getMainReportPreferencesTitle();
        Map<String, String> namesMap = this.reportsConfig.getReportsFileNamesMap();
        for(Iterator<Map.Entry<String, String>> it = namesMap.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<String, String> entry = it.next();
            String reportName = entry.getKey();
            String fileName = entry.getValue();
            try
            {
                getMediaService().getMedia(reportName);
            }
            catch(UnknownIdentifierException uie)
            {
                importReport(reportName, fileName);
            }
        }
        JasperWidgetPreferencesModel mainRepPreferences = saveReportPreferences(mainRepPreferencesTitle);
        saveParameters(mainRepPreferences);
        saveAdditionalParameters(mainRepPreferences);
        LOG.info("Import of jasper reports for editor area: Finished.");
    }


    private JasperWidgetPreferencesModel saveReportPreferences(String mainRepPreferencesTitle)
    {
        List<Object> jasperWidgets = this.flexibleSearchService.search("select {pk} from {JasperWidgetPreferences} where {title} like '" + mainRepPreferencesTitle + "'").getResult();
        JasperWidgetPreferencesModel mainRepPreferences = null;
        if(CollectionUtils.isEmpty(jasperWidgets))
        {
            mainRepPreferences = (JasperWidgetPreferencesModel)getModelService().create("JasperWidgetPreferences");
            for(Locale loc : this.i18nService.getSupportedLocales())
            {
                mainRepPreferences.setTitle(mainRepPreferencesTitle, loc);
            }
            mainRepPreferences.setReport((JasperMediaModel)getMediaService().getMedia(this.reportsConfig.getMainReportName()));
            getModelService().save(mainRepPreferences);
        }
        else
        {
            mainRepPreferences = (JasperWidgetPreferencesModel)jasperWidgets.get(0);
        }
        return mainRepPreferences;
    }


    private void saveParameters(JasperWidgetPreferencesModel mainRepPreferences)
    {
        Map<String, Object> existingParameters = createExistingParametersMap(mainRepPreferences.getParameters());
        Map<String, String> paramsMap = this.reportsConfig.getReportsParamNamesMap();
        for(Iterator<Map.Entry<String, String>> it = paramsMap.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<String, String> entry = it.next();
            String reportName = entry.getKey();
            String paramName = entry.getValue();
            boolean paramExists = existingParameters.containsKey(paramName);
            Object object = existingParameters.get(paramName);
            WidgetParameterModel paramModel = null;
            if(paramExists)
            {
                if(object == null)
                {
                    List<Object> params = this.flexibleSearchService.search("select {pk} from {WidgetParameter} where {name} like '" + paramName + "'").getResult();
                    if(!CollectionUtils.isEmpty(params))
                    {
                        paramModel = (WidgetParameterModel)params.get(0);
                        paramModel.setValue(getMediaService().getMedia(reportName));
                        getModelService().save(paramModel);
                    }
                }
                continue;
            }
            paramModel = (WidgetParameterModel)getModelService().create("WidgetParameter");
            paramModel.setName(paramName);
            paramModel.setValue(getMediaService().getMedia(reportName));
            paramModel.setWidgetPreferences((DynamicWidgetPreferencesModel)mainRepPreferences);
            getModelService().save(paramModel);
        }
    }


    private Map<String, Object> createExistingParametersMap(Collection<WidgetParameterModel> parameters)
    {
        Map<String, Object> map = new HashMap<>();
        for(WidgetParameterModel widgetParameterModel : parameters)
        {
            map.put(widgetParameterModel.getName(), widgetParameterModel.getValue());
        }
        return map;
    }


    private void saveAdditionalParameters(JasperWidgetPreferencesModel mainRepPreferences)
    {
        Map<String, Object> existingParameters = createExistingParametersMap(mainRepPreferences.getParameters());
        if(!existingParameters.containsKey(this.reportsConfig.getMainReportDsExpParamName()))
        {
            WidgetParameterModel mainReportParamModel = (WidgetParameterModel)getModelService().create("WidgetParameter");
            mainReportParamModel.setName(this.reportsConfig.getMainReportDsExpParamName());
            mainReportParamModel.setValue(this.reportsConfig.getMainReportDsExp());
            mainReportParamModel.setWidgetPreferences((DynamicWidgetPreferencesModel)mainRepPreferences);
            getModelService().save(mainReportParamModel);
        }
        if(!existingParameters.containsKey(this.reportsConfig.getLocalizedReportDsExpParamName()))
        {
            WidgetParameterModel localizedReportParamModel = (WidgetParameterModel)getModelService().create("WidgetParameter");
            localizedReportParamModel.setName(this.reportsConfig.getLocalizedReportDsExpParamName());
            localizedReportParamModel.setValue(this.reportsConfig.getLocalizedReportDSExp());
            localizedReportParamModel.setWidgetPreferences((DynamicWidgetPreferencesModel)mainRepPreferences);
            getModelService().save(localizedReportParamModel);
        }
        if(!existingParameters.containsKey(this.reportsConfig.getCustomSectionDSExpParamName()))
        {
            WidgetParameterModel customSectionParamModel = (WidgetParameterModel)getModelService().create("WidgetParameter");
            customSectionParamModel.setName(this.reportsConfig.getCustomSectionDSExpParamName());
            customSectionParamModel.setValue(this.reportsConfig.getCustomSectionDSExp());
            customSectionParamModel.setWidgetPreferences((DynamicWidgetPreferencesModel)mainRepPreferences);
            getModelService().save(customSectionParamModel);
        }
        if(!existingParameters.containsKey(this.reportsConfig.getCustomRowDSExpParamName()))
        {
            WidgetParameterModel customRowParamModel = (WidgetParameterModel)getModelService().create("WidgetParameter");
            customRowParamModel.setName(this.reportsConfig.getCustomRowDSExpParamName());
            customRowParamModel.setValue(this.reportsConfig.getCustomRowDSExp());
            customRowParamModel.setWidgetPreferences((DynamicWidgetPreferencesModel)mainRepPreferences);
            getModelService().save(customRowParamModel);
        }
        if(!existingParameters.containsKey(this.reportsConfig.getMainTitleReportParamName()))
        {
            WidgetParameterModel mainReportTitleModel = (WidgetParameterModel)getModelService().create("WidgetParameter");
            mainReportTitleModel.setName(this.reportsConfig.getMainTitleReportParamName());
            mainReportTitleModel.setValue(this.reportsConfig.getMainTitleReport());
            mainReportTitleModel.setWidgetPreferences((DynamicWidgetPreferencesModel)mainRepPreferences);
            getModelService().save(mainReportTitleModel);
        }
    }


    private void importReport(String reportName, String fileName)
    {
        LOG.info("Saving report for editor area: " + fileName + " as jasper media :" + reportName);
        InputStream xmlReportIS = CockpitSystemSetup.class.getResourceAsStream(fileName);
        if(xmlReportIS == null)
        {
            LOG.error("Can't get file: " + fileName + "as stream. Report was not imported.");
        }
        else
        {
            try
            {
                MediaModel xmlReportMedia = null;
                try
                {
                    xmlReportMedia = getMediaService().getMedia(reportName);
                }
                catch(UnknownIdentifierException uie)
                {
                    xmlReportMedia = (MediaModel)getModelService().create("CompiledJasperMedia");
                    xmlReportMedia.setCode(reportName);
                    xmlReportMedia.setFolder(getReportsFolder());
                    getModelService().save(xmlReportMedia);
                }
                try
                {
                    getMediaService().setStreamForMedia(xmlReportMedia, new DataInputStream(xmlReportIS));
                }
                catch(Throwable t)
                {
                    LOG.error("Failed to import report: " + reportName, t);
                }
                getModelService().save(xmlReportMedia);
            }
            finally
            {
                try
                {
                    xmlReportIS.close();
                }
                catch(IOException ioe)
                {
                    LOG.error("Exception occurred while closing report file + " + fileName + "stream.", ioe);
                }
            }
        }
    }


    private MediaFolderModel getReportsFolder()
    {
        MediaFolderModel folder = null;
        try
        {
            folder = getMediaService().getRootFolder();
        }
        catch(ModelNotFoundException e)
        {
            LOG.error("Can't get root folder!", (Throwable)e);
        }
        return folder;
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setReportsConfig(ReportsConfiguration reportsConfig)
    {
        this.reportsConfig = reportsConfig;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
