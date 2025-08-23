package de.hybris.platform.cockpit.reports.interceptor;

import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.widgets.factory.JasperReportParameterFactory;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.lang.StringUtils;

public class JasperWidgetPreferencesPreparer implements PrepareInterceptor
{
    private ModelService modelService;
    private MediaService mediaService;
    private JasperReportParameterFactory jasperReportParameterFactory;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof JasperWidgetPreferencesModel)
        {
            saveParametersWithReport((JasperWidgetPreferencesModel)model, ctx.isNew(model));
        }
    }


    private void saveParametersWithReport(JasperWidgetPreferencesModel jasperPreferences, boolean isNew) throws InterceptorException
    {
        Collection<WidgetParameterModel> preferencesParameters = new ArrayList<>();
        JRParameter[] parameters = getParametersFromReport(jasperPreferences);
        for(JRParameter param : parameters)
        {
            if(param.isForPrompting() && !param.isSystemDefined())
            {
                WidgetParameterModel parameter = null;
                if(!isNew)
                {
                    parameter = getParameterFromPreferences(jasperPreferences, param.getName());
                }
                if(parameter == null || !this.jasperReportParameterFactory.isSameMetaData(param, parameter))
                {
                    WidgetParameterModel newParameter = this.jasperReportParameterFactory.createParameter(param);
                    preferencesParameters.add(newParameter);
                    this.modelService.save(newParameter);
                }
                else
                {
                    preferencesParameters.add(parameter);
                }
            }
        }
        jasperPreferences.setParameters(preferencesParameters);
    }


    private JRParameter[] getParametersFromReport(JasperWidgetPreferencesModel jasperPreferences) throws InterceptorException
    {
        JasperReport compiledReport;
        InputStream reportInputStream = this.mediaService.getDataStreamFromMedia((MediaModel)jasperPreferences.getReport());
        try
        {
            compiledReport = JasperCompileManager.compileReport(reportInputStream);
        }
        catch(Throwable t)
        {
            throw new InterceptorException("Couldn't compile jasper report source definition", t);
        }
        return compiledReport.getParameters();
    }


    private WidgetParameterModel getParameterFromPreferences(JasperWidgetPreferencesModel jasperPreferences, String name)
    {
        WidgetParameterModel parameter = null;
        for(WidgetParameterModel param : jasperPreferences.getParameters())
        {
            if(StringUtils.equals(param.getName(), name))
            {
                parameter = param;
            }
        }
        return parameter;
    }


    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public void setJasperReportParameterFactory(JasperReportParameterFactory jasperReportParameterFactory)
    {
        this.jasperReportParameterFactory = jasperReportParameterFactory;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
