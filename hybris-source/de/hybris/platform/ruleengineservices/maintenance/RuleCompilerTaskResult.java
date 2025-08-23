package de.hybris.platform.ruleengineservices.maintenance;

import de.hybris.platform.ruleengine.concurrency.TaskResult;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class RuleCompilerTaskResult implements TaskResult
{
    private List<RuleCompilerResult> ruleCompilerResults;


    public RuleCompilerTaskResult(List<RuleCompilerResult> ruleCompilerResults)
    {
        this.ruleCompilerResults = ruleCompilerResults;
    }


    public TaskResult.State getState()
    {
        if(CollectionUtils.isNotEmpty(this.ruleCompilerResults))
        {
            return this.ruleCompilerResults.stream().filter(r -> (r.getResult() == RuleCompilerResult.Result.ERROR)).findAny().map(r -> TaskResult.State.FAILURE)
                            .orElse(TaskResult.State.SUCCESS);
        }
        return TaskResult.State.SUCCESS;
    }


    public List<RuleCompilerResult> getRuleCompilerResults()
    {
        return this.ruleCompilerResults;
    }
}
