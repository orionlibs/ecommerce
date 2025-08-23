package de.hybris.platform.util.zip;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipInputStream;

public class SafeZipInputStream extends ZipInputStream
{
    public SafeZipInputStream(InputStream in)
    {
        super(in);
    }


    public SafeZipInputStream(InputStream in, Charset charset)
    {
        super(in, charset);
    }


    public SafeZipEntry getNextEntry() throws IOException
    {
        return (SafeZipEntry)super.getNextEntry();
    }


    protected SafeZipEntry createZipEntry(String name)
    {
        return new SafeZipEntry(name);
    }
}
