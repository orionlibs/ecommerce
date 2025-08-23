package de.hybris.platform.auditreport.service.impl;

import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.audit.view.impl.ReportView;
import de.hybris.platform.auditreport.model.AuditReportDataModel;
import de.hybris.platform.auditreport.model.AuditReportTemplateModel;
import de.hybris.platform.auditreport.service.AuditReportDataService;
import de.hybris.platform.auditreport.service.CreateAuditReportParams;
import de.hybris.platform.auditreport.service.ReportConversionData;
import de.hybris.platform.auditreport.service.ReportGenerationException;
import de.hybris.platform.auditreport.service.ReportViewConverterStrategy;
import de.hybris.platform.auditreport.service.impl.io.TempFileInputStream;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAuditReportDataService implements AuditReportDataService
{
    public static final String DEFAULT_FILE_NAME = "report.zip";
    protected static final String ZIP_MIME_TYPE = "application/zip";
    protected static final String ZIP_EXTENSION = ".zip";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuditReportDataService.class);
    private String reportFileName;
    private AuditViewService auditViewService;
    private MediaService mediaService;
    private ModelService modelService;
    private UserService userService;
    private List<ReportViewConverterStrategy> reportViewConverterStrategies;
    private String reportFolderName;
    private FlexibleSearchService flexibleSearchService;


    public AuditReportDataModel createReport(CreateAuditReportParams params)
    {
        ItemModel rootItem = params.getRootItem();
        LOG.debug("Audit report generation started for :{}", rootItem);
        String reportId = params.getReportId();
        String configName = params.getConfigName();
        Stream<ReportView> reportsViews = createReportsViewsStream(rootItem, configName, params.isAudit(), params
                        .getIncludedLanguages());
        LOG.debug("Audit report views generated for :{}", rootItem);
        Map<String, Object> context = populateReportGenerationContext(rootItem, configName, reportId, params.getTemplate());
        Map<String, InputStream> files = evaluateStrategiesToStreams(reportsViews, context);
        LOG.debug("Audit reports generated for :{}", rootItem);
        try
        {
            InputStream content = compressContentStream(files);
            try
            {
                AuditReportDataModel auditReportData = saveReport(rootItem, reportId, configName, content);
                LOG.debug("Audit reports saved for :{}", rootItem);
                AuditReportDataModel auditReportDataModel1 = auditReportData;
                if(content != null)
                {
                    content.close();
                }
                return auditReportDataModel1;
            }
            catch(Throwable throwable)
            {
                if(content != null)
                {
                    try
                    {
                        content.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException ioe)
        {
            throw new ReportGenerationException("Could not generate zip with the report data", ioe);
        }
    }


    public void deleteReportsForItem(ItemModel item)
    {
        SearchResult<Object> searchResult = getFlexibleSearchService().search("select {pk} from {AuditReportData} where {auditRootItem} = " + item.getPk());
        if(searchResult.getTotalCount() > 0)
        {
            LOG.info("Removing {} Audit data reports", Integer.valueOf(searchResult.getTotalCount()));
            this.modelService.removeAll(searchResult.getResult());
        }
    }


    protected Map<String, Object> populateReportGenerationContext(ItemModel rootItem, String configName, String reportId, AuditReportTemplateModel template)
    {
        Map<String, Object> context = new HashMap<>();
        context.put("rootItem", rootItem);
        context.put("configName", configName);
        context.put("reportId", reportId);
        context.put("currentUser", getUserService().getCurrentUser());
        context.put("template", template);
        return context;
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected Map<String, byte[]> evaluateStrategies(List<ReportView> reports, Map<String, Object> context)
    {
        return evaluateStrategies(reports.stream(), context);
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected Map<String, byte[]> evaluateStrategies(Stream<ReportView> reports, Map<String, Object> context)
    {
        Map<String, InputStream> streams = evaluateStrategiesToStreams(reports, context);
        Map<String, byte[]> bytes = (Map)new LinkedHashMap<>();
        for(Map.Entry<String, InputStream> entry : streams.entrySet())
        {
            try
            {
                bytes.put(entry.getKey(), IOUtils.toByteArray(entry.getValue()));
            }
            catch(IOException e)
            {
                throw new IllegalStateException(e);
            }
        }
        return bytes;
    }


    protected Map<String, InputStream> evaluateStrategiesToStreams(Stream<ReportView> reports, Map<String, Object> context)
    {
        Map<String, InputStream> files = new HashMap<>();
        for(ReportViewConverterStrategy strategy : getReportViewConverterStrategies())
        {
            Collection<ReportConversionData> conversionResult = strategy.convert(reports, context);
            if(CollectionUtils.isNotEmpty(conversionResult))
            {
                conversionResult.forEach(file -> {
                    InputStream previousFileData = files.put(file.getName(), file.getStream());
                    if(previousFileData != null)
                    {
                        LOG.warn(String.format("Another file uses the same name: \"%s\". The content of the previous file was discarded.", new Object[] {file.getName()}));
                    }
                });
            }
        }
        return files;
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected List<ReportView> createReportsViews(ItemModel item, String configName, boolean audit, Collection<LanguageModel> includedLanguages)
    {
        return createReportsViewsStream(item, configName, audit, includedLanguages).collect((Collector)Collectors.toList());
    }


    protected Stream<ReportView> createReportsViewsStream(ItemModel item, String configName, boolean audit, Collection<LanguageModel> includedLanguages)
    {
        TypeAuditReportConfig.Builder builder = TypeAuditReportConfig.builder().withRootTypePk(item.getPk()).withConfigName(configName);
        if(audit)
        {
            builder.withFullReport();
        }
        if(CollectionUtils.isNotEmpty(includedLanguages))
        {
            builder.withLangIsoCodes((String[])includedLanguages.stream().map(C2LItemModel::getIsocode).toArray(x$0 -> new String[x$0]));
        }
        TypeAuditReportConfig config = builder.build();
        return getAuditViewService().getViewOn(config);
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected byte[] compressContent(Map<String, byte[]> filesByName) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            ZipOutputStream zip = new ZipOutputStream(out);
            try
            {
                for(Map.Entry<String, byte[]> file : filesByName.entrySet())
                {
                    ZipEntry entry = new ZipEntry(file.getKey());
                    zip.putNextEntry(entry);
                    zip.write(file.getValue());
                    zip.closeEntry();
                }
                zip.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    zip.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            byte[] arrayOfByte = out.toByteArray();
            out.close();
            return arrayOfByte;
        }
        catch(Throwable throwable)
        {
            try
            {
                out.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
    }


    protected InputStream compressContentStream(Map<String, InputStream> filesByName) throws IOException
    {
        File tempFile = File.createTempFile("_audit_report", ".zip");
        tempFile.deleteOnExit();
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(tempFile));
        try
        {
            for(Map.Entry<String, InputStream> entry : filesByName.entrySet())
            {
                zip.putNextEntry(new ZipEntry(entry.getKey()));
                IOUtils.copy(entry.getValue(), zip);
                zip.closeEntry();
            }
            zip.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                zip.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
        return (InputStream)new TempFileInputStream(tempFile);
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected AuditReportDataModel saveReport(ItemModel rootItem, String reportId, String configName, byte[] content)
    {
        AuditReportDataModel report = (AuditReportDataModel)getModelService().create(AuditReportDataModel.class);
        SearchResult<Object> searchResult = getFlexibleSearchService().search("SELECT {pk} FROM {AuditReportConfig} where {code}='" + configName + "'");
        if(searchResult != null && searchResult.getCount() > 0)
        {
            report.setAuditReportConfig(searchResult.getResult().get(0));
        }
        report.setCode(reportId);
        report.setAuditRootItem(rootItem);
        MediaFolderModel auditReportsFolder = getMediaService().getFolder(getReportFolderName());
        report.setFolder(auditReportsFolder);
        getModelService().save(report);
        getMediaService().setStreamForMedia((MediaModel)report, new ByteArrayInputStream(content), createReportFileName(), "application/zip");
        return report;
    }


    protected AuditReportDataModel saveReport(ItemModel rootItem, String reportId, String configName, InputStream content)
    {
        AuditReportDataModel report = (AuditReportDataModel)getModelService().create(AuditReportDataModel.class);
        SearchResult<Object> searchResult = getFlexibleSearchService().search("SELECT {pk} FROM {AuditReportConfig} where {code}='" + configName + "'");
        if(searchResult != null && searchResult.getCount() > 0)
        {
            report.setAuditReportConfig(searchResult.getResult().get(0));
        }
        report.setCode(reportId);
        report.setAuditRootItem(rootItem);
        MediaFolderModel auditReportsFolder = getMediaService().getFolder(getReportFolderName());
        report.setFolder(auditReportsFolder);
        getModelService().save(report);
        getMediaService().setStreamForMedia((MediaModel)report, content, createReportFileName(), "application/zip");
        return report;
    }


    protected String createReportFileName()
    {
        String reportName = StringUtils.defaultString(getReportFileName(), "report.zip");
        if(!reportName.endsWith(".zip"))
        {
            return reportName + ".zip";
        }
        return reportName;
    }


    public String getReportFileName()
    {
        return this.reportFileName;
    }


    public void setReportFileName(String reportFileName)
    {
        this.reportFileName = reportFileName;
    }


    public AuditViewService getAuditViewService()
    {
        return this.auditViewService;
    }


    @Required
    public void setAuditViewService(AuditViewService auditViewService)
    {
        this.auditViewService = auditViewService;
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


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public List<ReportViewConverterStrategy> getReportViewConverterStrategies()
    {
        return this.reportViewConverterStrategies;
    }


    @Required
    public void setReportViewConverterStrategies(List<ReportViewConverterStrategy> reportViewConverterStrategies)
    {
        this.reportViewConverterStrategies = reportViewConverterStrategies;
    }


    public String getReportFolderName()
    {
        return this.reportFolderName;
    }


    @Required
    public void setReportFolderName(String reportFolderName)
    {
        this.reportFolderName = reportFolderName;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
