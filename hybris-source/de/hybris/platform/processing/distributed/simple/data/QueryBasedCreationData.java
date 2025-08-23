package de.hybris.platform.processing.distributed.simple.data;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.core.PK;
import de.hybris.platform.processing.distributed.simple.data.split.FlexibleSearchResultSpliterator;
import de.hybris.platform.processing.model.SimpleDistributedProcessModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class QueryBasedCreationData extends SimpleAbstractDistributedProcessCreationData
{
    private boolean useDatabasePaging = false;
    private final QueryHook beforeQueryHook;
    private final FlexibleSearchQuery fQuery;


    protected QueryBasedCreationData(String handlerId, String processId, String nodeGroup, Integer batchSize, Integer numOfRetries, String query, Map<String, Object> queryParams, List<Class<?>> resultClasses, FlexibleSearchQuery flexibleSearchQuery, String scriptCode, boolean useDatabasePaging,
                    QueryHook beforeQueryHook, Class<? extends SimpleDistributedProcessModel> processModelClass)
    {
        super(handlerId, processId, scriptCode, nodeGroup, batchSize, numOfRetries, processModelClass);
        this
                        .fQuery = (flexibleSearchQuery == null) ? getFlexibleSearchQuery(query, queryParams, resultClasses, 0, useDatabasePaging ? getBatchSize() : -1) : flexibleSearchQuery;
        this.beforeQueryHook = beforeQueryHook;
        this.useDatabasePaging = useDatabasePaging;
    }


    private FlexibleSearchQuery getFlexibleSearchQuery(String query, Map<String, Object> queryParams, List<Class<?>> resultClasses, int start, int count)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query, queryParams);
        fQuery.setResultClassList((resultClasses == null) ? (List)ImmutableList.of(PK.class) : resultClasses);
        fQuery.setStart(start);
        fQuery.setCount(count);
        return fQuery;
    }


    public Stream<? extends SimpleBatchCreationData> initialBatches()
    {
        FlexibleSearchResultSpliterator spliterator = new FlexibleSearchResultSpliterator(this.fQuery, this.beforeQueryHook, getBatchSize(), this.useDatabasePaging);
        return StreamSupport.stream((Spliterator<? extends SimpleBatchCreationData>)spliterator, false);
    }


    public static Builder builder()
    {
        return new Builder();
    }
}
