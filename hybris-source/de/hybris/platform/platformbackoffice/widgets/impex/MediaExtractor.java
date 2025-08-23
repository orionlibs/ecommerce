package de.hybris.platform.platformbackoffice.widgets.impex;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.apache.commons.io.input.CountingInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;

public class MediaExtractor
{
    private static final Logger LOG = LoggerFactory.getLogger(MediaExtractor.class);
    private static final int MAX_BINARY_BYTE_SIZE = 8192;
    private static final int SIZE_CALCULATION_BUFFER = 1048576;
    private final Media uploadedMedia;


    public MediaExtractor(Media uploadedMedia)
    {
        this.uploadedMedia = uploadedMedia;
    }


    public String getDataAsString()
    {
        if(this.uploadedMedia.isBinary())
        {
            BoundedInputStream boundedInputStream = new BoundedInputStream(this.uploadedMedia.getStreamData(), 8192L);
            try
            {
                return IOUtils.toString((InputStream)boundedInputStream, StandardCharsets.UTF_8);
            }
            catch(IOException e)
            {
                LOG.error("Error occurred while trying to preview file", e);
                return "";
            }
        }
        return this.uploadedMedia.getStringData();
    }


    public InputStream getDataAsStream(Charset encoding)
    {
        if(this.uploadedMedia.isBinary())
        {
            return this.uploadedMedia.getStreamData();
        }
        return IOUtils.toInputStream(getDataAsString(), encoding);
    }


    public InputStream getDataAsStream()
    {
        return getDataAsStream(StandardCharsets.UTF_8);
    }


    public long getDataSize()
    {
        try
        {
            CountingInputStream countingInputStream = new CountingInputStream(getDataAsStream());
            try
            {
                byte[] buffer = new byte[1048576];
                int data = countingInputStream.read();
                while(data != -1)
                {
                    data = countingInputStream.read(buffer);
                }
                long l = countingInputStream.getByteCount();
                countingInputStream.close();
                return l;
            }
            catch(Throwable throwable)
            {
                try
                {
                    countingInputStream.close();
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
            LOG.error("Failed to calculate size of uploaded file", e);
            return -1L;
        }
    }


    public String getName()
    {
        return this.uploadedMedia.getName();
    }


    public String getTargetName()
    {
        String timeStamp = (new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")).format(new Date());
        String baseName = FilenameUtils.removeExtension(this.uploadedMedia.getName());
        String ext = FilenameUtils.getExtension(this.uploadedMedia.getName());
        return "upload_" + baseName + "_" + timeStamp + "." + ext;
    }


    public String getContentType()
    {
        return this.uploadedMedia.getContentType();
    }
}
