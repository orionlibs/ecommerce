package de.hybris.platform.auditreport.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportConversionData
{
    private static final Logger LOG = LoggerFactory.getLogger(ReportConversionData.class);
    private final String name;
    private final InputStream stream;


    @Deprecated(since = "1811", forRemoval = true)
    public ReportConversionData(String name, byte[] data)
    {
        this.name = name;
        this.stream = new ByteArrayInputStream(data);
    }


    public ReportConversionData(String name, InputStream stream)
    {
        this.name = name;
        this.stream = stream;
    }


    public String getName()
    {
        return this.name;
    }


    @Deprecated(since = "1811", forRemoval = true)
    public byte[] getData()
    {
        try
        {
            return IOUtils.toByteArray(this.stream);
        }
        catch(IOException ioException)
        {
            LOG.warn("Could not read stream", ioException);
            throw new IllegalStateException(ioException);
        }
    }


    public InputStream getStream()
    {
        return this.stream;
    }
}
