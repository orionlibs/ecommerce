package de.hybris.platform.task.impl;

import de.hybris.platform.task.impl.gateways.WorkerStateGateway;
import java.util.List;
import java.util.Map;

public interface WorkerHelper
{
    Map<Integer, WorkerStateGateway.WorkerRange> calculateRanges(List<WorkerStateGateway.WorkerState> paramList, int paramInt1, int paramInt2);
}
