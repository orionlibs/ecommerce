package de.hybris.platform.util.zip;

import de.hybris.platform.util.SecurityUtils;
import java.io.UnsupportedEncodingException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;

public class SafeZipEntry extends ZipEntry
{
    private String platformHome;


    public SafeZipEntry(String name)
    {
        super(name);
    }


    public SafeZipEntry(ZipEntry e)
    {
        super(e);
    }


    public SafeZipEntry(ZipEntry e, String platformHome)
    {
        super(e);
        this.platformHome = platformHome;
    }


    public String getName()
    {
        String name = super.getName();
        Path path = getSupportedPath(name);
        if(this.platformHome == null)
        {
            if(SecurityUtils.isPathNotEscapingDirectory(path))
            {
                return name;
            }
        }
        else if(SecurityUtils.isPathNotEscapingDirectory(path, this.platformHome))
        {
            return name;
        }
        throw new UnsupportedZipEntryException(name);
    }


    private Path getSupportedPath(String name)
    {
        Path result;
        try
        {
            result = Paths.get(name, new String[0]);
        }
        catch(InvalidPathException ipe)
        {
            try
            {
                result = Paths.get(new String(name.getBytes("US-ASCII")), new String[0]);
            }
            catch(InvalidPathException ipe2)
            {
                throw new UnsupportedZipEntryException(ipe2);
            }
            catch(UnsupportedEncodingException uee)
            {
                throw new UnsupportedZipEntryException(uee);
            }
        }
        return result;
    }
}
