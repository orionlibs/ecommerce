package de.hybris.platform.ruleengine.init;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import java.util.List;

public class InitializationFuture
{
    private RuleEngineKieModuleSwapper moduleSwapper;
    private List<RuleEngineActionResult> results;


    private InitializationFuture(RuleEngineKieModuleSwapper moduleSwapper)
    {
        this.moduleSwapper = moduleSwapper;
        this.results = Lists.newCopyOnWriteArrayList();
    }


    public static InitializationFuture of(RuleEngineKieModuleSwapper moduleSwapper)
    {
        return new InitializationFuture(moduleSwapper);
    }


    public InitializationFuture waitForInitializationToFinish()
    {
        this.moduleSwapper.waitForSwappingToFinish();
        return this;
    }


    public List<RuleEngineActionResult> getResults()
    {
        return this.results;
    }
}
