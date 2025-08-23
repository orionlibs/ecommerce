package de.hybris.platform.ruleengine.util;

import de.hybris.platform.util.zip.SafeZipEntry;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class JarValidator
{
    public static void validateZipSlipSecure(InputStream jarFileInputStream) throws IOException
    {
        JarInputStream jarInputStream = new JarInputStream(jarFileInputStream);
        try
        {
            ZipEntry entry;
            while((entry = jarInputStream.getNextEntry()) != null)
            {
                (new SafeZipEntry(entry)).getName();
            }
            jarInputStream.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                jarInputStream.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
    }
}
