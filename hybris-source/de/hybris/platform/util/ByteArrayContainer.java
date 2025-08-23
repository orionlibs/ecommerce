package de.hybris.platform.util;

import de.hybris.platform.util.zip.SafeZipEntry;
import de.hybris.platform.util.zip.SafeZipInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;

public class ByteArrayContainer implements Serializable
{
    private static final Logger log = Logger.getLogger(ByteArrayContainer.class.getName());
    private final byte[] data;


    public static String uncompressString(ByteArrayContainer bac)
    {
        try
        {
            SafeZipInputStream zis = new SafeZipInputStream(new ByteArrayInputStream(bac.getData()));
            InputStreamReader isr = new InputStreamReader((InputStream)zis);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            for(String line = reader.readLine(); line != null; line = reader.readLine())
            {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
        catch(IOException e)
        {
            log.error(" - error uncompressing container. Exception: " + e);
            return null;
        }
    }


    public static ByteArrayContainer compressString(String src, String fileName)
    {
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(bos);
            zos.putNextEntry((ZipEntry)new SafeZipEntry(fileName));
            OutputStreamWriter osw = new OutputStreamWriter(zos);
            osw.write(src);
            osw.flush();
            zos.closeEntry();
            zos.close();
            byte[] ba = bos.toByteArray();
            return new ByteArrayContainer(ba);
        }
        catch(IOException e)
        {
            log.error(" - error compressing string. Exception: " + e);
            return null;
        }
    }


    public ByteArrayContainer(byte[] data)
    {
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }


    public byte[] getData()
    {
        return this.data;
    }
}
