package de.hybris.platform.ruleengine;

import java.io.Serializable;
import java.util.Collection;

public class RuleEngineActionResult implements Serializable
{
    private String moduleName;
    private boolean actionFailed;
    private String deployedVersion;
    private String oldVersion;
    private Collection<ResultItem> results;
    private ExecutionContext executionContext;


    public void setModuleName(String moduleName)
    {
        this.moduleName = moduleName;
    }


    public String getModuleName()
    {
        return this.moduleName;
    }


    public void setActionFailed(boolean actionFailed)
    {
        this.actionFailed = actionFailed;
    }


    public boolean isActionFailed()
    {
        return this.actionFailed;
    }


    public void setDeployedVersion(String deployedVersion)
    {
        this.deployedVersion = deployedVersion;
    }


    public String getDeployedVersion()
    {
        return this.deployedVersion;
    }


    public void setOldVersion(String oldVersion)
    {
        this.oldVersion = oldVersion;
    }


    public String getOldVersion()
    {
        return this.oldVersion;
    }


    public void setResults(Collection<ResultItem> results)
    {
        this.results = results;
    }


    public Collection<ResultItem> getResults()
    {
        return this.results;
    }


    public void setExecutionContext(ExecutionContext executionContext)
    {
        this.executionContext = executionContext;
    }


    public ExecutionContext getExecutionContext()
    {
        return this.executionContext;
    }


    public String getMessagesAsString(MessageLevel level)
    {
        StringBuilder sb = (new StringBuilder("RulesModule:")).append(getModuleName());
        if(this.results != null)
        {
            int n = 0;
            for(ResultItem item : this.results)
            {
                if(level == null || level.equals(item.getLevel()))
                {
                    String messageLine = String.format("%s line %d : %s", new Object[] {item.getPath(), Integer.valueOf(item.getLine()), item
                                    .getMessage()});
                    sb.append((this.results.size() > 1) ? String.format("%d) ", new Object[] {Integer.valueOf(++n)}) : "").append(messageLine)
                                    .append(System.lineSeparator());
                }
            }
        }
        return sb.toString();
    }
}
