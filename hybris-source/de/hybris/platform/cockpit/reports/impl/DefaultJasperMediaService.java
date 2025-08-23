package de.hybris.platform.cockpit.reports.impl;

import de.hybris.platform.cockpit.enums.RefreshTimeOption;
import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.reports.JasperMediaService;
import de.hybris.platform.cockpit.reports.exceptions.JasperReportLoadException;
import de.hybris.platform.cockpit.reports.model.CompiledJasperMediaModel;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.widgets.factory.JasperReportParameterFactory;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class DefaultJasperMediaService implements JasperMediaService
{
    public static final String JASPER_REPORTS_MEDIA_FOLDER = "jasperreports";
    private ModelService modelService;
    private MediaService mediaService;
    private TypeService typeService;
    private CommonI18NService commonI18nService;
    private JasperReportParameterFactory jasperReportParameterFactory;


    public MediaFolderModel getJasperReportsMediaFolder()
    {
        return this.mediaService.getFolder("jasperreports");
    }


    public JasperMediaModel createJasperMedia(MediaFolderModel folder, String code)
    {
        JasperMediaModel model = (JasperMediaModel)this.modelService.create(JasperMediaModel.class);
        model.setCode(code);
        model.setFolder(folder);
        return model;
    }


    public CompiledJasperMediaModel setCompiledCounterpartForMedia(CompiledJasperMediaModel jasperMedia) throws JRException
    {
        if(jasperMedia.getCompiledReport() == null)
        {
            jasperMedia.setCompiledReport((MediaModel)createJasperMedia(jasperMedia.getFolder(), jasperMedia.getCode() + " (compiled)"));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.mediaService.setFolderForMedia(jasperMedia.getCompiledReport(), jasperMedia.getFolder());
        try
        {
            JasperCompileManager.compileReportToStream(this.mediaService.getStreamFromMedia((MediaModel)jasperMedia), out);
        }
        catch(Throwable t)
        {
            throw new JRException("Failed to compile report", t);
        }
        this.mediaService.setDataForMedia(jasperMedia.getCompiledReport(), out.toByteArray());
        return jasperMedia;
    }


    public JasperReport getReportFromMedia(MediaModel media) throws JasperReportLoadException
    {
        try
        {
            return (JasperReport)JRLoader.loadObject(this.mediaService.getStreamFromMedia(media));
        }
        catch(JRException ex)
        {
            throw new JasperReportLoadException(ex);
        }
    }


    public JasperMediaModel getJasperMediaForCode(String code)
    {
        return (JasperMediaModel)this.mediaService.getMedia(code);
    }


    public JasperWidgetPreferencesModel createJasperWidgetPreferencesFromMedia(JasperReport compiledReport, JasperMediaModel media, UserModel user)
    {
        JasperWidgetPreferencesModel jasperWidgetPreferencesModel = (JasperWidgetPreferencesModel)this.modelService.create(JasperWidgetPreferencesModel.class);
        jasperWidgetPreferencesModel.setOwnerUser(user);
        jasperWidgetPreferencesModel.setReport(media);
        saveParametersWithReport(jasperWidgetPreferencesModel, media, compiledReport);
        return jasperWidgetPreferencesModel;
    }


    private void saveParametersWithReport(JasperWidgetPreferencesModel jasperWidgetPreferences, JasperMediaModel jasperMedia, JasperReport compiledReport)
    {
        JRParameter[] parameters = compiledReport.getParameters();
        Collection<WidgetParameterModel> preferencesParameters = new ArrayList<>();
        for(JRParameter param : parameters)
        {
            if(param.isForPrompting() && !param.isSystemDefined())
            {
                WidgetParameterModel widgetParameterModel = this.jasperReportParameterFactory.createParameter(param);
                if(param.getDefaultValueExpression() != null)
                {
                    widgetParameterModel.setDefaultValueExpression(param.getDefaultValueExpression().getText());
                }
                preferencesParameters.add(widgetParameterModel);
            }
        }
        WidgetParameterModel newParameter = createRefreshParameter();
        preferencesParameters.add(newParameter);
        jasperWidgetPreferences.setParameters(preferencesParameters);
        for(LanguageModel lang : this.commonI18nService.getAllLanguages())
        {
            Locale locale = new Locale(lang.getIsocode());
            jasperWidgetPreferences.setTitle(jasperMedia.getTitle(locale), locale);
        }
    }


    protected WidgetParameterModel createRefreshParameter()
    {
        WidgetParameterModel parameter = (WidgetParameterModel)this.modelService.create(WidgetParameterModel.class);
        parameter.setName("Refresh");
        parameter.setType(this.typeService.getTypeForCode(RefreshTimeOption.class.getSimpleName()));
        parameter.setValue(RefreshTimeOption.NEVER);
        parameter.setDefaultValueExpression("NEVER");
        return parameter;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setCommonI18nService(CommonI18NService commonI18nService)
    {
        this.commonI18nService = commonI18nService;
    }


    public void setJasperReportParameterFactory(JasperReportParameterFactory jasperReportParameterFactory)
    {
        this.jasperReportParameterFactory = jasperReportParameterFactory;
    }
}
