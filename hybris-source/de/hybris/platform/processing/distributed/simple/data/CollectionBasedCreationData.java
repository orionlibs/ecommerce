package de.hybris.platform.processing.distributed.simple.data;

import de.hybris.platform.processing.distributed.simple.data.split.CollectionSpliterator;
import de.hybris.platform.processing.model.SimpleDistributedProcessModel;
import java.util.Collection;
import java.util.Objects;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CollectionBasedCreationData extends SimpleAbstractDistributedProcessCreationData
{
    protected Collection<?> elements;


    protected CollectionBasedCreationData(String handlerId, String processId, String scriptCode, String nodeGroup, Integer batchSize, Integer numOfRetries, Collection<?> elements, Class<? extends SimpleDistributedProcessModel> processModelClass)
    {
        super(handlerId, processId, scriptCode, nodeGroup, batchSize, numOfRetries, processModelClass);
        this.elements = Objects.<Collection>requireNonNull(elements, "elements to split are required");
    }


    public Stream<? extends SimpleBatchCreationData> initialBatches()
    {
        CollectionSpliterator spliterator = new CollectionSpliterator(this.elements, this.batchSize);
        return StreamSupport.stream((Spliterator<? extends SimpleBatchCreationData>)spliterator, false);
    }


    public static Builder builder()
    {
        return new Builder();
    }
}
