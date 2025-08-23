package de.hybris.platform.ruleengine.init;

import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.concurrency.TaskResult;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class RuleDeploymentTaskResult implements TaskResult
{
    private final List<RuleEngineActionResult> rulePublishingResults;


    public RuleDeploymentTaskResult(List<RuleEngineActionResult> rulePublishingResults)
    {
        this.rulePublishingResults = rulePublishingResults;
    }


    public List<RuleEngineActionResult> getRulePublishingResults()
    {
        return this.rulePublishingResults;
    }


    public TaskResult.State getState()
    {
        TaskResult.State state = TaskResult.State.SUCCESS;
        List<RuleEngineActionResult> results = getRulePublishingResults();
        if(CollectionUtils.isNotEmpty(results) && results.stream().anyMatch(RuleEngineActionResult::isActionFailed))
        {
            state = TaskResult.State.FAILURE;
        }
        return state;
    }
}
