package de.hybris.platform.task.impl.gateways;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkerStateGateway extends BaseGateway
{
    void registerAsWorker(WorkerState paramWorkerState);


    Map<Integer, Duration> getWorkersHealthChecks();


    Optional<List<WorkerRange>> getWorkerRangeById(int paramInt);


    List<WorkerState> getWorkers();


    void updateWorkersRanges(Map<Integer, WorkerRange> paramMap);


    void deleteWorkers(List<Integer> paramList);


    void deactivateWorkers(List<Integer> paramList);
}
