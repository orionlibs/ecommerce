package de.hybris.platform.auditreport.service.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.audit.view.impl.ReportView;
import de.hybris.platform.auditreport.service.ReportConversionData;
import de.hybris.platform.auditreport.service.ReportGenerationException;
import de.hybris.platform.auditreport.service.ReportViewConverterStrategy;
import de.hybris.platform.auditreport.service.impl.io.TempFileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonReportViewConverterStrategy implements ReportViewConverterStrategy
{
    public static final String DEFAULT_FILE_NAME = "report.json";
    private static final Logger LOG = LoggerFactory.getLogger(JsonReportViewConverterStrategy.class);
    private String reportName;


    public List<ReportConversionData> convert(List<ReportView> reports, Map<String, Object> context)
    {
        return convert(reports.stream(), context);
    }


    public List<ReportConversionData> convert(Stream<ReportView> reports, Map<String, Object> context)
    {
        try
        {
            InputStream dataInput = convertToJson(reports, context);
            String reportFileName = StringUtils.defaultString(getReportName(), "report.json");
            return Collections.singletonList(new ReportConversionData(reportFileName, dataInput));
        }
        catch(IOException e)
        {
            LOG.error("Could not convert report to JSON format", e);
            throw new ReportGenerationException(e);
        }
    }


    @Deprecated(since = "1811", forRemoval = true)
    protected byte[] convertToJson(List<ReportView> reports, Map<String, Object> context) throws IOException
    {
        return IOUtils.toByteArray(convertToJson(reports.stream(), context));
    }


    protected InputStream convertToJson(Stream<ReportView> reports, Map<String, Object> context) throws IOException
    {
        File tempFile = null;
        try
        {
            tempFile = File.createTempFile("_tml_json_", ".json");
            tempFile.deleteOnExit();
            JsonFactory factory = new JsonFactory();
            JsonGenerator generator = factory.createJsonGenerator(new FileOutputStream(tempFile));
            try
            {
                ObjectMapper mapper = new ObjectMapper();
                generator.setPrettyPrinter((PrettyPrinter)new DefaultPrettyPrinter());
                mapper.writerWithDefaultPrettyPrinter();
                generator.setCodec((ObjectCodec)mapper);
                generator.writeStartArray();
                reports.forEach(report -> {
                    try
                    {
                        generator.writeObject(report);
                    }
                    catch(IOException ioe)
                    {
                        throw new ReportGenerationException(ioe);
                    }
                });
                generator.writeEndArray();
                if(generator != null)
                {
                    generator.close();
                }
            }
            catch(Throwable throwable)
            {
                if(generator != null)
                {
                    try
                    {
                        generator.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
            return (InputStream)new TempFileInputStream(tempFile);
        }
        catch(IOException | RuntimeException e)
        {
            try
            {
                if(tempFile != null && tempFile.exists())
                {
                    Files.delete(tempFile.toPath());
                }
            }
            catch(IOException innerExc)
            {
                LOG.warn("Unable to delete temp file", innerExc);
            }
            throw e;
        }
    }


    public String getReportName()
    {
        return this.reportName;
    }


    public void setReportName(String reportName)
    {
        this.reportName = reportName;
    }
}
