package de.hybris.platform.auditreport.service.impl;

import de.hybris.platform.audit.view.impl.ReportView;
import de.hybris.platform.auditreport.service.ReportConversionData;
import de.hybris.platform.auditreport.service.ReportGenerationException;
import de.hybris.platform.auditreport.service.ReportViewConverterStrategy;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractTemplateViewConverterStrategy implements ReportViewConverterStrategy
{
    public static final String CTX_CURRENT_USER = "currentUser";
    public static final String CTX_REPORT_ID = "reportId";
    public static final String CTX_CONFIG_NAME = "configName";
    public static final String CTX_ROOT_ITEM = "rootItem";
    public static final String CTX_TEMPLATE = "template";


    @Deprecated(since = "1811", forRemoval = true)
    public List<ReportConversionData> convert(List<ReportView> reports, Map<String, Object> context)
    {
        return convert(reports.stream(), context);
    }


    public List<ReportConversionData> convert(Stream<ReportView> reports, Map<String, Object> context)
    {
        RendererTemplateModel reportTemplate = (RendererTemplateModel)context.get("template");
        if(reportTemplate == null)
        {
            throw new ReportGenerationException("Missing template for report generation");
        }
        return convert(reports, reportTemplate, context);
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected abstract List<ReportConversionData> convert(List<ReportView> paramList, RendererTemplateModel paramRendererTemplateModel, Map<String, Object> paramMap);


    protected List<ReportConversionData> convert(Stream<ReportView> reports, RendererTemplateModel template, Map<String, Object> context)
    {
        return convert(reports.collect((Collector)Collectors.toList()), template, context);
    }
}
