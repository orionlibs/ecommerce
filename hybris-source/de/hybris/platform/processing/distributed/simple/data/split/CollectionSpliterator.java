package de.hybris.platform.processing.distributed.simple.data.split;

import de.hybris.platform.processing.distributed.simple.data.SimpleBatchCreationData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Spliterators;
import java.util.function.Consumer;

public class CollectionSpliterator extends Spliterators.AbstractSpliterator<SimpleBatchCreationData>
{
    protected int page;
    protected int batchSize;
    protected int elementsSize;
    protected List<?> elements;


    public CollectionSpliterator(Collection<?> elements, int batchSize)
    {
        super(Long.MAX_VALUE, 256);
        this.page = 0;
        this.elements = new ArrayList(elements);
        this.batchSize = batchSize;
        this.elementsSize = elements.size();
    }


    public boolean tryAdvance(Consumer<? super SimpleBatchCreationData> action)
    {
        Collection<?> nextBatch = getNextBatch();
        if(nextBatch.isEmpty())
        {
            return false;
        }
        action.accept(new SimpleBatchCreationData(new ArrayList(nextBatch)));
        return true;
    }


    private Collection<?> getNextBatch()
    {
        int fromIdx = this.page * this.batchSize;
        int toIdx = (this.page + 1) * this.batchSize;
        toIdx = (toIdx < this.elementsSize) ? toIdx : this.elementsSize;
        List<?> toReturn = (fromIdx < this.elementsSize) ? this.elements.subList(fromIdx, toIdx) : Collections.emptyList();
        this.page++;
        return toReturn;
    }
}
