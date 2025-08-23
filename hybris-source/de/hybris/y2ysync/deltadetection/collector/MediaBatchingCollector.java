package de.hybris.y2ysync.deltadetection.collector;

import com.google.common.collect.Lists;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.springframework.util.SerializationUtils;

public class MediaBatchingCollector implements BatchingCollector
{
    private final int batchSize;
    private final MediaService mediaService;
    private final ModelService modelService;
    private final List<PK> createdMedias = Lists.newArrayList();
    private final List<ItemChangeDTO> currentBatch = Lists.newArrayList();
    private final String mediaCodePrefix;
    private String collectorId;
    private int batchCounter = 0;


    public MediaBatchingCollector(String mediaCodePrefix, int batchSize, ModelService modelService, MediaService mediaService)
    {
        this.mediaCodePrefix = mediaCodePrefix;
        this.batchSize = batchSize;
        this.modelService = modelService;
        this.mediaService = mediaService;
    }


    public boolean collect(ItemChangeDTO change)
    {
        this.currentBatch.add(change);
        if(this.currentBatch.size() == this.batchSize)
        {
            dumpBatchToMedia();
        }
        return true;
    }


    public void finish()
    {
        dumpBatchToMedia();
    }


    private void dumpBatchToMedia()
    {
        if(this.currentBatch.isEmpty())
        {
            return;
        }
        CatalogUnawareMediaModel media = createMediaInDb();
        byte[] serialized = SerializationUtils.serialize(this.currentBatch);
        this.mediaService.setDataForMedia((MediaModel)media, serialized);
        this.createdMedias.add(media.getPk());
        this.currentBatch.clear();
        this.batchCounter++;
    }


    private CatalogUnawareMediaModel createMediaInDb()
    {
        CatalogUnawareMediaModel media = (CatalogUnawareMediaModel)this.modelService.create(CatalogUnawareMediaModel.class);
        String code = this.mediaCodePrefix + "-" + this.mediaCodePrefix + "-" + this.collectorId;
        media.setCode(code);
        media.setMime("application/x-java-serialized-object");
        media.setRealFileName(code);
        this.modelService.save(media);
        return media;
    }


    public List<PK> getPksOfBatches()
    {
        return this.createdMedias;
    }


    public void setId(String id)
    {
        this.collectorId = id;
    }
}
