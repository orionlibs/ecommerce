package de.hybris.platform.ruleengineservices.maintenance;

import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import java.util.List;

public interface RuleCompilerPublisherResult
{
    Result getResult();


    List<RuleCompilerResult> getCompilerResults();


    List<RuleEngineActionResult> getPublisherResults();
}
