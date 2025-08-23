package de.hybris.platform.impex.jalo.cronjob;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.media.DefaultMediaDataHandler;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.zip.SafeZipEntry;
import de.hybris.platform.util.zip.SafeZipInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DefaultCronJobMediaDataHandler extends DefaultMediaDataHandler
{
    private static final Logger LOG = Logger.getLogger(DefaultCronJobMediaDataHandler.class.getName());
    public static final String ZIP_FILENAME_ENCODINGS = "zip.filename.encodings";
    private Set<String> encodings = null;
    private ImpExImportCronJob impCronJob = null;
    private ImpExExportCronJob expCronJob = null;
    private volatile File unzipFolder;


    public DefaultCronJobMediaDataHandler(ImpExImportCronJob cronJob)
    {
        this.impCronJob = cronJob;
        setLegacyMode(isLegacyMode());
    }


    private boolean isLegacyMode()
    {
        return (this.impCronJob.isLegacyMode() == null) ? Config.getBoolean("impex.legacy.mode", false) :
                        this.impCronJob.isLegacyModeAsPrimitive();
    }


    public DefaultCronJobMediaDataHandler(ImpExExportCronJob cronJob)
    {
        this.expCronJob = cronJob;
    }


    public void importData(Media destMedia, String path) throws ImpExException
    {
        if(destMedia == null)
        {
            throw new ImpExException("Assigned media couldn't be null!");
        }
        if(path == null || path.length() <= 0)
        {
            throw new ImpExException("Invalid path definition!");
        }
        if(this.impCronJob == null || this.impCronJob.getMediasMedia() == null || isAbsolutePath(path) || isURLBasedPath(path) ||
                        isJarBasedPath(path) || isZipBasedPath(path) || isExplodedJarBasedPath(path))
        {
            super.importData(destMedia, path);
        }
        else
        {
            String normalizedLocation;
            Media sourceMedia = this.impCronJob.getMediasMedia();
            String location = this.impCronJob.getMediasTarget();
            if(location != null && location.length() > 0)
            {
                normalizedLocation = FilenameUtils.normalizeNoEndSeparator(location) + FilenameUtils.normalizeNoEndSeparator(location);
            }
            else
            {
                normalizedLocation = "";
            }
            String normalizedPath = FilenameUtils.normalizeNoEndSeparator(path);
            if(this.impCronJob.isUnzipMediasMediaAsPrimitive())
            {
                if(this.unzipFolder == null)
                {
                    synchronized(this)
                    {
                        if(this.unzipFolder == null)
                        {
                            this.unzipFolder = unzipMediasMedia(sourceMedia, normalizedLocation);
                        }
                    }
                }
                importFromUnzippedFolder(this.unzipFolder, destMedia, normalizedPath);
            }
            else
            {
                importFromZip(sourceMedia, destMedia, normalizedLocation + normalizedLocation);
            }
        }
    }


    public String exportData(Media media)
    {
        if(this.expCronJob == null)
        {
            return "";
        }
        if(media.getURL() == null || media.getURL().trim().length() == 0)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("skipping export of data of media " + media
                                .getCode() + "(" + media.getFileName() + ") - it has no data");
            }
            this.expCronJob.emptyMedias++;
            return "";
        }
        ZipOutputStream zipOut = this.expCronJob.zipOut;
        int dot = media.getFileName().lastIndexOf(".");
        String ext = (dot > 0) ? media.getFileName().substring(dot) : "";
        String zipentry = media.getComposedType().getCode() + media.getComposedType().getCode() + File.separator + media.getPK().toString();
        if(zipOut != null)
        {
            try
            {
                zipOut.putNextEntry(new ZipEntry(zipentry));
                try
                {
                    MediaUtil.copy(media.getDataFromStreamSure(), zipOut, false);
                }
                catch(IOException e)
                {
                    LOG.warn("Can not read media " + media + ": " + e.getMessage(), e);
                    this.expCronJob.invalidMedias++;
                }
                zipOut.closeEntry();
            }
            catch(Exception e)
            {
                LOG.warn("Can not read media " + media + ": " + e.getMessage(), e);
                this.expCronJob.invalidMedias++;
            }
        }
        return zipentry;
    }


    protected File unzipMediasMedia(Media sourceMedia, String normalizedLocation)
    {
        File folder = new File(Config.getParameter("java.io.tmpdir") + Config.getParameter("java.io.tmpdir") + File.separator);
        folder.mkdir();
        long start = System.currentTimeMillis();
        if(LOG.isInfoEnabled())
        {
            LOG.info("Unzipping medias media to temporary directory " + folder.getAbsolutePath() + (
                            "".equals(normalizedLocation) ?
                                            "" : (" excluding folders not equivalent to " + normalizedLocation)));
        }
        ZipFile zFile = null;
        SafeZipInputStream zin = null;
        try
        {
            Enumeration<? extends ZipEntry> entries;
            if(sourceMedia.hasData())
            {
                zFile = new ZipFile(sourceMedia.getFiles().iterator().next());
                entries = zFile.entries();
            }
            else
            {
                zin = new SafeZipInputStream(sourceMedia.getDataFromStreamSure());
                entries = null;
            }
            for(SafeZipEntry safeZipEntry = getNext(entries, zin); safeZipEntry != null; safeZipEntry = getNext(entries, zin))
            {
                if(this.impCronJob.isRequestAbortAsPrimitive())
                {
                    LOG.warn("Stopping unzipping medias media because of user abort");
                    break;
                }
                String entryName = FilenameUtils.separatorsToSystem(safeZipEntry.getName());
                if(!safeZipEntry.isDirectory() && entryName.startsWith(normalizedLocation))
                {
                    entryName = entryName.substring(normalizedLocation.length(), entryName.length());
                    File newFile = new File(folder, entryName);
                    if(newFile.getParentFile() != null)
                    {
                        newFile.getParentFile().mkdirs();
                    }
                    newFile.createNewFile();
                    InputStream inputStream = (zFile != null) ? zFile.getInputStream((ZipEntry)safeZipEntry) : (InputStream)zin;
                    MediaUtil.copy(inputStream, new FileOutputStream(newFile));
                    if(zFile != null)
                    {
                        inputStream.close();
                    }
                }
            }
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            if(zFile != null)
            {
                try
                {
                    zFile.close();
                }
                catch(IOException e)
                {
                    throw new JaloSystemException(e);
                }
            }
            if(zin != null)
            {
                try
                {
                    zin.close();
                    zin = null;
                }
                catch(IOException e)
                {
                    throw new JaloSystemException(e);
                }
            }
        }
        long stop = System.currentTimeMillis();
        if(LOG.isInfoEnabled())
        {
            LOG.info("Unzipping medias media to temporary directory " + folder.getAbsolutePath() + (
                            "".equals(normalizedLocation) ? "" : (" excluding folders not equivalent to " + normalizedLocation)) + " done in " + (stop - start) / 1000L + " seconds");
        }
        return folder;
    }


    private SafeZipEntry getNext(Enumeration<? extends ZipEntry> entriesFromFile, SafeZipInputStream zin) throws IOException
    {
        if(entriesFromFile != null)
        {
            return entriesFromFile.hasMoreElements() ? new SafeZipEntry(entriesFromFile.nextElement()) : null;
        }
        return zin.getNextEntry();
    }


    private void importFromUnzippedFolder(File folder, Media destination, String normalizedPathWithinFolder)
    {
        try
        {
            File srcFile = new File(folder, normalizedPathWithinFolder);
            String realName = destination.getRealFileName();
            DataInputStream dis = new DataInputStream(new FileInputStream(srcFile));
            try
            {
                destination.setData(dis, (realName == null) ? (new File(normalizedPathWithinFolder)).getName() : realName, destination
                                .getMime());
            }
            finally
            {
                try
                {
                    dis.close();
                }
                catch(IOException exp)
                {
                    LOG.warn("Can not close data input stream for file " + srcFile.getAbsolutePath());
                }
            }
        }
        catch(FileNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    private void importFromZip(Media source, Media destination, String normalizedPathWithinZip)
    {
        ZipFile zFile = null;
        SafeZipInputStream zin = null;
        try
        {
            Enumeration<? extends ZipEntry> entries = null;
            if(source.hasData())
            {
                File file = source.getFiles().iterator().next();
                zFile = openZipFileWithFallbackEncoding(file);
                entries = zFile.entries();
            }
            else
            {
                zin = new SafeZipInputStream(source.getDataFromStreamSure());
            }
            boolean found = false;
            for(SafeZipEntry safeZipEntry = getNext(entries, zin); safeZipEntry != null; safeZipEntry = getNext(entries, zin))
            {
                if(FilenameUtils.normalizeNoEndSeparator(safeZipEntry.getName()).equalsIgnoreCase(normalizedPathWithinZip))
                {
                    found = true;
                    String realName = destination.getRealFileName();
                    destination.setData(new DataInputStream(
                                                    (zFile != null) ? zFile.getInputStream((ZipEntry)safeZipEntry) : (InputStream)zin),
                                    (realName == null) ? (new File(normalizedPathWithinZip)).getName() : realName, destination
                                                    .getMime());
                    zin = null;
                    break;
                }
            }
            if(!found)
            {
                LOG.warn("Binary data for media '" + normalizedPathWithinZip + "' not found in zip media " + source.getCode());
            }
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            if(zFile != null)
            {
                try
                {
                    zFile.close();
                }
                catch(IOException e)
                {
                    throw new JaloSystemException(e);
                }
            }
            if(zin != null)
            {
                try
                {
                    zin.close();
                    zin = null;
                }
                catch(IOException e)
                {
                    throw new JaloSystemException(e);
                }
            }
        }
    }


    private ZipFile openZipFileWithFallbackEncoding(File file) throws IOException
    {
        if(this.encodings == null)
        {
            computeSupportedEncodings();
        }
        ZipFile result = isEncodingOK(file, null);
        if(result != null)
        {
            return result;
        }
        for(String encoding : this.encodings)
        {
            result = isEncodingOK(file, encoding);
            if(result != null)
            {
                return result;
            }
        }
        throw new JaloSystemException("Filenames in zip file " + file.getName() + " are encoded using different encoding than defined in zip.filename.encodings property (" + this.encodings + ")");
    }


    private void computeSupportedEncodings()
    {
        String str = Config.getParameter("zip.filename.encodings");
        if(StringUtils.isBlank(str))
        {
            this.encodings = Collections.emptySet();
        }
        else
        {
            Set<String> set = new LinkedHashSet<>();
            for(StringTokenizer tok = new StringTokenizer(str, ",;"); tok.hasMoreTokens(); )
            {
                String strElement = tok.nextToken().toLowerCase().trim();
                if(strElement.length() > 0)
                {
                    set.add(strElement);
                }
            }
            this.encodings = Collections.unmodifiableSet(set);
        }
    }


    private ZipFile isEncodingOK(File file, String encodingName) throws IOException
    {
        ZipFile zFile;
        try
        {
            if(encodingName == null)
            {
                zFile = new ZipFile(file);
            }
            else
            {
                zFile = new ZipFile(file, Charset.forName(encodingName));
            }
            zFile.entries().asIterator().forEachRemaining(e -> e.getName());
        }
        catch(UnsupportedEncodingException | java.nio.charset.IllegalCharsetNameException | java.util.zip.ZipException uee)
        {
            LOG.debug("The property zip.filename.encodings contains unsupported encoding", uee);
            return null;
        }
        catch(IllegalArgumentException iae)
        {
            LOG.debug("Exception while working on file: " + file.getName(), iae);
            return null;
        }
        return zFile;
    }


    public void cleanUp()
    {
        if(this.unzipFolder != null)
        {
            synchronized(this)
            {
                if(this.unzipFolder != null)
                {
                    try
                    {
                        if(LOG.isInfoEnabled())
                        {
                            LOG.info("Deleting temporary unzip folder " + this.unzipFolder.getAbsolutePath());
                        }
                        FileUtils.deleteDirectory(this.unzipFolder);
                    }
                    catch(IOException e)
                    {
                        LOG.warn("Can not delete temporary directory " + this.unzipFolder.getAbsolutePath());
                    }
                    finally
                    {
                        this.unzipFolder = null;
                    }
                }
            }
        }
        super.cleanUp();
    }


    public DefaultCronJobMediaDataHandler()
    {
    }
}
