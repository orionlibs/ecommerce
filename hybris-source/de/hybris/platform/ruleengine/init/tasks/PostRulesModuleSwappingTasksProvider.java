package de.hybris.platform.ruleengine.init.tasks;

import de.hybris.platform.ruleengine.RuleEngineActionResult;
import java.util.List;
import java.util.function.Supplier;

public interface PostRulesModuleSwappingTasksProvider
{
    List<Supplier<Object>> getTasks(RuleEngineActionResult paramRuleEngineActionResult);
}
