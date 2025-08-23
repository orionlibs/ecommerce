package de.hybris.platform.servicelayer.media.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.AbstractMediaModel;
import de.hybris.platform.core.model.media.DerivedMediaModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaContextModel;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContext;
import de.hybris.platform.jalo.media.MediaDataStoreCommand;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.exceptions.MediaRemovalException;
import de.hybris.platform.media.exceptions.MediaStoreException;
import de.hybris.platform.media.services.MimeService;
import de.hybris.platform.media.storage.impl.StoredMediaData;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.media.MediaIOException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.media.NoDataAvailableException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMediaService extends AbstractBusinessService implements MediaService
{
    private static final Logger LOG = Logger.getLogger(DefaultMediaService.class);
    public static final String FROM_JAR = "fromjar";
    private MediaDao mediaDao;
    private MimeService mimeService;
    private FlexibleSearchService flexibleSearchService;
    private static final String FOREIGN_DATA_OWNERS_WITHOUT_DATAPK_QRY = "SELECT {pk} FROM {Media} WHERE {pk}<>?myPK AND {internalURL}=?hasdata AND {dataPK}=?myPK";
    private static final String FOREIGN_DATA_OWNERS_WITH_DATAPK_QRY = "SELECT {pk} FROM {Media} WHERE {pk}<>?myPK AND {internalURL}=?hasData AND ({dataPK}=?myDataPK \tOR ({dataPK} IS NULL AND {pk}=?myDataPK))";


    public void copyData(MediaModel fromMedia, MediaModel toMedia)
    {
        ServicesUtil.validateParameterNotNull(fromMedia, "Source media model cannot be null");
        ServicesUtil.validateParameterNotNull(toMedia, "Target media model cannot be null");
        copyData(fromMedia, toMedia, true);
    }


    public void duplicateData(MediaModel fromMedia, MediaModel toMedia)
    {
        ServicesUtil.validateParameterNotNull(fromMedia, "Source media model cannot be null");
        ServicesUtil.validateParameterNotNull(toMedia, "Target media model cannot be null");
        copyData(fromMedia, toMedia, false);
    }


    private void copyData(MediaModel fromMedia, MediaModel toMedia, boolean reUseInSameFolder)
    {
        validateMediaStateForBinaryOperations(fromMedia);
        validateMediaStateForBinaryOperations(toMedia);
        if(hasData(fromMedia))
        {
            if(reUseInSameFolder &&
                            folderWithFallbackToRoot(fromMedia.getFolder()).equals(folderWithFallbackToRoot(toMedia.getFolder())))
            {
                connectMediaToExistingData(fromMedia, toMedia);
            }
            else
            {
                copyBinaryData(fromMedia, toMedia);
            }
        }
        else
        {
            Preconditions.checkState(StringUtils.isNotBlank(fromMedia.getURL()), "Source media has not data and empty URL");
            setUrlForMedia(toMedia, fromMedia.getInternalURL());
            getModelService().save(toMedia);
        }
    }


    private void copyBinaryData(MediaModel fromMedia, MediaModel toMedia)
    {
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, fromMedia, toMedia));
        }
        catch(Exception e)
        {
            throw new MediaIOException(e);
        }
    }


    private void connectMediaToExistingData(MediaModel fromMedia, MediaModel toMedia)
    {
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, toMedia, fromMedia));
        }
        catch(Exception e)
        {
            throw new MediaIOException(e);
        }
    }


    private void copyMetaData(MediaModel fromMedia, MediaModel toMedia)
    {
        toMedia.setDataPK(
                        (fromMedia.getDataPK() == null) ? Long.valueOf(fromMedia.getDataPK().longValue()) : fromMedia.getDataPK());
        toMedia.setMime(fromMedia.getMime());
        toMedia.setLocation(fromMedia.getLocation());
        toMedia.setLocationHash(fromMedia.getLocationHash());
        toMedia.setRealFileName(fromMedia.getRealFileName());
        toMedia.setSize(fromMedia.getSize());
    }


    public void moveData(MediaModel fromMedia, MediaModel toMedia)
    {
        copyData(fromMedia, toMedia);
        removeDataFromMedia(fromMedia);
    }


    public MediaModel getMediaByContext(MediaModel media, MediaContextModel context)
    {
        ServicesUtil.validateParameterNotNull(media, "Argument media cannot be null");
        ServicesUtil.validateParameterNotNull(context, "Argument context cannot be null");
        Media result = ((Media)getModelService().getSource(media)).getForContext((MediaContext)getModelService()
                        .getSource(context));
        if(result == null)
        {
            throw new ModelNotFoundException("Can not find a related media for media " + media.getCode());
        }
        return (MediaModel)getModelService().get(result);
    }


    public MediaModel getMediaByFormat(MediaModel media, MediaFormatModel format)
    {
        ServicesUtil.validateParameterNotNull(media, "Argument media must not be null.");
        ServicesUtil.validateParameterNotNull(format, "Argument format must not be null.");
        if(format.equals(media.getMediaFormat()))
        {
            return media;
        }
        MediaContainerModel container = media.getMediaContainer();
        if(container == null)
        {
            throw new ModelNotFoundException("Can not find a related media for media " + media.getCode() + " and format " + format
                            .getQualifier() + ". Container not set.");
        }
        return getMediaByFormat(container, format);
    }


    public MediaModel getMediaByFormat(MediaContainerModel container, MediaFormatModel format)
    {
        ServicesUtil.validateParameterNotNull(container, "Argument container must not be null.");
        ServicesUtil.validateParameterNotNull(format, "Argument format must not be null.");
        return this.mediaDao.findMediaByFormat(container, format);
    }


    public byte[] getDataFromMedia(MediaModel media) throws NoDataAvailableException
    {
        ServicesUtil.validateParameterNotNull(media, "Argument media cannot be null");
        MediaManager mediaManager = MediaManager.getInstance();
        try
        {
            if(hasData(media))
            {
                return mediaManager.getMediaAsByteArray(folderWithFallbackToRoot(media.getFolder()).getQualifier(), media
                                .getLocation());
            }
            return mediaManager.tryToGetMediaAsByteArrayFromUrl((MediaSource)new ModelMediaSource((AbstractMediaModel)media));
        }
        catch(Exception e)
        {
            throw new NoDataAvailableException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    public DataInputStream getDataStreamFromMedia(MediaModel media) throws NoDataAvailableException
    {
        InputStream inputStream = getStreamFromMedia(media);
        return (inputStream == null) ? null : new DataInputStream(inputStream);
    }


    public InputStream getStreamFromMedia(MediaModel media) throws NoDataAvailableException
    {
        ServicesUtil.validateParameterNotNull(media, "Argument media cannot be null");
        validateMediaStateForBinaryOperations(media);
        try
        {
            return MediaManager.getInstance().getMediaAsStream((MediaSource)new ModelMediaSource((AbstractMediaModel)media));
        }
        catch(Exception e)
        {
            throw new NoDataAvailableException(e);
        }
    }


    public Collection<File> getFiles(MediaModel media) throws NoDataAvailableException
    {
        ServicesUtil.validateParameterNotNull(media, "Argument media cannot be null");
        validateMediaStateForBinaryOperations(media);
        try
        {
            File file = hasData(media) ? MediaManager.getInstance().getMediaAsFile(folderWithFallbackToRoot(media.getFolder()).getQualifier(), media.getLocation()) : null;
            return Lists.newArrayList((Object[])new File[] {file});
        }
        catch(Exception e)
        {
            throw new NoDataAvailableException(e);
        }
    }


    public MediaFolderModel getFolder(String qualifier)
    {
        ServicesUtil.validateParameterNotNull(qualifier, "Argument qualifier cannot be null");
        List<MediaFolderModel> result = this.mediaDao.findMediaFolderByQualifier(qualifier);
        ServicesUtil.validateIfSingleResult(result, "No media folder with qualifier " + qualifier + " can be found.", "More than one media folder with qualifier " + qualifier + " found.");
        return result.iterator().next();
    }


    public MediaFormatModel getFormat(String qualifier)
    {
        ServicesUtil.validateParameterNotNull(qualifier, "Argument qualifier cannot be null");
        List<MediaFormatModel> result = this.mediaDao.findMediaFormatByQualifier(qualifier);
        ServicesUtil.validateIfSingleResult(result, "No media format with qualifier " + qualifier + " can be found.", "More than one media format with qualifier " + qualifier + " found.");
        return result.iterator().next();
    }


    public void setFolderForMedia(MediaModel media, MediaFolderModel targetFolder)
    {
        ServicesUtil.validateParameterNotNull(media, "Argument media cannot be null");
        ServicesUtil.validateParameterNotNull(targetFolder, "Argument targetFolder cannot be null");
        moveMediaToFolder(media, targetFolder);
    }


    public void moveMediaToFolder(MediaModel media, MediaFolderModel targetFolder) throws MediaIOException, IllegalArgumentException
    {
        ServicesUtil.validateParameterNotNull(media, "Argument media cannot be null");
        ServicesUtil.validateParameterNotNull(targetFolder, "Argument targetFolder cannot be null");
        validateMediaStateForBinaryOperations(media);
        MediaFolderModel expectedFolder = determineMediaFolder(media, targetFolder);
        if(folderWithFallbackToRoot(media.getFolder()).equals(expectedFolder))
        {
            LOG.warn("Will not move media " + media.getCode() + " because current folder is same as target folder");
        }
        else
        {
            InputStream streamFromMedia = null;
            try
            {
                streamFromMedia = getStreamFromMedia(media);
                setStreamForMedia(media, streamFromMedia, media.getRealFileName(), media.getMime(), expectedFolder);
            }
            catch(MediaStoreException | de.hybris.platform.media.exceptions.MediaNotFoundException e)
            {
                LOG.error("Cannot move media to target folder " + targetFolder
                                .getQualifier() + "(reason: " + e.getMessage() + ")");
            }
            finally
            {
                IOUtils.closeQuietly(streamFromMedia);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setDataStreamForMedia(MediaModel media, DataInputStream data)
    {
        setStreamForMedia(media, data, media.getRealFileName(), media.getMime());
    }


    public void setStreamForMedia(MediaModel media, InputStream data)
    {
        setStreamForMedia(media, data, media.getRealFileName(), media.getMime());
    }


    public boolean removeDataFromMediaQuietly(MediaModel media)
    {
        Preconditions.checkNotNull(media, "media is required");
        validateMediaStateForBinaryOperations(media);
        try
        {
            removeDataFromMedia(media);
            return true;
        }
        catch(MediaRemovalException e)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info("Removal of media has failed.");
            }
            return false;
        }
    }


    public void removeDataFromMedia(MediaModel media)
    {
        Preconditions.checkNotNull(media, "media is required");
        validateMediaStateForBinaryOperations(media);
        if(hasData(media))
        {
            PK mediaPk = media.getPk();
            Long dataPK = media.getDataPK();
            String folderQualifier = folderWithFallbackToRoot(media.getFolder()).getQualifier();
            String mediaLocation = media.getLocation();
            clearAllMetadataForMedia(media);
            MediaManager.getInstance().deleteMediaDataUnlessReferenced(mediaPk, dataPK, folderQualifier, mediaLocation);
            getModelService().save(media);
        }
    }


    public boolean hasData(MediaModel media)
    {
        return MediaManager.getInstance().hasData((MediaSource)new ModelMediaSource((AbstractMediaModel)media));
    }


    private void clearAllMetadataForMedia(MediaModel media)
    {
        media.setLocation(null);
        media.setLocationHash(null);
        media.setDataPK(null);
        media.setSize(null);
        media.setMime(null);
        media.setInternalURL(null);
    }


    public void addVersionStreamForMedia(MediaModel media, String versionId, InputStream data)
    {
        addVersionStreamForMedia(media, versionId, null, data);
    }


    public void addVersionStreamForMedia(MediaModel media, String versionId, String mimeType, InputStream data)
    {
        ServicesUtil.validateParameterNotNull(media, "Argument media cannot be null");
        ServicesUtil.validateParameterNotNull(versionId, "Argument versionId cannot be null");
        ServicesUtil.validateParameterNotNull(data, "Argument data cannot be null");
        MediaFolderModel folder = folderWithFallbackToRoot(media.getFolder());
        DerivedMediaModel derivedMedia = (DerivedMediaModel)getModelService().create(DerivedMediaModel.class);
        MediaDataStoreCommand.Builder builder = MediaDataStoreCommand.builder().folderQualifier(folder.getQualifier());
        builder.folderPath(folder.getPath()).fileName(media.getRealFileName()).overrideMime(media.getMime()).dataStream(data);
        StoredMediaData mediaData = MediaManager.getInstance().storeMedia(builder.build());
        derivedMedia.setDataPK(mediaData.getDataPk());
        derivedMedia.setMedia(media);
        derivedMedia.setVersion(versionId);
        derivedMedia.setMime((mimeType == null) ? media.getMime() : mimeType);
        derivedMedia.setLocation(mediaData.getLocation());
        derivedMedia.setLocationHash(mediaData.getHashForLocation());
        derivedMedia.setSize(mediaData.getSize());
        tryToSave(derivedMedia, folder.getQualifier(), mediaData.getLocation());
    }


    private void tryToSave(DerivedMediaModel derivedMedia, String folderQualifier, String location)
    {
        try
        {
            getModelService().save(derivedMedia);
        }
        catch(ModelSavingException e)
        {
            MediaManager.getInstance().deleteMedia(folderQualifier, location);
            LOG.error("Removing previously stored media version data due to: " + e.getMessage());
            throw e;
        }
    }


    public void removeVersionForMedia(MediaModel media, String versionId)
    {
        DerivedMediaModel version = findMediaVersion(media, versionId);
        if(version == null)
        {
            LOG.warn("No media version has been found with versionId: " + versionId + " for media: " + media);
        }
        else
        {
            String location = version.getLocation();
            getModelService().remove(version);
            getModelService().refresh(media);
            MediaManager.getInstance().deleteMedia(folderWithFallbackToRoot(media.getFolder()).getQualifier(), location);
        }
    }


    public String getUrlForMediaVersion(MediaModel media, String versionId)
    {
        DerivedMediaModel version = findMediaVersion(media, versionId);
        if(version == null)
        {
            throw new ModelNotFoundException("No media version has been found with versionId: " + versionId + " for media: " + media);
        }
        return MediaManager.getInstance().getURLForMedia(folderWithFallbackToRoot(media.getFolder()).getQualifier(), (MediaSource)new ModelMediaSource((AbstractMediaModel)version));
    }


    public InputStream getStreamForMediaVersion(MediaModel media, String versionId)
    {
        DerivedMediaModel version = findMediaVersion(media, versionId);
        if(version == null)
        {
            throw new ModelNotFoundException("No media version has been found with versionId: " + versionId + " for media: " + media);
        }
        return MediaManager.getInstance().getMediaAsStream(folderWithFallbackToRoot(media.getFolder()).getQualifier(), version
                        .getLocation());
    }


    private DerivedMediaModel findMediaVersion(MediaModel baseMedia, String versionId)
    {
        List<DerivedMediaModel> result = this.mediaDao.findMediaVersion(baseMedia, versionId);
        return (DerivedMediaModel)Iterables.getFirst(result, null);
    }


    public void setStreamForMedia(MediaModel media, InputStream data, String originalName, String mimeType)
    {
        setStreamForMedia(media, data, originalName, mimeType, folderWithFallbackToRoot(media.getFolder()));
    }


    public void setStreamForMedia(MediaModel media, InputStream data, String originalName, String mimeType, MediaFolderModel folder)
    {
        try
        {
            Preconditions.checkState(!getModelService().isNew(media));
            ServicesUtil.validateParameterNotNull(media, "Argument media cannot be null");
            ServicesUtil.validateParameterNotNull(data, "Argument data cannot be null");
            validateMediaStateForBinaryOperations(media);
            Transaction.current().execute((TransactionBody)new Object(this, media, folder, data, mimeType, originalName));
        }
        catch(Exception e)
        {
            throw new ModelSavingException(e.getMessage(), e);
        }
        finally
        {
            IOUtils.closeQuietly(data);
        }
    }


    public void setUrlForMedia(MediaModel media, String url)
    {
        media.setSize(getMediaSizeIfFromClasspath(url));
        media.setInternalURL(url);
        media.setMime(this.mimeService.getBestMime(url, null, null));
        media.setLocation(null);
        media.setLocationHash(null);
        media.setDataPK(null);
    }


    private Long getMediaSizeIfFromClasspath(String url)
    {
        Long result = null;
        if(url != null && url.contains("fromjar"))
        {
            String resourceName = url.substring(url.indexOf("/fromjar") + 1 + "fromjar".length());
            URL fileUrl = DefaultMediaService.class.getResource(resourceName);
            if(fileUrl != null)
            {
                try
                {
                    File file = new File(fileUrl.toURI());
                    if(file.exists())
                    {
                        result = Long.valueOf(file.length());
                    }
                }
                catch(URISyntaxException e)
                {
                    LOG.error(e.getMessage());
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(e.getMessage(), e);
                    }
                }
            }
        }
        return result;
    }


    public String getUrlForMedia(MediaModel media)
    {
        if(hasData(media))
        {
            return MediaManager.getInstance().getURLForMedia(folderWithFallbackToRoot(media.getFolder()).getQualifier(), (MediaSource)new ModelMediaSource((AbstractMediaModel)media));
        }
        return media.getInternalURL();
    }


    private MediaFolderModel folderWithFallbackToRoot(MediaFolderModel folder)
    {
        return (folder == null) ? getRootFolder() : folder;
    }


    private MediaFolderModel determineMediaFolder(MediaModel media, MediaFolderModel expectedFolder)
    {
        MediaFolderModel myFolder = folderWithFallbackToRoot(media.getFolder());
        MediaFolderModel newFolder = folderWithFallbackToRoot(expectedFolder);
        if(!myFolder.equals(newFolder))
        {
            return newFolder;
        }
        return myFolder;
    }


    private void setAllMetadataForMedia(StoredMediaData mediaData, MediaModel media)
    {
        media.setDataPK(mediaData.getDataPk());
        media.setLocation(mediaData.getLocation());
        media.setLocationHash(mediaData.getHashForLocation());
        media.setSize(mediaData.getSize());
        media.setMime(mediaData.getMime());
    }


    public void setDataForMedia(MediaModel media, byte[] data)
    {
        ServicesUtil.validateParameterNotNull(media, "Argument media cannot be null");
        ServicesUtil.validateParameterNotNull(data, "Argument data cannot be null");
        DataInputStream inputStream = null;
        try
        {
            inputStream = new DataInputStream(new ByteArrayInputStream(data));
            setStreamForMedia(media, inputStream);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }


    public MediaFolderModel getRootFolder()
    {
        try
        {
            MediaFolder folder = MediaManager.getInstance().getRootMediaFolder();
            return (MediaFolderModel)getModelService().get(folder);
        }
        catch(JaloSystemException e)
        {
            throw new ModelNotFoundException("Can not find root folder", e);
        }
    }


    public MediaModel getMedia(CatalogVersionModel catalogVersion, String code)
    {
        ServicesUtil.validateParameterNotNull(catalogVersion, "Argument catalogVersion cannot be null");
        ServicesUtil.validateParameterNotNull(code, "Argument code cannot be null");
        List<MediaModel> result = this.mediaDao.findMediaByCode(catalogVersion, code);
        ServicesUtil.validateIfSingleResult(result, "No media with code " + code + " in catalog version " + catalogVersion.getVersion() + " can be found.", "More than one media with code " + code + " in catalog version " + catalogVersion
                        .getVersion() + " found.");
        return result.iterator().next();
    }


    public MediaModel getMedia(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "Argument code cannot be null");
        List<MediaModel> result = this.mediaDao.findMediaByCode(code);
        ServicesUtil.validateIfSingleResult(result, "No media with code " + code + " can be found.", "More than one media with code " + code + " found.");
        return result.iterator().next();
    }


    public Collection<MediaModel> getMediaWithSameDataReference(MediaModel media)
    {
        if(!hasData(media))
        {
            return Collections.emptyList();
        }
        boolean useLocalCtx = BooleanUtils.isNotTrue((Boolean)getSessionService().getAttribute("disableRestrictions"));
        if(useLocalCtx)
        {
            return (Collection<MediaModel>)getSessionService().executeInLocalViewWithParams(
                            (Map)ImmutableMap.of("disableRestrictions", Boolean.TRUE), (SessionExecutionBody)new Object(this, media));
        }
        return queryForForeignDataOwners(media);
    }


    private Collection<MediaModel> queryForForeignDataOwners(MediaModel media)
    {
        Long myDataPK = (Long)media.getProperty("dataPK");
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("myDataPK", myDataPK);
        params.put("myPK", media.getPk());
        params.put("hasData", "replicated273654712");
        if(myDataPK == null)
        {
            return this.flexibleSearchService.search("SELECT {pk} FROM {Media} WHERE {pk}<>?myPK AND {internalURL}=?hasdata AND {dataPK}=?myPK", params).getResult();
        }
        List<MediaModel> result = this.flexibleSearchService.search("SELECT {pk} FROM {Media} WHERE {pk}<>?myPK AND {internalURL}=?hasData AND ({dataPK}=?myDataPK \tOR ({dataPK} IS NULL AND {pk}=?myDataPK))", params).getResult();
        return result;
    }


    private void validateMediaStateForBinaryOperations(MediaModel media)
    {
        Preconditions.checkState(!getModelService().isNew(media), "media must be persisted to do binary operations");
    }


    @Required
    public void setMediaDao(MediaDao mediaDao)
    {
        this.mediaDao = mediaDao;
    }


    @Required
    public void setMimeService(MimeService mimeService)
    {
        this.mimeService = mimeService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
