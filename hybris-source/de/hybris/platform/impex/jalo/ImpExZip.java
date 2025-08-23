package de.hybris.platform.impex.jalo;

import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.zip.SafeZipEntry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class ImpExZip implements ImpExFile
{
    private ZipEntry currentDataEntry;
    private ZipOutputStream zos;
    private File zipFile;
    private final Set<String> entrynames = new HashSet<>();
    private static final Logger LOG = Logger.getLogger(ImpExZip.class.getName());
    private boolean hasEntries = false;


    public ImpExZip() throws IOException
    {
        this(null);
    }


    public ImpExZip(String fileName) throws IOException
    {
        if(fileName == null)
        {
            this
                            .zipFile = Files.createTempFile(Utilities.getPlatformTempDir().toPath(), "ImpExZip_" + System.currentTimeMillis(), ".zip", (FileAttribute<?>[])new FileAttribute[0]).toFile();
        }
        else
        {
            this.zipFile = new File(fileName);
            this.zipFile.createNewFile();
        }
        this.zos = new ZipOutputStream(new FileOutputStream(this.zipFile));
    }


    public OutputStream getOutputStream()
    {
        return this.zos;
    }


    public void startNewEntry(String filename) throws IOException
    {
        if(this.currentDataEntry != null)
        {
            closeEntry();
        }
        if(!this.entrynames.contains(filename))
        {
            this.entrynames.add(filename);
            this.currentDataEntry = (ZipEntry)new SafeZipEntry(filename);
            this.zos.putNextEntry(this.currentDataEntry);
            this.hasEntries = true;
        }
    }


    public boolean hasOpenEntry()
    {
        return (this.currentDataEntry != null);
    }


    public String getCurrentEntryName()
    {
        if(hasOpenEntry())
        {
            return this.currentDataEntry.getName();
        }
        return null;
    }


    public void close() throws IOException
    {
        if(this.zos == null)
        {
            return;
        }
        try
        {
            if(hasOpenEntry())
            {
                closeEntry();
            }
            if(this.hasEntries)
            {
                this.zos.flush();
                this.zos.close();
            }
        }
        finally
        {
            IOUtils.closeQuietly(this.zos);
            this.zos = null;
        }
    }


    public void closeEntry() throws IOException
    {
        if(this.currentDataEntry != null)
        {
            this.zos.flush();
            this.zos.closeEntry();
            this.currentDataEntry = null;
        }
    }


    public File getFile()
    {
        return (this.zos == null) ? this.zipFile : null;
    }


    public String getMimeType()
    {
        return ImpExConstants.File.MIME_TYPE_ZIP;
    }


    public String getFileExtension()
    {
        return "zip";
    }


    public boolean containsEntry(String entryname)
    {
        return this.entrynames.contains(entryname);
    }
}
