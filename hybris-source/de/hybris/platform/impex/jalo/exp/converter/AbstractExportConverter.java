package de.hybris.platform.impex.jalo.exp.converter;

import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.exp.Export;
import de.hybris.platform.impex.jalo.exp.ImpExExportMedia;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.util.zip.SafeZipEntry;
import de.hybris.platform.util.zip.SafeZipInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public abstract class AbstractExportConverter implements ExportConverter
{
    private ImpExExportMedia data = null;
    private ImpExExportMedia medias = null;
    private ZipFile datazf = null;
    private ZipFile mediaszf = null;
    private ImpExMedia script = null;
    private Export export = null;
    private static final Logger log = Logger.getLogger(AbstractExportConverter.class.getName());


    public void setExport(Export export)
    {
        this.export = export;
        this.data = export.getExportedData();
        if(this.data != null && this.data.hasData())
        {
            try
            {
                this.datazf = new ZipFile(this.data.getFiles().iterator().next());
            }
            catch(Exception e)
            {
                log.warn("Processing of exported data' zip (export:" + export.getCode() + "): " + e.getMessage());
            }
        }
        this.medias = export.getExportedMedias();
        if(this.medias != null && this.medias.hasData())
        {
            try
            {
                this.mediaszf = new ZipFile(this.medias.getFiles().iterator().next());
            }
            catch(Exception e)
            {
                log.warn("Processing of exported media's zip (export:" + export.getCode() + "): " + e.getMessage());
            }
        }
        this.script = export.getExportScript();
    }


    public Export getExport()
    {
        return this.export;
    }


    public void close()
    {
        IOUtils.closeQuietly(this.datazf);
        IOUtils.closeQuietly(this.mediaszf);
    }


    public List<ZipEntry> getDataEntries() throws IOException, JaloBusinessException
    {
        return getZipEntries((ImpExMedia)this.data);
    }


    public List<ZipEntry> getMediasEntries() throws IOException, JaloBusinessException
    {
        return getZipEntries((ImpExMedia)this.medias);
    }


    public StringBuilder getDataContent(String name) throws IOException, JaloBusinessException
    {
        return getDataContent(getZipEntryByName((ImpExMedia)this.data, name));
    }


    public StringBuilder getDataContent(ZipEntry entry) throws IOException, JaloBusinessException
    {
        InputStream is = this.datazf.getInputStream(entry);
        try
        {
            StringBuilder stringBuilder = getDataContent(is);
            if(is != null)
            {
                is.close();
            }
            return stringBuilder;
        }
        catch(Throwable throwable)
        {
            if(is != null)
            {
                try
                {
                    is.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    public StringBuilder getMediasContent(String name) throws IOException, JaloBusinessException
    {
        return getMediasContent(getZipEntryByName((ImpExMedia)this.medias, name));
    }


    public StringBuilder getMediasContent(ZipEntry entry) throws IOException, JaloBusinessException
    {
        InputStream is = this.datazf.getInputStream(entry);
        try
        {
            StringBuilder stringBuilder = getDataContent(is);
            if(is != null)
            {
                is.close();
            }
            return stringBuilder;
        }
        catch(Throwable throwable)
        {
            if(is != null)
            {
                try
                {
                    is.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    public List<ZipEntry> getZipEntries(ImpExMedia media) throws IOException, JaloBusinessException
    {
        if(media != null && media.getMime() != null && media.getMime().equals("application/zip"))
        {
            List<ZipEntry> entries = new ArrayList<>();
            SafeZipInputStream zin = new SafeZipInputStream(media.getDataFromStreamSure());
            try
            {
                while(true)
                {
                    try
                    {
                        SafeZipEntry safeZipEntry;
                        if((safeZipEntry = zin.getNextEntry()) != null)
                        {
                            entries.add(safeZipEntry);
                            continue;
                        }
                    }
                    catch(IOException e)
                    {
                        throw new JaloBusinessException(e, "Error while retrieving zip entries of file: " + e.getMessage(), 0);
                    }
                    break;
                }
                zin.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    zin.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            return entries;
        }
        return Collections.EMPTY_LIST;
    }


    public ZipEntry getZipEntryByName(ImpExMedia media, String name) throws IOException, JaloBusinessException
    {
        if(media.getMime() != null && media.getMime().equals("application/zip"))
        {
            SafeZipInputStream zin = new SafeZipInputStream(media.getDataFromStreamSure());
            try
            {
                SafeZipEntry entry;
                while((entry = zin.getNextEntry()) != null)
                {
                    if(entry.getName().equalsIgnoreCase(name))
                    {
                        SafeZipEntry safeZipEntry = entry;
                        zin.close();
                        return (ZipEntry)safeZipEntry;
                    }
                }
                zin.close();
            }
            catch(IOException e)
            {
                throw new JaloBusinessException(e, "Error while retrieving zip entries of file: " + e.getMessage(), 0);
            }
            catch(Throwable throwable)
            {
                try
                {
                    zin.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            return null;
        }
        return null;
    }


    protected StringBuilder getDataContent(InputStream is) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        for(String line = reader.readLine(); line != null; line = reader.readLine())
        {
            sb.append(line);
            sb.append("\n");
        }
        return sb;
    }


    public void mergeEntries(Collection<ZipEntry> entries, File target)
    {
    }


    protected void dump() throws JaloBusinessException, IOException
    {
        List<ZipEntry> dataEntries = getZipEntries((ImpExMedia)this.data);
        List<ZipEntry> mediaEntries = getZipEntries((ImpExMedia)this.medias);
        List<ZipEntry> scriptEntries = getZipEntries(this.script);
        log.info("### DATA ###");
        log.info(dumpEntries(dataEntries).toString());
        log.info("\n### MEDIAS ###");
        log.info(dumpEntries(mediaEntries).toString());
        log.info("\n### SCRIPT ####");
        log.info(dumpEntries(scriptEntries).toString());
    }


    private StringBuilder dumpEntries(List<ZipEntry> entries) throws IOException, JaloBusinessException
    {
        StringBuilder sb = new StringBuilder();
        for(ZipEntry unsafeEntry : entries)
        {
            SafeZipEntry safeZipEntry = new SafeZipEntry(unsafeEntry);
            sb.append(safeZipEntry.getName());
            sb.append("\n");
            sb.append(getDataContent(safeZipEntry.getName()).toString());
            sb.append("\n");
        }
        return sb;
    }
}
