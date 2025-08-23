package de.hybris.platform.processing.distributed.defaultimpl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.processing.enums.BatchType;
import de.hybris.platform.processing.enums.DistributedProcessState;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RemainingWorkloadBasedProgressCalculator
{
    private final FlexibleSearchService flexibleSearch;
    private final DistributedProcessModel process;


    public RemainingWorkloadBasedProgressCalculator(DistributedProcessModel process, FlexibleSearchService flexibleSearch)
    {
        Objects.requireNonNull(process, "process must be given");
        Objects.requireNonNull(flexibleSearch, "flexibleSearch must be given");
        this.flexibleSearch = flexibleSearch;
        this.process = process;
    }


    public double calculateProgress()
    {
        if(this.process.getState() == DistributedProcessState.CREATED || this.process.getPk() == null)
        {
            return 0.0D;
        }
        long initialWorkload = calculateInitialWorkload();
        if(initialWorkload <= 0.0D)
        {
            return 0.0D;
        }
        long consumedWorkload = calculateConsumedWorkload();
        double progress = BigInteger.valueOf(10000L).multiply(BigInteger.valueOf(consumedWorkload)).divide(BigInteger.valueOf(initialWorkload)).doubleValue() / 100.0D;
        return Math.min(Math.max(0.0D, progress), 100.0D);
    }


    protected long calculateInitialWorkload()
    {
        String query = "select sum({remainingWorkLoad}) from {Batch} where {process}=?process and {type}=?type";
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery("select sum({remainingWorkLoad}) from {Batch} where {process}=?process and {type}=?type", (Map)ImmutableMap.of("process", this.process, "type", BatchType.INITIAL));
        fsQuery.setDisableCaching(true);
        fsQuery.setResultClassList((List)ImmutableList.of(Long.class));
        Long queryResult = this.flexibleSearch.search(fsQuery).getResult().get(0);
        return (queryResult == null) ? 0L : queryResult.longValue();
    }


    protected long calculateConsumedWorkload()
    {
        String query = "select sum({input.remainingWorkLoad} - {result.remainingWorkLoad}) from {Batch as input join Batch as result on ({input.id}={result.id} and {input.executionId}={result.executionId} and {result.type}=?resultType and {result.process}=?process)} where {input.type}=?inputType and {input.process}=?process";
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(
                        "select sum({input.remainingWorkLoad} - {result.remainingWorkLoad}) from {Batch as input join Batch as result on ({input.id}={result.id} and {input.executionId}={result.executionId} and {result.type}=?resultType and {result.process}=?process)} where {input.type}=?inputType and {input.process}=?process",
                        (Map)ImmutableMap.of("process", this.process, "inputType", BatchType.INPUT, "resultType", BatchType.RESULT));
        fsQuery.setDisableCaching(true);
        fsQuery.setResultClassList((List)ImmutableList.of(Long.class));
        Long queryResult = this.flexibleSearch.search(fsQuery).getResult().get(0);
        return (queryResult == null) ? 0L : queryResult.longValue();
    }
}
