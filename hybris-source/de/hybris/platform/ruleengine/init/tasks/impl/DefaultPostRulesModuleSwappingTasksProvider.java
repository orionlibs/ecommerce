package de.hybris.platform.ruleengine.init.tasks.impl;

import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.init.tasks.PostRulesModuleSwappingTask;
import de.hybris.platform.ruleengine.init.tasks.PostRulesModuleSwappingTasksProvider;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPostRulesModuleSwappingTasksProvider implements PostRulesModuleSwappingTasksProvider
{
    private List<PostRulesModuleSwappingTask> postRulesModuleSwappingTasks;


    public List<Supplier<Object>> getTasks(RuleEngineActionResult result)
    {
        return (List<Supplier<Object>>)getPostRulesModuleSwappingTasks().stream().map(task -> ())
                        .collect(Collectors.toList());
    }


    protected List<PostRulesModuleSwappingTask> getPostRulesModuleSwappingTasks()
    {
        return this.postRulesModuleSwappingTasks;
    }


    @Required
    public void setPostRulesModuleSwappingTasks(List<PostRulesModuleSwappingTask> postRulesModuleSwappingTasks)
    {
        this.postRulesModuleSwappingTasks = postRulesModuleSwappingTasks;
    }
}
