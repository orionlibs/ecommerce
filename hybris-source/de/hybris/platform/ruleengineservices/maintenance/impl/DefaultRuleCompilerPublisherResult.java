package de.hybris.platform.ruleengineservices.maintenance.impl;

import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerPublisherResult;
import java.util.List;

public class DefaultRuleCompilerPublisherResult implements RuleCompilerPublisherResult
{
    private final RuleCompilerPublisherResult.Result result;
    private final List<RuleCompilerResult> compilerResults;
    private final List<RuleEngineActionResult> publisherResults;


    public DefaultRuleCompilerPublisherResult(RuleCompilerPublisherResult.Result result, List<RuleCompilerResult> compilerResults, List<RuleEngineActionResult> publisherResults)
    {
        this.compilerResults = compilerResults;
        this.publisherResults = publisherResults;
        this.result = result;
    }


    public RuleCompilerPublisherResult.Result getResult()
    {
        return this.result;
    }


    public List<RuleCompilerResult> getCompilerResults()
    {
        return this.compilerResults;
    }


    public List<RuleEngineActionResult> getPublisherResults()
    {
        return this.publisherResults;
    }
}
