package de.hybris.platform.task.impl;

import de.hybris.platform.task.impl.gateways.WorkerStateGateway;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultWorkerHelper implements WorkerHelper
{
    public Map<Integer, WorkerStateGateway.WorkerRange> calculateRanges(List<WorkerStateGateway.WorkerState> activeWorkers, int rangeStart, int rangeEnd)
    {
        Map<Boolean, List<WorkerStateGateway.WorkerState>> partitionedWorkersIsExclusive = (Map<Boolean, List<WorkerStateGateway.WorkerState>>)activeWorkers.stream().collect(Collectors.partitioningBy(WorkerStateGateway.WorkerState::isExclusiveMode));
        Map<Integer, WorkerStateGateway.WorkerRange> ranges = new HashMap<>(getRangesForExclusiveWorkers(rangeStart, rangeEnd, partitionedWorkersIsExclusive.get(Boolean.valueOf(true))));
        ranges.putAll(getRangesForNonExclusiveWorkers(rangeStart, rangeEnd, partitionedWorkersIsExclusive.get(Boolean.valueOf(false))));
        return ranges;
    }


    private static Map<? extends Integer, ? extends WorkerStateGateway.WorkerRange> getRangesForNonExclusiveWorkers(int rangeStart, int rangeEnd, List<WorkerStateGateway.WorkerState> activeWorkersNotExclusive)
    {
        Map<Integer, WorkerStateGateway.WorkerRange> range = new HashMap<>();
        int left = rangeEnd - rangeStart;
        int currentBucketRangeStart = rangeStart;
        for(int i = 0, n = activeWorkersNotExclusive.size(); i < n; i++)
        {
            int j = n - i;
            int bucketSize = left / j;
            int currentBucketRangeEnd = currentBucketRangeStart + bucketSize;
            range.put(
                            Integer.valueOf(((WorkerStateGateway.WorkerState)activeWorkersNotExclusive.get(i)).getNodeId()), new WorkerStateGateway.WorkerRange(currentBucketRangeStart, currentBucketRangeEnd));
            currentBucketRangeStart = currentBucketRangeEnd;
            left -= bucketSize;
        }
        return range;
    }


    private static Map<Integer, WorkerStateGateway.WorkerRange> getRangesForExclusiveWorkers(int rangeStart, int rangeEnd, List<WorkerStateGateway.WorkerState> activeWorkersExclusive)
    {
        return (Map<Integer, WorkerStateGateway.WorkerRange>)activeWorkersExclusive.stream()
                        .collect(Collectors.toMap(WorkerStateGateway.WorkerState::getNodeId, w -> new WorkerStateGateway.WorkerRange(rangeStart, rangeEnd)));
    }
}
