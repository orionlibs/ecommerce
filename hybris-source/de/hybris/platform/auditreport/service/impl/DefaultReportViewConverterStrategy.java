package de.hybris.platform.auditreport.service.impl;

import de.hybris.platform.audit.view.impl.ReportView;
import de.hybris.platform.auditreport.model.AuditReportTemplateModel;
import de.hybris.platform.auditreport.service.ReportConversionData;
import de.hybris.platform.auditreport.service.ReportGenerationException;
import de.hybris.platform.auditreport.service.ReportViewConverterStrategy;
import de.hybris.platform.auditreport.service.impl.io.TempFileInputStream;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.directive.Directive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultReportViewConverterStrategy extends AbstractTemplateViewConverterStrategy
{
    public static final String VELOCITY_CTX_REPORTS = "reports";
    public static final String VELOCITY_CTX_REPORTS_TEXT = "reportsText";
    public static final String VELOCITY_CTX_GENERATED_TIMESTAMP = "generatedTimestamp";
    public static final String VELOCITY_CTX_GENERATED_BY = "generatedBy";
    public static final String VELOCITY_CTX_GENERATED_FOR = "generatedFor";
    public static final String VELOCITY_CTX_REPORT_NAME = "reportName";
    public static final String VELOCITY_CTX_CONFIG_NAME = "configName";
    public static final String DEFAULT_FILE_NAME = "report.html";
    public static final String TEMP_FILE_EXTENSION = ".tmp";
    public static final String TEMP_FILE_PREFIX = "_vt_rep_";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultReportViewConverterStrategy.class);
    private String reportName;
    private ReportViewConverterStrategy textConverterStrategy;
    private RendererService rendererService;
    private SessionService sessionService;
    private I18NService i18NService;
    private Collection<Directive> customDirectives;


    @Deprecated(since = "1811", forRemoval = true)
    protected List<ReportConversionData> convert(List<ReportView> reports, RendererTemplateModel template, Map<String, Object> context)
    {
        return convert(reports.stream(), context);
    }


    public List<ReportConversionData> convert(Stream<ReportView> reports, RendererTemplateModel template, Map<String, Object> context)
    {
        AuditReportTemplateModel reportTemplate = (AuditReportTemplateModel)template;
        Map<String, Object> velocityContext = createConversionContext(context);
        if(BooleanUtils.isTrue(reportTemplate.getIncludeText()))
        {
            List<ReportConversionData> textData = getTextConverterStrategy().convert(reports, context);
            if(!textData.isEmpty() && textData.get(0) != null)
            {
                velocityContext.put("reportsText", ((ReportConversionData)textData.get(0)).getStream());
            }
        }
        return Collections.singletonList(convertUsingTemplate((RendererTemplateModel)reportTemplate, velocityContext));
    }


    protected Map<String, Object> createConversionContext(Map<String, Object> context)
    {
        Map<String, Object> additionalContext = prepareAdditionalContext(context);
        return new HashMap<>(additionalContext);
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected Map<String, Object> createConversionContext(List<ReportView> reports, Map<String, Object> context)
    {
        Map<String, Object> res = createConversionContext(context);
        res.put("reports", reports);
        return res;
    }


    protected ReportConversionData convertUsingTemplate(RendererTemplateModel template, Map<String, Object> conversionContext)
    {
        try
        {
            File tempFile = File.createTempFile("_vt_rep_", ".tmp");
            tempFile.deleteOnExit();
            return convertUsingTemplate(tempFile, template, conversionContext);
        }
        catch(IOException e)
        {
            throw new ReportGenerationException("Report generation failed - could not create temp file", e);
        }
    }


    private ReportConversionData convertUsingTemplate(File tempTarget, RendererTemplateModel template, Map<String, Object> context)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempTarget)));
            try
            {
                getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, template, context, writer));
                String reportFileName = StringUtils.defaultString(getReportName(), "report.html");
                ReportConversionData reportConversionData = new ReportConversionData(reportFileName, (InputStream)new TempFileInputStream(tempTarget));
                writer.close();
                return reportConversionData;
            }
            catch(Throwable throwable)
            {
                try
                {
                    writer.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new ReportGenerationException("Report generation failed", e);
        }
    }


    protected void registerRequiredCustomDirectives()
    {
        RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
        if(runtimeServices instanceof RuntimeInstance)
        {
            Collection<Directive> directives = getCustomDirectives();
            if(directives != null)
            {
                RuntimeInstance runtimeInstance = (RuntimeInstance)runtimeServices;
                directives.stream().forEach(directive -> {
                    if(runtimeInstance.getDirective(directive.getName()) == null)
                    {
                        LOG.info(String.format("Registering additional velocity directive: %s", new Object[] {directive.getName()}));
                        runtimeInstance.addDirective(directive);
                    }
                });
            }
        }
        else
        {
            LOG.warn("Could not register custom directives on runtime service: {}", runtimeServices);
        }
    }


    protected Map<String, Object> prepareAdditionalContext(Map<String, Object> context)
    {
        Map<String, Object> additionalContext = new HashMap<>();
        additionalContext.put("generatedTimestamp", ZonedDateTime.now());
        if(context != null)
        {
            Object user = context.get("currentUser");
            if(user instanceof UserModel)
            {
                additionalContext.put("generatedBy", ((UserModel)user).getName());
                additionalContext.put("generatedByPK", ((UserModel)user).getPk().getLongValueAsString());
            }
            Object rootItem = context.get("rootItem");
            if(rootItem instanceof ItemModel)
            {
                if(rootItem instanceof UserModel)
                {
                    additionalContext.put("generatedFor", ((UserModel)rootItem).getName());
                }
                else
                {
                    additionalContext.put("generatedFor", rootItem.toString());
                }
                additionalContext.put("generatedForPK", ((ItemModel)rootItem).getPk().getLongValueAsString());
            }
            Object reportId = context.get("reportId");
            if(reportId instanceof String)
            {
                additionalContext.put("reportName", reportId);
            }
            Object configName = context.get("configName");
            if(reportId instanceof String)
            {
                additionalContext.put("configName", configName);
            }
        }
        return additionalContext;
    }


    public String getReportName()
    {
        return this.reportName;
    }


    public void setReportName(String reportName)
    {
        this.reportName = reportName;
    }


    protected ReportViewConverterStrategy getTextConverterStrategy()
    {
        return this.textConverterStrategy;
    }


    @Required
    public void setTextConverterStrategy(ReportViewConverterStrategy textConverterStrategy)
    {
        this.textConverterStrategy = textConverterStrategy;
    }


    protected RendererService getRendererService()
    {
        return this.rendererService;
    }


    @Required
    public void setRendererService(RendererService rendererService)
    {
        this.rendererService = rendererService;
    }


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }


    public Collection<Directive> getCustomDirectives()
    {
        return this.customDirectives;
    }


    public void setCustomDirectives(Collection<Directive> customDirectives)
    {
        this.customDirectives = customDirectives;
    }
}
