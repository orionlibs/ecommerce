package de.hybris.platform.auditreport.service;

import de.hybris.platform.audit.view.impl.ReportView;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ReportViewConverterStrategy
{
    @Deprecated(since = "1811", forRemoval = true)
    List<ReportConversionData> convert(List<ReportView> paramList, Map<String, Object> paramMap);


    default List<ReportConversionData> convert(Stream<ReportView> reports, Map<String, Object> context)
    {
        return convert(reports.collect((Collector)Collectors.toList()), context);
    }
}
