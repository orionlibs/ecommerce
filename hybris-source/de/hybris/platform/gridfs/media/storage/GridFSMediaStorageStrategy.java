package de.hybris.platform.gridfs.media.storage;

import com.google.common.base.Preconditions;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import de.hybris.platform.core.Registry;
import de.hybris.platform.media.exceptions.MediaNotFoundException;
import de.hybris.platform.media.services.MediaLocationHashService;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import de.hybris.platform.media.storage.MediaStorageStrategy;
import de.hybris.platform.media.storage.impl.StoredMediaData;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.MongoDbFactory;

public class GridFSMediaStorageStrategy implements MediaStorageStrategy
{
    private static final String BUCKET_ID_KEY = "bucketId";
    private final MediaLocationHashService mediaLocationhashService;
    protected String tenantPrefix;
    private final ConcurrentHashMap<String, GridFS> gridFSPool;
    private final MongoDbFactory dbFactory;


    public GridFSMediaStorageStrategy(MongoDbFactory dbFactory, MediaLocationHashService mediaLocationHashService)
    {
        this.dbFactory = dbFactory;
        this.mediaLocationhashService = mediaLocationHashService;
        this.gridFSPool = new ConcurrentHashMap<>();
    }


    public void setTenantPrefix()
    {
        this.tenantPrefix = "sys_" + Registry.getCurrentTenantNoFallback().getTenantID().toLowerCase();
    }


    public StoredMediaData store(MediaStorageConfigService.MediaFolderConfig config, String mediaId, Map<String, Object> metaData, InputStream dataStream)
    {
        Preconditions.checkArgument((config != null), "config is required!");
        Preconditions.checkArgument((mediaId != null), "mediaId is required!");
        Preconditions.checkArgument((metaData != null), "metaData is required!");
        Preconditions.checkArgument((dataStream != null), "dataStream is required!");
        String location = assembleLocation(mediaId, (String)metaData.get("fileName"));
        GridFSInputFile file = getGridFS(config).createFile(dataStream, location, true);
        String mime = (String)metaData.get("mime");
        if(StringUtils.isNotBlank(mime))
        {
            file.setContentType(mime);
        }
        file.save();
        return new StoredMediaData(location, this.mediaLocationhashService
                        .createHash(config.getFolderQualifier(), location, file.getLength(), mime), file
                        .getLength(), mime);
    }


    private String assembleLocation(String mediaId, String realFileName)
    {
        StringBuilder builder = new StringBuilder(mediaId);
        if(StringUtils.isNotEmpty(realFileName))
        {
            builder.append("/").append(realFileName);
        }
        return builder.toString();
    }


    public void delete(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        Preconditions.checkArgument((config != null), "config is required!");
        Preconditions.checkArgument((location != null), "location is required!");
        getGridFS(config).remove(location);
    }


    public InputStream getAsStream(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        Preconditions.checkArgument((config != null), "config is required!");
        Preconditions.checkArgument((location != null), "location is required!");
        GridFSDBFile file = getGridFS(config).findOne(location);
        if(file == null)
        {
            throw new MediaNotFoundException("Media not found: " + location);
        }
        return file.getInputStream();
    }


    private GridFS getGridFS(MediaStorageConfigService.MediaFolderConfig config)
    {
        String bucketName = computeBucketName(config);
        GridFS gridFS = this.gridFSPool.get(bucketName);
        if(gridFS != null)
        {
            return gridFS;
        }
        gridFS = buildGridFsInstance(bucketName);
        GridFS oldGridFs = this.gridFSPool.putIfAbsent(bucketName, gridFS);
        if(oldGridFs != null)
        {
            return oldGridFs;
        }
        return gridFS;
    }


    protected GridFS buildGridFsInstance(String bucketName)
    {
        return new GridFS(this.dbFactory.getLegacyDb(), bucketName);
    }


    private String computeBucketName(MediaStorageConfigService.MediaFolderConfig config)
    {
        String configuredBucket = getValueFromCustomSettings(config, "bucketId");
        if(StringUtils.isNotBlank(configuredBucket))
        {
            return this.tenantPrefix + "_" + this.tenantPrefix;
        }
        return this.tenantPrefix + "_" + this.tenantPrefix;
    }


    private String getValueFromCustomSettings(MediaStorageConfigService.MediaFolderConfig config, String configKey)
    {
        String value = (String)config.getParameter(configKey, String.class);
        return value;
    }


    public File getAsFile(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        throw new UnsupportedOperationException("Obtaining media as file is not supported for GridFS storage. Use getMediaAsStream method.");
    }


    public long getSize(MediaStorageConfigService.MediaFolderConfig config, String location)
    {
        GridFSDBFile file = getGridFS(config).findOne(location);
        if(file == null)
        {
            throw new MediaNotFoundException("Media not found: " + location);
        }
        return file.getLength();
    }
}
