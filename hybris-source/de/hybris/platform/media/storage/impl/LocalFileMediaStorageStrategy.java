package de.hybris.platform.media.storage.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.exceptions.MediaRemovalException;
import de.hybris.platform.media.exceptions.MediaStoreException;
import de.hybris.platform.media.services.MediaLocationHashService;
import de.hybris.platform.media.services.MimeService;
import de.hybris.platform.media.services.impl.HierarchicalMediaPathBuilder;
import de.hybris.platform.media.storage.LocalStoringStrategy;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MediaUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class LocalFileMediaStorageStrategy implements MediaStorageStrategy, LocalStoringStrategy
{
    public static final String FOLDER_INTEGRITY_VERIFICATION_ENABLED_FLAG = "local.file.media.storage.folder.integrity.verification.enabled";
    private static final Logger LOG = Logger.getLogger(LocalFileMediaStorageStrategy.class);
    private File mainDataDir;
    private List<File> replicationDirs;
    private MediaLocationHashService locationHashService;
    private MimeService mimeService;
    private MediaService mediaService;
    protected MediaStorageConfigService storageConfigService;
    private Boolean folderIntegrityVerificationEnabled = null;


    public StoredMediaData store(MediaStorageConfigService.MediaFolderConfig config, String mediaId, Map<String, Object> metaData, InputStream dataStream)
    {
        Preconditions.checkNotNull(config);
        Preconditions.checkNotNull(mediaId);
        Preconditions.checkNotNull(metaData);
        Preconditions.checkNotNull(dataStream);
        String mime = (String)metaData.get("mime");
        String fileName = getFileName(mediaId, mime);
        HierarchicalMediaPathBuilder pathBuilder = HierarchicalMediaPathBuilder.forDepth(config.getHashingDepth());
        String relativeMediaDirPath = pathBuilder.buildPath((String)metaData.get("folderPath"), fileName);
        int replDirsSize = this.replicationDirs.size();
        Collection<OutputStream> fos = new ArrayList<>(replDirsSize);
        Collection<File> files = new ArrayList<>(replDirsSize);
        File absoluteMediaDirPath = null;
        File media = null;
        try
        {
            for(File baseDir : this.replicationDirs)
            {
                absoluteMediaDirPath = MediaUtil.composeOrGetParent(baseDir, relativeMediaDirPath);
                media = new File(absoluteMediaDirPath, fileName);
                files.add(media);
                boolean fileCreated = createNewMediaFile(absoluteMediaDirPath, media);
                if(fileCreated)
                {
                    fos.add(new FileOutputStream(media));
                    continue;
                }
                throw new MediaStoreException("New media file already exists! (mediaId: " + mediaId + ", file:" + media + ", dir: " + absoluteMediaDirPath + ")");
            }
            long mediaSize = MediaUtil.copy(dataStream, fos, true);
            String location = relativeMediaDirPath + relativeMediaDirPath;
            String hashForLocation = this.locationHashService.createHash(config.getFolderQualifier(), location, mediaSize, mime);
            return new StoredMediaData(location, hashForLocation, mediaSize, mime);
        }
        catch(IOException e)
        {
            LOG.error(e.getMessage(), e);
            throw new MediaStoreException("Error writing media file (mediaId: " + mediaId + ", file:" + media + ", dir:" + absoluteMediaDirPath + ")", e);
        }
        finally
        {
            closeInputStream(dataStream);
            closeOutputStreams(fos);
        }
    }


    private String getFileName(String mediaId, String mime)
    {
        String fileExtension = this.mimeService.getBestExtensionFromMime(mime);
        String fileName = StringUtils.isNotBlank(fileExtension) ? (mediaId + "." + mediaId) : mediaId;
        return fileName;
    }


    private void closeInputStream(InputStream dataStream)
    {
        try
        {
            dataStream.close();
        }
        catch(IOException e)
        {
            LOG.error("cannot close stream", e);
        }
    }


    private void closeOutputStreams(Collection<OutputStream> fos)
    {
        for(OutputStream outputStream : fos)
        {
            try
            {
                outputStream.close();
            }
            catch(IOException e)
            {
                LOG.error("cannot close stream", e);
            }
        }
    }


    public void delete(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        Preconditions.checkArgument((config != null), "config is required!");
        Preconditions.checkArgument((location != null), "location is required!");
        for(File baseDir : this.replicationDirs)
        {
            File media = MediaUtil.composeOrGetParent(baseDir, location);
            try
            {
                if(media.exists())
                {
                    Files.deleteIfExists(media.toPath());
                }
            }
            catch(IOException e)
            {
                throw new MediaRemovalException("Removal of file: " + media + " has failed.", e);
            }
        }
    }


    public InputStream getAsStream(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        File media = getAsFile(config, location);
        try
        {
            return new FileInputStream(media);
        }
        catch(FileNotFoundException e)
        {
            throw new MediaNotFoundException("Media not found (requested media location: " + location + ")", e);
        }
    }


    protected void verifyFolderAndLocationIntegrity(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        if(isFolderVerificationDisabled())
        {
            return;
        }
        (new LocalFileMediaVerifier(this.mediaService, config, location)).verifyFolderAndLocationIntegrity();
    }


    protected boolean isFolderVerificationDisabled()
    {
        Boolean enabledGlobally = this.folderIntegrityVerificationEnabled;
        if(enabledGlobally == null)
        {
            enabledGlobally = this.folderIntegrityVerificationEnabled = Boolean.valueOf(Config.getBoolean("local.file.media.storage.folder.integrity.verification.enabled", true));
        }
        Boolean sessionOverride = (Boolean)JaloSession.getCurrentSession().getSessionContext().getAttribute("local.file.media.storage.folder.integrity.verification.enabled");
        if(sessionOverride != null)
        {
            return !sessionOverride.booleanValue();
        }
        return !enabledGlobally.booleanValue();
    }


    public File getAsFile(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        Preconditions.checkArgument((config != null), "folderQualifier is required!");
        Preconditions.checkArgument((location != null), "location is required!");
        verifyFolderAndLocationIntegrity(config, location);
        return MediaUtil.composeOrGetParent(this.mainDataDir, location);
    }


    public long getSize(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        File file = getAsFile(config, location);
        if(file.exists())
        {
            return file.length();
        }
        throw new MediaNotFoundException("Media not found (requested media location: " + location + ")");
    }


    private boolean createNewMediaFile(File absoluteMediaDirPath, File media) throws IOException
    {
        if(!absoluteMediaDirPath.exists())
        {
            absoluteMediaDirPath.mkdirs();
        }
        return media.createNewFile();
    }


    @Required
    public void setStorageConfigService(MediaStorageConfigService storageConfigService)
    {
        this.storageConfigService = storageConfigService;
    }


    @Required
    public void setMainDataDir(File mainDataDir)
    {
        this.mainDataDir = mainDataDir;
    }


    @Required
    public void setReplicationDirs(List<File> replicationDirs)
    {
        this.replicationDirs = replicationDirs;
    }


    @Required
    public void setLocationHashService(MediaLocationHashService locationHashService)
    {
        this.locationHashService = locationHashService;
    }


    @Required
    public void setMimeService(MimeService mimeService)
    {
        this.mimeService = mimeService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
