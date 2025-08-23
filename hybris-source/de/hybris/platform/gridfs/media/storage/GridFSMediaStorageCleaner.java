package de.hybris.platform.gridfs.media.storage;

import com.mongodb.DB;
import de.hybris.platform.core.Registry;
import de.hybris.platform.media.exceptions.ExternalStorageServiceException;
import de.hybris.platform.media.services.MediaStorageInitializer;
import de.hybris.platform.media.storage.MediaStorageConfigService;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.mongodb.MongoDbFactory;

public class GridFSMediaStorageCleaner implements MediaStorageInitializer
{
    private static final Logger LOG = LoggerFactory.getLogger(GridFSMediaStorageCleaner.class);
    private static final String GRID_FS_STORAGE_STRATEGY = "gridFsStorageStrategy";
    private boolean cleanOnInit = true;
    private MongoDbFactory dbFactory;
    private MediaStorageConfigService storageConfigService;
    protected String tenantPrefix;


    public void setTenantPrefix()
    {
        this.tenantPrefix = "sys_" + Registry.getCurrentTenantNoFallback().getTenantID().toLowerCase();
    }


    public void onInitialize()
    {
        if(this.cleanOnInit && this.storageConfigService.isStorageStrategyConfigured("gridFsStorageStrategy"))
        {
            try
            {
                DB mongoDb = this.dbFactory.getLegacyDb();
                Set<String> collections = mongoDb.getCollectionNames();
                for(String collectionName : collections)
                {
                    if(collectionName.startsWith(this.tenantPrefix))
                    {
                        mongoDb.getCollection(collectionName).drop();
                    }
                }
            }
            catch(Exception e)
            {
                String reason = (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage();
                String msg = "Problem with MongoDB communication, storage will not be cleaned (reason:" + reason + ")";
                LOG.warn(msg);
                LOG.debug(msg, e);
                throw new ExternalStorageServiceException(reason, e);
            }
        }
        else
        {
            LOG.debug("GridFS storage is not configured properly in properties or clean on init feature is disabled in properties file. Cleaning storage has been skipped.");
        }
    }


    public void checkStorageConnection()
    {
        try
        {
            if(this.storageConfigService.isStorageStrategyConfigured("gridFsStorageStrategy"))
            {
                this.dbFactory.getLegacyDb().getStats();
            }
        }
        catch(Exception e)
        {
            String reason = (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage();
            throw new ExternalStorageServiceException(reason, e);
        }
    }


    public void setCleanOnInit(boolean cleanOnInit)
    {
        this.cleanOnInit = cleanOnInit;
    }


    @Required
    public void setDbFactory(MongoDbFactory dbFactory)
    {
        this.dbFactory = dbFactory;
    }


    @Required
    public void setStorageConfigService(MediaStorageConfigService storageConfigService)
    {
        this.storageConfigService = storageConfigService;
    }


    public void onUpdate()
    {
    }
}
