package de.hybris.platform.impex.jalo.media;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.media.exceptions.MediaStoreException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.zip.SafeZipEntry;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class DefaultMediaDataHandler implements MediaDataHandler
{
    private static final Splitter PATH_SPLITTER = Splitter.on('&');
    private static final String EXPLODED_JAR_PREFIX = "/medias/";
    private boolean legacyMode = Config.getBoolean("impex.legacy.mode", true);
    private MediaService mediaService;
    private ModelService modelService;
    private final String localMediaWebUrlPrefix;


    public DefaultMediaDataHandler()
    {
        this.localMediaWebUrlPrefix = MediaUtil.addTrailingFileSepIfNeeded(MediaUtil.getLocalMediaWebRootUrl());
    }


    public void importData(Media media, String path) throws ImpExException
    {
        assureParameters(media, path);
        if(isAbsolutePath(path))
        {
            setDataFromAbsolutePath(media, path);
        }
        else if(isJarBasedPath(path))
        {
            setDataFromClasspath(media, path);
        }
        else if(isZipBasedPath(path))
        {
            setDataFromZip(media, path);
        }
        else if(isURLBasedPath(path))
        {
            setUrlForMedia(media, removeControlPrefixFromPath("http:", path));
        }
        else if(isExplodedJarBasedPath(path))
        {
            setUrlForMedia(media, path.replace("/medias/", this.localMediaWebUrlPrefix));
        }
        else
        {
            throw new ImpExException("Path '" + path + "' couldn't be resolved!");
        }
    }


    private void assureParameters(Media media, String path) throws ImpExException
    {
        if(media == null)
        {
            throw new ImpExException("Assigned media couldn't be null!");
        }
        if(StringUtils.isEmpty(path))
        {
            throw new ImpExException("Invalid path definition!");
        }
    }


    protected boolean isAbsolutePath(String path)
    {
        return StringUtils.startsWithIgnoreCase(path, "file:");
    }


    private void setDataFromAbsolutePath(Media media, String path) throws ImpExException
    {
        String filename = removeControlPrefixFromPath("file:", path);
        InputStream dataStream = null;
        try
        {
            dataStream = getAsInputStream(filename);
            setStreamForMedia(media, dataStream);
        }
        catch(MediaStoreException e)
        {
            throw new ImpExException(e, e.getMessage(), 0);
        }
        catch(FileNotFoundException e)
        {
            throw new ImpExException(e, "Can not find file: " + filename, 0);
        }
        finally
        {
            IOUtils.closeQuietly(dataStream);
        }
    }


    InputStream getAsInputStream(String filename) throws FileNotFoundException
    {
        return new DataInputStream(new FileInputStream(new File(FilenameUtils.separatorsToSystem(filename))));
    }


    protected boolean isJarBasedPath(String path)
    {
        return StringUtils.startsWithIgnoreCase(path, "jar:");
    }


    private void setDataFromClasspath(Media media, String _path) throws ImpExException
    {
        String path = removeControlPrefixFromPath("jar:", _path);
        InputStream dataStream = null;
        try
        {
            Class<Item> classLoaderClass;
            String resourceName;
            if(containsCustomClassloaderClass(path))
            {
                Iterable<String> splitResult = PATH_SPLITTER.split(path);
                Preconditions.checkState((Iterables.size(splitResult) == 2));
                String classLoaderClassToken = (String)Iterables.get(splitResult, 0);
                Preconditions.checkState(StringUtils.isNotBlank(classLoaderClassToken));
                classLoaderClass = (Class)Class.forName(classLoaderClassToken);
                resourceName = FilenameUtils.separatorsToUnix((String)Iterables.get(splitResult, 1));
            }
            else
            {
                classLoaderClass = Item.class;
                resourceName = FilenameUtils.separatorsToUnix(path);
            }
            dataStream = getResourceAsStreamUsingClassloader(classLoaderClass, resourceName);
            if(dataStream == null)
            {
                throw new ImpExException("Cannot find classloader resource '" + resourceName + "'");
            }
            setStreamForMedia(media, dataStream);
        }
        catch(Exception e)
        {
            throw new ImpExException(e, e.getMessage(), 0);
        }
        finally
        {
            IOUtils.closeQuietly(dataStream);
        }
    }


    private boolean containsCustomClassloaderClass(String path)
    {
        return path.contains("&");
    }


    InputStream getResourceAsStreamUsingClassloader(Class classLoaderClass, String resourceName)
    {
        return classLoaderClass.getResourceAsStream(resourceName);
    }


    protected boolean isZipBasedPath(String path)
    {
        return StringUtils.startsWithIgnoreCase(path, "zip:");
    }


    private void setDataFromZip(Media media, String _path) throws ImpExException
    {
        ZipFile zipFile = null;
        try
        {
            String path = removeControlPrefixFromPath("zip:", _path);
            Iterable<String> splitResult = PATH_SPLITTER.split(path);
            Preconditions.checkArgument((Iterables.size(splitResult) == 2), "Zip location and one zip entry is required");
            String zipFilePath = (String)Iterables.get(splitResult, 0);
            String inZipResourcePath = (String)Iterables.get(splitResult, 1);
            zipFile = getZipFileFromPath(zipFilePath);
            ZipEntry zipEntry = getZipEntryFromZip(zipFile, inZipResourcePath);
            Preconditions.checkArgument((zipEntry != null), "Zip entry for path: " + zipEntry + " was not found in zip file");
            setStreamForMedia(media, zipFile.getInputStream(zipEntry));
        }
        catch(Exception e)
        {
            throw new ImpExException(e, e.getMessage(), 0);
        }
        finally
        {
            if(zipFile != null)
            {
                IOUtils.closeQuietly(zipFile);
            }
        }
    }


    private ZipFile getZipFileFromPath(String path) throws IOException
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(path));
        return new ZipFile(new File(FilenameUtils.separatorsToSystem(path)));
    }


    private ZipEntry getZipEntryFromZip(ZipFile zip, String path)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(path));
        return (ZipEntry)new SafeZipEntry(zip.getEntry(FilenameUtils.separatorsToSystem(path)));
    }


    protected boolean isURLBasedPath(String path)
    {
        return StringUtils.startsWithIgnoreCase(path, "http:");
    }


    protected boolean isExplodedJarBasedPath(String path)
    {
        return path.startsWith("/medias/");
    }


    private void setUrlForMedia(Media media, String path) throws ImpExException
    {
        try
        {
            if(this.legacyMode)
            {
                media.setURL(path);
            }
            else
            {
                MediaModel mediaModel = (MediaModel)getModelService().get(media);
                mediaModel.setURL(path);
                getModelService().save(mediaModel);
            }
        }
        catch(Exception e)
        {
            throw new ImpExException(e, "Error while setting URL to media", 0);
        }
    }


    private void setStreamForMedia(Media media, InputStream dataStream)
    {
        if(this.legacyMode)
        {
            media.setData(dataStream, media.getRealFileName(), media.getMime());
        }
        else
        {
            MediaModel mediaModel = (MediaModel)getModelService().get(media);
            getMediaService().setStreamForMedia(mediaModel, dataStream, mediaModel.getRealFileName(), mediaModel.getMime());
        }
    }


    private String removeControlPrefixFromPath(String controlPrefix, String fullPath)
    {
        return fullPath.substring(controlPrefix.length(), fullPath.length()).trim();
    }


    public String exportData(Media media)
    {
        throw new JaloSystemException("Not implemented yet! -- Use de.hybris.platform.impex.jalo.cronjob.DefaultCronJobMediaDataHandler");
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected static String normalize(String path)
    {
        return FilenameUtils.separatorsToSystem(path);
    }


    public void cleanUp()
    {
    }


    protected void setLegacyMode(boolean legacyMode)
    {
        this.legacyMode = legacyMode;
    }


    MediaService getMediaService()
    {
        if(this.mediaService == null)
        {
            this.mediaService = (MediaService)Registry.getApplicationContext().getBean("mediaService", MediaService.class);
        }
        return this.mediaService;
    }


    ModelService getModelService()
    {
        if(this.modelService == null)
        {
            this.modelService = (ModelService)Registry.getApplicationContext().getBean("modelService", ModelService.class);
        }
        return this.modelService;
    }
}
