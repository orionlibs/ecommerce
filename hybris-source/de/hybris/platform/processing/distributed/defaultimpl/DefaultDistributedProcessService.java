package de.hybris.platform.processing.distributed.defaultimpl;

import com.google.common.base.Preconditions;
import de.hybris.platform.processing.distributed.DistributedProcessService;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.platform.processing.distributed.ProcessStatus;
import de.hybris.platform.processing.model.DistributedProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class DefaultDistributedProcessService implements DistributedProcessService
{
    private final ModelService modelService;
    private final FlexibleSearchService flexibleSearchService;
    private final Controller controller;
    private static final String PROCESS_BY_CODE_QUERY = "select {pk} from {DistributedProcess} where {code}=?code";


    public DefaultDistributedProcessService(ModelService modelService, FlexibleSearchService flexibleSearchService, Controller controller)
    {
        this.modelService = Objects.<ModelService>requireNonNull(modelService, "modelService mustn't be null");
        this.flexibleSearchService = Objects.<FlexibleSearchService>requireNonNull(flexibleSearchService, "flexibleSearchService mustn't be null");
        this.controller = Objects.<Controller>requireNonNull(controller, "controller mustn't be null");
    }


    public <T extends DistributedProcessModel> T create(ProcessCreationData creationData)
    {
        return (T)this.controller.createProcess(creationData);
    }


    public <T extends DistributedProcessModel> T start(String processCode)
    {
        return (T)this.controller.startProcess(requireExistingProcess(processCode));
    }


    public <T extends DistributedProcessModel> T requestToStop(String processCode)
    {
        return (T)this.controller.requestToStopProcess(requireExistingProcess(processCode));
    }


    public <T extends DistributedProcessModel> T wait(String processCode, long maxWaitTimeInSeconds) throws InterruptedException
    {
        return (T)this.controller.waitForProcess(requireExistingProcess(processCode), maxWaitTimeInSeconds);
    }


    public ProcessStatus getCurrentStatus(String processCode)
    {
        return this.controller.getProcessStatus(requireExistingProcess(processCode));
    }


    private DistributedProcessModel requireExistingProcess(String processCode)
    {
        Map<String, String> params = Collections.singletonMap("code", processCode);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {DistributedProcess} where {code}=?code", params);
        query.setDisableCaching(true);
        SearchResult<DistributedProcessModel> searchResult = this.flexibleSearchService.search(query);
        Preconditions.checkState((searchResult.getCount() == 1), "Expected exactly one process with code '%s', but found %s", processCode,
                        Integer.valueOf(searchResult.getCount()));
        DistributedProcessModel result = searchResult.getResult().get(0);
        Preconditions.checkState((result != null), "Process with code '%s' has been removed", processCode);
        this.modelService.refresh(result);
        return result;
    }
}
