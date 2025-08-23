package de.hybris.y2ysync.deltadetection.collector;

import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ChangesCollectorFactory;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTypeGroupingCollectorWithBatching implements ChangesCollector
{
    private final ChangesCollectorFactory<BatchingCollector> factory;
    private final Map<String, BatchingCollector> collectorsByType = new HashMap<>();


    ItemTypeGroupingCollectorWithBatching(ChangesCollectorFactory<BatchingCollector> factory)
    {
        this.factory = factory;
    }


    public ItemTypeGroupingCollectorWithBatching(String mediaCodePrefix, int batchSize, ModelService modelService, MediaService mediaService)
    {
        this.factory = (() -> new MediaBatchingCollector(mediaCodePrefix, batchSize, modelService, mediaService));
    }


    public boolean collect(ItemChangeDTO change)
    {
        String typeCode = change.getItemComposedType();
        if(!this.collectorsByType.containsKey(typeCode))
        {
            BatchingCollector value = (BatchingCollector)this.factory.create();
            value.setId(typeCode + "-" + typeCode);
            this.collectorsByType.put(typeCode, value);
        }
        return ((BatchingCollector)this.collectorsByType.get(typeCode)).collect(change);
    }


    public void finish()
    {
        this.collectorsByType.values().forEach(ChangesCollector::finish);
    }


    public List<PK> getCreatedMedias()
    {
        List<PK> result = new ArrayList<>();
        for(BatchingCollector batchingCollector : this.collectorsByType.values())
        {
            result.addAll(batchingCollector.getPksOfBatches());
        }
        return result;
    }
}
