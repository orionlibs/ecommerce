package de.hybris.platform.processing.distributed.simple.data.split;

import de.hybris.platform.core.Registry;
import de.hybris.platform.processing.distributed.simple.data.QueryBasedCreationData;
import de.hybris.platform.processing.distributed.simple.data.SimpleBatchCreationData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterators;
import java.util.function.Consumer;

public class FlexibleSearchResultSpliterator extends Spliterators.AbstractSpliterator<SimpleBatchCreationData>
{
    private CollectionSpliterator collectionSpliterator;
    private final FlexibleSearchService flexibleSearchService;
    private final SessionService sessionService;
    private final FlexibleSearchQuery fQuery;
    private final QueryBasedCreationData.QueryHook beforeQueryHook;
    private final boolean useDatabasePaging;
    private final int batchSize;


    public FlexibleSearchResultSpliterator(FlexibleSearchQuery fQuery, QueryBasedCreationData.QueryHook beforeQueryHook, int batchSize, boolean useDatabasePaging)
    {
        super(Long.MAX_VALUE, 256);
        this
                        .flexibleSearchService = (FlexibleSearchService)Registry.getApplicationContext().getBean("flexibleSearchService", FlexibleSearchService.class);
        this.sessionService = (SessionService)Registry.getApplicationContext().getBean("sessionService", SessionService.class);
        this.fQuery = fQuery;
        this.beforeQueryHook = beforeQueryHook;
        this.batchSize = batchSize;
        this.useDatabasePaging = useDatabasePaging;
    }


    public boolean tryAdvance(Consumer<? super SimpleBatchCreationData> action)
    {
        if(this.useDatabasePaging)
        {
            return tryFromDatabase(action);
        }
        return tryFromList(action);
    }


    protected boolean tryFromDatabase(Consumer<? super SimpleBatchCreationData> action)
    {
        List<?> nextBatch = getNextBatch();
        if(nextBatch.isEmpty())
        {
            return false;
        }
        action.accept(new SimpleBatchCreationData(new ArrayList(nextBatch)));
        return true;
    }


    protected boolean tryFromList(Consumer<? super SimpleBatchCreationData> action)
    {
        if(this.collectionSpliterator == null)
        {
            runBeforeQueryHook();
            List<?> result = this.flexibleSearchService.search(this.fQuery).getResult();
            this.collectionSpliterator = new CollectionSpliterator(result, this.batchSize);
        }
        return this.collectionSpliterator.tryAdvance(action);
    }


    protected List<?> getNextBatch()
    {
        return (List)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this));
    }


    protected void runBeforeQueryHook()
    {
        if(this.beforeQueryHook == null)
        {
            return;
        }
        this.beforeQueryHook.apply();
    }
}
