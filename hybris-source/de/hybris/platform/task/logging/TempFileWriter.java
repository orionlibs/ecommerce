package de.hybris.platform.task.logging;

import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.util.Utilities;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import org.apache.commons.io.IOUtils;

public class TempFileWriter implements AutoCloseable
{
    private final Path tempFile = createTempFile();
    private final BufferedWriter logCollector = createTempWriter();


    public void write(String message)
    {
        if(this.logCollector == null)
        {
            return;
        }
        try
        {
            this.logCollector.write(message);
            this.logCollector.newLine();
        }
        catch(IOException e)
        {
            System.out.println("Problem with writing log for task " + e.getMessage());
        }
    }


    public String getContextAsText()
    {
        try
        {
            InputStream inputStream = getStreamForTempFile();
            try
            {
                String str = IOUtils.toString(inputStream, "UTF-8");
                if(inputStream != null)
                {
                    inputStream.close();
                }
                return str;
            }
            catch(Throwable throwable)
            {
                if(inputStream != null)
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new SystemException("Failed to read temp log file: " + this.tempFile, e);
        }
    }


    public InputStream getStreamForTempFile()
    {
        try
        {
            return Files.newInputStream(this.tempFile, new java.nio.file.OpenOption[0]);
        }
        catch(IOException e)
        {
            throw new SystemException("Failed to read temp log file: " + this.tempFile, e);
        }
    }


    public void deleteTempFile()
    {
        if(this.tempFile.toFile().exists())
        {
            this.tempFile.toFile().delete();
        }
    }


    private BufferedWriter createTempWriter()
    {
        try
        {
            return Files.newBufferedWriter(this.tempFile, Charset.forName("UTF-8"), new java.nio.file.OpenOption[0]);
        }
        catch(IOException e)
        {
            throw new SystemException(e);
        }
    }


    private Path createTempFile()
    {
        try
        {
            return Files.createTempFile(Utilities.getPlatformTempDir().toPath(), "task", "log.txt", (FileAttribute<?>[])new FileAttribute[0]);
        }
        catch(IOException e)
        {
            throw new SystemException(e);
        }
    }


    public void close()
    {
        IOUtils.closeQuietly(this.logCollector);
    }
}
