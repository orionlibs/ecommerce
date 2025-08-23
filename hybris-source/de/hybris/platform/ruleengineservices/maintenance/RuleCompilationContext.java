package de.hybris.platform.ruleengineservices.maintenance;

import de.hybris.platform.ruleengine.concurrency.SuspendResumeTaskManager;
import de.hybris.platform.ruleengine.concurrency.TaskContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerService;
import java.util.concurrent.atomic.AtomicLong;

public interface RuleCompilationContext extends TaskContext
{
    RuleCompilerService getRuleCompilerService();


    AtomicLong resetRuleEngineRuleVersion(String paramString);


    Long getNextRuleEngineRuleVersion(String paramString);


    void cleanup(String paramString);


    void registerCompilationListeners(String paramString);


    SuspendResumeTaskManager getSuspendResumeTaskManager();
}
