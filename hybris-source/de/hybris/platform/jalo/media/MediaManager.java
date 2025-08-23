package de.hybris.platform.jalo.media;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.exceptions.MediaInvalidLocationException;
import de.hybris.platform.media.impl.JaloMediaSource;
import de.hybris.platform.media.services.MediaHeadersRegistry;
import de.hybris.platform.media.services.MediaLocationHashService;
import de.hybris.platform.media.services.MediaStorageInitializer;
import de.hybris.platform.media.services.MimeService;
import de.hybris.platform.media.storage.LocalMediaFileCacheService;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageRegistry;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.media.storage.impl.StoredMediaData;
import de.hybris.platform.media.url.MediaURLStrategy;
import de.hybris.platform.media.url.impl.LocalMediaWebURLStrategy;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.SearchTools;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class MediaManager extends Manager
{
    private static final Logger LOG = Logger.getLogger(MediaManager.class);
    public static final String BEAN_NAME = "core.mediaManager";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String MEDIAFOLDER = GeneratedCoreConstants.TC.MEDIAFOLDER;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String MEDIACONTAINER = GeneratedCoreConstants.TC.MEDIACONTAINER;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String MEDIACONTEXT = GeneratedCoreConstants.TC.MEDIACONTEXT;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String MEDIAFORMAT = GeneratedCoreConstants.TC.MEDIAFORMAT;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String MEDIAFORMATMAPPING = GeneratedCoreConstants.TC.MEDIAFORMATMAPPING;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String MEDIAFORMATTER = "MediaFormatter".intern();
    public static final String ROOT_FOLDER_QUALIFIER = "root";
    public static final String PREFERRED_URL_STRATEGY_ID = "preferredUrlStrategyId";
    @Autowired
    private Map<String, MediaStorageInitializer> storageInitializers;
    private MediaLocationHashService locationHashService;
    private MediaStorageRegistry mediaStorageRegistry;
    private MediaStorageConfigService mediaStorageConfigService;
    private final LocalMediaFileCacheService.StreamGetter streamGetter = (LocalMediaFileCacheService.StreamGetter)new Object(this);
    private LocalMediaFileCacheService localMediaFileCache;
    private MimeService mimeService;
    private MediaHeadersRegistry mediaHeadersRegistry;


    @Required
    public void setMimeService(MimeService mimeService)
    {
        this.mimeService = mimeService;
    }


    public void setMediaHeadersRegistry(MediaHeadersRegistry mediaHeadersRegistry)
    {
        this.mediaHeadersRegistry = mediaHeadersRegistry;
    }


    @Required
    public void setLocalMediaFileCache(LocalMediaFileCacheService localMediaFileCache)
    {
        this.localMediaFileCache = localMediaFileCache;
    }


    @Required
    public void setMediaStorageConfigService(MediaStorageConfigService mediaStorageConfig)
    {
        this.mediaStorageConfigService = mediaStorageConfig;
    }


    @Required
    public void setMediaStorageRegistry(MediaStorageRegistry mediaStorageRegistry)
    {
        this.mediaStorageRegistry = mediaStorageRegistry;
    }


    @Required
    public void setLocationHashService(MediaLocationHashService locationHashService)
    {
        this.locationHashService = locationHashService;
    }


    public MediaStorageRegistry getMediaStorageFactory()
    {
        return this.mediaStorageRegistry;
    }


    public String getFileExtensionFromMime(String mime)
    {
        return this.mimeService.getBestExtensionFromMime(mime);
    }


    public String getBestMime(String fileName, byte[] firstBytes, String overrideMime)
    {
        return this.mimeService.getBestMime(fileName, firstBytes, overrideMime);
    }


    public String getBestMime(String filePath)
    {
        return this.mimeService.getBestMime(filePath, null, null);
    }


    public boolean hasForeignDataOwners(PK mediaPk, Long dataPk)
    {
        SessionContext myCtx = getSession().createLocalSessionContext(null);
        myCtx.setAttribute("disableRestrictions", Boolean.TRUE);
        try
        {
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
            params.put("myDataPK", dataPk);
            params.put("myPK", mediaPk);
            params.put("hasdata", "replicated273654712");
            if(dataPk == null)
            {
                return
                                (((Integer)FlexibleSearch.getInstance().search("SELECT count({" + Item.PK + "}) FROM {MEDIA} WHERE {" + Media.PK + "}<>?myPK AND {internalURL}=?hasdata AND {dataPK}=?myPK", params, Integer.class).getResult().get(0)).intValue() > 0);
            }
            return
                            (((Integer)FlexibleSearch.getInstance().search("SELECT count({" + Item.PK + "}) FROM {MEDIA} WHERE {" + Media.PK + "}<>?myPK AND {internalURL}=?hasdata AND ({dataPK}=?myDataPK OR ({dataPK} IS NULL AND {" + Media.PK + "}=?myDataPK))", params, Integer.class).getResult()
                                            .get(0)).intValue() > 0);
        }
        finally
        {
            getSession().removeLocalSessionContext();
        }
    }


    public StoredMediaData storeMedia(MediaDataStoreCommand command)
    {
        return command.execute();
    }


    public void deleteMedia(String folderQualifier, String location)
    {
        MediaStorageConfigService.MediaFolderConfig config = this.mediaStorageConfigService.getConfigForFolder(folderQualifier);
        MediaStorageStrategy strategy = this.mediaStorageRegistry.getStorageStrategyForFolder(config);
        strategy.delete(config, location);
        if(isStorageCacheConfigured(config))
        {
            this.localMediaFileCache.removeFromCache(config, location);
        }
    }


    public void deleteMediaDataUnlessReferenced(PK mediaPk, Long dataPk, String folderQualifier, String location)
    {
        Transaction tx = Transaction.current();
        if(tx.isRunning())
        {
            tx.executeOnCommit((Transaction.TransactionAwareExecution)new Object(this, mediaPk, dataPk, folderQualifier, location));
        }
        else if(!hasForeignDataOwners(mediaPk, dataPk))
        {
            deleteMedia(folderQualifier, location);
        }
    }


    public InputStreamWithSize getMediaAsStreamWithSize(String folderQualifier, String location)
    {
        InputStream inputStream;
        MediaStorageConfigService.MediaFolderConfig config = this.mediaStorageConfigService.getConfigForFolder(folderQualifier);
        if(isStorageCacheConfigured(config))
        {
            inputStream = this.localMediaFileCache.storeOrGetAsStream(config, location, this.streamGetter);
        }
        else
        {
            inputStream = this.streamGetter.getStream(config, location);
        }
        long size = this.streamGetter.getSize(config, location);
        return new InputStreamWithSize(inputStream, size);
    }


    public InputStream getMediaAsStream(String folderQualifier, String location)
    {
        return getMediaAsStreamWithSize(folderQualifier, location).getInputStream();
    }


    public InputStream getMediaAsStream(MediaSource mediaSource)
    {
        if(hasData(mediaSource))
        {
            return getMediaAsStream(mediaSource.getFolderQualifier(), mediaSource.getLocation());
        }
        return tryToGetStreamFromUrl(mediaSource);
    }


    private InputStream tryToGetStreamFromUrl(MediaSource mediaSource)
    {
        Preconditions.checkState(StringUtils.isNotEmpty(mediaSource.getInternalUrl()), "media does not have URL, cannot get the stream");
        try
        {
            URL url = null;
            String localMediaRootUrl = MediaUtil.getLocalMediaWebRootUrl();
            String resource = mediaSource.getInternalUrl();
            if(resource.startsWith(localMediaRootUrl))
            {
                resource = resource.substring(localMediaRootUrl.length());
                if(resource.charAt(0) == '/')
                {
                    resource = resource.substring(1);
                }
                if(resource.startsWith("fromjar"))
                {
                    resource = resource.substring("fromjar".length());
                    url = getClass().getResource(resource);
                }
            }
            if(url == null)
            {
                url = new URL(mediaSource.getInternalUrl());
            }
            return url.openStream();
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    public byte[] getMediaAsByteArray(String folderQualifier, String location) throws IOException
    {
        InputStream stream = null;
        try
        {
            stream = getMediaAsStream(folderQualifier, location);
            return IOUtils.toByteArray(stream);
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }
    }


    public byte[] tryToGetMediaAsByteArrayFromUrl(MediaSource media) throws IOException
    {
        return IOUtils.toByteArray(tryToGetStreamFromUrl(media));
    }


    public boolean hasData(MediaSource mediaSource)
    {
        return "replicated273654712".equals(mediaSource.getInternalUrl());
    }


    public MediaHeadersRegistry getMediaHeadersRegistry()
    {
        return this.mediaHeadersRegistry;
    }


    public boolean isZipRelatedMime(String mime)
    {
        return this.mimeService.isZipRelatedMime(mime);
    }


    @Deprecated(since = "2011", forRemoval = true)
    public void verifyMediaHashForLocation(String folderQualifier, String location, String storedHash)
    {
        this.locationHashService.verifyHashForLocation(storedHash, folderQualifier, location);
    }


    public MediaLocationHashService.HashType verifyMediaHash(String folderQualifier, String location, String storedHash, long size, String mime)
    {
        return this.locationHashService.verifyHash(storedHash, folderQualifier, location, size,
                        "-".equals(mime) ? null : mime);
    }


    public boolean isPathTargetSecuredFolder(String folderQualifier, String location)
    {
        Path path = Paths.get(location, new String[0]).normalize();
        if(path.getNameCount() < 1)
        {
            throw new MediaInvalidLocationException("Invalid media location");
        }
        String pathForMediaFolder = getPathForMediaFolder(folderQualifier);
        String pathFromLocation = path.subpath(0, 1).toString();
        if(!"root".equals(folderQualifier) && !pathFromLocation.equals(pathForMediaFolder))
        {
            throw new MediaInvalidLocationException("Location doesn't match folder qualifier");
        }
        return isFolderConfiguredAsSecured(folderQualifier);
    }


    private String getPathForMediaFolder(String folderQualifier)
    {
        Collection<MediaFolder> foundFolders = getMediaFolderByQualifier(folderQualifier);
        if(CollectionUtils.isNotEmpty(foundFolders))
        {
            return ((MediaFolder)foundFolders.iterator().next()).getPath();
        }
        return null;
    }


    public boolean isFolderConfiguredAsSecured(String folderQualifier)
    {
        Collection<String> securedFolders = this.mediaStorageConfigService.getSecuredFolders();
        return securedFolders.contains(folderQualifier);
    }


    public File getMediaAsFile(String folderQualifier, String location)
    {
        MediaStorageConfigService.MediaFolderConfig config = this.mediaStorageConfigService.getConfigForFolder(folderQualifier);
        MediaStorageStrategy strategy = this.mediaStorageRegistry.getStorageStrategyForFolder(config);
        try
        {
            return strategy.getAsFile(config, location);
        }
        catch(UnsupportedOperationException e)
        {
            if(isStorageCacheConfigured(config))
            {
                return this.localMediaFileCache.storeOrGetAsFile(config, location, this.streamGetter);
            }
            LOG.error("Local caching for folder '" + folderQualifier + "' is not configured properly. To get media as File configure folder with file cache.");
            throw e;
        }
    }


    private boolean isStorageCacheConfigured(MediaStorageConfigService.MediaFolderConfig config)
    {
        return (config.isLocalCacheEnabled() &&
                        !(this.mediaStorageRegistry.getStorageStrategyForFolder(config) instanceof de.hybris.platform.media.storage.LocalStoringStrategy));
    }


    public String getURLForMedia(String folderQualifier, MediaSource mediaSource)
    {
        MediaStorageConfigService.MediaFolderConfig config = this.mediaStorageConfigService.getConfigForFolder(folderQualifier);
        if(config.isSecured())
        {
            return handleSecureURL(mediaSource);
        }
        return handlePublicURL(config, mediaSource);
    }


    public String getDownloadURLForMedia(String folderQualifier, MediaSource mediaSource)
    {
        MediaStorageConfigService.MediaFolderConfig config = this.mediaStorageConfigService.getConfigForFolder(folderQualifier);
        if(config.isSecured())
        {
            return handleSecureDownloadURL(mediaSource);
        }
        return handlePublicDownloadURL(config, mediaSource);
    }


    private String handlePublicURL(MediaStorageConfigService.MediaFolderConfig config, MediaSource mediaSource)
    {
        if(hasData(mediaSource))
        {
            MediaURLStrategy strategy = this.mediaStorageRegistry.getURLStrategyForFolder(config, getPreferredUrlStrategyIds());
            String rawUrl = strategy.getUrlForMedia(config, mediaSource);
            if(strategy instanceof LocalMediaWebURLStrategy)
            {
                String url = MediaUtil.assemblePublicMediaURL(rawUrl);
                return (url == null) ? rawUrl : url;
            }
            return rawUrl;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Media has no data, using internal URL [mediaSource" + mediaSource + "]");
        }
        return mediaSource.getInternalUrl();
    }


    private String handlePublicDownloadURL(MediaStorageConfigService.MediaFolderConfig config, MediaSource mediaSource)
    {
        if(hasData(mediaSource))
        {
            MediaURLStrategy strategy = this.mediaStorageRegistry.getURLStrategyForFolder(config, getPreferredUrlStrategyIds());
            if(strategy instanceof LocalMediaWebURLStrategy)
            {
                String rawUrl = ((LocalMediaWebURLStrategy)strategy).getDownloadUrlForMedia(config, mediaSource);
                String url = MediaUtil.assemblePublicMediaURL(rawUrl);
                return (url == null) ? rawUrl : url;
            }
            return strategy.getUrlForMedia(config, mediaSource);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Media has no data, using internal URL [mediaSource" + mediaSource + "]");
        }
        return mediaSource.getInternalUrl();
    }


    private String handleSecureURL(MediaSource mediaSource)
    {
        String secureUrl = MediaUtil.assembleSecureMediaURL(mediaSource);
        if(secureUrl == null)
        {
            LOG.warn("No secure media renderer available in current context. Media PK:" + mediaSource.getMediaPk() + ". No URL will be rendered.");
            return "";
        }
        return secureUrl;
    }


    private String handleSecureDownloadURL(MediaSource mediaSource)
    {
        String secureUrl = MediaUtil.assembleSecureMediaURL(mediaSource);
        if(secureUrl == null)
        {
            LOG.warn("No secure media renderer available in current context. Media PK:" + mediaSource.getMediaPk() + ". No URL will be rendered.");
            return "";
        }
        return secureUrl + "&attachment=true";
    }


    protected MediaSource createMediaSource(AbstractMedia media)
    {
        return (MediaSource)new JaloMediaSource(media);
    }


    public void initializeMediaStorage(boolean freshInit)
    {
        for(Map.Entry<String, MediaStorageInitializer> entry : this.storageInitializers.entrySet())
        {
            MediaStorageInitializer initializer = entry.getValue();
            if(initializer != null)
            {
                try
                {
                    initializer.checkStorageConnection();
                    if(freshInit)
                    {
                        initializer.onInitialize();
                        continue;
                    }
                    initializer.onUpdate();
                }
                catch(Exception exc)
                {
                    if(initializer.failOnInitUpdateError())
                    {
                        LOG.error("Media storage could not be initialized. Init/Update process will be stopped.");
                        throw exc;
                    }
                    LOG.warn("Problems with media storage connection occured. You may observe problems with storing medias into external storage.");
                }
            }
        }
    }


    public boolean isSecuredFolder(String folderQualifier)
    {
        MediaStorageConfigService.MediaFolderConfig config = this.mediaStorageConfigService.getConfigForFolder(folderQualifier);
        return config.isSecured();
    }


    private Collection<String> getPreferredUrlStrategyIds()
    {
        SessionContext sessionContext = getSession().getSessionContext();
        Object preferredUrlStrategyId = sessionContext.getAttribute("preferredUrlStrategyId");
        if(preferredUrlStrategyId instanceof Collection)
        {
            return (Collection<String>)preferredUrlStrategyId;
        }
        if(preferredUrlStrategyId instanceof String)
        {
            return Collections.singleton((String)preferredUrlStrategyId);
        }
        return Collections.emptyList();
    }


    public static MediaManager getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getMediaManager();
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
    }


    protected String getMediaTypeCode()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(Media.class).getCode();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "Media type code not found", 0);
        }
    }


    public Media createMedia(String code)
    {
        return createMedia(null, code, null);
    }


    public Media createMedia(PK pk, String code)
    {
        return createMedia(pk, code, null);
    }


    public Media createMedia(String code, ComposedType type)
    {
        return createMedia(null, code, type);
    }


    public Media createMedia(PK pkBase, String code, ComposedType type)
    {
        try
        {
            ComposedType adjustedType;
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put(Item.PK, pkBase);
            params.put("code", code);
            params.put("folder", getRootMediaFolder());
            if(type == null)
            {
                adjustedType = TypeManager.getInstance().getComposedType(Media.class);
            }
            else
            {
                adjustedType = type;
            }
            return (Media)adjustedType.newInstance(getSession().getSessionContext(), (Map)params);
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllMedias()
    {
        return getAllMedia();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllMedia()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                        getMediaTypeCode() + "}", null, Collections.singletonList(Media.class), false, false, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getMediasByCode(String searchString)
    {
        return getMediaByCode(searchString);
    }


    public Collection getMediaByCode(String searchString)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("code", searchString);
        StringBuilder query = new StringBuilder("SELECT {" + Item.PK + "} FROM {" + getMediaTypeCode() + "} WHERE {code} ");
        query.append(SearchTools.isLIKEPattern(searchString) ? "LIKE ?code" : "= ?code");
        SearchResult res = getSession().getFlexibleSearch().search(query.toString(), values,
                        Collections.singletonList(Media.class), false, false, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getMediasByMimeType(String mimePattern)
    {
        return getMediaByMimeType(mimePattern);
    }


    public Collection getMediaByMimeType(String mimePattern)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("mime", mimePattern);
        StringBuilder query = new StringBuilder("SELECT {" + Item.PK + "} FROM {" + getMediaTypeCode() + "} WHERE {mime} ");
        query.append(SearchTools.isLIKEPattern(mimePattern) ? "LIKE ?mime" : "= ?mime");
        SearchResult res = getSession().getFlexibleSearch().search(query.toString(), values,
                        Collections.singletonList(Media.class), false, false, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getMediaByURL(String urlPattern)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("internalURL", urlPattern);
        StringBuilder query = new StringBuilder("SELECT {" + Item.PK + "} FROM {" + getMediaTypeCode() + "} WHERE {internalURL} ");
        query.append(SearchTools.isLIKEPattern(urlPattern) ? "LIKE ?internalURL" : "= ?internalURL");
        SearchResult res = getSession().getFlexibleSearch().search(query.toString(), values,
                        Collections.singletonList(Media.class), false, false, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getMediaByURLExact(String url)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("internalURL", url);
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                        getMediaTypeCode() + "} WHERE {internalURL} = ?internalURL", values,
                        Collections.singletonList(Media.class), false, false, 0, -1);
        return res.getResult();
    }


    public String getMimeTypeByExtension(String fileExtension)
    {
        return Config.getString("mediatype.by.fileextension." + fileExtension.toLowerCase(), "mediatype.by.fileextension.bin");
    }


    public MediaFolder createMediaFolder(String qualifier, String path)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("qualifier", qualifier);
        map.put("path", path);
        return createMediaFolder(map);
    }


    public MediaFolder createMediaFolder(Map<String, Object> attributeValues)
    {
        return createMediaFolder(getSession().getSessionContext(), attributeValues);
    }


    public MediaFolder createMediaFolder(SessionContext ctx, Map<String, Object> attributeValues)
    {
        try
        {
            ComposedType type = TypeManager.getInstance().getComposedType(GeneratedCoreConstants.TC.MEDIAFOLDER);
            return (MediaFolder)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ? (RuntimeException)cause : new JaloSystemException(cause, cause
                            .getMessage(), e
                            .getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaFolder : " + e.getMessage(), 0);
        }
    }


    public Collection<MediaFolder> getMediaFolderByQualifier(String searchString)
    {
        try
        {
            Map<Object, Object> values = new HashMap<>();
            values.put("qualifier", searchString);
            StringBuilder query = new StringBuilder("SELECT {" + Item.PK + "} FROM {" + getSession().getTypeManager().getComposedType(MediaFolder.class).getCode() + "} WHERE {qualifier} ");
            query.append(
                            SearchTools.isLIKEPattern(searchString) ? "LIKE ?qualifier" : "= ?qualifier");
            SearchResult res = getSession().getFlexibleSearch().search(query.toString(), values,
                            Collections.singletonList(Media.class), false, false, 0, -1);
            return res.getResult();
        }
        catch(JaloItemNotFoundException e)
        {
            return Collections.EMPTY_LIST;
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<MediaFolder> getAllMediaFolders()
    {
        StringBuilder query = new StringBuilder("SELECT {" + Item.PK + "} FROM {" + getSession().getTypeManager().getComposedType(MediaFolder.class).getCode() + "} ORDER BY {qualifier} ");
        SearchResult res = getSession().getFlexibleSearch().search(query.toString(), Collections.EMPTY_MAP,
                        Collections.singletonList(Media.class), false, false, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public MediaFolder getRootMediaFolder()
    {
        Collection<MediaFolder> folders = getMediaFolderByQualifier("root");
        if(folders.isEmpty())
        {
            throw new JaloSystemException("Root media folder does not exist!");
        }
        MediaFolder rootFolder = folders.iterator().next();
        if(!"root".equals(rootFolder.getQualifier()))
        {
            throw new JaloSystemException("Root media folder does not exist!");
        }
        return rootFolder;
    }


    public MediaFolder getOrCreateRootMediaFolder()
    {
        MediaFolder rootFolder;
        try
        {
            rootFolder = getRootMediaFolder();
        }
        catch(JaloSystemException e)
        {
            rootFolder = getInstance().createMediaFolder("root", null);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("created the root folder " + rootFolder);
            }
        }
        return rootFolder;
    }


    public MediaContainer createMediaContainer(String qualifier)
    {
        return createMediaContainer(Collections.singletonMap("qualifier", qualifier));
    }


    public MediaContainer createMediaContainer(Map<String, Object> attributeValues)
    {
        return createMediaContainer(getSession().getSessionContext(), attributeValues);
    }


    public MediaContainer createMediaContainer(SessionContext ctx, Map<String, Object> attributeValues)
    {
        try
        {
            ComposedType type = TypeManager.getInstance().getComposedType(GeneratedCoreConstants.TC.MEDIACONTAINER);
            return (MediaContainer)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ? (RuntimeException)cause : new JaloSystemException(cause, cause
                            .getMessage(), e
                            .getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaContainer : " + e.getMessage(), 0);
        }
    }


    public MediaFormat createMediaFormat(String qualifier)
    {
        return createMediaFormat(Collections.singletonMap("qualifier", qualifier));
    }


    public MediaFormat createMediaFormat(Map<String, Object> attributeValues)
    {
        return createMediaFormat(getSession().getSessionContext(), attributeValues);
    }


    public MediaFormat createMediaFormat(SessionContext ctx, Map<String, Object> attributeValues)
    {
        try
        {
            ComposedType type = TypeManager.getInstance().getComposedType(GeneratedCoreConstants.TC.MEDIAFORMAT);
            return (MediaFormat)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ? (RuntimeException)cause : new JaloSystemException(cause, cause
                            .getMessage(), e
                            .getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaFormat : " + e.getMessage(), 0);
        }
    }


    public MediaContext createMediaContext(String qualifier)
    {
        return createMediaContext(Collections.singletonMap("qualifier", qualifier));
    }


    public MediaContext createMediaContext(Map<String, Object> attributeValues)
    {
        return createMediaContext(getSession().getSessionContext(), attributeValues);
    }


    public MediaContext createMediaContext(SessionContext ctx, Map<String, Object> attributeValues)
    {
        try
        {
            ComposedType type = TypeManager.getInstance().getComposedType(GeneratedCoreConstants.TC.MEDIACONTEXT);
            return (MediaContext)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ? (RuntimeException)cause : new JaloSystemException(cause, cause
                            .getMessage(), e
                            .getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaContext : " + e.getMessage(), 0);
        }
    }


    public MediaFormatMapping createMediaFormatMapping(MediaFormat source, MediaFormat target)
    {
        Map<String, Object> values = new HashMap<>();
        values.put("source", source);
        values.put("target", target);
        return createMediaFormatMapping(values);
    }


    public MediaFormatMapping createMediaFormatMapping(Map<String, Object> attributeValues)
    {
        return createMediaFormatMapping(getSession().getSessionContext(), attributeValues);
    }


    public MediaFormatMapping createMediaFormatMapping(SessionContext ctx, Map<String, Object> attributeValues)
    {
        try
        {
            ComposedType type = TypeManager.getInstance().getComposedType(GeneratedCoreConstants.TC.MEDIAFORMATMAPPING);
            return (MediaFormatMapping)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ? (RuntimeException)cause : new JaloSystemException(cause, cause
                            .getMessage(), e
                            .getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaFormatMapping : " + e.getMessage(), 0);
        }
    }


    public Media createMedia(String code, MediaFormat format)
    {
        try
        {
            Item.ItemAttributeMap params = new Item.ItemAttributeMap();
            params.put("code", code);
            params.put("mediaFormat", format);
            return (Media)TypeManager.getInstance().getComposedType(Media.class)
                            .newInstance(getSession().getSessionContext(), (Map)params);
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List<MediaContext> getAllMediaContexts()
    {
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" + GeneratedCoreConstants.TC.MEDIACONTEXT + "} ORDER BY {" + MediaContext.CREATION_TIME + "} ASC, {" + MediaContext.PK + "} ASC", null,
                        Collections.singletonList(MediaContext.class), false, false, 0, -1);
        return res.getResult();
    }


    public MediaFormat getMediaFormatByQualifier(String qualifier)
    {
        return (MediaFormat)getFirstItemByAttribute(MediaFormat.class, "qualifier", qualifier);
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new MediaManagerSerializableDTO(getTenant());
    }
}
