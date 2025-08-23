package de.hybris.platform.ruleengine.event;

import de.hybris.platform.ruleengine.ResultItem;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.util.Collection;

public class RuleEngineModuleSwapCompletedEvent extends AbstractEvent
{
    private final String rulesModuleName;
    private final String previousRulesModuleVersion;
    private final String rulesModuleVersion;
    private boolean failed;
    private String failureReason;
    private Collection<ResultItem> results;


    private RuleEngineModuleSwapCompletedEvent(String rulesModuleName, String previousRulesModuleVersion, String rulesModuleVersion, Collection<ResultItem> results)
    {
        this.rulesModuleName = rulesModuleName;
        this.previousRulesModuleVersion = previousRulesModuleVersion;
        this.rulesModuleVersion = rulesModuleVersion;
        this.results = results;
    }


    public static RuleEngineModuleSwapCompletedEvent ofSuccess(String rulesModuleName, String previousRulesModuleVersion, String rulesModuleVersion, Collection<ResultItem> results)
    {
        return new RuleEngineModuleSwapCompletedEvent(rulesModuleName, previousRulesModuleVersion, rulesModuleVersion, results);
    }


    public static RuleEngineModuleSwapCompletedEvent ofFailure(String rulesModuleName, String previousRulesModuleVersion, String failureReason, Collection<ResultItem> results)
    {
        RuleEngineModuleSwapCompletedEvent completedEvent = new RuleEngineModuleSwapCompletedEvent(rulesModuleName, previousRulesModuleVersion, null, results);
        completedEvent.setFailed(true);
        completedEvent.setFailureReason(failureReason);
        return completedEvent;
    }


    public String toString()
    {
        return "RuleEngineModuleSwapCompletedEvent{rulesModuleName='" + this.rulesModuleName + "', previousRulesModuleVersion='" + this.previousRulesModuleVersion + "', rulesModuleVersion='" + this.rulesModuleVersion + "', failed=" + this.failed + ", failureReason='" + this.failureReason + "'}";
    }


    public String getRulesModuleName()
    {
        return this.rulesModuleName;
    }


    public String getPreviousRulesModuleVersion()
    {
        return this.previousRulesModuleVersion;
    }


    public String getRulesModuleVersion()
    {
        return this.rulesModuleVersion;
    }


    public boolean isFailed()
    {
        return this.failed;
    }


    public void setFailed(boolean failed)
    {
        this.failed = failed;
    }


    public String getFailureReason()
    {
        return this.failureReason;
    }


    public void setFailureReason(String failureReason)
    {
        this.failureReason = failureReason;
    }


    public Collection<ResultItem> getResults()
    {
        return this.results;
    }
}
